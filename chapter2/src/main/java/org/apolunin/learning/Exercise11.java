package org.apolunin.learning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/*
 * #################################################################################################################
 * Task description
 * #################################################################################################################
 *
 * It should be possible to concurrently collect stream results in a single ArrayList, instead of merging
 * multiple array lists, provided it has been constructed with the stream’s size, since concurrent set operations at
 * disjoint positions are threadsafe. How can you achieve that?
 *
 * #################################################################################################################
 */
public class Exercise11 {
    public static void main(final String[] args) {
        final int SIZE = 4096;
        final AtomicInteger insertPosition = new AtomicInteger(0);
        final List<Integer> result = new ArrayList<>(Collections.nCopies(SIZE, 0));
        final Stream<Integer> stream = Stream.iterate(1, x -> x + 1).limit(SIZE);

        stream.parallel().collect(
                () -> result,
                (acc, e) -> acc.set(insertPosition.getAndIncrement(), e),
                (acc1, acc2) -> {});

        result.stream().sorted().forEach(System.out::println);
        System.out.println(result.size());
    }
}
