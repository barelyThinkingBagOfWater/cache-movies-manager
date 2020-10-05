package ch.xavier.rest;

import ch.xavier.movies.MoviesRestHandler;
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

    @Bean
    public RouterFunction<ServerResponse> route(MoviesRestHandler handler) {
        return RouterFunctions
                .route(GET("/movie/{movieId}"), handler::getMovie)
                .andRoute(GET("/movies"), handler::getMovies)
                .andRoute(GET("/cache/refresh"), handler::refreshCache)
                .andRoute(GET("/readiness"), handler::isCacheReady);
    }
}