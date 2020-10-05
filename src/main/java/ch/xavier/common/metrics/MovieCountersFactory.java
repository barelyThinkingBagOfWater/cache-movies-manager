package ch.xavier.common.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class MovieCountersFactory {

    private final PrometheusMeterRegistry registry;
    
    @Autowired
    MovieCountersFactory(PrometheusMeterRegistry registry) {
        this.registry = registry;
    }


    Counter getMoviesImportedCounter() {
        return Counter
                .builder("movies.imported")
                .description("indicates the number of movies imported")
                .tag("entity", "movie")
                .tag("action", "import")
                .register(registry);
    }

    Counter getMoviesImportedErrorsCounter() {
        return Counter
                .builder("movies.imported.errors")
                .description("indicates the number of errors encountered when trying to import a movie")
                .tag("entity", "movie")
                .tag("action", "import")
                .tag("type", "error")
                .register(registry);
    }

    Counter getMoviesSearchedCounter() {
        return Counter
                .builder("movies.searched") //do you want to add the movieId here?
                .description("indicates the number of movies searched")
                .tag("entity", "movie")
                .tag("action", "search")
                .register(registry);
    }
}