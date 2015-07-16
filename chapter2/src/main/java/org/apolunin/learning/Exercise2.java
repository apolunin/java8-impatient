package org.apolunin.learning;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Collections.nCopies;

/*
 * ################################################################################
 * Task description
 * ################################################################################
 *
 * Verify that asking for the first five long words does not call the filter method
 * once the fifth long word has been found. Simply log each method call.
 *
 * ################################################################################
 */
public class Exercise2 {
    public static void main(final String[] args) {
        final long LIMIT = 5;
        final long WORD_LENGTH = 8;

        final List<String> words = createListOfWords();
        final AtomicLong filteredElementsConsumed = new AtomicLong(0);

        words.stream()
                .filter(w -> w.length() > WORD_LENGTH)
                .limit(LIMIT)
                .peek(s -> filteredElementsConsumed.incrementAndGet())
                .count();

        System.out.println(filteredElementsConsumed.get());
    }

    private static List<String> createListOfWords() {
        final int SIZE = 1024 * 1024 * 32;

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

