package org.apolunin.learning;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/*
 * ###################################################################################################################
 * Task description
 * ###################################################################################################################
 *
 * Implement the doInOrderAsync of Section 3.8, “Dealing with Exceptions,” on page 58, where the second parameter is a
 * BiConsumer<T, Throwable>. Provide a plausible use case. Do you still need the third parameter?
 *
 * ###################################################################################################################
 */
public class Exercise16 {
    public static void main(final String[] args) {
        readUserNamesFromDatabase(() -> Arrays.asList("John", "Jack", "Jane"));
        readUserNamesFromDatabase(() -> {
            throw new RuntimeException("cannot access database");
        });
    }

    private static <T> void doInOrderAsync(final Supplier<? extends T> first,
            final BiConsumer<? super T, ? super Throwable> second) {

        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    final T result = first.get();
                    second.accept(result, null);
                } catch (Throwable e) {
                    second.accept(null, e);
                }
            }
        };

        thread.start();
    }

    private static void readUserNamesFromDatabase(final Supplier<List<String>> reader) {
        doInOrderAsync(reader, (data, e) -> {
            synchronized (System.out) {
                if (e != null) {
                    System.out.println("error: " + e.getMessage());
                } else {
                    data.forEach(System.out::println);
                }
            }
        });
    }
}
