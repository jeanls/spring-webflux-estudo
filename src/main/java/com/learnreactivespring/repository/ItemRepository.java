package com.learnreactivespring.repository;

import com.learnreactivespring.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemRepository extends ReactiveMongoRepository<Item, String> {

    Flux<Item> findAllByDescription(String description);

    Mono<Item> findByDescription(String description);

}
