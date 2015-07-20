package org.apolunin.learning;

import java.util.*;
import java.util.stream.Stream;

/*
 * ################################################################
 * Task description
 * ################################################################
 *
 * Join all elements in a Stream<ArrayList<T>> to one ArrayList<T>.
 * Show how to do this with the three forms of reduce.
 *
 * ################################################################
 */
public class Exercise9 {
    public static void main(final String[] args) {
        final List<Integer> list1 = Arrays.asList(1, 2, 3);
        final List<Integer> list2 = Arrays.asList(4, 5, 6);
        final List<Integer> list3 = Arrays.asList(7, 8, 9);

        testJoin1(list1, list2, list3);
        testJoin2(list1, list2, list3);
        testJoin3(list1, list2, list3);
    }

    public static <T> Optional<List<T>> join1(final Stream<List<T>> stream) {
        return stream.reduce((list1, list2) -> {
            // we cannot assume list1 or list2 to be modifiable
            final List<T> result = new ArrayList<>(list1.size() + list2.size());

            result.addAll(list1);
            result.addAll(list2);

            return result;
        });
    }

    public static <T> List<T> join2(final Stream<List<T>> stream) {
        return stream.reduce(new ArrayList<>(), (acc, e) -> {
            acc.addAll(e);
            return acc;
        });
    }

    public static <T> List<T> join3(final Stream<List<T>> stream) {
        return stream.reduce(new ArrayList<>(),
                (acc, e) -> {
                    acc.addAll(e);
                    return acc;
                },
                (e1, e2) -> {
                    e1.addAll(e2);
                    return e1;
                }
        );
    }

    @SafeVarargs
    private static <T> void testJoin1(final List<T> ... lists) {
        final Stream<List<T>> stream = Arrays.stream(lists);
        final Optional<List<T>> result = join1(stream);

        result.ifPresent(list -> {
            list.stream().forEach(System.out::print);
            System.out.println();
        });
    }

    @SafeVarargs
    private static <T> void testJoin2(final List<T> ... lists) {
        final Stream<List<T>> stream = Arrays.stream(lists);
        final List<T> result = join2(stream);

        result.forEach(System.out::print);
        System.out.println();
    }

    @SafeVarargs
    private static <T> void testJoin3(final List<T> ... lists) {
        final Stream<List<T>> stream = Arrays.stream(lists);
        final List<T> result = join3(stream);

        result.forEach(System.out::print);
        System.out.println();
    }
}
