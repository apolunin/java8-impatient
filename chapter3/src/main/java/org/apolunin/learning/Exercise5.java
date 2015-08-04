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
 * #################################################################################################################
 * Task description
 * #################################################################################################################
 *
 * Here is a concrete example of a ColorTransformer. We want to put a frame around an image, like this.
 * First, implement a variant of the transform method of Section 3.3, “Choosing a Functional Interface,” on page 50,
 * with a ColorTransformer instead of an UnaryOperator<Color>. Then call it with an appropriate lambda expression
 * to put a 10 pixel gray frame replacing the pixels on the border of an image.
 *
 * #################################################################################################################
 */
public class Exercise5 extends Application {
    public static void main(final String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Path inputPath = getInputPath();
        final Path outputPath = getOutputPath();

        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(inputPath)) {
            final String OUT_IMAGE_FORMAT = "png";
            final int FRAME_SIZE = 10;

            stream.forEach(path -> {
                final Path outImagePath = Paths.get(
                        outputPath.toString(), path.getFileName().toString());

                final Image originalImage = new Image("file:" + path.toString());

                final int width = (int) originalImage.getWidth();
                final int height = (int) originalImage.getHeight();

                final Image transformedImage = transform(originalImage,
                        (x, y, color) ->
                                (x >= FRAME_SIZE) &&
                                (x <= width - FRAME_SIZE - 1) &&
                                (y >= FRAME_SIZE) &&
                                (y <= height - FRAME_SIZE - 1) ? color : Color.GRAY);

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

