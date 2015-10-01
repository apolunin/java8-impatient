package org.apolunin.learning;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import static org.apolunin.learning.utils.ImageUtils.*;

/*
 * ####################################################################################################################
 * Task description
 * ####################################################################################################################
 *
 * To deal with lazy evaluation on a per-pixel basis, change the transformers so that they are passed a PixelReader
 * object from which they can read other pixels in the image. For example,
 *
 *     (x, y, reader) -> reader.get(width - x, y)
 *
 * is a mirroring operation. The convolution filters from the preceding exercises can be easily implemented in terms of
 * such a reader. The straightforward operations would simply have the form
 *
 *     (x, y, reader) -> reader.get(x, y).grayscale(),
 *
 * and you can provide an adapter from UnaryOperation<Color>. A PixelReader is at a particular level in the pipeline of
 * operations. Keep a cache of recently read pixels at each level in the pipeline. If a reader is asked for a pixel,
 * it looks in the cache (or in the original image at level 0); if that fails, it constructs a reader that asks the
 * previous transform.
 *
 * ####################################################################################################################
 */
public class Exercise14 extends Application {
    /**
     * This class represents a key in LRU cache
     */
    private static final class Point2D {
        final int x;
        final int y;

        public Point2D(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point2D point2D = (Point2D) o;

            if (x != point2D.x) return false;
            return y == point2D.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    /**
     * Implements a simple LRU cache based on LinkedHashMap
     * @param <K> key type
     * @param <V> value type
     */
    private static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private int cacheSize;

        public LRUCache(final int cacheSize) {
            super(16, 0.75F, true);
            this.cacheSize = cacheSize;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() >= cacheSize;
        }
    }

    private static class LatentImage {
        private final Image image;

        private PixelReader currentReader;

        private LatentImage(final Image image) {
            this.image = image;
            this.currentReader = image.getPixelReader();
        }

        static LatentImage from(final Image image) {
            return new LatentImage(image);
        }

        LatentImage transform(final ColorTransformer transformer) {
            return doTransform(transformer);
        }

        LatentImage transform(final UnaryOperator<Color> operation) {
            return doTransform((x, y, reader) -> operation.apply(reader.getColor(x, y)));
        }

        LatentImage blur() {
            return doTransform((x, y, reader) -> {
                int count = 0;

                double red   = 0.0;
                double green = 0.0;
                double blue  = 0.0;

                for (int i = -1; i <= 1; ++i) {
                    for (int j = -1; j <= 1; ++j) {
                        final Color color = getColor(x + i, y + j, reader);

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

                return new Color(red, green, blue, reader.getColor(x, y).getOpacity());
            });
        }

        LatentImage edgeDetection() {
            return doTransform((x, y, reader) -> {
                final Color currentColor = getColor(x, y, reader);

                double red   = currentColor.getRed() * 4;
                double green = currentColor.getGreen() * 4;
                double blue  = currentColor.getBlue() * 4;

                final Color[] neighbours = {
                        getColor(x - 1, y, reader),   // west
                        getColor(x + 1, y, reader),   // east
                        getColor(x, y - 1, reader),   // north
                        getColor(x, y + 1, reader)    // south
                };

                for (final Color color : neighbours) {
                    red   -= color.getRed();
                    green -= color.getGreen();
                    blue  -= color.getBlue();
                }

                red   = truncateComponent(red);
                green = truncateComponent(green);
                blue  = truncateComponent(blue);

                return new Color(red, green, blue, currentColor.getOpacity());
            });
        }

        Image toImage() {
            final int width = (int) image.getWidth();
            final int height = (int) image.getHeight();
            final WritableImage out = new WritableImage(width, height);

            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    out.getPixelWriter().setColor(x, y, currentReader.getColor(x, y));
                }
            }

            return out;
        }

        private Color getColor(final int x, final int y, final PixelReader reader) {
            final int imageWidth = (int) image.getWidth();
            final int imageHeight = (int) image.getHeight();

            final int xPos = (x + imageWidth) % imageWidth;
            final int yPos = (y + imageHeight) % imageHeight;

            return reader.getColor(xPos, yPos);
        }

        private LatentImage doTransform(final ColorTransformer transformer) {
            final PixelReader reader = currentReader;

            currentReader = new PixelReader() {
                private final PixelReader parent = reader;
                private LRUCache<Point2D, Color> cache = new LRUCache<>(512);

                @Override
                public PixelFormat getPixelFormat() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public int getArgb(int x, int y) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Color getColor(int x, int y) {
                    final Point2D key = new Point2D(x, y);

                    Color value = cache.get(key);

                    if (value == null) {
                        value = transformer.apply(x, y, parent);
                        cache.put(key, value);
                    }

                    return value;
                }

                @Override
                public <T extends Buffer> void getPixels(int x, int y, int w, int h,
                        WritablePixelFormat<T> pixelformat, T buffer, int scanlineStride) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void getPixels(int x, int y, int w, int h,
                        WritablePixelFormat<ByteBuffer> pixelformat, byte[] buffer, int offset, int scanlineStride) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void getPixels(int x, int y, int w, int h,
                        WritablePixelFormat<IntBuffer> pixelformat, int[] buffer, int offset, int scanlineStride) {
                    throw new UnsupportedOperationException();
                }
            };

            return this;
        }

        private static double truncateComponent(final double value) {
            return Math.min(Math.max(0, value), 1);
        }
    }

    @FunctionalInterface
    private interface ColorTransformer {
        Color apply(int x, int y, PixelReader reader);
    }

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

