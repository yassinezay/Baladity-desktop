package edu.esprit.controllers.tache;

import edu.esprit.entities.Tache;
import edu.esprit.services.ServiceTache;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.IOException;

public class TacheFrontController {
    ServiceTache serviceTache = new ServiceTache();
    @FXML
    private TextField TF_Titre_Tache;
    private Tache taches;

    public void setData(Tache taches) {
        this.taches = taches;
        TF_Titre_Tache.setText(taches.getTitre_T());

        // Enable drag functionality
        TF_Titre_Tache.setOnDragDetected(event -> {
            Dragboard dragboard = TF_Titre_Tache.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(taches.getId_T())); // Assuming task ID is used for drag and drop
            dragboard.setContent(content);
            event.consume();
        });
    }

    @FXML
    void viewDetailTache(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tacheGui/DetailTacheFront.fxml"));
            Parent root = loader.load();
            DetailTacheFrontController controller = loader.getController();
            controller.setData(taches);
            TF_Titre_Tache.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}