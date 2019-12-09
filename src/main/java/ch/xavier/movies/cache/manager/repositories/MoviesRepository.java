package ch.xavier.movies.cache.manager.repositories;

import ch.xavier.movies.cache.domain.Movie;
import reactor.core.publisher.Mono;

public interface MoviesRepository {

    Mono save(Movie movie);

    Mono<Movie> find(String movieId);

    Mono<Boolean> addTagToMovie(String tag, String movieId);

    Mono<Long> count();

    Mono<Long> empty();
}