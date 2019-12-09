package ch.xavier.movies.cache.metrics;

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


    Counter getMovieImportedCounter() {
        return Counter
                .builder("cache-movies-manager.movies.imported")
                .description("indicates the number of movies imported in the cache")
                .tag("entity", "movie")
                .tag("action", "import")
                .register(registry);
    }

    Counter getMovieImportedErrorsCounter() {
        return Counter
                .builder("cache-movies-manager.movies.imported.errors")
                .description("indicates the number of errors encountered when importing movies in the cache") //isn't it only one error if everything fails?
                .tag("entity", "movie")
                .tag("action", "import")
                .tag("type", "error")
                .register(registry);
    }

    Counter getTagAddedCounter() {
        return Counter
                .builder("cache-movies-manager.tags.added")
                .description("indicates the number of tags added to movies in the cache")
                .tag("entity", "tag")
                .tag("action", "add")
                .register(registry);
    }

    Counter getTagAddedErrorsCounter() {
        return Counter
                .builder("cache-movies-manager.tags.added.errors")
                .description("indicates the number of errors encountered when adding a tag to movies in the cache") //isn't it only one error if everything fails?
                .tag("entity", "tag")
                .tag("action", "add")
                .tag("type", "error")
                .register(registry);
    }

    //Could cause DOS in Prometheus
    Counter getMovieSearchedCounter(Long movieId) {
        return Counter
                .builder("cache-movies-manager.movie." + movieId + ".searched")
                .description("indicates the number of searches for movieId:" + movieId)
                .tag("entity", "movie")
                .tag("action", "search")
                .tag("movieId", movieId.toString())
                .register(registry);
    }
}
