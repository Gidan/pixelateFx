package com.aamatucci.pixelatefx;

import java.nio.IntBuffer;
import java.util.Arrays;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Pixelate extends Application {

    private int image_w;
    private int image_h;
    private PixelWriter pixelWriter;
    private PixelReader pixelReader;

    public static void main(final String[] args) {
        Application.launch(args);
    }

    public void start(final Stage primaryStage) throws Exception {
        final AnchorPane root = new AnchorPane();
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();

        final HBox imgContainer = new HBox();
        imgContainer.setAlignment(Pos.CENTER);
        AnchorPane.setLeftAnchor(imgContainer, .0);
        AnchorPane.setTopAnchor(imgContainer, .0);
        AnchorPane.setRightAnchor(imgContainer, .0);
        AnchorPane.setBottomAnchor(imgContainer, .0);
        imgContainer.setSpacing(50);
        root.getChildren().add(imgContainer);

        final Image originalImage = new Image(this.getClass().getResource("/clouds.jpg").toExternalForm());
        final ImageView imageView = new ImageView(originalImage);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(300);
        imgContainer.getChildren().add(imageView);

        final Slider slider = new Slider(5, 50, 5);
        root.getChildren().add(slider);
        slider.valueProperty().addListener((obs, o, pixelSize) -> this.pixelate(pixelSize.intValue()));

        this.image_w = (int) originalImage.getWidth();
        this.image_h = (int) originalImage.getHeight();

        final WritableImage writableImage = new WritableImage(this.image_w, this.image_h);

        final ImageView imageViewRight = new ImageView(writableImage);
        imageViewRight.setPreserveRatio(true);
        imageViewRight.setFitHeight(300);
        imgContainer.getChildren().add(imageViewRight);

        this.pixelWriter = writableImage.getPixelWriter();
        this.pixelReader = originalImage.getPixelReader();
    }

    private void pixelate(final int pixelSize) {
        for (int x = 0; x < this.image_w; x += pixelSize) {
            for (int y = 0; y < this.image_h; y += pixelSize) {
                final int w = x + pixelSize > this.image_w ? this.image_w - x : pixelSize;
                final int h = y + pixelSize > this.image_h ? this.image_h - y : pixelSize;
                Pixelate.pixelate(this.pixelReader, this.pixelWriter, x, y, w, h);
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
        final int alpha  = 255;
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

        final int c =  (alpha << 24) + (red << 16) + (green << 8) + blue;
        Arrays.fill(pixels, c);
        pixelWriter.setPixels(startX, startY, w, h, format, pixels, 0, w);
    }
}
