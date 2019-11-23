package ch.xavier.movies.importers;

import ch.xavier.movies.domain.Movie;
import reactor.core.publisher.Flux;

public interface MoviesImporter {

    /**
     * Import all tags from source
     */
    Flux<Movie> importAll();
}
