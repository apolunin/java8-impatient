package org.apolunin.learning.utils;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class LatentImage {
    private Image in;

    private List<ColorTransformer> pendingOperations = new ArrayList<>();

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

