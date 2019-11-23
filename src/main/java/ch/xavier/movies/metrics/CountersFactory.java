package ch.xavier.movies.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class CountersFactory {

    private final PrometheusMeterRegistry registry;

    @Autowired
    CountersFactory(PrometheusMeterRegistry registry) {
        this.registry = registry;
    }

    Counter getMovieUpdateCounter() {
        return Counter
                .builder("movies.update")
                .description("indicates the number of movies updated in the cache")
                .tag("entity", "movie")
                .tag("action", "update")
                .register(registry);
    }

    Counter getMovieImportCounter() {
        return Counter
                .builder("movies.import")
                .description("indicates the number of movies imported in the cache")
                .tag("entity", "movie")
                .tag("action", "import")
                .register(registry);
    }

    Counter getMovieAddedCounter() {
        return Counter
                .builder("movies.add")
                .description("indicates the number of new movies added in the cache")
                .tag("entity", "movie")
                .tag("action", "add")
                .register(registry);
    }

    //Could cause DOS in Prometheus
    Counter getMovieSearchCounter(Long movieId) {
        return Counter
                .builder("movie." + movieId + ".searched")
                .description("indicates the number of searches for movieId:" + movieId)
                .tag("entity", "movie")
                .tag("action", "search")
                .tag("movieId", movieId.toString())
                .register(registry);
    }

    Counter getUpdateConflictsCounter() {
        return Counter
                .builder("movies.update.conflicts")
                .description("indicates the number of conflicts encountered during a movie update")
                .tag("entity", "movie")
                .tag("action", "update")
                .tag("type", "conflicts")
                .register(registry);
    }

    Counter getUpdateErrorsCounter() {
        return Counter
                .builder("movies.update.errors")
                .description("indicates the number of errors that aborted a movie update")
                .tag("entity", "movie")
                .tag("action", "update")
                .tag("type", "errors")
                .register(registry);
    }
}
