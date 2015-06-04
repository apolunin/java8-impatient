package org.apolunin.learning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/*
 * Form a subclass Collection2 from Collection and add a default method void
 *     forEachIf(Consumer<T> action, Predicate<T> filter)
 * that applies action to each element for which filter returns true. How could you use it?
 */
public class Exercise9 {
    public static void main(final String[] args) {
        final Collection2<Integer> intCollection = new MyArrayList<>(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7));

        final Collection2<String> stringCollection = new MyArrayList<>(
                Arrays.asList("Mary", "Had", "a", "Little", "Lamb"));

        // print only odd numbers
        intCollection.forEachIf(
                System.out::println,
                e -> e % 2 != 0);

        // print strings which start with uppercase character
        stringCollection.forEachIf(
                System.out::println,
                e -> !e.isEmpty() && Character.isUpperCase(e.charAt(0)));
    }
}

interface Collection2<E> extends Collection<E> {
    default void forEachIf(final Consumer<E> action, final Predicate<E> filter) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(filter);

        forEach(e -> {
            if (filter.test(e)) {
                action.accept(e);
            }
        });
    }
}

class MyArrayList<E> extends ArrayList<E> implements Collection2<E> {
    public MyArrayList(Collection<? extends E> c) {
        super(c);
    }
}
