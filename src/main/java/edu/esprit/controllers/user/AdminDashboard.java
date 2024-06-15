package edu.esprit.controllers.user;

import edu.esprit.entities.EndUser;
import edu.esprit.entities.Municipality;
import edu.esprit.services.ServiceMuni;
import edu.esprit.services.ServiceUser;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


public class AdminDashboard implements Initializable {

    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private VBox MainLeftSidebar;

    private boolean isSidebarVisible = true;

    @FXML
    private VBox usersLayout;

    @FXML
    private HBox muniButtonLayout;

    @FXML
    private TextField tfRecherche;

    void userItem(List<EndUser> userList){
        //Users list
        List<EndUser> users = new ArrayList<>(userList);
        for (EndUser user : users) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL location = getClass().getResource("/user/UserItem.fxml");
            fxmlLoader.setLocation(location);
//            fxmlLoader.setLocation(getClass().getResource("UserItem.fxml"));



            try {
                HBox hBox = fxmlLoader.load();
                UserItem cic = fxmlLoader.getController();
                cic.setData(user);
                usersLayout.getChildren().add(hBox);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tfRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProducts(newValue, users());
        });

        double sidebarWidth = MainLeftSidebar.getWidth();
        SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() + sidebarWidth);

        userItem(users());

        //Muni list
        List<Municipality> munis = new ArrayList<>(munis());
        for (Municipality muni : munis) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL location = getClass().getResource("/user/MuniItem.fxml");
            fxmlLoader.setLocation(location);
//            fxmlLoader.setLocation(getClass().getResource("UserItem.fxml"));



            try {
                HBox hBox = fxmlLoader.load();
                MuniItem cic = fxmlLoader.getController();
                cic.setData(muni);
                muniButtonLayout.getChildren().add(hBox);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<EndUser> users(){
        List<EndUser> userList = new ArrayList<>();
//        EndUser user = new EndUser();

        ServiceUser serviceUser = new ServiceUser();
//        EndUser users = serviceUser.getOneByID(35);
        Set<EndUser> list = serviceUser.getAll();

        // Utilisation de la boucle for-each
        for (EndUser user : list) {
            // Faire quelque chose avec chaque utilisateur (EndUser)
            itemUser(user,userList);
        }

        return userList;
    }

    private List<Municipality> munis(){
        List<Municipality> muniList = new ArrayList<>();

        ServiceMuni serviceMuni = new ServiceMuni();
//        Muni muni = serviceMuni.getOneByID(15);
        Set<Municipality> list = serviceMuni.getAll();

        // Utilisation de la boucle for-each
        for (Municipality muni1 : list) {
            // Faire quelque chose avec chaque utilisateur (EndUser)
            itemMuni(muni1,muniList);
        }

        return muniList;
    }

    void itemUser(EndUser user,List<EndUser> userList){
        user.setNom(user.getNom());
        user.setImage(user.getImage());
        user.setPhoneNumber(user.getPhoneNumber());
        user.setEmail(user.getEmail());
        user.setType(user.getType());
        userList.add(user);
    }

    void itemMuni(Municipality muni, List<Municipality> muniList){
        muni.setNom_muni(muni.getNom_muni());
        muniList.add(muni);
    }

    @FXML
    void addMuni(ActionEvent event) {
        openForm();
    }

    public void openForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/AjoutMuniFrom.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Popup Form");

            Scene popupScene = new Scene(root, 600, 500);
            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
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

    private void filterProducts(String searchText, List<EndUser> userList ) {
        // Filter the productList based on the searchText
        List<EndUser> filteredList = userList.stream()
                .filter(user ->
                        user.getNom().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());

        // Clear the existing content in the grid
        usersLayout.getChildren().clear();

        // Display the filtered results
        userItem(filteredList);
    }
    @FXML
    void filterByName(ActionEvent event) {

        // Tri de la liste de réclamations par date
        users().sort(Comparator.comparing(EndUser::getEmail));

        // Affichage des réclamations triées
        userItem(users());
    }


    public void buttonreturnuser(ActionEvent actionEvent) {
        try {
            System.out.println("Resource URL: " + getClass().getResource("/MainGuiBack.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/MainGuiBack.fxml"));
            muniButtonLayout.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

}
