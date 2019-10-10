package com.aamatucci.pixelatefx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;

import java.nio.IntBuffer;
import java.util.Arrays;

public class PixelImageView extends StackPane {

    private static final int ALPHA = 255 << 24;
    private static final WritablePixelFormat<IntBuffer> FORMAT = PixelFormat.getIntArgbInstance();
    private final ImageView imageView;

    /**
     * Minimum pixel size. Below this value, the pixelation is not applied.
     */
    private IntegerProperty minPixelSize = new SimpleIntegerProperty(5);

    /**
     * Pixel size value to determine how strong is the pixelation effect.
     */
    private IntegerProperty pixelSize = new SimpleIntegerProperty();
    private ObjectProperty<Image> image = new SimpleObjectProperty<>();
    private WritableImage writableImage;
    private PixelReader pixelReader;
    private PixelWriter pixelWriter;

    public PixelImageView() {
        imageView = new ImageView();
        this.getChildren().add(imageView);

        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());
        imageView.setPreserveRatio(true);

        this.image.addListener((observable, oldValue, image) -> {
            if (image != null) {
                writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
                pixelReader = image.getPixelReader();
                pixelWriter = writableImage.getPixelWriter();
                pixelate();
            }
        });

        pixelSize.addListener(obs -> pixelate());

    }

    private void pixelate() {
        Image image = this.image.get();
        int pixel = pixelSize.get();
        if (pixelReader != null && pixelWriter != null && image != null ) {
            if (pixel < minPixelSize.get()){
                imageView.setImage(image);
            }
            else {
                imageView.setImage(writableImage);
                int imageW = (int) image.getWidth();
                int imageH = (int) image.getHeight();
                int w;
                int h;
                for (int x = 0; x < imageW; x += pixel) {
                    for (int y = 0; y < imageH; y += pixel) {
                        w = x + pixel > imageW ? imageW - x : pixel;
                        h = y + pixel > imageH ? imageH - y : pixel;
                        PixelImageView.pixelate(pixelReader, pixelWriter, x, y, w, h);
                    }
                }
            }
        }
    }

    private static void pixelate(final PixelReader pixelReader,
                                 final PixelWriter pixelWriter,
                                 final int startX,
                                 final int startY,
                                 final int w,
                                 final int h) {
        final int len = w * h;
        int red = 0;
        int green = 0;
        int blue = 0;
        final int[] pixels = new int[len];
        pixelReader.getPixels(startX, startY, w, h, FORMAT, pixels, 0, w);
        for (final int color : pixels) {
            red += (color >>> 16 & 0xFF);
            green += (color >>> 8 & 0xFF);
            blue += (color & 0xFF);
        }

        red /= len;
        green /= len;
        blue /= len;

        final int c = ALPHA + (red << 16) + (green << 8) + blue;
        Arrays.fill(pixels, c);
        pixelWriter.setPixels(startX, startY, w, h, FORMAT, pixels, 0, w);

    }

    public void setImage(Image image) {
        this.image.set(image);
    }

    public IntegerProperty pixelSizeProperty() {
        return pixelSize;
    }

    public void setPixelSize(int p){
        this.pixelSize.set(p);
    }

    public int getPixelSize() {
        return pixelSize.get();
    }

    public int getMinPixelSize() {
        return minPixelSize.get();
    }

    public IntegerProperty minPixelSizeProperty() {
        return minPixelSize;
    }

    public void setMinPixelSize(int minPixelSize) {
        this.minPixelSize.set(minPixelSize);
    }
}
