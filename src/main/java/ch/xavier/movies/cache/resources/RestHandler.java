package ch.xavier.movies.cache.resources;

import ch.xavier.movies.cache.domain.Movie;
import ch.xavier.movies.cache.manager.MoviesCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Service
@Slf4j
class RestHandler {

    private final MoviesCacheManager moviesCacheManager;

    private final static int SERVICE_NOT_AVAILABLE_HTTP_STATUS_CODE = 503;

    @Autowired
    public RestHandler(MoviesCacheManager moviesCacheManager) {
        this.moviesCacheManager = moviesCacheManager;
    }

    Mono<ServerResponse> getMovie(ServerRequest request) {
        String movieId = request.pathVariable("movieId");

        log.info("Getting movie id:{}", movieId);

        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(moviesCacheManager.find(movieId)
                        .switchIfEmpty(Mono.error(new MovieNotFoundException())),
                        Movie.class);
    }

    Mono<ServerResponse> getMovies(ServerRequest request) {
        List<String> movieIds = request.queryParams().get("id");

        log.info("Getting movies ids:{}", movieIds);

        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(moviesCacheManager.findAll(movieIds)
                        .switchIfEmpty(Mono.error(new MovieNotFoundException())),
                        Movie.class);
    }

    Mono<ServerResponse> refreshCache(ServerRequest request) {
        moviesCacheManager.importAll().subscribe();

        log.info("Refreshing cache from all importers");

        return ok().build();
    }

    Mono<ServerResponse> isCacheReady(ServerRequest serverRequest) {
        return moviesCacheManager.isCacheReady() ?
                ok().build() :
                status(SERVICE_NOT_AVAILABLE_HTTP_STATUS_CODE).build();
    }

//    Mono<ServerResponse> addTagToMovieId(ServerRequest request) {
//        String tag = request.pathVariable("tag");
//        String movieId = request.pathVariable("movieId");
//
//        log.info("Adding tag:{} to movieId:{}", tag, movieId);
//
//        moviesCacheManager.addTagsToMovie(Set.of(tag), movieId).subscribe();
//
//         return ok().build();
//    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private class MovieNotFoundException extends RuntimeException {}
}