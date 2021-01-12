package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {

    @Test
    public void backPressureTest() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressureCancelTest() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        integerFlux.subscribe(element -> {
            System.out.println(element);
        }, throwable -> {
            System.err.println(throwable);
        }, () -> {
            System.out.println("DONE");
        }, subscription -> {
            subscription.cancel();
        });
    }

    @Test
    public void backPressureCustomTest() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        integerFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println("Value is " + value);

                if (value == 4) {
                    cancel();
                }
            }
        });
    }
}
