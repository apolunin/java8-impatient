package org.apolunin.learning;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Using the list(FilenameFilter) method of the java.io.File class,
 * write a method that returns all files in a given directory with a given extension.
 * Use a lambda expression, not a FilenameFilter. Which variables from the enclosing scope does it capture?
 */
public class Exercise3 {
    public static void main(String[] args) {
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

        final List<String> fileNames = findFilesWithExtension(directory, "xml");
        fileNames.forEach(System.out::println);
    }

    private static List<String> findFilesWithExtension(final File directory, final String extension) {
        // 'extension' is a free variable which is captured
        return Arrays.asList(directory.list((dir, name) -> name.endsWith(extension)));
    }
}
