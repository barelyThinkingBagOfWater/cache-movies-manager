package ch.xavier.movies;

import ch.xavier.common.EntitiesImporter;
import ch.xavier.common.movies.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@ConditionalOnProperty("importer.rest.uri")
public class MoviesRestImporter implements EntitiesImporter<Movie> {

    @Autowired
    private WebClient client;


    private Flux<Movie> getMovies() {
        return client.get().uri("/movies")
                .retrieve()
                .bodyToFlux(Movie.class);
    }

    @Override
    public Flux<Movie> importAll() {
        return getMovies();
    }
}
