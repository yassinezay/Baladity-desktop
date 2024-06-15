package edu.esprit.controllers.evenement;

import edu.esprit.entities.Evenement;
import edu.esprit.services.ServiceEvenement;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class EventShowFront implements Initializable {
    @FXML
    private VBox eventsLayout;
    private Scene currentScene;
    private List<Evenement> evenementList;
    @FXML
    private TextField searchEvenement;
    @FXML
    private Label countLabelFront;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countEvents();
        evenementList = new ArrayList<>(getEvenements());
        Comparator<Evenement> comparator = Comparator.comparing(e -> e.getNomEvent().toLowerCase());
        evenementList.sort(comparator);
        for (Evenement evenement : evenementList) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL location = getClass().getResource("/evenementGui/EventFront.fxml");
            fxmlLoader.setLocation(location);

            try {
                AnchorPane anchorPane = fxmlLoader.load();
                EventFront eventItemController = fxmlLoader.getController();
                eventItemController.setData(evenement);
                // Set the EventDashboard instance to the EventItem controller
                eventItemController.setEventShowFront(this);
                eventsLayout.getChildren().add(anchorPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<Evenement> getEvenements() {
        List<Evenement> evenementList = new ArrayList<>();
        ServiceEvenement serviceEvenement = new ServiceEvenement();
        Set<Evenement> evenements = serviceEvenement.getAll();

        for (Evenement evenement : evenements) {
            item(evenement, evenementList);
        }

        return evenementList;
    }

    void item(Evenement evenement, List<Evenement> evenementList) {
        evenement.setNomEvent(evenement.getNomEvent());
        evenement.setDateEtHeureDeb(evenement.getDateEtHeureDeb());
        evenement.setDateEtHeureFin(evenement.getDateEtHeureFin());
        evenement.setCapaciteMax(evenement.getCapaciteMax());
        evenement.setCategorie(evenement.getCategorie());
        evenementList.add(evenement);
    }
    public void RechercherEvent(String searchEvenement, List<Evenement> evenementList ) {
        List<Evenement> filteredList = evenementList.stream()
                .filter(evenement -> evenement.getNomEvent().toLowerCase().startsWith(searchEvenement.toLowerCase()))
                .collect(Collectors.toList());
        eventsLayout.getChildren().clear();

        for (Evenement evenement : filteredList) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL location = getClass().getResource("/evenementGui/EventFront.fxml");
            fxmlLoader.setLocation(location);

            try {
                AnchorPane anchorPane = fxmlLoader.load();
                EventFront eventFront = fxmlLoader.getController();
                eventFront.setData(evenement);
                eventFront.setEventShowFront(this);
                eventsLayout.getChildren().add(anchorPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void searchEventTextChanged(KeyEvent event) {
        String searchText = searchEvenement.getText();
        RechercherEvent(searchText, getEvenements());
    }



    public void loadEvents() {
        // Effacer les événements existants de l'interface
        eventsLayout.getChildren().clear();

        // Charger à nouveau la liste des événements à partir de la base de données
        List<Evenement> evenements = new ArrayList<>(getEvenements());
        for (Evenement evenement : evenements) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL location = getClass().getResource("/evenementGui/EventFront.fxml");
            fxmlLoader.setLocation(location);

            try {
                GridPane gridPane = fxmlLoader.load();
                EventItem eventItemController = fxmlLoader.getController();
                eventItemController.setData(evenement);
                eventsLayout.getChildren().add(gridPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void closeCurrentWindow() {
        Stage stage = (Stage) eventsLayout.getScene().getWindow();
        stage.close();
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
    private void openWebPageCit() {
        try {
            // Specify the URL you want to open
            String url = "https://calendar.google.com/calendar/u/2/r/day/2024/5/4";

            // Open the URL in the default web browser
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private void countEvents() {
        List<Evenement> evenements = getEvenements();
        int EventCount = evenements.size();
        countLabelFront.setText("Il existe"   + EventCount + " événements programmés.");
    }

}
