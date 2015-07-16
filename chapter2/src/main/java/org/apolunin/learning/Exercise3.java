package org.apolunin.learning;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.nCopies;

/**
 * Measure the difference when counting long words with a parallelStream instead of a stream.
 * Call System.currentTimeMillis before and after the call, and print the difference.
 * Switch to a larger document (such as War and Peace) if you have a fast computer.
 */
public class Exercise3 {
    public static void main(String[] args) {
        final int WORD_LENGTH = 12;
        final List<String> words = createListOfWords();

        final long sequentialStart = System.currentTimeMillis();
        words.stream().filter(w -> w.length() > WORD_LENGTH).count();
        final long sequentialEnd = System.currentTimeMillis();

        final long parallelStart = System.currentTimeMillis();
        words.parallelStream().filter(w -> w.length() > WORD_LENGTH).count();
        final long parallelEnd = System.currentTimeMillis();

        System.out.println("time elapsed (sequential): " + (sequentialEnd - sequentialStart));
        System.out.println("time elapsed (parallel): " + (parallelEnd - parallelStart));
    }

    private static List<String> createListOfWords() {
        final int SIZE = 1024 * 1024 * 256;

        final List<String> smallWords = nCopies(SIZE, "word");
        final List<String> mediumWords = nCopies(SIZE, "occurrence");
        final List<String> longWords = nCopies(SIZE, "internationalization");

        final List<String> allWords = new ArrayList<>(smallWords.size() + mediumWords.size() + longWords.size());

        allWords.addAll(smallWords);
        allWords.addAll(mediumWords);
        allWords.addAll(longWords);

        return allWords;
    }
}
