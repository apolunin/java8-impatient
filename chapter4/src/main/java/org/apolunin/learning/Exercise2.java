package org.apolunin.learning;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
 * ###############################################################################################################
 * Task description
 * ###############################################################################################################
 *
 * Consider a class with many JavaFX properties, such as a chart or table.
 * Chances are that in a particular application, most properties never have listeners attached to them.
 * It is therefore wasteful to have a property object per property. Show how the property can be set up on demand,
 * first using a regular field for storing the property value, and then using a property object only when
 * the xxxProperty() method is called for the first time.
 *
 * ###############################################################################################################
 */
public class Exercise2 {
    public static void main(final String[] args) {
        final Person person = new Person("John", "Doe");

        person.getFirstName();
        person.getLastName();
        person.setFirstName("Jane");
        person.setLastName("Smith");
    }
}

class Person {
    private String firstNameValue;
    private StringProperty firstNameProperty;

    private String lastNameValue;
    private StringProperty lastNameProperty;

    public Person(final String firstName, final String lastName) {
        firstNameValue = firstName;
        lastNameValue = lastName;
        lastNameProperty();
    }

    public String getFirstName() {
        if (firstNameProperty != null) {
            System.out.println("getting first name via property");
            return firstNameProperty.get();
        }

        System.out.println("getting first name via value");
        return firstNameValue;
    }

    public void setFirstName(final String firstName) {
        if (firstNameProperty != null) {
            System.out.println("setting first name via property");
            firstNameProperty.set(firstName);
        } else {
            System.out.println("setting first name via value");
            firstNameValue = firstName;
        }
    }

    public StringProperty firstNameProperty() {
        if (firstNameProperty == null) {
            firstNameProperty = new SimpleStringProperty(firstNameValue);
            firstNameValue = null;
        }

        return firstNameProperty;
    }
    public String getLastName() {
        if (lastNameProperty != null) {
            System.out.println("getting last name via property");
            return lastNameProperty.get();
        }

        System.out.println("getting last name via value");
        return lastNameValue;
    }

    public void setLastName(final String lastName) {
        if (lastNameProperty != null) {
            System.out.println("setting last name via property");
            lastNameProperty.set(lastName);
        } else {
            System.out.println("setting last name via value");
            lastNameValue = lastName;
        }
    }

    public StringProperty lastNameProperty() {
        if (lastNameProperty == null) {
            lastNameProperty = new SimpleStringProperty(lastNameValue);
            lastNameValue = null;
        }

        return lastNameProperty;
    }
}

