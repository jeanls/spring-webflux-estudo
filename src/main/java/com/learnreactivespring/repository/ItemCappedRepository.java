package com.learnreactivespring.repository;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.document.ItemCapped;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ItemCappedRepository extends ReactiveMongoRepository<ItemCapped, String> {

    @Tailable
    Flux<ItemCapped> findItemsBy();
}
