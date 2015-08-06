package org.apolunin.learning;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/*
 * ######################################################################################################
 * Task description
 * ######################################################################################################
 *
 * Write a method lexicographicComparator(String... fieldNames) that yields a comparator that compares
 * the given fields in the given order. For example, a lexicographicComparator("lastname", "firstname")
 * takes two objects and, using reflection, gets the values of the lastname field. If they are different,
 * return the difference, otherwise move on to the firstname field. If all fields match, return 0.
 *
 * ######################################################################################################
 */
public class Exercise9 {
    public static void main(final String[] args){
        final List<Person> persons = Arrays.asList(
                new Person("John", "Doe", 23, 180),
                new Person("Jane", "Doe", 25, 180),
                new Person("Bill", "Smith", 33, 173),
                new Person("John", "Black", 33, 175));

        final Comparator<Person> c1 = lexicographicComparator("firstName", "lastName");
        final Comparator<Person> c2 = lexicographicComparator("firstName", "age", "height");
        final Comparator<Person> c3 = lexicographicComparator("age", "height");
        final Comparator<Person> c4 = lexicographicComparator("height", "age");

        runSort(persons, c1);
        runSort(persons, c2);
        runSort(persons, c3);
        runSort(persons, c4);
    }

    public static <T> Comparator<T> lexicographicComparator(final String... fieldNames) {
        return Arrays.stream(fieldNames).map(name -> Comparator.<T, Comparable>comparing(object -> {
            try {
                final Field field = object.getClass().getDeclaredField(name);
                field.setAccessible(true);
                return (Comparable<?>) field.get(object);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        })).reduce(Comparator::thenComparing).get();
    }

    private static void runSort(final List<Person> persons, final Comparator<Person> comparator) {
        persons.stream().sorted(comparator).forEach(System.out::println);
        System.out.println("======================");
    }

    private static class Person {
        private final String firstName;
        private final String lastName;
        private final int age;
        private final int height;

        public Person(final String firstName, final String lastName, final int age, final int height) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.height = height;
        }

        @Override
        public String toString() {
            return String.format("[%s, %s, %d, %d]", firstName, lastName, age, height);
        }
    }
}

