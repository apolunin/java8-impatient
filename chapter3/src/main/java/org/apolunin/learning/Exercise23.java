package org.apolunin.learning;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/*
 * #######################################################################################
 * Task description
 * #######################################################################################
 *
 * Define a map operation for a class Pair<T> that represents a pair of objects of type T.
 *
 * #######################################################################################
 */
public class Exercise23 {
    private static class Pair<T> {
        private final T first;
        private final T second;

        Pair(final T first, final T second) {
            this.first = first;
            this.second = second;
        }

        T getFirst() {
            return first;
        }

        T getSecond() {
            return second;
        }

        <U> Pair<U> map(final Function<? super T, ? extends U> function) {
            Objects.requireNonNull(function);
            return new Pair<>(function.apply(first), function.apply(second));
        }

        <U> Pair<U> flatMap(final BiFunction<? super T, ? super T, Pair<U>> function) {
            Objects.requireNonNull(function);
            return function.apply(getFirst(), getSecond());
        }

        @Override
        public String toString() {
            return String.format("{%s, %s}", first, second);
        }
    }

    public static void main(final String[] args) {
        final Pair<Integer> in = new Pair<>(4, 9);
        final Pair<Double> out1 = in.map(Math::sqrt);
        final Pair<Double> out2 = in.flatMap((x, y) -> new Pair<>(Math.sqrt(x), Math.pow(y, 2)));

        System.out.println(in);
        System.out.println(out1);
        System.out.println(out2);
    }
}
