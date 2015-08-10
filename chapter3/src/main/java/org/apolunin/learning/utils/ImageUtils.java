package org.apolunin.learning.utils;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiFunction;

public interface ImageUtils {
    String INPUT_PATH_PROPERTY = "image.input.dir";
    String OUTPUT_PATH_PROPERTY = "image.output.dir";

    static Image transform(final Image in, final ColorTransformer transformer) {
        final int width = (int) in.getWidth();
        final int height = (int) in.getHeight();

        final WritableImage out = new WritableImage(width, height);

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                out.getPixelWriter().setColor(x, y,
                        transformer.apply(x, y, in.getPixelReader().getColor(x, y)));
            }
        }

        return out;
    }

    static <T> Image transform(final Image in, final BiFunction<Color, T, Color> function, final T arg) {
        final int width = (int) in.getWidth();
        final int height = (int) in.getHeight();

        final WritableImage out = new WritableImage(width, height);

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                out.getPixelWriter().setColor(x, y,
                        function.apply(in.getPixelReader().getColor(x, y), arg));
            }
        }

        return out;
    }

    static Path getInputPath() {
        final Path inputPath = Paths.get(System.getProperty(INPUT_PATH_PROPERTY));

        if (Files.notExists(inputPath)) {
            System.out.println("input path doesn't exist: " + inputPath);
            System.exit(-1);
        }

        if (!Files.isDirectory(inputPath)) {
            System.out.println(inputPath + " is not a directory");
            System.exit(-1);
        }

        return inputPath;
    }

    static Path getOutputPath() throws IOException {
        final Path outputPath = Paths.get(System.getProperty(OUTPUT_PATH_PROPERTY));

        if (Files.notExists(outputPath)) {
            Files.createDirectory(outputPath);
        }

        if (!Files.isDirectory(outputPath)) {
            System.out.println(outputPath + " is not a directory");
            System.exit(-1);
        }

        return outputPath;
    }
}

