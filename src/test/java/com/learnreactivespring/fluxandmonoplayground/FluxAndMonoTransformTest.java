package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {

    private final List<String> names = Arrays.asList("Jean", "Jessica", "Jamila", "Marluce");

    @Test
    public void transformUsingMap() {
        Flux<String> stringFlux = Flux.fromIterable(names).map(String::toUpperCase).log();

        StepVerifier.create(stringFlux)
                .expectNext("JEAN", "JESSICA", "JAMILA", "MARLUCE")
                .verifyComplete();
    }

    @Test
    public void transformUsingMapLength() {
        Flux<Integer> integerFlux = Flux.fromIterable(names).map(String::length).log();

        StepVerifier.create(integerFlux)
                .expectNext(4, 7, 6, 7)
                .verifyComplete();
    }

    @Test
    public void transformUsingMapLengthRepeat() {
        Flux<Integer> integerFlux = Flux.fromIterable(names).map(String::length).repeat(1).log();

        StepVerifier.create(integerFlux)
                .expectNext(4, 7, 6, 7, 4, 7, 6, 7)
                .verifyComplete();
    }

    @Test
    public void transformUsingMapFilter() {
        Flux<String> integerFlux = Flux.fromIterable(names)
                .filter(s -> s.length() > 4)
                .map(String::toUpperCase)
                .log();

        StepVerifier.create(integerFlux)
                .expectNext("JESSICA", "JAMILA", "MARLUCE")
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .flatMap(s -> Flux.fromIterable(convertToList(s))).log();
        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMapParallel() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2)
                .flatMap(s -> s.map(this::convertToList)
                        .subscribeOn(parallel())
                        .flatMap(Flux::fromIterable))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMapParallelMaintainOrder() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2)
                .flatMapSequential(s -> s.map(this::convertToList)
                        .subscribeOn(parallel())
                        .flatMap(Flux::fromIterable))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }
}
