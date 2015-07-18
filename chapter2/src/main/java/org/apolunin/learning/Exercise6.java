package org.apolunin.learning;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * ###################################################################################################################
 * Task description
 * ###################################################################################################################
 *
 * The characterStream method in Section 2.3, “The filter, map, and flatMap Methods,” on page 25, was a bit clumsy,
 * first filling an array list and then turning it into a stream. Write a stream-based one-liner instead. One approach
 * is to make a stream of integers from 0 to s.length() - 1 and map that with the s::charAt method reference.
 *
 * ###################################################################################################################
 */
public class Exercise6 {
    public static void main(final String[] args) {
        characterStream("Java rocks").forEach(System.out::println);
    }

    private static Stream<Character> characterStream(final String string) {
        return IntStream.range(0, string.length()).mapToObj(string::charAt);
    }
}
