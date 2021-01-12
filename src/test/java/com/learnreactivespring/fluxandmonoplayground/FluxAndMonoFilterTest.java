package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {
    private List<String> names = Arrays.asList("Jean", "Jessica", "Jamila", "Marluce");

    @Test
    public void filterTest() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .filter(s -> s.startsWith("J"))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Jean")
                .expectNext("Jessica")
                .expectNext("Jamila")
                .verifyComplete();
    }

    @Test
    public void filterTestLength() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .filter(s -> s.length() > 4)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Jessica", "Jamila", "Marluce")
                .verifyComplete();
    }
}
