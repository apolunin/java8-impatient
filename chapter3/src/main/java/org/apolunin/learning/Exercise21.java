package org.apolunin.learning;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.*;
import java.util.function.Function;

/*
 * ###################################################################################################################
 * Task description
 * ###################################################################################################################
 *
 * Supply a static method <T, U> Future<U> map(Future<T>, Function<T, U>). Return an object of an anonymous class that
 * implements all methods of the Future interface. In the get methods, invoke the function.
 *
 * ###################################################################################################################
 */
public class Exercise21 {
    public static void main(final String[] args) throws InterruptedException, ExecutionException {
        final int n = 1000000;
        final ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            final Future<BigDecimal> value = executor.submit(() -> {
                // approximate PI using Leibniz series 4 - 4/3 + 4/5 - 4/7 + 4/9 - 4/11 ...
                final BigDecimal four = new BigDecimal(4);

                int sign = -1;
                BigDecimal pi = four;

                for (int i = 1; i <= n; ++i) {
                    pi = pi.add(new BigDecimal(sign).multiply(four).divide(new BigDecimal(2 * i + 1),
                            1024, RoundingMode.HALF_UP));
                    sign = -sign;
                }

                return pi;
            });

            final Future<String> result = map(value,
                    v -> String.format("value of PI after %d iterations: %s", n, v.toPlainString()));


            System.out.println(result.get());
        } finally {
            executor.shutdown();
            executor.awaitTermination(24, TimeUnit.HOURS);
        }
    }

    private static <T, U> Future<U> map(final Future<T> future, final Function<? super T, ? extends U> function) {
        return new Future<U>() {
            @Override
            public boolean cancel(final boolean mayInterruptIfRunning) {
                return future.cancel(mayInterruptIfRunning);
            }

            @Override
            public boolean isCancelled() {
                return future.isCancelled();
            }

            @Override
            public boolean isDone() {
                return future.isDone();
            }

            @Override
            public U get() throws InterruptedException, ExecutionException {
                return function.apply(future.get());
            }

            @Override
            public U get(final long timeout, final TimeUnit unit)
                    throws InterruptedException, ExecutionException, TimeoutException {
                return function.apply(future.get(timeout, unit));
            }
        };
    }
}
