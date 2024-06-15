package edu.esprit.controllers.evenement;

import edu.esprit.controllers.user.Login;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Vote;
import edu.esprit.services.ServiceUser;
import edu.esprit.services.ServiceVote;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.prefs.Preferences;

public class AjouterVote {
    private static final String USER_PREF_KEY = "current_user";

    ServiceUser serviceUser = new ServiceUser();

    int userId  = Integer.parseInt(getCurrentUser());

    EndUser user = serviceUser.getOneByID(userId);

    private VoteList voteListController;

    @FXML
    private TextField TDdesc;

    @FXML
    private TextField TFdateS;

    private final ServiceVote serviceVote = new ServiceVote();

    public void setVoteListController(VoteList voteListController) {
        this.voteListController = voteListController;
    }

    @FXML
    void AjouterVoteOnClick(ActionEvent event) {
        if (validateFields()) {

            // Récupération de l'utilisateur actuel (à remplacer par votre mécanisme d'authentification)
             // Exemple : suppose que l'utilisateur actuel a l'ID 1

            // Création du vote
            Vote vote = new Vote(
                    user,
                    TDdesc.getText(),
                    TFdateS.getText()
            );

            // Ajout du vote via le service
            serviceVote.ajouter(vote);

            // Affichage d'une notification de succès
            showAlert(Alert.AlertType.INFORMATION, "Success", "Vote ajouté avec succès !");

            // Refresh the VoteList
            refreshVoteList();
        } else {
            // Affichage d'une notification d'erreur si les champs ne sont pas valides
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs correctement !");
        }
    }

    @FXML
    void returnOnclick(ActionEvent event) {
        try {
            // Fermer la fenêtre actuelle
            Stage stage = (Stage) TDdesc.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer les exceptions liées à la fermeture de la fenêtre
        }
    }

    private boolean validateFields() {
        boolean isValid = true;

        // Validation de la description
        if (TDdesc.getText().isEmpty()) {
            setInvalidFieldStyle(TDdesc);
            isValid = false;
        } else {
            setValidFieldStyle(TDdesc);
        }

        return isValid;
    }

    private void setInvalidFieldStyle(TextField textField) {
        textField.setStyle("-fx-border-color: red;");
    }

    private void setValidFieldStyle(TextField textField) {
        textField.setStyle("-fx-border-color: lime;");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    private void refreshVoteList() {
        if (voteListController != null) {
            voteListController.loadVotes();
        }
    }
    private String getCurrentUser() {
        Preferences preferences = Preferences.userNodeForPackage(Login.class);
        return preferences.get(USER_PREF_KEY, "DefaultUser");
    }
}
