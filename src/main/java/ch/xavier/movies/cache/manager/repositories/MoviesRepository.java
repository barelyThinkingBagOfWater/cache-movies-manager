package ch.xavier.movies.cache.manager.repositories;

import ch.xavier.movies.cache.domain.Movie;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MoviesRepository {

    Mono save(Movie movie);

    Mono<Movie> find(String movieId);

    Mono<List<Movie>> findAll(List<String> movieIds);

    Mono<Boolean> addTagToMovie(String tag, String movieId);

    Mono<Long> count();

    Mono<Long> empty();
}