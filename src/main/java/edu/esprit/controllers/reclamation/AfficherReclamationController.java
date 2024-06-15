package edu.esprit.controllers.reclamation;

import edu.esprit.controllers.user.Login;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Reclamation;
import edu.esprit.services.ServiceReclamation;
import edu.esprit.services.ServiceUser;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class AfficherReclamationController implements Initializable {
    private static final String USER_PREF_KEY = "current_user";

    ServiceUser serviceUser = new ServiceUser();

    int userId  = Integer.parseInt(getCurrentUser());

    EndUser user = serviceUser.getOneByID(userId);
    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private VBox MainLeftSidebar;
    private boolean isSidebarVisible = true;
    @FXML
    private GridPane grid;
    @FXML
    private ScrollPane scroll;
    @FXML
    private TextField Recherche;
    private ServiceReclamation sr=new ServiceReclamation();
    Set<Reclamation> reclamationSet = sr.getReclamationsByUser(user);
    List<Reclamation> reclamationList = new ArrayList<>(reclamationSet);



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Recherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProducts(newValue, reclamationList);
        });
        affichergrid(reclamationList);
    }
    private void affichergrid(List<Reclamation> reclamationList){
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < reclamationList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/reclamationGui/ReclamationItemComponentGui.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ReclamationItemComponentController itemController = fxmlLoader.getController(); // Utilisez ReclamationItemComponentController
                itemController.setData(reclamationList.get(i));

                if (column == 1) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row); //(child,column,row)
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

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
    @FXML
    void buttonReturnAfficherReclamation(ActionEvent event) {
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
    private void filterProducts(String searchText, List<Reclamation> reclamationList ) {
        // Filter the productList based on the searchText
        List<Reclamation> filteredList = reclamationList.stream()
                .filter(reclamation ->
                        reclamation.getSujet_reclamation().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());

        // Clear the existing content in the grid
        grid.getChildren().clear();

        // Display the filtered results
        affichergrid(filteredList);
    }
    private boolean isSortedAscending = true;

    @FXML
    void filtrerParDate(ActionEvent event) {
        // Inverser l'état du tri à chaque clic
        isSortedAscending = !isSortedAscending;

        // Tri de la liste de réclamations par date
        if (isSortedAscending) {
            reclamationList.sort((r1, r2) -> r1.getDate_reclamation().compareTo(r2.getDate_reclamation()));
        } else {
            reclamationList.sort((r1, r2) -> r2.getDate_reclamation().compareTo(r1.getDate_reclamation()));
        }

        // Affichage des réclamations triées
        affichergrid(reclamationList);
    }
    private String getCurrentUser() {
        Preferences preferences = Preferences.userNodeForPackage(Login.class);
        return preferences.get(USER_PREF_KEY, "DefaultUser");
    }


    public void goToMessagerie(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/MessagerieAutreGui.fxml"));
            MainAnchorPaneBaladity.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
}


