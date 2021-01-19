package com.learnreactivespring.handler;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
public class ItemsHandlerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ItemRepository itemRepository;

    private List<Item> getItems() {
        return Arrays.asList(
                new Item(null, "LG TV", 420.0),
                new Item(null, "Apple Watch", 299.99),
                new Item(null, "Beats Headphones", 149.99),
                new Item("abc", "Bose Headphones", 200.99)
        );
    }

    @Before
    public void setup() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(getItems()))
                .flatMap(itemRepository::save)
                .blockLast();
    }

    @Test
    public void indexTest() {
        webTestClient.get()
                .uri("/v2/items")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item.class)
                .hasSize(4);
    }

    @Test
    public void getTest() {
        webTestClient.get()
                .uri("/v2/items/abc")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.id").isEqualTo("abc")
                .jsonPath("$.description").isEqualTo("Bose Headphones")
                .jsonPath("$.price").isEqualTo(200.99);
    }

    @Test
    public void getTestNotFound() {
        webTestClient.get()
                .uri("/v2/items/aaa")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createTest() {

        final Item item = new Item(null, "Product create", 99.00);

        webTestClient.post()
                .uri("/v2/items")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo(item.getDescription())
                .jsonPath("$.price").isEqualTo(item.getPrice());

    }

    @Test
    public void deleteTest() {
        webTestClient.get()
                .uri("/v2/items/".concat("{id}"), "abc")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void deleteNotFoundTest() {
        webTestClient.get()
                .uri("/v2/items/".concat("{id}"), "ccc")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void updateTest() {
        Item item = new Item("abc", "Bose Headphones XOC", 300.99);
        webTestClient.put()
                .uri("/v2/items")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(item.getId())
                .jsonPath("$.description").isEqualTo(item.getDescription())
                .jsonPath("$.price").isEqualTo(item.getPrice());
    }

    @Test
    public void updateNotFoundTest() {
        Item item = new Item("aaaa", "Bose Headphones XOC", 300.99);
        webTestClient.put()
                .uri("/v2/items")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void errorTest() {
        webTestClient.get()
                .uri("/v2/error")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}