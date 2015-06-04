package org.apolunin.learning;

import java.util.ArrayList;
import java.util.List;

/*
 * ######################################################################################################
 * Task description
 * ######################################################################################################
 *
 * What happens when a lambda expression captures values in an enhanced for loop such as this one?
 * String[] names = { "Peter", "Paul", "Mary" };
 * List<Runnable> runners = new ArrayList<>();
 * for (String name : names)
 *     runners.add(() -> System.out.println(name));
 * Is it legal? Does each lambda expression capture a different value, or do they all get the last value?
 * What happens if you use a traditional loop for (int i = 0; i < names.length; i++)?
 *
 * ######################################################################################################
 */
public class Exercise8 {
    public static void main(final String[] args) {
        enhancedForLoop();
        traditionalForLoop();
    }

    private static void enhancedForLoop() {
        final String[] names = { "Peter", "Paul", "Mary" };
        final List<Runnable> runners = new ArrayList<>();

        // enhanced for loop will work fine, because 'name' is effectively final
        // ('name' cannot be modified inside body of the loop, hence lambda-expression can capture it)
        for (final String name : names) {
            runners.add(() -> System.out.println(name));
        }

        runners.forEach(Runnable::run);
    }

    private static void traditionalForLoop() {
        final String[] names = { "Peter", "Paul", "Mary" };
        final List<Runnable> runners = new ArrayList<>();

        // traditional for loop will not work, because names[i] is not considered effectively final
        // (names[i] can be modified inside body of the loop, hence lambda-expression cannot capture it)
        // for (int i = 0; i < names.length; ++i) {
        //    runners.add(() -> System.out.println(names[i]));
        // }

        runners.forEach(Runnable::run);
    }
}
