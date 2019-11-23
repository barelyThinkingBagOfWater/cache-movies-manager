package ch.xavier.movies.manager.resources;

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
                .route(GET(URL_PREFIX + "/movies/hello"),
                        handler::hello);
    }
}