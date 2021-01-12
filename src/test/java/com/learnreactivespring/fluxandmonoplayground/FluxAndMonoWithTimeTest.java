package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {

//    @Test
//    public void infiniteSequenceTest() throws InterruptedException {
//        Flux<Long> longFlux = Flux.interval(Duration.ofMillis(200)).log(); // 0 -> n
//
//        longFlux.subscribe(System.out::println);
//
//        Thread.sleep(3000);
//    }

//    @Test
//    public void infiniteSequenceTakeTest() throws InterruptedException {
//        Flux<Long> longFlux = Flux.interval(Duration.ofMillis(100)).take(3).log(); // 0 -> n
//
//        StepVerifier.create(longFlux)
//                .expectSubscription()
//                .expectNext(0L, 1L, 2L)
//                .verifyComplete();
//    }
}
