package ch.xavier;

import reactor.core.publisher.Flux;

/**
 * Where should I go?
 *
 * @param <T> entity to import
 */
public interface Importer<T> {

    /**
     * Import all entities from source
     */
    Flux<T> importAll();
}
