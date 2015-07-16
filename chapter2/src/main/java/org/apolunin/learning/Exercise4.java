package org.apolunin.learning;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * ##################################################################
 * Task description
 * ##################################################################
 *
 * Suppose you have an array int[] values = { 1, 4, 9, 16 }.
 * What is Stream.of(values)? How do you get a stream of int instead?
 *
 * ##################################################################
 */
public class Exercise4 {
    public static void main(final String[] args) {
        final int[] values = {1, 4, 9, 16};

        final Stream<int[]> arrayStream = Stream.of(values);    // stream of int[]
        final IntStream intStream = Arrays.stream(values);      // stream of int

        final int[] arrayStreamElement = arrayStream.findFirst().get();
        final int intStreamElement = intStream.findFirst().getAsInt();

        System.out.println(arrayStreamElement.length);
        System.out.println(intStreamElement);
    }
}
