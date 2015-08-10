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
 * ##############################################################################################
 * Task description
 * ##############################################################################################
 *
 * Enhance the LatentImage class in Section 3.6, “Laziness,” on page 56, so that it supports both
 * UnaryOperator<Color> and ColorTransformer. Hint: Adapt the former to the latter.
 *
 * ##############################################################################################
 */
public class Exercise12 extends Application {
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

                final int width = (int) originalImage.getWidth();
                final int height = (int) originalImage.getHeight();

                final LatentImage transformedImage = LatentImage.from(originalImage)
                        .transform(Color::brighter)
                        .transform(createTransformer(width, height, 10, Color.GRAY));

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

