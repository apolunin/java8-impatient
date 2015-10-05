package org.apolunin.learning;

import java.util.function.Consumer;

/*
 * ##################################################################################################################
 * Task description
 * ##################################################################################################################
 *
 * Implement a doInParallelAsync(Runnable first, Runnable second, Consumer<Throwable>) method that executes first and
 * second in parallel, calling the handler if either method throws an exception.
 *
 * ##################################################################################################################
 */
public class Exercise17 {
    public static void main(final String[] args) {
        final Runnable successTask = () -> {
            synchronized (System.out) {
                System.out.println("task completed successfully");
            }
        };

        final Runnable failureTask = () -> {
            throw new RuntimeException("exception occurred during execution");
        };

        final Consumer<Throwable> handler = e -> {
            synchronized (System.out) {
                System.out.println("error: " + e.getMessage());
            }
        };

        doInParallelAsync(successTask, failureTask, handler);
    }

    private static void doInParallelAsync(final Runnable first,
            final Runnable second, final Consumer<Throwable> handler) {
        doAsync(first, handler);
        doAsync(second, handler);
    }

    private static void doAsync(final Runnable task, final Consumer<Throwable> handler) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Throwable e) {
                    handler.accept(e);
                }
            }
        };

        thread.start();
    }
}
