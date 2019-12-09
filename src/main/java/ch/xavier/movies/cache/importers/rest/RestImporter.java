package ch.xavier.movies.cache.importers.rest;

import ch.xavier.movies.cache.importers.MoviesImporter;
import ch.xavier.movies.cache.domain.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@ConditionalOnProperty("importer.rest.uri")
public class RestImporter implements MoviesImporter {

    private final WebClient client;

    @Autowired
    public RestImporter(@Value("${importer.rest.uri}") String uri) {
        client = WebClient.create(uri);
    }


    private Flux<Movie> getMovies() {
        return client.get().uri("/movies-manager/movies")
                .retrieve()
                .bodyToFlux(Movie.class);
    }

    @Override
    public Flux<Movie> importAll() {
        return getMovies();
    }
}
