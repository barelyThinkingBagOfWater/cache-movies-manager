package ch.xavier.movies.manager.repositories;

import ch.xavier.movies.domain.Movie;
import ch.xavier.movies.manager.repositories.redis.RedisMoviesRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = "redis.host=175.17.42.6")
//@Ignore
@Slf4j
public class ITestRedisMoviesRepository {

    //You could test all methods and consider the error cases as well...

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private RedisMoviesRepository redisMoviesRepository;

    @Before
    public void setUp() {
//        redisMoviesRepository.removeIndex(DEFAULT_INDEX_NAME)
//                .then(redisMoviesRepository.createIndex(DEFAULT_INDEX_NAME))
//                .block();
    }

    @Test
    public void SaveAndGetOneTag() {
        // GIVEN
        Movie preparedMovie = new Movie(1L, "title", "genres", Collections.emptySet());

        redisMoviesRepository.save(preparedMovie)

                // WHEN
                .then(redisMoviesRepository.find(1L))

                // THEN
                .doOnNext(Assertions::assertNotNull)
                .doOnNext(movie -> assertThat(movie.getTitle(), is("title")))
                .block();
    }

//    @Test
//    public void save100Tags() {
//        // GIVEN
//        long numberSent = 100;
//
//        // WHEN
//        Flux.fromIterable(generateRandomTags(numberSent))
//                .flatMap(redisMoviesRepository::save)
//
//                // THEN
//                .count()
//                .doOnNext(next -> waitOneSecond())
//                .then(redisMoviesRepository.count())
//                .doOnNext(count -> assertThat(count, is(numberSent)))
//                .block();
//    }
//
//
//    @Test
//    public void testSecondLevelFuzziness() {
//        // WHEN
//        String tagName1 = "tga1";
//        String tagName2 = "tag11";
//        String tagName3 = "tag111";
//
//        redisMoviesRepository.save(new Tag(tagName1))
//                .then(redisMoviesRepository.save(new Tag(tagName2)))
//                .then(redisMoviesRepository.save(new Tag(tagName3)))
//                .doOnNext(next -> waitOneSecond())
//
//                // WHEN
//                .then(redisMoviesRepository.fuzzilyFind("tag").count())
//
//                // THEN
//                .doOnNext(count -> assertThat(count, is(2L)))
//                .block();
//    }
//
//    @Test
//    public void testVersionedUpdate_withCorrectVersion() {
//        // GIVEN1
//        String tagName = "update";
//        Set<Long> movieIds1 = Set.of(1L);
//
//        Tag tag1 = new Tag(tagName, movieIds1);
//
//        //GIVEN2
//        VersionedTag tag2 = new VersionedTag(new Tag(tagName, Set.of(1L, 2L)), 0L, 1L);
//
//        redisMoviesRepository.save(tag1)
//                .then(redisMoviesRepository.getVersionedTagAndCreateItIfMissing(tagName))
//
//                // THEN1
//                .doOnNext(tag -> assertThat(tag.getTagName(), is(tag1.getTagName())))
//                .doOnNext(tag -> assertThat(tag.getMovieIds(), is(tag1.getMovieIds())))
//                .doOnNext(tag -> assertThat(tag.getPrimaryTerm(), is(1L)))
//                .doOnNext(tag -> assertThat(tag.getSeqNumber(), is(0L)))
//                // WHEN2
//                .then(redisMoviesRepository.updateWithVersion(tag2))
//
//                .then(redisMoviesRepository.getVersionedTagAndCreateItIfMissing(tagName))
//
//                // THEN2
//                .doOnNext(tag -> assertThat(tag.getTagName(), is(tag2.getTagName())))
//                .doOnNext(tag -> assertThat(tag.getMovieIds(), is(tag2.getMovieIds())))
//                .doOnNext(tag -> assertThat(tag.getPrimaryTerm(), is(1L)))
//                .doOnNext(tag -> assertThat(tag.getSeqNumber(), is(1L)))
//                .block();
//    }
//
//    @Test
//    public void testVersionedUpdate_withIncorrectVersion() {
//        // GIVEN1
//        String tagName = "update";
//        Set<Long> movieIds1 = Set.of(1L);
//
//        Tag tag1 = new Tag(tagName, movieIds1);
//        VersionedTag tag2 = new VersionedTag(tag1, 42L, 1L);
//
//        // GIVEN2
//        exception.expect(ElasticsearchStatusException.class);
//
//        // WHEN1
//        redisMoviesRepository.save(tag1)
//
//                // THEN1
//                .then(redisMoviesRepository.getVersionedTagAndCreateItIfMissing(tagName))
//                .doOnNext(tag -> assertThat(tag.getTagName(), is(tag1.getTagName())))
//                .doOnNext(tag -> assertThat(tag.getMovieIds(), is(tag1.getMovieIds())))
//                .doOnNext(tag -> assertThat(tag.getSeqNumber(), is(0L)))
//                .doOnNext(tag -> assertThat(tag.getPrimaryTerm(), is(1L)))
//
//                // THEN2
//                .then(redisMoviesRepository.updateWithVersion(tag2))
//                .block();
//    }
//
//
//    private List<Tag> generateRandomTags(long number) {
//        List<Tag> tags = new ArrayList<>();
//
//        for (int i = 0; i < number; i++) {
//            tags.add(generateRandomTag());
//        }
//
//        return tags;
//    }
//
//    private Tag generateRandomTag() {
//        String tagName = generateRandomTagName();
//        long id = new Random().nextLong();
//
//        return new Tag(tagName, Set.of(id));
//    }
//
//    private String generateRandomTagName() {
//        int leftLimit = 97; // letter 'a'
//        int rightLimit = 122; // letter 'z'
//        Random random = new Random();
//        StringBuilder buffer = new StringBuilder(10);
//        for (int i = 0; i < 10; i++) {
//            int randomLimitedInt = leftLimit + (int)
//                    (random.nextFloat() * (rightLimit - leftLimit + 1));
//            buffer.append((char) randomLimitedInt);
//        }
//        return buffer.toString();
//    }
//
//    private void waitOneSecond() {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
