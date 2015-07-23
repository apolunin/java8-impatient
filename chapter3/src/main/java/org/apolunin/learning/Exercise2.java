package org.apolunin.learning;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * ################################################################################
 * Task description
 * ################################################################################
 *
 * When you use a ReentrantLock, you are required to lock and unlock with the idiom
 *
 *     myLock.lock();
 *     try {
 *         some action
 *     } finally {
 *         myLock.unlock();
 *     }
 *
 * Provide a method withLock so that one can call
 *
 *     withLock(myLock, () -> { some action })
 *
 * ################################################################################
 */
public class Exercise2 {
    public static void main(final String[] args) throws InterruptedException {
        final Lock lock = new ReentrantLock();

        class PrintJob implements Runnable {
            private final int threadNumber;

            PrintJob(final int threadNumber) {
                this.threadNumber = threadNumber;
            }

            @Override
            public void run() {
                withLock(lock, () -> System.out.println("hello from thread " + threadNumber));
            }
        }

        final Thread[] threads = new Thread[8];

        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new Thread(new PrintJob(i));
        }

        for (final Thread thread : threads) {
            thread.start();
        }

        for (final Thread thread : threads) {
            thread.join();
        }
    }

    public static void withLock(final Lock lock, final Runnable action) {
        lock.lock();

        try {
            action.run();
        } finally {
            lock.unlock();
        }
    }
}
