package org.apolunin.learning;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/*
 * #############################################################################################
 * Task description
 * #############################################################################################
 *
 * Consider a class with many JavaFX properties, most of which are never changed from a default.
 * Show how the property can be set up on demand, when it is set to a non-default value or when
 * the xxxProperty() method is called for the first time.
 *
 * #############################################################################################
 */
public class Exercise3 {
    public static void main(String[] args) {
        final Square square = new Square();
        
        square.getSide();
        square.setSide(2.0);
        square.getSide();
    }
}

class Square {
    private double side = 1.0;
    private DoubleProperty sideProperty;

    public void setSide(final double value) {
        if (sideProperty != null) {
            sideProperty.setValue(value);
        } else {
            sideProperty = new SimpleDoubleProperty(value);
        }
    }

    public double getSide() {
        if (sideProperty != null) {
            System.out.println("get side via property");
            return sideProperty.get();
        }

        System.out.println("get side via value");
        return side;
    }

    public DoubleProperty sideProperty() {
        if (sideProperty == null) {
            sideProperty = new SimpleDoubleProperty(side);
        }

        return sideProperty;
    }
}
