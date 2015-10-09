package org.apolunin.learning;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * ###################################################################
 * Task description
 * ###################################################################
 *
 * Supply a static method <T, U> List<U> map(List<T>, Function<T, U>).
 *
 * ###################################################################
 */
public class Exercise20 {
    private static <T, U> List<U> map(
            final List<? extends T> list,
            final Function<? super T, ? extends U> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    public static void main(final String[] args) {
        final List<String> input = Arrays.asList("apple", "orange", "banana");
        final List<String> output = map(input, String::toUpperCase);
        output.forEach(System.out::println);
    }
}
