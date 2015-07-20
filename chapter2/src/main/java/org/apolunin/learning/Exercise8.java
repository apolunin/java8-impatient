package org.apolunin.learning;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/*
 * ##########################################################################################################
 * Task description
 * ##########################################################################################################
 *
 * Write a method public static <T> Stream<T> zip(Stream<T> first, Stream<T> second) that alternates elements
 * from the streams first and second, stopping when one of them runs out of elements.
 *
 * ##########################################################################################################
 */
public class Exercise8 {
    public static void main(final String[] args) {
        testTwoInfiniteStreams();
        testOneInfiniteStream();
        testTwoFiniteStreams();
        testOneEmptyStream();
        testTwoEmptyStreams();
    }

    public static <T> Stream<T> zip(final Stream<T> first, final Stream<T> second) {
        return StreamSupport.stream(new Spliterator<T>() {
            private final Spliterator<T> split1 = first.spliterator();
            private final Spliterator<T> split2 = second.spliterator();
            private Spliterator<T> current = split2;

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                current = (current == split1) ? split2 : split1;

                return current.tryAdvance(action);
            }

            @Override
            public Spliterator<T> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return Long.MAX_VALUE;
            }

            @Override
            public int characteristics() {
                return 0;
            }

        }, false);
    }

    private static void testTwoInfiniteStreams() {
        final Stream<String> stream1 = Stream.generate(() -> "a");
        final Stream<String> stream2 = Stream.generate(() -> "b");
        final Stream<String> zippedStream = zip(stream1, stream2);

        zippedStream.limit(10).forEach(System.out::print);

        System.out.println();
    }

    private static void testOneInfiniteStream() {
        final Stream<String> stream1 = Stream.of("a", "b", "c");
        final Stream<String> stream2 = Stream.generate(() -> "X");
        final Stream<String> zippedStream = zip(stream1, stream2);

        zippedStream.forEach(System.out::print);

        System.out.println();
    }

    private static void testTwoFiniteStreams() {
        final Stream<String> stream1 = Stream.of("a", "b", "c", "d", "e");
        final Stream<String> stream2 = Stream.of("x", "y", "z");
        final Stream<String> zippedStream = zip(stream1, stream2);

        zippedStream.forEach(System.out::print);

        System.out.println();
    }

    private static void testOneEmptyStream() {
        final Stream<String> stream1 = Stream.of("a", "b", "c");
        final Stream<String> stream2 = Stream.empty();
        final Stream<String> zippedStream = zip(stream1, stream2);

        zippedStream.forEach(System.out::print);

        System.out.println();
    }

    private static void testTwoEmptyStreams() {
        final Stream<String> stream1 = Stream.empty();
        final Stream<String> stream2 = Stream.empty();
        final Stream<String> zippedStream = zip(stream1, stream2);

        zippedStream.forEach(System.out::print);

        System.out.println();
    }
}
