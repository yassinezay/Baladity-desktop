package edu.esprit.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class MainTFX extends Application {
    public static Stage stg;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stg = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        //InputStream fxmlStream = getClass().getResourceAsStream("/AfficherTacheFront.fxml");
        InputStream fxmlStream = getClass().getResourceAsStream("/tacheGui/AfficherTache.fxml");
        if (fxmlStream == null) {
            throw new IOException("FXML file not found");
        }

        // Set application icon
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/logoiconT.png"))));
        // Set the location of the FXML file in the loader
        Scene scene = new Scene(loader.load(fxmlStream));

        primaryStage.setTitle("Baladity");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
    }
}
