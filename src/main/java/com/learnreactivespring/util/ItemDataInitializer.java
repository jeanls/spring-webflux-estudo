package com.learnreactivespring.util;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.repository.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;

    @Override
    public void run(String... args) throws Exception {
        initData();
    }

    private List<Item> getItems() {
        return Arrays.asList(
                new Item(null, "LG TV", 420.0),
                new Item(null, "Apple Watch", 299.99),
                new Item(null, "Beats Headphones", 149.99),
                new Item(null, "Bose Headphones", 200.99)
        );
    }

    private void initData() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(getItems()))
                .flatMap(itemRepository::save)
                .thenMany(itemRepository.findAll())
                .subscribe();
    }
}
