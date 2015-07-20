package org.apolunin.learning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * #################################################################################################################
 * Task description
 * #################################################################################################################
 *
 * Count all short words in a parallel Stream<String>, as described in Section 2.13, “Parallel Streams,” on page 40,
 * by updating an array of AtomicInteger. Use the atomic getAndIncrement method to safely increment each counter.
 *
 * #################################################################################################################
 */
public class Exercise12 {
    public static void main(final String[] args) {
        final List<String> wordsOfLength1  = Collections.nCopies(1,  "I");
        final List<String> wordsOfLength2  = Collections.nCopies(2,  "me");
        final List<String> wordsOfLength3  = Collections.nCopies(3,  "cat");
        final List<String> wordsOfLength4  = Collections.nCopies(4,  "rock");
        final List<String> wordsOfLength5  = Collections.nCopies(5,  "bread");
        final List<String> wordsOfLength6  = Collections.nCopies(6,  "butter");
        final List<String> wordsOfLength7  = Collections.nCopies(7,  "blocker");
        final List<String> wordsOfLength8  = Collections.nCopies(8,  "splitter");
        final List<String> wordsOfLength9  = Collections.nCopies(9,  "something");
        final List<String> wordsOfLength10 = Collections.nCopies(10, "helicopter");
        final List<String> wordsOfLength11 = Collections.nCopies(11, "lamborghini");
        final List<String> wordsOfLength12 = Collections.nCopies(12, "circumstance");

        final List<String> allWords = new ArrayList<>();

        allWords.addAll(wordsOfLength1);
        allWords.addAll(wordsOfLength2);
        allWords.addAll(wordsOfLength3);
        allWords.addAll(wordsOfLength4);
        allWords.addAll(wordsOfLength5);
        allWords.addAll(wordsOfLength6);
        allWords.addAll(wordsOfLength7);
        allWords.addAll(wordsOfLength8);
        allWords.addAll(wordsOfLength9);
        allWords.addAll(wordsOfLength10);
        allWords.addAll(wordsOfLength11);
        allWords.addAll(wordsOfLength12);

        final AtomicInteger[] shortWords = new AtomicInteger[12];

        for (int i = 0; i < shortWords.length; ++i) {
            shortWords[i] = new AtomicInteger(0);
        }

        allWords.parallelStream().forEach(word -> {
            if (word.length() < 12) {
                shortWords[word.length()].getAndIncrement();
            }
        });

        for (final AtomicInteger count : shortWords) {
            System.out.println(count.get());
        }
    }
}
