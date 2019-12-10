package ch.xavier.movies.cache.manager;

import ch.xavier.movies.cache.domain.Movie;
import ch.xavier.movies.cache.importers.MoviesImporter;
import ch.xavier.movies.cache.manager.repositories.MoviesRepository;
import ch.xavier.movies.cache.metrics.MetricsManager;
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
    private final List<MoviesImporter> importers;


    private final Long retryDelayInMs;
    private final Integer retryAttempts;
    private final Boolean logEachImport;
    private final Integer timeout;

    @Autowired
    public MoviesCacheManager(MetricsManager metricsManager,
                              MoviesRepository repository,
                              List<MoviesImporter> importers,
                              @Value("${manager.retryDelayInMs}") Long retryDelayInMs,
                              @Value("${manager.retryAttempts}") Integer retryAttempts,
                              @Value("${manager.logEachImport}") Boolean logEachImport,
                              @Value("${manager.numberOfThreads:0}") Integer numberOfThreads,
                              @Value("${manager.repositoryTimeoutInMs}") Integer timeout) {
        this.metricsManager = metricsManager;
        this.retryDelayInMs = retryDelayInMs;
        this.repository = repository;
        this.importers = importers;
        this.retryAttempts = retryAttempts;
        this.logEachImport = logEachImport;
        this.timeout = timeout;

        if (numberOfThreads < 1) {
            this.scheduler = Schedulers.parallel();
        } else {
            this.scheduler = Schedulers.fromExecutor(
                    Executors.newFixedThreadPool(numberOfThreads));
        }

        fillCacheIfEmpty(repository);
    }


    private void fillCacheIfEmpty(MoviesRepository repository) {
        repository.count()
                .doOnNext(count -> {
                    if (count == 0) {
                        log.info("Cache is empty, now filling it from every available importers");
                        importAll().subscribe();
                    } else {
                        log.info("The cache just started but is already filled with {} entries", count);
                    }
                }).subscribe();
    }

    public Flux<Boolean> importAll() {
        return repository.empty()
                .thenMany(importMoviesFromAllImporters());
    }

    private Flux<Boolean> importMoviesFromAllImporters() {
        return Flux.fromIterable(importers)
                .flatMap(moviesImporter -> importMovies(moviesImporter.importAll()));
    }

    private Flux<Boolean> importMovies(Flux<Movie> movies) {
        return movies
                .doOnNext(movie -> logImport(movie.getTitle()))
                .flatMap(repository::save)
                .doOnNext(response -> metricsManager.notifyMovieImported())
                .doOnError(e -> {
                    metricsManager.notifyMovieImportedError();
                    log.info("Error caught when importing movies:", e);
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


    public Flux<Boolean> addTagsToMovie(Set<String> tags, String movieId) {
        return Flux.fromIterable(tags)
                .flatMap(tag -> addTagToMovie(tag, movieId));
    }

    private Mono<Boolean> addTagToMovie(String tagName, String movieId) {
        return repository.addTagToMovie(tagName, movieId)
                .timeout(Duration.ofMillis(timeout))
                .retryBackoff(retryAttempts, Duration.ofMillis(retryDelayInMs))
                .publishOn(scheduler)
                .doOnError(e -> {
                    metricsManager.notifyTagAddedError();
                    log.error("error when adding tag:{} to movieId:{}", tagName, movieId, e);
                })
                .doOnSuccess(movie -> metricsManager.notifyTagAdded());
    }

    public Mono<Movie> find(String movieId) {
        return repository.find(movieId);
    }

    public Mono<List<Movie>> findAll(List<String> movieIds) {
        return repository.findAll(movieIds);
    }
}
