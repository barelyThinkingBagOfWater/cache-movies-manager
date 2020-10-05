package ch.xavier.common;

import reactor.core.publisher.Flux;


public interface EntitiesImporter<T> {

    /**
     * Import all entities
     * @return Flux of entities
     */
    Flux<T> importAll();
}
