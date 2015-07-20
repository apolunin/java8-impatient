package org.apolunin.learning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

/*
 * ###############################################################################################
 * Task description
 * ###############################################################################################
 *
 * Repeat the preceding exercise, but filter out the short strings and use the collect method with
 * Collectors.groupingBy and Collectors.counting.
 *
 * ###############################################################################################
 */
public class Exercise13 {
    public static void main(final String[] args) {
        final List<String> wordsOfLength1  = Collections.nCopies(11,  "I");
        final List<String> wordsOfLength2  = Collections.nCopies(12,  "me");
        final List<String> wordsOfLength3  = Collections.nCopies(13,  "cat");
        final List<String> wordsOfLength4  = Collections.nCopies(14,  "rock");
        final List<String> wordsOfLength5  = Collections.nCopies(15,  "bread");
        final List<String> wordsOfLength6  = Collections.nCopies(16,  "butter");
        final List<String> wordsOfLength7  = Collections.nCopies(17,  "blocker");
        final List<String> wordsOfLength8  = Collections.nCopies(18,  "splitter");
        final List<String> wordsOfLength9  = Collections.nCopies(19,  "something");
        final List<String> wordsOfLength10 = Collections.nCopies(110, "helicopter");
        final List<String> wordsOfLength11 = Collections.nCopies(111, "lamborghini");
        final List<String> wordsOfLength12 = Collections.nCopies(112, "circumstance");

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

        final Map<Integer, Long> shortWords = allWords.parallelStream()
                .filter(word -> word.length() < 12)
                .collect(groupingBy(String::length, counting()));

        shortWords.forEach((length, count) -> System.out.println("length: " + length + ", count: " + count));

    }
}
