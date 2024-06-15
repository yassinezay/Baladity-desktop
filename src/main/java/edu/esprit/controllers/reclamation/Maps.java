package edu.esprit.controllers.reclamation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class Maps implements Initializable {

    @FXML
    private WebView webView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisez votre contrôleur si nécessaire
    }

    public void loadGoogleMaps(String latitude, String longitude) {
        if (latitude != null && longitude != null) {
            String googleMapsUrl = "https://www.google.com/maps?q=" + latitude + "," + longitude;
            WebEngine webEngine = webView.getEngine();
            webEngine.load(googleMapsUrl);
        } else {
            System.err.println("Les coordonnées de latitude ou de longitude sont nulles.");
        }
    }
}
