package ch.xavier.movies.manager.resources;

import ch.xavier.movies.manager.MoviesCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@Slf4j
class RestHandler {

    private final MoviesCacheManager moviesCacheManager;

    @Autowired
    public RestHandler(MoviesCacheManager moviesCacheManager) {
        this.moviesCacheManager = moviesCacheManager;
    }

        Mono<ServerResponse> hello(ServerRequest request) {

        log.info("Hello from Rest Handler!");

        return ok().build();
    }

//    Mono<ServerResponse> getMovie(ServerRequest request) {
//        String movieId = request.pathVariable("movieId");
//
//        log.info("getting movieId:{}", movieId);
//
//        return ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(
//                        moviesCacheManager.fuzzilyFind(movieId),
//                        Movie.class
//                );
//    }
//
//    Mono<ServerResponse> importTagsWithVersion(ServerRequest request) {
//        moviesCacheManager.importAll().subscribe();
//
//        return ok().build();
//    }
//
//    Mono<ServerResponse> addTagToMovieId(ServerRequest request) {
//        String tag = request.pathVariable("tag");
//        String movieId = request.pathVariable("movieId");
//
//        log.info("Adding tag:{} to movieId:{}", tag, movieId);
//
//        moviesCacheManager.addMovieIdsToTag(Set.of(Long.valueOf(movieId)), tag)
//                .flatMap(response -> {
//                    String result = response.getResult().getLowercase();
//                    log.info("result:{}", result);
//                    if (result.equals("created") ||
//                            result.equals("updated")) {
//                        return ok().build();
//                    }
//
//                    return ServerResponse.status(500).build();
//                }).subscribe();
//
//        return notFound().build();
//    }
}