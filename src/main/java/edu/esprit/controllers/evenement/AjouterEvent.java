package edu.esprit.controllers.evenement;

import edu.esprit.controllers.user.Login;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Evenement;
import edu.esprit.services.ServiceEvenement;
import edu.esprit.services.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.prefs.Preferences;

public class AjouterEvent {
    private static final String USER_PREF_KEY = "current_user";

    ServiceUser serviceUser = new ServiceUser();

    int userId  = Integer.parseInt(getCurrentUser());

    EndUser user = serviceUser.getOneByID(userId);
    private EventDashboard eventDashboardController;

    @FXML
    private TextField TFnom;

    @FXML
    private TextField TFdateDeb;

    @FXML
    private TextField TFdateFin;

    @FXML
    private TextField TFcapacite;

    @FXML
    private TextField TFcategorie;

    @FXML
    private ImageView imageID;

    private String imagePath; // Variable to store the path of the selected image

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    @FXML
    void AjoutEventClick(ActionEvent event) {
        updateTextFieldStyles();
        try {
            // Vérifier si tous les champs sont remplis
            if (isAnyFieldEmpty()) {
                showAlert("Error", "Tous les champs doivent être remplis.");
                return;
            }

            // Vérifier si le champ Capacite Max est un entier
            if (!isCapaciteValid()) {
                showAlert("Error", "Le champ Capacite Max doit être un entier.");
                return;
            }

            // Vérifier si la date de fin est après la date de début
            if (!isDateFinValid()) {
                showAlert("Error", "La date de fin doit être après la date de début.");
                return;
            }

            // Vérifier si une image est sélectionnée
            if (imagePath == null || imagePath.isEmpty()) {
                showAlert("Error", "Veuillez sélectionner une image.");
                return;
            }

            // Vérifier si le nom de l'événement est une chaîne de caractères
            if (!isString(TFnom.getText())) {
                showAlert("Error", "Le nom de l'événement doit être une chaîne de caractères.");
                return;
            }

            // Vérifier si la catégorie est une chaîne de caractères
            if (!isString(TFcategorie.getText())) {
                showAlert("Error", "La catégorie doit être une chaîne de caractères.");
                return;
            }

            // Récupération de l'utilisateur actuel (à remplacer par votre mécanisme d'authentification)
            // Exemple : suppose que l'utilisateur actuel a l'ID 1

            // Création de l'événement
            Evenement evenement = new Evenement(
                    user,
                    TFnom.getText(),
                    TFdateDeb.getText(),
                    TFdateFin.getText(),
                    Integer.parseInt(TFcapacite.getText()),
                    TFcategorie.getText(),
                    imagePath // Set the image path
            );

            // Ajout de l'événement via le service
            serviceEvenement.ajouter(evenement);
            if (eventDashboardController != null) {
                eventDashboardController.loadEvents();
            }


            // Affichage d'une notification de succès après l'ajout de l'événement
            showAlert("Success", "Événement ajouté avec succès !");
        } catch (NumberFormatException e) {
            // En cas d'erreur de formatage du nombre pour la capacité
            showAlert("Error", "Le champ Capacite Max doit être un entier.");
        }
    }

    @FXML
    void browseOnClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Image", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                new FileChooser.ExtensionFilter("All image files", "*.jpg", "*.png")
        );
        Stage stage = (Stage) imageID.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                // Extract the filename with extension
                String filenameWithExtension = selectedFile.getName();
                // Set the image path in the database
                imagePath = filenameWithExtension;

                // Load and display the image
                Image image = new Image(selectedFile.toURI().toString());
                imageID.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load image: " + e.getMessage());
            }
        }
    }


    @FXML
    void navigateOnClickk(ActionEvent event) {
        try {
            // Fermer la fenêtre actuelle
            Stage stage = (Stage) TFnom.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer les exceptions liées à la fermeture de la fenêtre
        }
    }



    private void updateTextFieldStyles() {
        setFieldStyle(TFnom, isFieldEmpty(TFnom) || !isString(TFnom.getText()));
        setFieldStyle(TFdateDeb, isFieldEmpty(TFdateDeb));
        setFieldStyle(TFdateFin, isFieldEmpty(TFdateFin) || !isDateFinValid());
        setFieldStyle(TFcapacite, !isCapaciteValid());
        setFieldStyle(TFcategorie, isFieldEmpty(TFcategorie) || !isString(TFcategorie.getText()));
    }

    private boolean isFieldEmpty(TextField textField) {
        return textField.getText().isEmpty();
    }

    private boolean isCapaciteValid() {
        try {
            Integer.parseInt(TFcapacite.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isDateFinValid() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateDeb = LocalDate.parse(TFdateDeb.getText(), formatter);
        LocalDate dateFin = LocalDate.parse(TFdateFin.getText(), formatter);
        return dateFin.isAfter(dateDeb);
    }

    private void setFieldStyle(TextField textField, boolean isInvalid) {
        if (isInvalid) {
            textField.setStyle("-fx-border-color: red;");
        } else {
            textField.setStyle("-fx-border-color: lime;");
        }
    }

    private boolean isAnyFieldEmpty() {
        return isFieldEmpty(TFnom) || isFieldEmpty(TFdateDeb) || isFieldEmpty(TFdateFin) ||
                isFieldEmpty(TFcapacite) || isFieldEmpty(TFcategorie);
    }

    private boolean isString(String text) {
        return text.matches("^[a-zA-Z]*$");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Change to INFORMATION
        alert.setTitle(title);
        alert.setContentText(content);
        try {
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setEventDashboardController(EventDashboard eventDashboardController) {
        this.eventDashboardController = eventDashboardController;
    }
    private String getCurrentUser() {
        Preferences preferences = Preferences.userNodeForPackage(Login.class);
        return preferences.get(USER_PREF_KEY, "DefaultUser");
    }
}
