package ch.xavier.movies.manager;

import ch.xavier.movies.importers.MoviesImporter;
import ch.xavier.movies.metrics.MetricsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class MoviesCacheManager {

    private final MetricsManager metricsManager;
    private final Scheduler scheduler;
    private final List<MoviesImporter> importers;


    private final Long retryDelayInMs;
    private final Integer retryAttempts;
    private final Boolean logEachImport;
    private final Integer timeout;

    @Autowired
    public MoviesCacheManager(MetricsManager metricsManager,
                              List<MoviesImporter> importers,
                              @Value("${manager.retryDelayInMs}") Long retryDelayInMs,
                              @Value("${manager.retryAttempts}") Integer retryAttempts,
                              @Value("${manager.logEachImport}") Boolean logEachImport,
                              @Value("${manager.numberOfThreads:0}") Integer numberOfThreads,
                              @Value("${manager.repositoryTimeoutInMs}") Integer timeout) {
        this.metricsManager = metricsManager;
        this.retryDelayInMs = retryDelayInMs;
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

//        fillCacheIfEmpty(repository);
    }


//    private void fillCacheIfEmpty(TagsRepository repository) {
//        Long currentCount = repository.count().block();
//        if (Objects.equals(currentCount, 0L)) {
//            log.info("Cache is empty, now filling it from every available importers");
//            importAll().subscribe();
//        } else {
//            log.info("Cache started but already filled with {} entries", currentCount);
//        }
//    }



    private void logImport(String title, String importerClass) {
        if (logEachImport) {
            log.info("Movie:{} imported from importer:{} on Thread:{}",
                    title, importerClass, Thread.currentThread().getName());
        }
    }
}
