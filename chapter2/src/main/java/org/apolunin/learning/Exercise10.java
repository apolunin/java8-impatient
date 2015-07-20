package org.apolunin.learning;

import java.util.Optional;
import java.util.stream.Stream;

/*
 * ###################################################################################
 * Task description
 * ###################################################################################
 *
 * Write a call to reduce that can be used to compute the average of a Stream<Double>.
 * Why can’t you simply compute the sum and divide by count()?
 *
 * ###################################################################################
 */
public class Exercise10 {
    public static void main(final String[] args) {
        testOnFiniteStream();
        testOnEmptyStream();
    }

    private static void testOnFiniteStream() {
        final Stream<Double> stream = Stream.of(1.0, 1.5, 2.5, 4.5, 0.5);
        final Optional<Double> avg = average(stream);

        avg.ifPresent(System.out::println);
    }

    private static void testOnEmptyStream() {
        final Stream<Double> stream = Stream.empty();
        final Optional<Double> avg = average(stream);

        if (!avg.isPresent()) {
            System.out.println("no value");
        }
    }

    /*
     * It is not possible to compute sum and then divide it by count because to compute a sum a terminal operation
     * should be invoked. And count is also a terminal operation. So to compute average using a call to 'reduce'
     * let's create a helper immutable class and collect sum and count simultaneously.
     */
    public static Optional<Double> average(final Stream<Double> stream) {
        class SumAndCount {
            private final double sum;
            private final int count;

            SumAndCount(final double sum, final int count) {
                this.sum = sum;
                this.count = count;
            }

            SumAndCount add(final double value) {
                return new SumAndCount(sum + value, count + 1);
            }

            SumAndCount combine(final SumAndCount value) {
                return new SumAndCount(sum + value.sum, count + value.count);
            }

            double getSum() {
                return sum;
            }

            int getCount() {
                return count;
            }
        }

        final SumAndCount result = stream.reduce(new SumAndCount(0, 0), SumAndCount::add, SumAndCount::combine);

        return (result.getCount() > 0) ? Optional.of(result.getSum() / result.getCount()) : Optional.empty();
    }
}
