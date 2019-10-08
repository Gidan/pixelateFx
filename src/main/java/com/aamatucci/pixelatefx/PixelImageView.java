package com.aamatucci.pixelatefx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;

import java.nio.IntBuffer;
import java.util.Arrays;

public class PixelImageView extends Pane {

    private IntegerProperty pixelSize = new SimpleIntegerProperty();
    private ObjectProperty<Image> image = new SimpleObjectProperty<>();
    private WritableImage writableImage;
    private PixelReader pixelReader;
    private PixelWriter pixelWriter;

    public PixelImageView() {
        ImageView imageView = new ImageView();
        this.getChildren().add(imageView);

        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.setPreserveRatio(true);

        this.image.addListener((observable, oldValue, image) -> {
            if (image != null){
                writableImage = new WritableImage((int)image.getWidth(), (int)image.getHeight());
                imageView.setImage(writableImage);
                pixelReader = image.getPixelReader();
                pixelWriter = writableImage.getPixelWriter();
                pixelate();
            }
        });

        pixelSize.addListener(obs -> pixelate());
        this.widthProperty().addListener(obs -> pixelate());
        this.heightProperty().addListener(obs -> pixelate());
    }

    private void pixelate() {
        if (pixelReader != null && pixelWriter != null) {
            int imageW = (int) getWidth();
            int imageH = (int) getHeight();
            int pixelSize = this.pixelSize.get();
            for (int x = 0; x < imageW; x += pixelSize) {
                for (int y = 0; y < imageH; y += pixelSize) {
                    final int w = x + pixelSize > imageW ? imageW - x : pixelSize;
                    final int h = y + pixelSize > imageH ? imageH - y : pixelSize;
                    PixelImageView.pixelate(pixelReader, pixelWriter, x, y, w, h);
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
        int red = 0;
        int green = 0;
        int blue = 0;
        final int alpha = 255;
        final int len = w * h;
        final int[] pixels = new int[len];
        final WritablePixelFormat<IntBuffer> format = PixelFormat.getIntArgbInstance();
        pixelReader.getPixels(startX, startY, w, h, format, pixels, 0, w);
        for (final int color : pixels) {
            red += (color >>> 16 & 0xFF);
            green += (color >>> 8 & 0xFF);
            blue += (color & 0xFF);
        }

        red /= len;
        green /= len;
        blue /= len;

        final int c = (alpha << 24) + (red << 16) + (green << 8) + blue;
        Arrays.fill(pixels, c);
        pixelWriter.setPixels(startX, startY, w, h, format, pixels, 0, w);
    }

    public void setImage(Image image){
        this.image.set(image);
    }

    public IntegerProperty pixelSizeProperty() {
        return pixelSize;
    }
}
