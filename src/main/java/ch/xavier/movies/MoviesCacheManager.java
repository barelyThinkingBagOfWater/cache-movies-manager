package ch.xavier.movies;

import ch.xavier.Importer;
import ch.xavier.metrics.MetricsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class MoviesCacheManager {

    private final MoviesRepository repository;
    private final MetricsManager metricsManager;
    private final Scheduler scheduler;
    private final List<Importer<Movie>> importers;

    private final Long retryDelayInMs;
    private final Integer retryAttempts;
    private final Boolean logEachImport;
    private final Integer timeout;
    private boolean isCacheReady = false;

    @Autowired
    public MoviesCacheManager(MetricsManager metricsManager,
                              MoviesRepository repository,
                              List<Importer<Movie>> importers,
                              @Value("${manager.retryDelayInMs}") Long retryDelayInMs,
                              @Value("${manager.retryAttempts}") Integer retryAttempts,
                              @Value("${manager.logEachImport}") Boolean logEachImport,
                              @Value("${manager.repositoryTimeoutInMs}") Integer timeout) {
        this.metricsManager = metricsManager;
        this.retryDelayInMs = retryDelayInMs;
        this.repository = repository;
        this.importers = importers;
        this.retryAttempts = retryAttempts;
        this.logEachImport = logEachImport;
        this.timeout = timeout;

        this.scheduler = Schedulers.fromExecutor(Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()));

        fillCacheIfEmpty();
    }


    private void fillCacheIfEmpty() {
        repository.count()
                .doOnNext(count -> {
                    if (count == 0) {
                        log.info("Cache is empty, now filling it from every available importers");
                        importAll().subscribe();
                    } else {
                        log.info("The cache just started but is already filled with {} entries", count);
                        isCacheReady = true;
                    }
                }).subscribe();
    }

    Flux<Boolean> importAll() {
        return repository.empty()
                .thenMany(importMoviesFromAllImporters())
                .doOnComplete(() -> isCacheReady = true);
    }

    private Flux<Boolean> importMoviesFromAllImporters() {
        return Flux.fromIterable(importers)
                .flatMap(moviesImporter -> importMovies(moviesImporter.importAll()));
    }

    private Flux<Boolean> importMovies(Flux<Movie> movies) {
        return movies
                .doOnNext(movie -> logImport(movie.getTitle()))
                .publishOn(scheduler)
                .flatMap(repository::save)
                .doOnNext(response -> metricsManager.notifyMovieImported())
                .doOnError(e -> {
                    metricsManager.notifyMovieImportedError();
                    log.error("Error caught when importing movies, exiting so that Kubernetes can restart the cache (with its exponential back-off delay):", e);
                    System.exit(1);
                })
                .doOnComplete(() -> log.info("Import successful, the cache is now filled."));
    }

    private void logImport(String title) {
        if (logEachImport) {
            log.info("Now importing movie titled:{} on Thread:{}", title, Thread.currentThread().getName());
        }
    }

    public Flux<Boolean> addTagsToMovies(Set<String> tags, Set<Long> movieIds) {
        return Flux.fromIterable(movieIds)
                .map(String::valueOf)
                .flatMap(movieId -> addTagsToMovie(tags, movieId));
    }


    private Flux<Boolean> addTagsToMovie(Set<String> tags, String movieId) {
        return Flux.fromIterable(tags)
                .flatMap(tag -> addTagToMovie(tag, movieId));
    }

    private Mono<Boolean> addTagToMovie(String tagName, String movieId) {
        return repository.addTagToMovie(tagName, movieId)
                .publishOn(scheduler)
                .timeout(Duration.ofMillis(timeout))
                .retryBackoff(retryAttempts, Duration.ofMillis(retryDelayInMs))
                .doOnError(e -> {
                    metricsManager.notifyTagAddedError();
                    log.error("error when adding tag:{} to movieId:{}", tagName, movieId, e);
                })
                .doOnSuccess(movie -> metricsManager.notifyTagAdded());
    }

    Mono<Movie> find(String movieId) {
        metricsManager.notifyMovieSearch(movieId);

        return repository.find(movieId);
    }

    Mono<List<Movie>> findAll(List<String> movieIds) {
        movieIds.forEach(metricsManager::notifyMovieSearch);

        return repository.findAll(movieIds);
    }

    boolean isCacheReady() {
        return isCacheReady;
    }
}
