package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void fluxText() {
        Flux<String> stringFlux = Flux.just("Jean", "Leal", "Silva")
                .concatWith(Flux.error(new RuntimeException("Error in Flux")))
                .concatWith(Flux.just("After error"));

        stringFlux.subscribe(System.out::println, System.err::println, () -> {
            System.out.println("COMPLETED");
        });
    }

    @Test
    public void fluxTestElementsWithoutError() {
        Flux<String> stringFlux = Flux.just("Jean", "Leal", "Silva");

        StepVerifier.create(stringFlux)
                .expectNext("Jean")
                .expectNext("Leal")
                .expectNext("Silva")
                .verifyComplete();
    }

    @Test
    public void fluxTestElementsWithError() {
        Flux<String> stringFlux = Flux.just("Jean", "Leal", "Silva").concatWith(Flux.error(new RuntimeException("Exception has ocurred")));

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
//                .expectNext("Jean")
//                .expectNext("Leal")
//                .expectNext("Silva")
                .expectErrorMessage("Exception has ocurred")
//                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void monoTest() {
        Mono<String> stringMono = Mono.just("Jean Mono");

        StepVerifier.create(stringMono.log())
                .expectNext("Jean Mono")
                .verifyComplete();
    }

    @Test
    public void monoTest_Error() {
        StepVerifier.create(Mono.error(new RuntimeException("Error has occurred")).log())
                .expectError(RuntimeException.class)
                .verify();
    }
}
