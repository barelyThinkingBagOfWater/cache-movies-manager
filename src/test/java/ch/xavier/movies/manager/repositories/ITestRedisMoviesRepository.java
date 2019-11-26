package ch.xavier.movies.manager.repositories;

import ch.xavier.movies.domain.Movie;
import ch.xavier.movies.manager.repositories.redis.RedisMoviesRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = "spring.redis.host=175.17.42.3")
@Slf4j

public class ITestRedisMoviesRepository {

    @Autowired
    private RedisMoviesRepository redisMoviesRepository;

    @Before
    public void setUp() {
        redisMoviesRepository.empty().block();
    }

    @Test
    public void save_and_find_work() {
        // GIVEN
        Movie preparedMovie = new Movie(1L, "title", "genres", Collections.emptySet());

        redisMoviesRepository.save(preparedMovie)

                // WHEN
                .then(redisMoviesRepository.find("1"))

                // THEN
                .doOnNext(Assertions::assertNotNull)
                .doOnNext(movie -> assertThat(movie.getTitle(), is("title")))
                .block();
    }

    @Test
    public void addTagToMovie_correctly_adds_the_tag() {
        // GIVEN
        String movieId = "2";
        HashSet<String> tags = new HashSet<>();
        Movie preparedMovie = new Movie(Long.valueOf(movieId), "title", "genres", tags);
        String newTag = "newTag";

        redisMoviesRepository.save(preparedMovie)

                // WHEN1
                .then(redisMoviesRepository.find(movieId))

                // THEN1
                .doOnNext(Assertions::assertNotNull)
                .doOnNext(movie -> assertThat(movie.getTitle(), is("title")))
                .doOnNext(movie -> assertThat(movie.getGenres(), is("genres")))
                .doOnNext(movie -> assertThat(movie.getTags(), is(Collections.emptySet())))

                // WHEN2
                .then(redisMoviesRepository.addTagToMovie(newTag, movieId))

                .then(redisMoviesRepository.find(movieId))

                // THEN2
                .doOnNext(Assertions::assertNotNull)
                .doOnNext(movie -> assertThat(movie.getTitle(), is("title")))
                .doOnNext(movie -> assertThat(movie.getGenres(), is("genres")))
                .doOnNext(movie -> assertThat(movie.getTags(), is(Set.of(newTag))))
                .block();
    }

    @Test
    public void empty_removes_all_movies_from_cache() {
        // GIVEN
        String movieId = "1";
        HashSet<String> tags = new HashSet<>();
        Movie preparedMovie = new Movie(Long.valueOf(movieId), "title", "genres", tags);

        redisMoviesRepository.save(preparedMovie)

                // WHEN1
                .then(redisMoviesRepository.find(movieId))

                // THEN1
                .doOnNext(Assertions::assertNotNull)
                .doOnNext(movie -> assertThat(movie.getTitle(), is("title")))
                .doOnNext(movie -> assertThat(movie.getGenres(), is("genres")))
                .doOnNext(movie -> assertThat(movie.getTags(), is(Collections.emptySet())))

                // WHEN2
                .thenMany(redisMoviesRepository.empty())
                .then(redisMoviesRepository.find(movieId))

                // THEN2
                .doOnNext(Assertions::assertNull)
                .block();
    }
}
