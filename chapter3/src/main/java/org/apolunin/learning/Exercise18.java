package org.apolunin.learning;

import java.io.IOException;
import java.util.function.Function;

/*
 * #################################################################################################################
 * Task description
 * #################################################################################################################
 *
 * Implement a version of the unchecked method in Section 3.8, “Dealing with Exceptions,” on page 58, that generates
 * a Function<T, U> from a lambda that throws checked exceptions. Note that you will need to find or provide a
 * functional interface whose abstract method throws arbitrary exceptions.
 *
 * #################################################################################################################
 */
public class Exercise18 {
    @FunctionalInterface
    private interface CheckedFunction<T, R> {
        R apply(T arg) throws Exception;
    }

    private static  <T, R> Function<T, R> unchecked(
            final CheckedFunction<? super T, ? extends R> function) {

       return arg -> {
           try {
               return function.apply(arg);
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
       };
    }

    public static void main(final String[] args) {
        unchecked(a -> { throw new IOException("boom!"); }).apply("value");
    }
}
