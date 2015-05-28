package org.apolunin.learning;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/*
 * ########################################################################################
 * Task description
 * ########################################################################################
 *
 * Using the listFiles(FileFilter) and isDirectory methods of the java.io.File class,
 * write a method that returns all subdirectories of a given directory.
 * Use a lambda expression instead of a FileFilter object. Repeat with a method expression.
 *
 * ########################################################################################
 */
public class Exercise2 {
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

        final List<File> directories1 = Arrays.asList(directory.listFiles(file -> file.isDirectory()));
        System.out.println("########################################");
        System.out.println("find directories using lambda expression");
        System.out.println("########################################");
        directories1.forEach(System.out::println);

        final List<File> directories2 = Arrays.asList(directory.listFiles(File::isDirectory));
        System.out.println("#######################################");
        System.out.println("find directories using method reference");
        System.out.println("#######################################");
        directories2.forEach(System.out::println);
    }
}
