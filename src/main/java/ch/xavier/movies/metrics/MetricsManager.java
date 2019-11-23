package ch.xavier.movies.metrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MetricsManager {

    private final CountersFactory countersFactory;

    @Autowired
    public MetricsManager(CountersFactory countersFactory) {
        this.countersFactory = countersFactory;
    }


    public void notifyMovieUpdate() {
        countersFactory.getMovieUpdateCounter().increment();
    }

    public void notifyMovieImport() {
        countersFactory.getMovieImportCounter().increment();
    }

    public void notifyNewMovieAdded() {
        countersFactory.getMovieAddedCounter().increment();
    }

    public void notifyMovieSearch(Long movieId) {
        countersFactory.getMovieSearchCounter(movieId).increment();
    }

    public void notifyMovieUpdateConflict() {
        countersFactory.getUpdateConflictsCounter().increment();
    }

    public void notifyMovieUpdateError() {
        countersFactory.getUpdateErrorsCounter().increment();
    }
}