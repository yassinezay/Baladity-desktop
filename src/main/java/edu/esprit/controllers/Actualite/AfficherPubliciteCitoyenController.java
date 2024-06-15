package edu.esprit.controllers.Actualite;

import edu.esprit.entities.Publicite;
import edu.esprit.services.ServicePublicite;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;


import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


public class AfficherPubliciteCitoyenController implements Initializable{

    @FXML
    private AnchorPane MainAnchorPaneBaladity;
    @FXML
    private Button retoutActCitoyen;
    @FXML
    private VBox MainLeftSidebar;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private BorderPane firstborderpane;

    @FXML
    private GridPane gridPubC;

    @FXML
    private ScrollPane scrollPubC;

    private boolean isSidebarVisible = true;
    private ServicePublicite servicePublicite = new ServicePublicite();
    Set<Publicite> publiciteSet = servicePublicite.getAll();
    List<Publicite> publiciteList = new ArrayList<>(publiciteSet);
    private int actualiteId; // New variable to store the id_a

    private int currentIndex = 0;
    private final Duration scrollInterval = Duration.seconds(10);
    private Timeline timeline;

    public void setActualiteId(int actualiteId) {
        this.actualiteId = actualiteId;
    }

    private void updateDisplayedAdvertisement() {
        currentIndex = (currentIndex + 1) % publiciteList.size();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/ActualiteGui/PubliciteItemCitoyen.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();

            PubliciteController itemController = fxmlLoader.getController();
            itemController.setData(publiciteList.get(currentIndex));

            anchorPane.prefWidthProperty().bind(scrollPubC.widthProperty());
            anchorPane.prefHeightProperty().bind(scrollPubC.heightProperty());

            // Replace the content with the new advertisement
            anchorPane.setOnMouseClicked(event -> {
                // Handle click events on the advertisement if needed
            });

            anchorPane.setOnMouseEntered(event -> {
                // Handle mouse hover events on the advertisement if needed
            });

            anchorPane.setOnMouseExited(event -> {
                // Handle mouse exit events on the advertisement if needed
            });

            scrollPubC.setContent(anchorPane);

            // Get the selected offer from your ComboBox or from the Publicite object
            String selectedOffer = publiciteList.get(currentIndex).getOffre_pub();

            // Calculate the display duration based on the selected offer
            int displayDuration = getDisplayDuration(selectedOffer);

            // Start the timeline to switch advertisements after the calculated duration
            Timeline displayTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(displayDuration))
            );
            displayTimeline.setOnFinished(event -> {
                // Update the displayed advertisement
                updateDisplayedAdvertisement();
            });
            displayTimeline.setCycleCount(1); // Make sure it runs only once
            displayTimeline.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private int getDisplayDuration(String selectedOffer) {
        // Use the ServicePublicite to get the duration from the database
        ServicePublicite servicePublicite = new ServicePublicite();
        Set<Publicite> publicites = servicePublicite.getAll();

        // Find the Publicite object with the selected offer
        Publicite selectedPublicite = publicites.stream()
                .filter(publicite -> publicite.getOffre_pub().equals(selectedOffer))
                .findFirst()
                .orElse(null);

        // If the Publicite with the selected offer is found, return the duration; otherwise, use defaults
        return (selectedPublicite != null) ? selectedPublicite.getDisplayDuration() : getDefaultDisplayDuration(selectedOffer);
    }

    // Add this method to handle default display duration
    private int getDefaultDisplayDuration(String selectedOffer) {
        // Set default display duration based on the value of "offre_pub" from the database
        switch (selectedOffer) {
            case "3 mois :50dt":
            case "6 mois :90dt":
                return 15;
            case "9 mois :130dt":
                return 30; // 12 seconds

            default:
                return 15; // Default duration if offer not recognized
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            // Add the first advertisement during initialization
            updateDisplayedAdvertisement();

            // Start the timeline to switch advertisements every 30 seconds
            timeline = new Timeline(
                    new KeyFrame(scrollInterval, event -> {
                        // Update the displayed advertisement
                        updateDisplayedAdvertisement();
                    })
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < publiciteList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ActualiteGui/PubliciteItemCitoyen.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                PubliciteController itemController = fxmlLoader.getController();
                itemController.setData(publiciteList.get(i));

                if (column == 1) {
                    column = 0;
                    row++;
                }

                gridPubC.add(anchorPane, column++, row); //(child,column,row)
                // set grid width
                gridPubC.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridPubC.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridPubC.setMaxWidth(Region.USE_PREF_SIZE);

                // set grid height
                gridPubC.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridPubC.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridPubC.setMaxHeight(Region.USE_PREF_SIZE);

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


    public void retoutActCitoyen(ActionEvent actionEvent) {
        try {
            System.out.println("Resource URL: " + getClass().getResource("/ActualiteGui/AfficherActualiteCitoyenGui.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/ActualiteGui/AfficherActualiteCitoyenGui.fxml"));
            retoutActCitoyen.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
}