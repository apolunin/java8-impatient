package org.apolunin.learning;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apolunin.learning.utils.ImageUtils.*;

/*
 * ##############################################################################
 * Task description
 * ##############################################################################
 *
 * Complete the method
 *
 *     public static <T> Image transform(Image in, BiFunction<Color, T> f, T arg)
 *
 * from Section 3.4, “Returning Functions,” on page 53.
 *
 * ##############################################################################
 */
public class Exercise6 extends Application {
    public static void main(final String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Path inputPath = getInputPath();
        final Path outputPath = getOutputPath();

        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(inputPath)) {
            final String OUT_IMAGE_FORMAT = "png";

            stream.forEach(path -> {
                final Path outImagePath = Paths.get(
                        outputPath.toString(), path.getFileName().toString());

                final Image originalImage = new Image("file:" + path.toString());
                final Image transformedImage = transform(originalImage,
                        (color, factor) -> color.deriveColor(0, 1, factor, 1), 1.5);

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
}

