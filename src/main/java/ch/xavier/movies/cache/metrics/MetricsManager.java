package ch.xavier.movies.cache.metrics;

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


    public void notifyMovieImported() {
        countersFactory.getMovieImportedCounter().increment();
    }

    public void notifyMovieImportedError() {
        countersFactory.getMovieImportedErrorsCounter().increment();
    }

    public void notifyTagAdded() {
        countersFactory.getTagAddedCounter().increment();
    }

    public void notifyTagAddedError() {
        countersFactory.getTagAddedErrorsCounter().increment();
    }

    public void notifyMovieSearch(String movieId) {
        countersFactory.getMovieSearchedCounter(movieId).increment();
    }
}