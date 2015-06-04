package org.apolunin.learning;

/*
 * #################################################################################################################
 * Task description
 * #################################################################################################################
 *
 * Didn’t you always hate it that you had to deal with checked exceptions in a Runnable?
 * Write a method uncheck that catches all checked exceptions and turns them into unchecked exceptions. For example,
 *
 * new Thread(uncheck(
 *     () -> { System.out.println("Zzz"); Thread.sleep(1000); })).start();
 *         // Look, no catch (InterruptedException)!
 *
 * Hint: Define an interface RunnableEx whose run method may throw any exceptions.
 * Then implement public static Runnable uncheck(RunnableEx runner). Use a lambda expression
 * inside the uncheck function. Why can’t you just use Callable<Void> instead of RunnableEx?
 *
 * #################################################################################################################
 */
public class Exercise6 {
    public static void main(final String[] args) {
        final Thread thread = new Thread(uncheck(() -> {
            System.out.println("A message from the second thread");
            Thread.sleep(1000);
        }));

        uncheck(() -> {
            thread.start();
            thread.join();
            System.out.println("A message from the first thread");
        }).run();
    }

    // Callable<Void> cannot be used, because 'Void' class and 'void' keyword mean different things
    public static Runnable uncheck(final RunnableEx runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}

@FunctionalInterface
interface RunnableEx {
    void run() throws Exception;
}
