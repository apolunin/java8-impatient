package org.apolunin.learning;

import java.util.stream.Stream;

/**
 * Using Stream.iterate, make an infinite stream of random numbers — not by calling Math.random but by directly
 * implementing a linear congruential generator. In such a generator, you start with x0 = seed and then produce
 * xn + 1 = (a*xn + c) % m, for appropriate values of a, c, and m. You should implement a method with parameters
 * a, c, m, and seed that yields a Stream<Long>. Try out a = 25214903917, c = 11, and m = 2^48.
 */
public class Exercise5 {
    public static void main(final String[] args) {
        final long a = 25214903917L;
        final long c = 11L;
        final long m = 1L << 48;

        final Stream<Long> randomNumbers = randomSteam(a, c, m, System.currentTimeMillis());

        randomNumbers.limit(16).forEach(System.out::println);
    }

    private static Stream<Long> randomSteam(final long a, final long c, final long m, final long seed) {
        return Stream.iterate(seed, x -> (a * x + c) % m);
    }
}
