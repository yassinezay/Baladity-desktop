package edu.esprit.controllers.evenement;

import edu.esprit.entities.Vote;
import edu.esprit.services.ServiceVote;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class VoteItem {

    @FXML
    private Text DescTT;

    @FXML
    private Text DateSomTT;

    private Vote vote;

    private ServiceVote serviceVote;

    public void setData(Vote vote) {
        this.vote = vote;
        DescTT.setText(vote.getDesc_E());
        DateSomTT.setText(vote.getDate_SV());
    }

    @FXML
    void SupprimerVoteClick(ActionEvent event) {
        if (vote != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce vote ?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (serviceVote == null) {
                    serviceVote = new ServiceVote();
                }
                serviceVote.supprimer(vote.getId_V());

                // Actualiser la vue des votes
                actualiserVueVotes();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Le vote a été supprimé avec succès.");
                alert.setTitle("Vote supprimé");
                alert.show();
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Impossible de supprimer le vote car aucun vote n'est sélectionné.");
            errorAlert.setTitle("Erreur de suppression");
            errorAlert.show();
        }
    }


    @FXML
    void ModifierVoteClick(ActionEvent event) {
        try {
            // Charger la vue de modification de vote
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenementGui/ModifierVote.fxml"));
            Parent root = loader.load();

            // Passer le vote à modifier au contrôleur de modification
            ModifierVote controller = loader.getController();
            controller.setData(vote);

            // Créer une nouvelle fenêtre pour afficher la vue de modification de vote
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Rend la fenêtre modale
            stage.setTitle("Modifier vote");
            stage.setScene(new Scene(root));

            // Afficher la fenêtre de modification de vote
            stage.showAndWait();

            // Rafraîchir la scène actuelle après la modification du vote
            refreshParentWindow();
        } catch (IOException e) {
            // Gérer les exceptions liées au chargement de la vue de modification
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur lors de la modification du vote.");
            alert.setTitle("Erreur de modification");
            alert.show();
        }
    }

    @FXML
    void returnnOnClick(ActionEvent event) {
        // Fermer la fenêtre actuelle
        Stage stage = (Stage) DescTT.getScene().getWindow();
        stage.close();
    }

    // Méthode pour actualiser la scène parente après la modification du vote
    private void refreshParentWindow() {
        Stage parentStage = (Stage) DescTT.getScene().getWindow();
        parentStage.requestFocus(); // Rendre la scène parente à nouveau active
    }


    // Méthode pour actualiser la vue des votes
    private void actualiserVueVotes() {
        // Redirection vers la vue précédente (par exemple, la liste des votes)
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/evenementGui/VoteList.fxml"));
            DescTT.getScene().setRoot(root);
        } catch (IOException e) {
            // Gérer l'exception si la redirection échoue
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Une erreur s'est produite lors de la redirection.");
            errorAlert.setTitle("Erreur de redirection");
            errorAlert.show();
        }
    }

}
