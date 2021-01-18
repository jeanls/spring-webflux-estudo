package com.learnreactivespring.handler;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.exeption.ServerException;
import com.learnreactivespring.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
@AllArgsConstructor
public class ItemsHandler {

    private final ItemRepository itemRepository;

    public Mono<ServerResponse> index(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(itemRepository.findAll(), Item.class);
    }

    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        final String id = serverRequest.pathVariable("id");
        final Mono<Item> itemMono = itemRepository.findById(id);

        return itemMono.flatMap(item -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(item))
        ).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        Mono<Item> itemMono = serverRequest.bodyToMono(Item.class);
        return itemMono.flatMap(item -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(itemRepository.save(item), Item.class));
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        final String id = serverRequest.pathVariable("id");
        Mono<Item> itemMono = itemRepository.findById(id);

        return itemMono.flatMap(item -> ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        Mono<Item> itemMono = serverRequest.bodyToMono(Item.class);
        return itemMono.flatMap(item -> itemRepository.findById(item.getId())
                .flatMap(itemDb -> {
                    itemDb.setPrice(item.getPrice());
                    itemDb.setDescription(item.getDescription());
                    return itemRepository.save(itemDb);
                })).flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(item)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> error(ServerRequest serverRequest) {
        throw new ServerException("Error in functional router");
    }
}
