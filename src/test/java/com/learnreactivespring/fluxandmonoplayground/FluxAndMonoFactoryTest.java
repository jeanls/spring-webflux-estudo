package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {

    private List<String> names = Arrays.asList("Jean", "Jessica", "Jamila", "Marluce");

    @Test
    public void fluxUsingIterable() {
        Flux<String> namesFlux = Flux.fromIterable(names).log();

        StepVerifier.create(namesFlux)
                .expectNext("Jean", "Jessica", "Jamila", "Marluce")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray() {
        String[] names = new String[]{"Jean", "Jessica", "Jamila", "Marluce"};

        Flux<String> namesFlux = Flux.fromArray(names);

        StepVerifier.create(namesFlux)
                .expectNext("Jean", "Jessica", "Jamila", "Marluce")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream() {
        Flux<String> namesFlux = Flux.fromStream(names.stream());

        StepVerifier.create(namesFlux)
                .expectNext("Jean", "Jessica", "Jamila", "Marluce")
                .verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty() {
        Mono<String> mono = Mono.justOrEmpty(null); //Mono.Empty()

        StepVerifier.create(mono.log())
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier() {
        Supplier<String> stringSupplier = () -> "Jean";

        Mono<String> stringMono = Mono.fromSupplier(stringSupplier);

        StepVerifier.create(stringMono.log())
                .expectNext("Jean")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange() {
       Flux<Integer> integerFlux = Flux.range(1, 5);

       StepVerifier.create(integerFlux.log())
               .expectNext(1, 2, 3, 4, 5)
               .verifyComplete();
    }
}
