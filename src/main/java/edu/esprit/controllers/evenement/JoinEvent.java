package edu.esprit.controllers.evenement;

import edu.esprit.entities.Evenement;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class JoinEvent {
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
    private Text CapMaxF;

    @FXML
    private Text CategorieF;

    @FXML
    private Text DateDebF;

    @FXML
    private Text DateFinF;

    @FXML
    private Text NomF;

    @FXML
    private ImageView FFFF;
    @FXML
    private Button backButtonn;

    // Method to set event details
    public void setData(Evenement evenement) {
        NomF.setText("Nom événement: " + evenement.getNomEvent());
        DateDebF.setText("Date début: " + evenement.getDateEtHeureDeb());
        DateFinF.setText("Date fin: " + evenement.getDateEtHeureFin());
        CapMaxF.setText("Capacité Max: " + evenement.getCapaciteMax());
        CategorieF.setText("Catégorie: " + evenement.getCategorie());

        // Set image if imagePath is provided
        String imagePath = evenement.getImageEvent();
        if (imagePath != null && !imagePath.isEmpty()) {
            FFFF.setImage(new javafx.scene.image.Image(new File(imagePath).toURI().toString()));
        }
    }
    @FXML
    void retournerOnClickk(ActionEvent event) {
        try {
            // Obtenir la scène actuelle à partir du bouton Retourner
            Stage stage = (Stage) backButtonn.getScene().getWindow();

            // Fermer la fenêtre du pop-up
            stage.close();
        } catch (Exception e) {
            // Gérer les exceptions
            e.printStackTrace();
        }

    }



}
