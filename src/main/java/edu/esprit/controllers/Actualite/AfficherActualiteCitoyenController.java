package edu.esprit.controllers.Actualite;

import edu.esprit.entities.EndUser;

import edu.esprit.entities.Actualite;
import edu.esprit.services.ServiceActualite;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class AfficherActualiteCitoyenController implements Initializable {
    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private Button toAJouterPub;
    @FXML
    private VBox MainLeftSidebar;
    private boolean isSidebarVisible = true;

    @FXML
    private GridPane gridCitoyen;

    @FXML
    private ImageView imgView_actualite;

    @FXML
    private ScrollPane scrollCitoyen;


    private ServiceActualite sr = new ServiceActualite();
    Set<Actualite> actualiteSet = sr.getAll();
    List<Actualite> actualiteList = new ArrayList<>(actualiteSet);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < actualiteList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ActualiteGui/ActualiteItemCitoyen.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ActualiteController itemController = fxmlLoader.getController();
                itemController.setData(actualiteList.get(i));

                if (column == 1) {
                    column = 0;
                    row++;
                }

                gridCitoyen.add(anchorPane, column++, row); //(child,column,row)
                //set grid width
                gridCitoyen.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridCitoyen.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridCitoyen.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                gridCitoyen.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridCitoyen.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridCitoyen.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void toAJouterPub(ActionEvent actionEvent) {
        try {
            System.out.println("Resource URL: " + getClass().getResource("/ActualiteGui/AjouterPubliciteGui.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/ActualiteGui/AjouterPubliciteGui.fxml"));
            toAJouterPub.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    public void TomainPub(ActionEvent actionEvent) {
        try {
            System.out.println("Resource URL: " + getClass().getResource("/MainGui.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/MainGui.fxml"));
            toAJouterPub.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
}

