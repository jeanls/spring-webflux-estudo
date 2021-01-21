package com.learnreactivespring.controller;

import com.learnreactivespring.document.ItemCapped;
import com.learnreactivespring.repository.ItemCappedRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
public class StreamControllerTest {

    @Autowired
    private ItemCappedRepository itemCappedRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private WebTestClient webTestClient;

    @Before
    public void setup() {
        mongoOperations.dropCollection(ItemCapped.class);
        mongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped());

        Flux<ItemCapped> flux = Flux.interval(Duration.ofMillis(100))
                .map(aLong -> new ItemCapped(null, "Random item " + aLong, 100.99 + aLong))
                .take(5);

        itemCappedRepository.insert(flux)
                .doOnNext(itemCapped -> System.out.println("Inserted item.: " + itemCapped))
                .blockLast();
    }

    @Test
    public void testStreamEndpoint() {
        Flux<ItemCapped> flux = webTestClient.get()
                .uri("/stream")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ItemCapped.class)
                .getResponseBody()
                .take(5);

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNextCount(5)
                .thenCancel()
                .verify();
    }
}