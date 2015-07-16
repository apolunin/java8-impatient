package org.apolunin.learning;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.nCopies;

/**
 * Write a parallel version of the for loop in Section 2.1, “From Iteration to Stream Operations,” on page 22.
 * Obtain the number of processors. Make that many separate threads, each working on a segment of the list, and
 * total up the results as they come in. (You don’t want the threads to update a single counter. Why?)
 *
 * NOTE: ExecutorService, Future and other Java 5 goodies are not used intentionally.
 */
public class Exercise1 {
    public static void main(final String[] args) throws InterruptedException {
        final int wordLength = 12;
        final List<String> words = createListOfWords();

        System.out.println(countWords(words, wordLength));
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

    private static long countWords(final List<String> words, final int wordLength) throws InterruptedException {
        final int allWordsCount = words.size();
        final int numberOfCores = Runtime.getRuntime().availableProcessors();

        long wordsCount;

        if (numberOfCores > allWordsCount) {
            // there are so much cores and so little words, let's use a single core
            final WordCounterJob counterJob = new WordCounterJob(words, wordLength);

            counterJob.start();
            counterJob.join();

            wordsCount = counterJob.getWordsCount();
        } else {
            final int chunkSize = allWordsCount / numberOfCores;
            final List<WordCounterJob> counterJobs = new ArrayList<>(numberOfCores);

            // create all jobs
            for (int i = 1; i <= numberOfCores; ++i) {
                final int fromIndex = (i - 1) * chunkSize;
                final int toIndex = (i == numberOfCores) ? allWordsCount : (i * chunkSize);
                final WordCounterJob job = new WordCounterJob(words.subList(fromIndex, toIndex), wordLength);

                counterJobs.add(job);
            }

            counterJobs.forEach(Thread::start);

            // wait for all threads to finish
            for (final WordCounterJob job : counterJobs) {
                job.join();
            }

            // collect results
            wordsCount = counterJobs.stream().mapToLong(WordCounterJob::getWordsCount).sum();
        }

        return wordsCount;
    }

    private static class WordCounterJob extends Thread {

        private final List<String> words;

        private final int wordLength;

        private long wordsCount = 0;

        WordCounterJob(final List<String> words, final int wordLength) {
            this.words = words;
            this.wordLength = wordLength;
        }

        @Override
        public void run() {
            wordsCount = words.stream().filter(word -> word.length() >= wordLength).count();
        }

        public long getWordsCount() {
            return wordsCount;
        }

    }
}

