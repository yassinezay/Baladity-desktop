package edu.esprit.controllers.reclamation;

import edu.esprit.entities.Reclamation;
import edu.esprit.services.ServiceReclamation;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static edu.esprit.api.SendMail.sendEmail;

public class ReclamationMailController implements Initializable {

    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private VBox MainLeftSidebar;

    private boolean isSidebarVisible = true;

    private ServiceReclamation serviceReclamation;
    private Reclamation reclamation;

    @FXML
    private TextField ObjetMail;
    @FXML
    private TextArea descriptionMail;
    @FXML
    private Label mailReceiver;
    private String objet,description;

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

    // Méthode pour initialiser les champs avec les données de la réclamation
    public void setData(Reclamation reclamation) {
        this.reclamation = reclamation;
        if (reclamation != null) {
            mailReceiver.setText(reclamation.getUser().getEmail());
        }
    }


    public void setServiceReclamation(ServiceReclamation serviceReclamation) {
        this.serviceReclamation = serviceReclamation;
    }

    @FXML
    void EnvoiMailReclamationAction(ActionEvent event) {
        objet = ObjetMail.getText();
        description= descriptionMail.getText();
        String sourceEmail = "zayaneyassine6@gmail.com";
        String sourcePwd = "eqzgdarhrzaysutq";
        String desEmail = mailReceiver.getText();
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation d'envoi");
        confirmationDialog.setHeaderText("Êtes-vous sûr de vouloir envoyer ce mail ?");
        confirmationDialog.setContentText("Objet : " + objet + "\nDescription : " + description);

        // Ajouter les boutons de confirmation et d'annulation
        confirmationDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // Si l'utilisateur a confirmé, envoyer le mail
                sendEmail(sourceEmail, sourcePwd, desEmail, objet, description);
                // Afficher une boîte de dialogue pour indiquer que le mail a été envoyé
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Confirmation d'envoi");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Le mail a été envoyé avec succès !");
                successAlert.showAndWait();
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/DirecteurReclamationStatusGui.fxml"));
                    descriptionMail.getScene().setRoot(root);
                } catch (IOException e) {
                    // Gérer l'exception si la redirection échoue
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Une erreur s'est produite lors de la redirection.");
                    errorAlert.setTitle("Erreur de redirection");
                    errorAlert.show();
                }
            }
        });
    }

    @FXML
    void cancelMailReclamationAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/DirecteurReclamationStatusGui.fxml"));
            descriptionMail.getScene().setRoot(root);
        } catch (IOException e) {
            // Gérer l'exception si la redirection échoue
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Une erreur s'est produite lors de la redirection.");
            errorAlert.setTitle("Erreur de redirection");
            errorAlert.show();
        }
    }

}
