package edu.esprit.controllers.evenement;

import edu.esprit.entities.Evenement;
import edu.esprit.services.ServiceEvenement;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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

public class EventDashboard implements Initializable {
    @FXML
    private VBox eventsLayout;
    private Scene currentScene;
    @FXML
    private TextField searchEvent;
    private List<Evenement> evenementList;
    @FXML
    private Label CountLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countEvents();
        evenementList = new ArrayList<>(getEvenements());

        // Sort the evenementList alphabetically by ignoring case
        Comparator<Evenement> comparator = Comparator.comparing(e -> e.getNomEvent().toLowerCase());
        evenementList.sort(comparator);

        for (Evenement evenement : evenementList) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL location = getClass().getResource("/evenementGui/EventItem.fxml");
            fxmlLoader.setLocation(location);

            try {
                AnchorPane anchorPane = fxmlLoader.load();
                EventItem eventItemController = fxmlLoader.getController();
                eventItemController.setData(evenement);
                // Set the EventDashboard instance to the EventItem controller
                eventItemController.setEventDashboard(this);
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

    @FXML
    void AjouterEvenementClick(ActionEvent event) {
        try {
            // Charger le fichier FXML de l'interface d'ajout d'événement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenementGui/AjouterEvent.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            AjouterEvent controller = loader.getController();
            controller.setEventDashboardController(this); // Injecter une référence vers EventDashboard

            // Obtenir la scène actuelle à partir de l'un des éléments de l'interface actuelle
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Rend la fenêtre modale
            stage.setTitle("Ajouter événement");
            stage.setScene(new Scene(root));

            // Afficher la fenêtre de ajout
            stage.showAndWait();
        } catch (IOException e) {
            // Gérer les exceptions liées au chargement de l'interface
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de navigation");
            alert.setContentText("Une erreur s'est produite lors de la navigation vers l'interface d'ajout d'événement.");
            alert.show();
        }
    }

    @FXML
    void AjouterVoteClick(ActionEvent event) {
        try {
            // Charger le fichier FXML de l'interface d'ajout d'événement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenementGui/AjouterVote.fxml"));
            Parent root = loader.load();

            AjouterVote controller = loader.getController();

            // Obtenir la scène actuelle à partir de l'un des éléments de l'interface actuelle
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Rend la fenêtre modale
            stage.setTitle("Ajouter proposition");
            stage.setScene(new Scene(root));

            // Afficher la fenêtre de ajout
            stage.showAndWait();

        } catch (IOException e) {
            // Gérer les exceptions liées au chargement de l'interface
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de navigation");
            alert.setContentText("Une erreur s'est produite lors de la navigation vers l'interface d'ajout de proposition.");
            alert.show();
        }
    }
    public void loadEvents() {
        // Effacer les événements existants de l'interface
        eventsLayout.getChildren().clear();

        // Charger à nouveau la liste des événements à partir de la base de données
        List<Evenement> evenements = new ArrayList<>(getEvenements());
        for (Evenement evenement : evenements) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL location = getClass().getResource("/evenementGui/EventItem.fxml");
            fxmlLoader.setLocation(location);

            try {
                AnchorPane anchorPane = fxmlLoader.load();
                EventItem eventItemController = fxmlLoader.getController();
                eventItemController.setData(evenement);
                eventsLayout.getChildren().add(anchorPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    private void openWebPage() {
        try {
            // Specify the URL you want to open
            String url = "https://calendar.google.com/calendar/u/2/r/day/2024/5/4";

            // Open the URL in the default web browser
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void PropositionsOnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenementGui/VoteList.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Vote List");
            stage.setScene(new Scene(root));

            stage.show();

            closeCurrentWindow();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setContentText("An error occurred while navigating to the VoteList interface.");
            alert.show();
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
            sideBarTransition.setByX(-sidebarWidth);
            isSidebarVisible = false;
            SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() + sidebarWidth);
            TranslateTransition borderPaneTransition = new TranslateTransition(Duration.millis(250), SecondBorderPane);
            borderPaneTransition.setByX(-sidebarWidth);
            borderPaneTransition.play();
        } else {
            sideBarTransition.setByX(sidebarWidth);
            isSidebarVisible = true;
            SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() - sidebarWidth);
            TranslateTransition borderPaneTransition = new TranslateTransition(Duration.millis(250), SecondBorderPane);
            borderPaneTransition.setByX(sidebarWidth);
            borderPaneTransition.play();
        }

        sideBarTransition.play();
    }
    public void RechercherEvent(String searchText, List<Evenement> evenementList ) {
        List<Evenement> filteredList = evenementList.stream()
                .filter(evenement -> evenement.getNomEvent().toLowerCase().startsWith(searchText.toLowerCase()))
                .collect(Collectors.toList());
        eventsLayout.getChildren().clear();

        for (Evenement evenement : filteredList) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL location = getClass().getResource("/evenementGui/EventItem.fxml");
            fxmlLoader.setLocation(location);

            try {
                AnchorPane anchorPane = fxmlLoader.load();
                EventItem eventItemController = fxmlLoader.getController();
                eventItemController.setData(evenement);
                eventItemController.setEventDashboard(this);
                eventsLayout.getChildren().add(anchorPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void searchEventTextChanged(KeyEvent event) {
        String searchText = searchEvent.getText();
        RechercherEvent(searchText, getEvenements());
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
    private void countEvents() {
        List<Evenement> evenements = getEvenements();
        int EventCount = evenements.size();
        CountLabel.setText("Nombre des Evenements: " + EventCount);
    }


    public void buttonreturneventr(ActionEvent actionEvent) {
        try {
            System.out.println("Resource URL: " + getClass().getResource("/MainGuiBack.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/MainGuiBack.fxml"));
            eventsLayout.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
    }

