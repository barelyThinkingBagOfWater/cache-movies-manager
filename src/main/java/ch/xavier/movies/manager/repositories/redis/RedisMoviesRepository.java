package ch.xavier.movies.manager.repositories.redis;

import ch.xavier.movies.domain.Movie;
import ch.xavier.movies.manager.repositories.MoviesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class RedisMoviesRepository implements MoviesRepository {

    private final ReactiveRedisOperations<String, Movie> reactiveRedisOperations;
    private ReactiveValueOperations<String, Movie> reactiveValueOps;

    @Autowired
    public RedisMoviesRepository(ReactiveRedisOperations<String, Movie> reactiveRedisOperations) {
        this.reactiveRedisOperations = reactiveRedisOperations;
        this.reactiveValueOps = reactiveRedisOperations.opsForValue();
    }


    public Mono<Boolean> save(Movie movie) {
        return reactiveValueOps.set(movie.getMovieId().toString(), movie);
    }

    public Mono<Movie> find(String movieId) {
        return reactiveValueOps.get(movieId);
    }

    public Mono<Boolean> addTagToMovie(String tag, String movieId) {
        return find(movieId)
                .switchIfEmpty(Mono.error(new Exception()))
                .flatMap(movie -> save(movie.withNewTag(tag)));
    }

    public Mono<Long> count() {
        return reactiveRedisOperations.scan().count();
    }

    @Override
    public Mono<Long> empty() {
        return reactiveRedisOperations.delete(reactiveRedisOperations.scan());
    }
}
