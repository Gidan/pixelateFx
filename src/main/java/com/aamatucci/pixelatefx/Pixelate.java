package com.aamatucci.pixelatefx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Pixelate extends Application {

    public static void main(final String[] args) {
        Application.launch(args);
    }

    public void start(final Stage primaryStage) throws Exception {
        final AnchorPane root = new AnchorPane();
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();

        final Image originalImage = new Image(this.getClass().getResource("/clouds.jpg").toExternalForm());

        final Slider slider = new Slider(5, 50, 5);


        PixelImageView pixelImageView = new PixelImageView();
        AnchorPane.setLeftAnchor(pixelImageView, .0);
        AnchorPane.setTopAnchor(pixelImageView, .0);
        AnchorPane.setRightAnchor(pixelImageView, .0);
        AnchorPane.setBottomAnchor(pixelImageView, .0);
        root.getChildren().add(pixelImageView);
        root.getChildren().add(slider);

        pixelImageView.pixelSizeProperty().bind(slider.valueProperty());
        pixelImageView.setImage(originalImage);


    }


}
