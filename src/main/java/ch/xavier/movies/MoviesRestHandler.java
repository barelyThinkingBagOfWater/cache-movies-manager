package ch.xavier.movies;

import ch.xavier.common.movies.Movie;
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
public
class MoviesRestHandler {

    private final MoviesCacheManager moviesCacheManager;

    private final static int SERVICE_NOT_AVAILABLE_HTTP_STATUS_CODE = 503;

    @Autowired
    public MoviesRestHandler(MoviesCacheManager moviesCacheManager) {
        this.moviesCacheManager = moviesCacheManager;
    }

    public Mono<ServerResponse> getMovie(ServerRequest request) {
        String movieId = request.pathVariable("movieId");

        log.info("Getting movie id:{}", movieId);

        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(moviesCacheManager.find(movieId)
                                .switchIfEmpty(Mono.error(new MovieNotFoundException())),
                        Movie.class);
    }

    public Mono<ServerResponse> getMovies(ServerRequest request) {
        List<String> movieIds = request.queryParams().get("id");

        if (movieIds == null || movieIds.isEmpty()) {
            log.error("Query parameters id missing for endpoint /movies, at least one is required");
            throw new MovieNotFoundException();
        }

        log.info("Getting movies ids:{}", movieIds);

        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(moviesCacheManager.findAll(movieIds)
                                .switchIfEmpty(Mono.error(new MovieNotFoundException())),
                        Movie.class);
    }

    public Mono<ServerResponse> refreshCache(ServerRequest request) {
        moviesCacheManager.importAll().subscribe();

        log.info("Refreshing cache from all importers");

        return ok().build();
    }

    public Mono<ServerResponse> isCacheReady(ServerRequest serverRequest) {
        return moviesCacheManager.isCacheReady() ?
                ok().build() :
                status(SERVICE_NOT_AVAILABLE_HTTP_STATUS_CODE).build();
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private static class MovieNotFoundException extends RuntimeException {
    }
}