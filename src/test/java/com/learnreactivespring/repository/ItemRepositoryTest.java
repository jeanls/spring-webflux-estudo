package com.learnreactivespring.repository;

import com.learnreactivespring.document.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

//@DirtiesContext
@DataMongoTest
@RunWith(SpringRunner.class)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    private final List<Item> items = Arrays.asList(
            new Item(null, "LG TV", 420.0),
            new Item(null, "Apple Watch", 299.99),
            new Item(null, "Beats Headphones", 149.99),
            new Item("abc", "Bose Headphones", 200.99)
    );

    @Before
    public void setup() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(itemRepository::save)
                .doOnNext(item -> {
                    System.out.println("Inserted item.: " + item);
                })
                .blockLast();
    }

    @Test
    public void getAllItem() {
        StepVerifier.create(itemRepository.findAll())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void getItemById() {
        StepVerifier.create(itemRepository.findById("abc"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Bose Headphones"))
                .verifyComplete();
    }

    @Test
    public void getItemByDescription() {
        StepVerifier.create(itemRepository.findAllByDescription("Bose Headphones"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Bose Headphones"))
                .verifyComplete();
    }

    @Test
    public void save() {
        Item item = new Item("aaa", "Air dots", 99.00);

        Mono<Item> savedItem = itemRepository.save(item);

        StepVerifier.create(savedItem)
                .expectSubscription()
                .expectNextMatches(item1 -> item1.getId() != null && item1.getDescription().equals("Air dots"))
                .verifyComplete();
    }

    @Test
    public void update() {
        Mono<Item> itemMono = itemRepository.findByDescription("LG TV")
                .map(item -> {
                    item.setPrice(200.99);
                    return item;
                }).flatMap(itemRepository::save);

        StepVerifier.create(itemMono)
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == 200.99)
                .verifyComplete();
    }

    @Test
    public void delete() {
        Mono<Void> mono = itemRepository.findById("abc")
                .flatMap(itemRepository::delete);

        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        Mono<Item> itemMono = itemRepository.findById("abc");

        StepVerifier.create(itemMono)
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void get() {

    }
}