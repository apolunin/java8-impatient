package org.apolunin.learning;

import java.util.*;
import java.util.function.Function;

import static org.apolunin.learning.CompareOptions.*;

/*
 * ####################################################################################################
 * Task description
 * ####################################################################################################
 *
 * Write a method that generates a Comparator<String> that can be normal or reversed, case-sensitive or
 * case-insensitive, space-sensitive or space-insensitive, or any combination thereof.
 * Your method should return a lambda expression.
 *
 * NOTE: requirement of returning lambda expression has been relaxed intentionally.
 *
 * ####################################################################################################
 */
public class Exercise7 {
    public static void main(final String[] args) {
        final List<String> strings = Arrays.asList(
                "Java", "assembly", "c++", "LISP", "scheme", "Racket", "sml",
                "3+2", "3 + 2", "  3  +  2  ", "  5\t+\t6  ");

        runSort(strings, EnumSet.of(REVERSED));
        runSort(strings, EnumSet.of(REVERSED, IGNORE_CASE));
        runSort(strings, EnumSet.of(REVERSED, IGNORE_SPACE));
        runSort(strings, EnumSet.of(REVERSED, IGNORE_CASE, IGNORE_SPACE));
    }

    private static void runSort(final List<String> data, final Set<CompareOptions> options) {
        System.out.println("================================================");
        data.stream().sorted(comparator(options)).forEach(System.out::println);
        System.out.println("================================================");
    }

    public static Comparator<String> comparator(final Set<CompareOptions> options) {
        return options.stream()
                .map(CompareOptions::transform)
                .reduce(Function.identity(), Function::andThen)
                .apply(Comparator.naturalOrder());
    }
}

enum CompareOptions {
    REVERSED(Function.<String>identity()) {
        @Override
        public Function<Comparator<String>, Comparator<String>> transform() {
            return Comparator::reversed;
        }
    },
    IGNORE_CASE(String::toLowerCase),
    IGNORE_SPACE(s -> s.replaceAll("\\s", ""));

    private final Function<String, String> argTransformer;

    CompareOptions(final Function<String, String> argTransformer) {
        this.argTransformer = argTransformer;
    }

    public Function<Comparator<String>, Comparator<String>> transform() {
        return c -> (x, y) -> c.compare(argTransformer.apply(x), argTransformer.apply(y));
    }
}
