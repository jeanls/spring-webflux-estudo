package com.learnreactivespring.controller.v1;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.exeption.ServerException;
import com.learnreactivespring.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin("*")
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Item> index() {
        return itemRepository.findAll();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public Mono<ResponseEntity<Item>> get(@PathVariable("id") final String id) {
        return itemRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Item>> create(@RequestBody final Item item) {
        return itemRepository.save(item)
                .map(item1 -> new ResponseEntity<>(item1, HttpStatus.CREATED));
    }

    @DeleteMapping(path = "/{id}")
    public Mono<Void> delete(@PathVariable("id")final String id) {
        return itemRepository.deleteById(id);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Item>> update(@RequestBody final Item item) {
        return itemRepository.findById(item.getId())
                .flatMap(currentItem -> {
                    currentItem.setPrice(item.getPrice());
                    currentItem.setDescription(item.getDescription());
                    return itemRepository.save(currentItem);
                }).map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/error")
    public Mono<Void> handleError() {
        return Mono.error(new ServerException("Exception Handler Error"));
    }
}
