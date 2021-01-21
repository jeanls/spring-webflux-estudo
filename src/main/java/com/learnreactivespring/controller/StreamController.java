package com.learnreactivespring.controller;

import com.learnreactivespring.document.ItemCapped;
import com.learnreactivespring.repository.ItemCappedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@RestController
@RequestMapping("/stream")
public class StreamController {

    private final ItemCappedRepository itemCappedRepository;

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<ItemCapped> getItemsStream() {
        return itemCappedRepository.findItemsBy();
    }
}
