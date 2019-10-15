package com.aamatucci.pixelatefx;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class Pixelate extends Application {

    private Stage stage;

    public static void main(final String[] args) {
        Application.launch(args);
    }

    @FXML
    private Slider slider;

    @FXML
    private PixelImageView pixelImageView;

    @FXML
    private Button btnFileChooser;

    final FileChooser fileChooser = new FileChooser();

    public void start(final Stage primaryStage) throws Exception {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/Pixelate.fxml"));
        fxmlLoader.setController(this);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("PixelateFX");

        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();
    }

    @FXML
    private void initialize() {
        pixelImageView.pixelSizeProperty().bind(slider.valueProperty());
        btnFileChooser.setOnMouseClicked(event -> this.openFileChooser());

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    private void openFileChooser() {
        File file = fileChooser.showOpenDialog(this.stage);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            pixelImageView.setImage(image);
        }
    }


}
