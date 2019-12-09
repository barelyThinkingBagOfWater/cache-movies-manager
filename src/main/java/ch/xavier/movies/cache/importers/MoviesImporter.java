package ch.xavier.movies.cache.importers;

import ch.xavier.movies.cache.domain.Movie;
import reactor.core.publisher.Flux;

public interface MoviesImporter {

    /**
     * Import all tags from source
     */
    Flux<Movie> importAll();
}
