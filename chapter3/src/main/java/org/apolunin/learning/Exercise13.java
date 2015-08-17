package org.apolunin.learning;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apolunin.learning.utils.LatentImage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apolunin.learning.utils.ImageUtils.*;

/*
 * ##################################################################################################################
 * Task description
 * ##################################################################################################################
 *
 * Convolution filters such as blur or edge detection compute a pixel from neighboring pixels. To blur an image,
 * replace each color value by the average of itself and its eight neighbors. For edge detection, replace each
 * color value c with 4c – n – e – s – w, where the other colors are those of the pixel to the north, east, south,
 * and west. Note that these cannot be implemented lazily, using the approach of Section 3.6, “Laziness,” on page 56,
 * since they require the image from the previous stage (or at least the neighboring pixels) to have been computed.
 * Enhance the lazy image processing to deal with these operations. Force computation of the previous stage when
 * one of these operators is evaluated.
 *
 * ##################################################################################################################
 */
public class Exercise13 extends Application {
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

                final LatentImage transformedImage = LatentImage.from(originalImage)
                        .transform(Color::brighter).transform(Color::brighter)
                        .blur().blur();

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(transformedImage.toImage(), null),
                            OUT_IMAGE_FORMAT, outImagePath.toFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        System.exit(0);
    }
}

