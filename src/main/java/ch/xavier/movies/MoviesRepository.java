package ch.xavier.movies;

import ch.xavier.common.movies.Movie;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MoviesRepository {

    Mono<Boolean> save(Movie movie);

    Mono<Movie> find(String movieId);

    Mono<List<Movie>> findAll(List<String> movieIds);

    Mono<Boolean> addTagToMovie(String tag, String movieId);

    Mono<Long> count();

    Mono<Long> empty();
}