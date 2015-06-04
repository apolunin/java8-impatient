package org.apolunin.learning;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/*
 * ###################################################################################################
 * Task description
 * ###################################################################################################
 *
 * Given an array of File objects, sort it so that the directories come before the files,
 * and within each group, elements are sorted by path name. Use a lambda expression, not a Comparator.
 *
 * ###################################################################################################
 */
public class Exercise4 {
    public static void main(final String[] args) {
        final String propertyName = "directory.path";
        final String path = System.getProperty(propertyName);

        if (path == null || path.isEmpty()) {
            System.out.println(String.format("%s property should be provided", propertyName));
            return;
        }

        final File directory = new File(path);

        if (!directory.isDirectory()) {
            System.out.println(String.format("%s is not a directory", path));
            return;
        }

        final List<File> files = sort(directory.listFiles());
        files.forEach(System.out::println);
    }

    private static List<File> sort(final File[] files) {
        Arrays.sort(files, (final File file1, final File file2) -> {
            final int compareResult = file1.isDirectory() ?
                    file2.isDirectory() ? file1.getAbsolutePath().compareTo(file2.getAbsolutePath()) : -1 :
                    file2.isDirectory() ? 1 : file1.getAbsolutePath().compareTo(file2.getAbsolutePath());

            return compareResult;
        });

        return Arrays.asList(files);
    }
}
