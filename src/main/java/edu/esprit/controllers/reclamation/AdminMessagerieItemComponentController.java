package edu.esprit.controllers.reclamation;
import edu.esprit.entities.Messagerie;
import edu.esprit.services.ServiceMessagerie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Optional;


public class AdminMessagerieItemComponentController {

    @FXML
    private AnchorPane AnchoPaneMessage1111;

    @FXML
    private Label ReceiverName;

    @FXML
    private ImageView arrowrightimg;

    @FXML
    private Label datemessagerie;

    @FXML
    private Label senderName;
    private Messagerie messagerie;
    ServiceMessagerie serviceMessagerie = new ServiceMessagerie();

    @FXML
    void MessageEditAction(ActionEvent event) {

    }

    @FXML
    void deleteMessageAction(ActionEvent event) {
        if (messagerie != null) {
            // Créer une boîte de dialogue de confirmation
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette réclamation ?");
            confirmationAlert.setTitle("Confirmation de suppression");

            // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            // Vérifier si l'utilisateur a cliqué sur le bouton OK
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer la réclamation
                ServiceMessagerie serviceMessagerie1 = new ServiceMessagerie();
                serviceMessagerie1.supprimer(messagerie.getId_message());

                // Afficher une alerte pour informer l'utilisateur que la réclamation a été supprimée avec succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("La réclamation a été supprimée avec succès.");
                alert.setTitle("Réclamation supprimée");
                alert.show();

                // Rediriger l'utilisateur vers la vue précédente (par exemple, la liste des réclamations)
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/121321.fxml"));
                    senderName.getScene().setRoot(root);
                } catch (IOException e) {
                    // Gérer l'exception si la redirection échoue
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Une erreur s'est produite lors de la redirection.");
                    errorAlert.setTitle("Erreur de redirection");
                    errorAlert.show();
                }
            }
        } else {
            // Afficher un message d'erreur si la réclamation est null
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Impossible de supprimer la réclamation car aucune réclamation n'est sélectionnée.");
            errorAlert.setTitle("Erreur de suppression");
            errorAlert.show();
        }
    }

    @FXML
    void viewDetailMessageAction(ActionEvent event) {

    }

    public void setData(Messagerie messagerie){
        this.messagerie = messagerie;
        senderName.setText(messagerie.getSender_message().getNom());
        ReceiverName.setText(messagerie.getReceiver_message().getNom());
        datemessagerie.setText(String.valueOf(messagerie.getDate_message()));
    }

}
