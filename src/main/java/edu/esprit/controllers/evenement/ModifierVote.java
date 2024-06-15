package edu.esprit.controllers.evenement;

import edu.esprit.entities.Vote;
import edu.esprit.services.ServiceVote;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifierVote {

    @FXML
    private TextField TDdescM;

    @FXML
    private TextField TFdateSM;

    private ServiceVote serviceVote;
    private Vote vote;
    private VoteList voteListController; // Reference to the VoteList controller

    public void setData(Vote vote) {
        this.vote = vote;
        TDdescM.setText(vote.getDesc_E());
        TFdateSM.setText(vote.getDate_SV());
    }

    public void setVoteListController(VoteList voteListController) {
        this.voteListController = voteListController;
    }

    @FXML
    void ModifierVoteOnClick(ActionEvent event) {
        String description = TDdescM.getText();
        String dateSoumission = TFdateSM.getText();

        // Input validation
        boolean isValid = validateInput(description, dateSoumission);
        if (!isValid) {
            return; // Do not proceed if input is not valid
        }
        vote.setDesc_E(description);
        vote.setDate_SV(dateSoumission);

        if (serviceVote == null) {
            serviceVote = new ServiceVote();
        }
        serviceVote.modifier(vote);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Le vote a été modifié avec succès.");
        alert.setTitle("Vote modifié");
        alert.show();

        // Notify the VoteList controller to refresh the list of votes
        if (voteListController != null) {
            voteListController.loadVotes();
        }

        closeWindow();
    }
    // Input validation
    private boolean validateInput(String description, String dateSoumission) {
        boolean isValid = true;

        // Check if description is empty
        if (description.isEmpty()) {
            TDdescM.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            TDdescM.setStyle("-fx-border-color: green;");
        }

        // Check if dateSoumission is empty or not a valid date (add your date validation logic here)
        if (dateSoumission.isEmpty() || !isValidDate(dateSoumission)) {
            TFdateSM.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            TFdateSM.setStyle("-fx-border-color: green;");
        }

        // Display alert if any field is empty
        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Champs non remplis");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
        }

        return isValid;
    }
    private boolean isValidDate(String date) {
        // Implement your date validation logic here
        // For simplicity, let's assume any non-empty string is a valid date
        return !date.isEmpty();
    }

    @FXML
    void returnnOnClick(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) TDdescM.getScene().getWindow();
        stage.close();
    }
}
