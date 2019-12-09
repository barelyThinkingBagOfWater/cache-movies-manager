package ch.xavier.movies.cache.resources;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
@Slf4j
public class RestResource {

    private static final String URL_PREFIX = "/cache-movies-manager";

    @Bean
    public RouterFunction<ServerResponse> route(RestHandler handler) {
        return RouterFunctions
                .route(GET(URL_PREFIX + "/movie/{movieId}"), handler::getMovie)
                .andRoute(GET(URL_PREFIX + "/movies/import"), handler::importAllMovies)
                .andRoute(GET(URL_PREFIX + "/movies"), handler::getMovies)
//                .andRoute(PUT(URL_PREFIX +"/movie/{movieId}/tag/{tag}"), handler::addTagToMovieId)
                ;
    }
}