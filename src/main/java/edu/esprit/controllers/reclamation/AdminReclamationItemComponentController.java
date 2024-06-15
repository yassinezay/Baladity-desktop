package edu.esprit.controllers.reclamation;
import edu.esprit.entities.Reclamation;
import edu.esprit.services.ServiceReclamation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Optional;


public class AdminReclamationItemComponentController {

    @FXML
    private Label TFdate_reclamationaff;

    @FXML
    private Label TFstatus_reclamationaff;

    @FXML
    private Label TFsujet_reclamationaff;

    @FXML
    private ImageView arrowrightimg;

    private Reclamation reclamation;
    ServiceReclamation serviceReclamation = new ServiceReclamation();

    public void setData(Reclamation reclamation){
        this.reclamation = reclamation;
        TFsujet_reclamationaff.setText(reclamation.getSujet_reclamation());
        TFdate_reclamationaff.setText(String.valueOf(reclamation.getDate_reclamation()));
        TFstatus_reclamationaff.setText(reclamation.getStatus_reclamation());
    }
    @FXML
    void viewDetailReclamationAction(ActionEvent event) {

    }
    @FXML
    void deleteReclamationAction(ActionEvent event) {
        if (reclamation != null) {
            // Créer une boîte de dialogue de confirmation
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette réclamation ?");
            confirmationAlert.setTitle("Confirmation de suppression");

            // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            // Vérifier si l'utilisateur a cliqué sur le bouton OK
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer la réclamation
                ServiceReclamation serviceReclamation = new ServiceReclamation();
                serviceReclamation.supprimer(reclamation.getId_reclamation());

                // Afficher une alerte pour informer l'utilisateur que la réclamation a été supprimée avec succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("La réclamation a été supprimée avec succès.");
                alert.setTitle("Réclamation supprimée");
                alert.show();

                // Rediriger l'utilisateur vers la vue précédente (par exemple, la liste des réclamations)
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/amine.fxml"));
                    TFsujet_reclamationaff.getScene().setRoot(root);
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
    void ReclamationEditAction(ActionEvent event) {

    }
}
