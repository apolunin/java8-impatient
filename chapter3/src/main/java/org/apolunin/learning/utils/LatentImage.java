package org.apolunin.learning.utils;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class LatentImage {
    private final Image in;

    private final List<ColorTransformer> pendingOperations = new ArrayList<>();

    private LatentImage(final Image in) {
        this.in = in;
    }

    public static LatentImage from(final Image in) {
        return new LatentImage(in);
    }

    public LatentImage transform(final ColorTransformer operation) {
        return doTransform(operation);
    }

    public LatentImage transform(final UnaryOperator<Color> operation) {
        return doTransform((x, y, color) -> operation.apply(color));
    }

    private LatentImage doTransform(final ColorTransformer operation) {
        pendingOperations.add(operation);
        return this;
    }

    public LatentImage blur() {
        final List<ColorTransformer> precedingOperations = new ArrayList<>(pendingOperations);

        final ColorTransformer op = (x, y, c) -> {
            int count = 0;

            double red   = 0.0;
            double green = 0.0;
            double blue  = 0.0;

            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    final Color color = applyOperations(precedingOperations, x + i, y + j);

                    red   += color.getRed();
                    green += color.getGreen();
                    blue  += color.getBlue();

                    ++count;
                }
            }

            red   /= count;
            green /= count;
            blue  /= count;

            red   = truncateComponent(red);
            green = truncateComponent(green);
            blue  = truncateComponent(blue);

            return new Color(red, green, blue, c.getOpacity());
        };

        pendingOperations.add(op);

        return this;
    }

    public LatentImage edgeDetection() {
        final List<ColorTransformer> precedingOperations = new ArrayList<>(pendingOperations);

        final ColorTransformer op = (x, y, c) -> {
            double red   = c.getRed() * 4;
            double green = c.getGreen() * 4;
            double blue  = c.getBlue() * 4;

            final Color[] neighbours = {
                    applyOperations(precedingOperations, x - 1, y),   // west
                    applyOperations(precedingOperations, x + 1, y),   // east
                    applyOperations(precedingOperations, x, y - 1),   // north
                    applyOperations(precedingOperations, x, y + 1)    // south
            };

            for (final Color color : neighbours) {
                red   -= color.getRed();
                green -= color.getGreen();
                blue  -= color.getBlue();
            }

            red   = truncateComponent(red);
            green = truncateComponent(green);
            blue  = truncateComponent(blue);

            return new Color(red, green, blue, c.getOpacity());
        };

        pendingOperations.add(op);

        return this;
    }

    private double truncateComponent(final double value) {
        return Math.min(Math.max(0, value), 1);
    }

    private Color applyOperations(final List<ColorTransformer> operations, final int x, final int y) {
        final int imageWidth = (int) in.getWidth();
        final int imageHeight = (int) in.getHeight();

        final int xPos = (x + imageWidth) % imageWidth;
        final int yPos = (y + imageHeight) % imageHeight;

        Color color = in.getPixelReader().getColor(xPos, yPos);

        for (final ColorTransformer operation : operations) {
            color = operation.apply(x, y, color);
        }

        return color;
    }

    public Image toImage() {
        final int width = (int) in.getWidth();
        final int height = (int) in.getHeight();
        final WritableImage out = new WritableImage(width, height);

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                Color color = in.getPixelReader().getColor(x, y);

                for (final ColorTransformer op : pendingOperations) {
                    color = op.apply(x, y, color);
                }

                out.getPixelWriter().setColor(x, y, color);
            }
        }

        return out;
    }
}

