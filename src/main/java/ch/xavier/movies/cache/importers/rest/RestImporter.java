package ch.xavier.movies.cache.importers.rest;

import ch.xavier.movies.cache.importers.MoviesImporter;
import ch.xavier.movies.cache.domain.Movie;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Slf4j
@Service
@ConditionalOnProperty("importer.rest.uri")
public class RestImporter implements MoviesImporter {

    private final WebClient client;

    @Autowired
    public RestImporter(@Value("${importer.rest.uri}") String uri) throws SSLException {
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE) //not for prod...
                .build();

        HttpClient httpClient = HttpClient
                .create()
                .secure(sslSpec -> sslSpec.sslContext(sslContext));

        client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(uri)
                .build();
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
