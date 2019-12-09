package ch.xavier.movies.cache.resources;

import ch.xavier.movies.cache.manager.MoviesCacheManager;
import ch.xavier.movies.cache.domain.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@Slf4j
class RestHandler {

    private final MoviesCacheManager moviesCacheManager;

    @Autowired
    public RestHandler(MoviesCacheManager moviesCacheManager) {
        this.moviesCacheManager = moviesCacheManager;
    }

    Mono<ServerResponse> getMovie(ServerRequest request) {
        String movieId = request.pathVariable("movieId");

        log.info("getting movieId:{}", movieId);

        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(moviesCacheManager.find(movieId), Movie.class); //if not found returns null, deal with it properly
    }

    Mono<ServerResponse> importAllMovies(ServerRequest request) {
        moviesCacheManager.importAll().subscribe();

        return ok().build();
    }

    Mono<ServerResponse> addTagToMovieId(ServerRequest request) {
        String tag = request.pathVariable("tag");
        String movieId = request.pathVariable("movieId");

        log.info("Adding tag:{} to movieId:{}", tag, movieId);

        moviesCacheManager.addTagsToMovie(Set.of(tag), movieId).subscribe();

         return ok().build();
    }
}