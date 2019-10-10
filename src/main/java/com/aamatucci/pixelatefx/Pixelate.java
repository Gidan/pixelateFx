package com.aamatucci.pixelatefx;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Pixelate extends Application {

    public static void main(final String[] args) {
        Application.launch(args);
    }

    @FXML
    Slider slider;

    @FXML
    private
    PixelImageView pixelImageView;

    public void start(final Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/Pixelate.fxml"));
        fxmlLoader.setController(this);
        Parent root = fxmlLoader.load();

        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();
    }

    @FXML
    private void initialize() {
        final Image originalImage = new Image(this.getClass().getResource("/clouds.jpg").toExternalForm());
        pixelImageView.pixelSizeProperty().bind(slider.valueProperty());
        pixelImageView.setImage(originalImage);
    }


}
