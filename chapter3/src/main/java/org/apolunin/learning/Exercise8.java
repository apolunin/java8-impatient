package org.apolunin.learning;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * ###############################################################################
 * Task description
 * ###############################################################################
 *
 * Generalize Exercise 5 by writing a static method that yields a ColorTransformer
 * that adds a frame of arbitrary thickness and color to an image.
 *
 * ###############################################################################
 */
public class Exercise8 extends Application {
    public static void main(final String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Path inputPath = getInputPath();
        final Path outputPath = getOutputPath();

        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(inputPath)) {
            final String OUT_IMAGE_FORMAT = "png";

            stream.forEach(path -> {
                final Path outImagePath = Paths.get(
                        outputPath.toString(), path.getFileName().toString());

                final Image originalImage = new Image("file:" + path.toString());

                final int width = (int) originalImage.getWidth();
                final int height = (int) originalImage.getHeight();

                final Image transformedImage = transform(originalImage,
                        createTransformer(width, height, 30, Color.RED));

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(transformedImage, null),
                            OUT_IMAGE_FORMAT, outImagePath.toFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        System.exit(0);
    }

    private ColorTransformer createTransformer(final int width, final int height,
            final int thickness, final Color frameColor) {

        return (x, y, color) ->
                    (x >= thickness) &&
                    (x <= width - thickness - 1) &&
                    (y >= thickness) &&
                    (y <= height - thickness - 1) ? color : frameColor;
    }

    private static Image transform(final Image in, final ColorTransformer transformer) {
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

    private static Path getInputPath() {
        final String inputPathPropertyName = "image.input.dir";
        final Path inputPath = Paths.get(System.getProperty(inputPathPropertyName));

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

    private static Path getOutputPath() throws IOException {
        final String outputPathPropertyName = "image.output.dir";
        final Path outputPath = Paths.get(System.getProperty(outputPathPropertyName));

        if (Files.notExists(outputPath)) {
            Files.createDirectory(outputPath);
        }

        if (!Files.isDirectory(outputPath)) {
            System.out.println(outputPath + " is not a directory");
            System.exit(-1);
        }

        return outputPath;
    }

    @FunctionalInterface
    private interface ColorTransformer {
        Color apply(int x, int y, Color colorAtXY);
    }
}

