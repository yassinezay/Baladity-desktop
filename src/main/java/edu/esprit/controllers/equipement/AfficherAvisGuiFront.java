package edu.esprit.controllers.equipement;

import edu.esprit.controllers.user.Login;
import edu.esprit.entities.Avis;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Equipement;
import edu.esprit.services.ServiceAvis;
import edu.esprit.services.ServiceEquipement;
import edu.esprit.services.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.prefs.Preferences;

public class AfficherAvisGuiFront implements Initializable {
    private static final String USER_PREF_KEY = "current_user";

    ServiceUser serviceUser = new ServiceUser();
    int userId  = Integer.parseInt(getCurrentUser());
    //  int userId = 48;
    EndUser user = serviceUser.getOneByID(userId);
    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private Button ajouterAvisbutton;

    @FXML
    private BorderPane firstborderpane;

    @FXML
    private GridPane grid;

    @FXML
    private Text lesavis;

    @FXML
    private Button retourEquipement;

    @FXML
    private ScrollPane scroll;




    private ServiceAvis sa = new ServiceAvis();
    private ServiceEquipement serviceEquipement;
    private Equipement equipement;
    Set<Avis> avisSet;
    List<Avis> avisList;

    public void setData(Equipement equipement) {
        this.equipement = equipement;
        avisSet = sa.getAvisByEquipement(equipement);
        avisList = new ArrayList<>(avisSet);
        initializeAvisGrid(); // Call the method to initialize the grid with data
    }

    @FXML
    void BTNGestionAct(ActionEvent event) {

    }

    @FXML
    void BTNGestionEquipement(ActionEvent event) {

    }

    @FXML
    void BTNGestionEvennement(ActionEvent event) {

    }

    @FXML
    void BTNGestionRec(ActionEvent event) {

    }

    @FXML
    void BTNGestionTache(ActionEvent event) {

    }

    @FXML
    void ajouterAvisAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/equipementGui/AjouterAvisGuiFront.fxml"));
            Parent root = loader.load();
            AjouterAvisGuiFront controller = loader.getController();
            controller.setServiceEquipement(serviceEquipement);
            controller.setData(equipement); // Set the data before loading the controller
            retourEquipement.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }
    @FXML
    void navigateAvisEquipement(ActionEvent event) {
        try {
            System.out.println("Resource URL: " + getClass().getResource("/equipementGui/AfficherEquipementGuiFront.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/equipementGui/AfficherEquipementGuiFront.fxml"));
            retourEquipement.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }
    public void setServiceEquipement(ServiceEquipement serviceEquipement) {
        this.serviceEquipement = serviceEquipement;
    }
    private void initializeAvisGrid() {
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < avisList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/equipementGui/AvisItemGuiFront.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                AvisItemGuiFront itemController = fxmlLoader.getController();
                itemController.setData(avisList.get(i));

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    private String getCurrentUser() {
        Preferences preferences = Preferences.userNodeForPackage(Login.class);
        return preferences.get(USER_PREF_KEY, "DefaultUser");
    }
}
