package edu.esprit.controllers.reclamation;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReclamationTypeController implements Initializable {

    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private VBox MainLeftSidebar;

    private boolean isSidebarVisible = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser la taille du SecondBorderPane avec la même largeur que la barre latérale
        double sidebarWidth = MainLeftSidebar.getWidth();
        SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() + sidebarWidth);
    }
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
            TranslateTransition borderPaneTransition = new TranslateTransition(Duration.millis(400), SecondBorderPane);
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

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setMainAnchorPaneContent(AnchorPane ajouterAP) {
        MainAnchorPaneBaladity.getChildren().setAll(ajouterAP);
    }
    @FXML
    void BTNtypenonurgent(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page AjoutReclamation.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamationGui/AjoutReclamationGui.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la page chargée
            AjoutReclamation ajoutReclamationController = loader.getController();

            // Appeler la méthode pour définir le contenu de la liste en fonction du cas non urgent
            ajoutReclamationController.setTypesReclamation(false);

            // Créer une nouvelle scène avec la racine chargée
            Scene scene = new Scene(root);

            // Récupérer la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène sur la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void BTNtypeurgent(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page AjoutReclamation.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamationGui/AjoutReclamationGui.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la page chargée
            AjoutReclamation ajoutReclamationController = loader.getController();

            // Appeler la méthode pour définir le contenu de la liste en fonction du cas non urgent
            ajoutReclamationController.setTypesReclamation(true);

            // Créer une nouvelle scène avec la racine chargée
            Scene scene = new Scene(root);

            // Récupérer la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène sur la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void buttonReturnTypeReclamation(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/ReclamationGui.fxml"));
            MainAnchorPaneBaladity.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
}
