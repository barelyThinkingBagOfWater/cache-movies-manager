package ch.xavier.movies.manager.repositories;

import ch.xavier.movies.domain.Movie;
import reactor.core.publisher.Mono;

public interface MoviesRepository {

    Mono save(Movie movie);

    Mono<Movie> find(Long movieId);

    Mono<Boolean> addTagToMovie(String tag, Long movieId);
}
