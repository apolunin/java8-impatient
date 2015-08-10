package org.apolunin.learning;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apolunin.learning.utils.ColorTransformer;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.UnaryOperator;

import static org.apolunin.learning.utils.ImageUtils.*;

/*
 * ############################################################################################################
 * Task description
 * ############################################################################################################
 *
 * Implement static methods that can compose two ColorTransformer objects, and a static method that turns
 * a UnaryOperator<Color> into a ColorTransformer that ignores the x- and y-coordinates. Then use these methods
 * to add a gray frame to a brightened image. (See Exercise 5 for the gray frame.)
 *
 * ############################################################################################################
 */
public class Exercise11 extends Application {
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
                        compose(wrap(Color::brighter),
                                createTransformer(width, height, 10, Color.GRAY)));

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

    public static ColorTransformer compose(final ColorTransformer before, final ColorTransformer after) {
        return (x, y, color) -> after.apply(x, y, before.apply(x, y, color));
    }

    public static ColorTransformer wrap(final UnaryOperator<Color> op) {
        return (x, y, color) -> op.apply(color);
    }
}

