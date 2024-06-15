package edu.esprit.controllers.evenement;

import edu.esprit.entities.Evenement;
import edu.esprit.services.ServiceEvenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class EventItem implements Initializable {
    private EventDashboard eventDashboard;

    @FXML
    private ImageView img;

    @FXML
    private Text nomEventT;

    private Evenement evenement;

    private ServiceEvenement serviceEvenement;

    public void setEventDashboard(EventDashboard eventDashboard) {
        this.eventDashboard = eventDashboard;
    }

    public void setData(Evenement evenement) {
        this.evenement = evenement;
        nomEventT.setText(evenement.getNomEvent());
        String imagePath = evenement.getImageEvent();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                // Construct the URL for the image
                String fileUrl = "C:\\Users\\amine\\Desktop\\PiDev\\DevMasters-Baladity\\public\\uploads\\" + imagePath;

                // Open an input stream to read the image data
                InputStream inputStream = new FileInputStream(fileUrl);

                // Load the image from the input stream
                Image image = new Image(inputStream);

                // Set the image in the ImageView
                img.setImage(image);
            } catch (FileNotFoundException e) {
                // Handle case where image file is not found
                System.err.println("Image file not found: " + imagePath);
                e.printStackTrace();
            }
        }
    }


    @FXML
    void ModifierEvenementClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenementGui/ModifierEvent.fxml"));
            Parent root = loader.load();

            ModifierEvent controller = loader.getController();
            controller.setData(evenement);

            // Make sure eventDashboard is initialized and set correctly
            if (eventDashboard != null) {
                controller.setEventDashboardController(eventDashboard);
            } else {
                System.err.println("EventDashboard instance is null.");
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier événement");
            stage.setScene(new Scene(root));

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions
        }
    }



    @FXML
    void SupprimerEvenementClick(ActionEvent event) {
        if (evenement != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (serviceEvenement == null) {
                    serviceEvenement = new ServiceEvenement();
                }
                serviceEvenement.supprimer(evenement.getId_E());

                // Actualiser la vue des événements
                actualiserVueEvenements();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("L'événement a été supprimé avec succès.");
                alert.setTitle("Événement supprimé");
                alert.show();
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Impossible de supprimer l'événement car aucun événement n'est sélectionné.");
            errorAlert.setTitle("Erreur de suppression");
            errorAlert.show();
        }
    }

    @FXML
    void DetailsEvenementClick(ActionEvent event) {
        try {
            // Charger la vue des détails de l'événement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenementGui/DetailsEvent.fxml"));
            Parent root = loader.load();

            // Passer l'événement dont les détails sont affichés au contrôleur des détails
            DetailsEvent controller = loader.getController();
            controller.setData(evenement);

            // Afficher la vue des détails de l'événement
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            // Gérer les exceptions liées au chargement de la vue des détails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur lors du chargement des détails de l'événement.");
            alert.setTitle("Erreur de chargement des détails");
            alert.show();
        }
    }
    // Méthode pour actualiser la vue des événements
    private void actualiserVueEvenements() {
        // Redirection vers la vue précédente (par exemple, la liste des événements)
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/evenementGui/EventDashboard.fxml"));
            nomEventT.getScene().setRoot(root);
        } catch (IOException e) {
            // Gérer l'exception si la redirection échoue
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Une erreur s'est produite lors de la redirection.");
            errorAlert.setTitle("Erreur de redirection");
            errorAlert.show();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
