package ch.xavier.movies.manager.repositories.redis;

import ch.xavier.movies.domain.Movie;
import ch.xavier.movies.manager.repositories.MoviesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
@ConditionalOnBean(CustomReactiveRedisTemplate.class)
public class RedisMoviesRepository implements MoviesRepository {


    @Autowired
    private ReactiveRedisTemplate reactiveRedisTemplate;
    private ReactiveValueOperations<Long, Movie> reactiveValueOps;

    @Autowired
    public RedisMoviesRepository() {
//        reactiveValueOps = reactiveRedisTemplate.opsForValue();
    }


    @Override
    public Mono<Boolean> save(Movie movie) {
        return reactiveValueOps.set(movie.getMovieId(), movie);
    }

    @Override
    public Mono<Movie> find(Long movieId) {
        return reactiveValueOps.get(movieId);
    }

    @Override
    public Mono<Boolean> addTagToMovie(String tag, Long movieId) {
        return find(movieId)
                .doOnNext(movie -> log.info("Now adding tag:{} to movie just fetched from db:{}", tag, movie))
                .doOnNext(movie -> movie.addTag(tag))
                .doOnNext(movie -> log.info("Now movie hopefully with the tag:{}", movie))
                .flatMap(this::save);
    }
}
