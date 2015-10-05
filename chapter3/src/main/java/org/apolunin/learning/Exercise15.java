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
 * ################################################################################################################
 * Task description
 * ################################################################################################################
 *
 * Combine the lazy evaluation of Section 3.6, “Laziness,” on page 56, with the parallel evaluation of Section 3.7,
 * “Parallelizing Operations,” on page 57.
 *
 * ################################################################################################################
 */
public class Exercise15 extends Application {
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

                final LatentImage transformedImage = LatentImage.from(originalImage)
                        .transform(Color::brighter).transform(Color::brighter)
                        .blur();

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(transformedImage.toImageParallel(), null),
                            OUT_IMAGE_FORMAT, outImagePath.toFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        System.exit(0);
    }
}
