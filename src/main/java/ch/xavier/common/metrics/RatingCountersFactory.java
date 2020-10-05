package ch.xavier.common.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class RatingCountersFactory {

    private final PrometheusMeterRegistry registry;

    @Autowired
    RatingCountersFactory(PrometheusMeterRegistry registry) {
        this.registry = registry;
    }

    Counter getRatingsImportedCounter() {
        return Counter
                .builder("ratings.imported")
                .description("indicates the number of ratings imported")
                .tag("entity", "rating")
                .tag("action", "import")
                .register(registry);
    }

    Counter getRatingsImportedErrorsCounter() {
        return Counter
                .builder("ratings.imported.errors")
                .description("indicates the number of errors encountered when trying to import a rating")
                .tag("entity", "rating")
                .tag("action", "import")
                .tag("type", "error")
                .register(registry);
    }

    Counter getRatingsAddedCounter() {
        return Counter
                .builder("ratings.added")
                .description("indicates the number of ratings added")
                .tag("entity", "rating")
                .tag("action", "add")
                .register(registry);
    }

    Counter getRatingsAddedErrorsCounter() {
        return Counter
                .builder("ratings.added.errors")
                .description("indicates the number of errors encountered when trying to add a rating")
                .tag("entity", "rating")
                .tag("action", "add")
                .tag("type", "error")
                .register(registry);
    }
}