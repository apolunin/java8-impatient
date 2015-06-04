package org.apolunin.learning;

/*
 * #############################################################################################################
 * Task description
 * #############################################################################################################
 *
 * Write a static method andThen that takes as parameters two Runnable instances and returns a Runnable
 * that runs the first, then the second. In the main method, pass two lambda expressions into a call to andThen,
 * and run the returned instance.
 *
 * #############################################################################################################
 */
public class Exercise7 {
    public static void main(final String[] args) {
        final Runnable runnable = andThen(
                () -> System.out.println("First Message"),
                () -> System.out.println("Second Message"));

        runnable.run();
    }

    public static Runnable andThen(final Runnable runnable1, final Runnable runnable2) {
        return () -> {
            runnable1.run();
            runnable2.run();
        };
    }
}
