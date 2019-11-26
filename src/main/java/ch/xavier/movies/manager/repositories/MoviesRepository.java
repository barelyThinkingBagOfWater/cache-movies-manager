package ch.xavier.movies.manager.repositories;

import ch.xavier.movies.domain.Movie;
import reactor.core.publisher.Mono;

public interface MoviesRepository {

    Mono save(Movie movie);

    Mono<Movie> find(String movieId);

    Mono<Boolean> addTagToMovie(String tag, String movieId);

    Mono<Long> count();

    Mono<Long> empty();
}