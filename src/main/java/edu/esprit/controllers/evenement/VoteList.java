package edu.esprit.controllers.evenement;

import edu.esprit.entities.Vote;
import edu.esprit.services.ServiceVote;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class VoteList implements Initializable {

    @FXML
    private VBox VoteLayout;
    @FXML
    private Label countLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadVotes();
        countPropositions(); // Call countPropositions() method

    }

    public void loadVotes() {
        VoteLayout.getChildren().clear();
        List<Vote> votes = new ArrayList<>(getVotes());
        for (Vote vote : votes) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL location = getClass().getResource("/evenementGui/VoteItem.fxml");
            fxmlLoader.setLocation(location);

            try {
                GridPane gridPane = fxmlLoader.load();
                VoteItem voteItemController = fxmlLoader.getController();
                voteItemController.setData(vote);
                VoteLayout.getChildren().add(gridPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<Vote> getVotes() {
        List<Vote> voteList = new ArrayList<>();
        ServiceVote serviceVote = new ServiceVote();
        Set<Vote> votes = serviceVote.getAll();

        for (Vote vote : votes) {
            item(vote, voteList);
        }

        return voteList;
    }

    void item(Vote vote, List<Vote> voteList) {
        vote.setDesc_E(vote.getDesc_E());
        vote.setDate_SV(vote.getDate_SV());
        voteList.add(vote);
    }

    @FXML
    void AjouterPropClick(ActionEvent event) {
        try {
            // Charger le fichier FXML de l'interface d'ajout d'événement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenementGui/AjouterVote.fxml"));
            javafx.scene.Parent root = loader.load();

            // Créer une nouvelle scène
            AjouterVote controller = loader.getController();
            controller.setVoteListController(this);

            // Afficher la fenêtre de ajout
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Ajouter Proposition");
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            // Gérer les exceptions liées au chargement de l'interface
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de navigation");
            alert.setContentText("Une erreur s'est produite lors de la navigation vers l'interface d'ajout de proposition.");
            alert.show();
        }
    }

    public BorderPane firstborderpane;
    @FXML
    private AnchorPane MainAnchorPaneBaladity;
    @FXML
    private BorderPane SecondBorderPane;
    @FXML
    private VBox MainLeftSidebar;
    private boolean isSidebarVisible = true;

    @FXML
    void BTNToggleSidebar(ActionEvent event) {
        TranslateTransition sideBarTransition = new TranslateTransition(Duration.millis(400), MainLeftSidebar);
        double sidebarWidth = MainLeftSidebar.getWidth();
        if (isSidebarVisible) {
            // Hide sidebar
            sideBarTransition.setByX(-sidebarWidth);
            isSidebarVisible = false;
            // Adjust the width of SecondBorderPane
            SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() + sidebarWidth);
            // Translate SecondBorderPane to the left to take the extra space
            TranslateTransition borderPaneTransition = new TranslateTransition(Duration.millis(250), SecondBorderPane);
            borderPaneTransition.setByX(-sidebarWidth);
            borderPaneTransition.play();
        } else {
            // Show sidebar
            sideBarTransition.setByX(sidebarWidth);
            isSidebarVisible = true;
            // Adjust the width of SecondBorderPane
            SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() - sidebarWidth);
            // Reset the translation of SecondBorderPane to 0
            TranslateTransition borderPaneTransition = new TranslateTransition(Duration.millis(250), SecondBorderPane);
            borderPaneTransition.setByX(sidebarWidth);
            borderPaneTransition.play();
        }

        sideBarTransition.play();
    }

    public void BTNGestionEvennement(ActionEvent actionEvent) {

    }

    public void BTNGestionUser(ActionEvent actionEvent) {
    }

    public void BTNGestionRec(ActionEvent actionEvent) {

    }

    public void BTNGestionAct(ActionEvent actionEvent) {

    }

    public void BTNGestionEquipement(ActionEvent actionEvent) {
    }

    public void BTNGestionTache(ActionEvent actionEvent) {

    }

    @FXML
    void retournerOnClickk(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/evenementGui/EventDashboard.fxml"));
            VoteLayout.getScene().setRoot(root);
        } catch (IOException e) {


        }
    }
    private void countPropositions() {
        List<Vote> votes = getVotes();
        int propositionsCount = votes.size();
        countLabel.setText("Nombre des Propositions: " + propositionsCount);
    }
}

