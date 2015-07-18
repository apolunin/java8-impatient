package org.apolunin.learning;

import java.util.stream.Stream;

/*
 * #############################################################################################
 * Task description
 * #############################################################################################
 *
 * Your manager asks you to write a method public static <T> boolean isFinite(Stream<T> stream).
 * Why isn’t that such a good idea? Go ahead and write it anyway.
 *
 * #############################################################################################
 */
public class Exercise7 {
    public static void main(final String[] args) {
        final Stream<Integer> finiteStream = Stream.of(1, 2, 3, 4, 5, 6);
        final Stream<Integer> infiniteStream = Stream.generate(() -> 1);

        isFinite(finiteStream);

        try {
            // throws IllegalStateException: stream has already been operated upon or closed
            finiteStream.count();
        } catch (final IllegalStateException e) {
            e.printStackTrace();
        }

        // runs forever
        isFinite(infiniteStream);
    }

    /*
     * This method is a bad idea because of at least two reasons:
     * 1. In order to count elements in a stream a terminal operation should be invoked.
     *    After this happens stream is no longer usable.
     * 2. If stream is actually infinite, this method will run forever.
     */
    public static <T> boolean isFinite(final Stream<T> stream) {
        return stream.count() <= Long.MAX_VALUE;
    }
}
