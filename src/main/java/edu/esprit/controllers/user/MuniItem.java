package edu.esprit.controllers.user;

import edu.esprit.entities.Municipality;
import edu.esprit.services.ServiceMuni;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MuniItem implements Initializable {

    public HBox muniButtonLayout;
    @FXML
    private String textButton;

    @FXML
    private Button muniButton;

    ServiceMuni serviceMuni = new ServiceMuni();

    int muniId;

    public void setData(Municipality muni){

        muniId = muni.getId_muni();
        muniButton.setText(muni.getNom_muni());

    }

    @FXML
    void editMuni(ActionEvent event) {
        try {
            Municipality muni = serviceMuni.getOneByID(muniId);
            // Charger la vue de modification d'événement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/ModifierMuni.fxml"));
            Parent root = loader.load();

            // Passer l'événement à modifier au contrôleur de modification
            ModifierMunicipality controller = loader.getController();
            controller.setData(muni);

            // Créer une nouvelle fenêtre (stage) pour afficher la vue de modification
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Rend la fenêtre modale
            stage.setTitle("Modifier municipalité");
            stage.setScene(new Scene(root));

            // Afficher la fenêtre de modification
            stage.showAndWait();
        } catch (IOException e) {
            // Gérer les exceptions liées au chargement de la vue de modification
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur lors de la modification de l'événement.");
            alert.setTitle("Erreur de modification");
            alert.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
