package com.learnreactivespring.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@CrossOrigin("*")
@RequestMapping("/api")
@RestController
public class FluxAndMonoController {

    @GetMapping(path = "/flux", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Integer> flux () {
        return Flux.just(1, 2, 3, 4);
    }

    @GetMapping(path = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> fluxStream () {
        return Flux.just(1, 2, 3, 4)
                .delayElements(Duration.ofSeconds(1));
    }
}
