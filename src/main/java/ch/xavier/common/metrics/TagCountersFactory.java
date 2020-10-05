package ch.xavier.common.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class TagCountersFactory {

    private final PrometheusMeterRegistry registry;

    @Autowired
    TagCountersFactory(PrometheusMeterRegistry registry) {
        this.registry = registry;
    }

    //TODO: What about search?
    Counter getTagsAddedCounter() {
        return Counter
                .builder("tags.added")
                .description("indicates the number of tags added to movies")
                .tag("entity", "tag")
                .tag("action", "add")
                .register(registry);
    }

    Counter getTagsAddedErrorsCounter() {
        return Counter
                .builder("tags.added.errors")
                .description("indicates the number of errors encountered when trying to add a tag to a movie")
                .tag("entity", "tag")
                .tag("action", "add")
                .tag("type", "error")
                .register(registry);
    }

    Counter getTagsImportedCounter() {
        return Counter
                .builder("tags.imported")
                .description("indicates the number of tags imported from movies")
                .tag("entity", "tag")
                .tag("action", "import")
                .register(registry);
    }

    Counter getTagsImportedErrorsCounter() {
        return Counter
                .builder("tags.imported.errors")
                .description("indicates the number of errors encountered when trying to import a tag from a movie")
                .tag("entity", "tag")
                .tag("action", "import")
                .tag("type", "error")
                .register(registry);
    }

    Counter getTagsSearchedCounter() {
        return Counter
                .builder("tags.searched") //do you want to add the movieId here?
                .description("indicates the number of tags searched")
                .tag("entity", "tag")
                .tag("action", "search")
                .register(registry);
    }
}