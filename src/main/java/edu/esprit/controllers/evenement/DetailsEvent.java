package edu.esprit.controllers.evenement;

import edu.esprit.entities.Evenement;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

public class DetailsEvent {
    @FXML
    private Text nomTFF;

    @FXML
    private Text debTFF;

    @FXML
    private Text finTFF;

    @FXML
    private Text capTFF;

    @FXML
    private Text cateTFF;

    @FXML
    private ImageView imageTFF;

    @FXML
    private Button backButton;

    // Method to set event details
    public void setData(Evenement evenement) {
        nomTFF.setText("Nom événement: " + evenement.getNomEvent());
        debTFF.setText("Date début: " + evenement.getDateEtHeureDeb());
        finTFF.setText("Date fin: " + evenement.getDateEtHeureFin());
        capTFF.setText("Capacité Max: " + evenement.getCapaciteMax());
        cateTFF.setText("Catégorie: " + evenement.getCategorie());

        // Set image if imagePath is provided
        String imagePath = evenement.getImageEvent();
        if (imagePath != null && !imagePath.isEmpty()) {
            imageTFF.setImage(new javafx.scene.image.Image(new File(imagePath).toURI().toString()));
        }
    }
    @FXML
    void retournerOnClick() {
        try {
            // Obtenir la scène actuelle à partir du bouton Retourner
            Stage stage = (Stage) backButton.getScene().getWindow();

            // Fermer la fenêtre du pop-up
            stage.close();
        } catch (Exception e) {
            // Gérer les exceptions
            e.printStackTrace();
        }
    }


}