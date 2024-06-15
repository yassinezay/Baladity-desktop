package edu.esprit.controllers.evenement;

import edu.esprit.entities.Evenement;
import edu.esprit.services.ServiceEvenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ModifierEvent {
    private EventDashboard eventDashboardController;

    @FXML
    private TextField TFnomM;

    @FXML
    private TextField TFdateDebM;

    @FXML
    private TextField TFdateFinM;

    @FXML
    private TextField TFcapaciteM;

    @FXML
    private TextField TFcategorieM;

    @FXML
    private ImageView imageM;

    private ServiceEvenement serviceEvenement;
    private Evenement evenement;

    public void setData(Evenement evenement) {
        this.evenement = evenement;
        TFnomM.setText(evenement.getNomEvent());
        TFdateDebM.setText(evenement.getDateEtHeureDeb());
        TFdateFinM.setText(evenement.getDateEtHeureFin());
        TFcapaciteM.setText(String.valueOf(evenement.getCapaciteMax()));
        TFcategorieM.setText(evenement.getCategorie());

        String imagePath = evenement.getImageEvent();
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageM.setImage(image);
            }
        }
    }

    @FXML
    void browseMOnClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                imageM.setImage(image);
                evenement.setImageEvent(selectedFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load image: " + e.getMessage());
            }
        }
    }


    @FXML
    void ModifierEventClick(ActionEvent event) {
        updateTextFieldStyles();
        try {
            if (isAnyFieldEmpty()) {
                showAlert("Error", "Tous les champs doivent être remplis.");
                return;
            }

            if (!isCapaciteValid()) {
                showAlert("Error", "Le champ Capacite Max doit être un entier.");
                return;
            }

            if (!isDateFinValid()) {
                showAlert("Error", "La date de fin doit être après la date de début.");
                return;
            }

            evenement.setNomEvent(TFnomM.getText());
            evenement.setDateEtHeureDeb(TFdateDebM.getText());
            evenement.setDateEtHeureFin(TFdateFinM.getText());
            evenement.setCapaciteMax(Integer.parseInt(TFcapaciteM.getText()));
            evenement.setCategorie(TFcategorieM.getText());

            if (serviceEvenement == null) {
                serviceEvenement = new ServiceEvenement();
            }
            serviceEvenement.modifier(evenement);

            if (eventDashboardController != null) {
                eventDashboardController.loadEvents();
            }

            showAlert("Success", "Événement modifié avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Error", "Le champ Capacite Max doit être un entier.");
        }
    }

    @FXML
    void navigateOnClick(ActionEvent event) {
        try {
            Stage stage = (Stage) TFnomM.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void updateTextFieldStyles() {
        setFieldStyle(TFnomM, isFieldEmpty(TFnomM));
        setFieldStyle(TFdateDebM, isFieldEmpty(TFdateDebM));
        setFieldStyle(TFdateFinM, isFieldEmpty(TFdateFinM) || !isDateFinValid());
        setFieldStyle(TFcapaciteM, !isCapaciteValid());
        setFieldStyle(TFcategorieM, isFieldEmpty(TFcategorieM));
    }

    private boolean isFieldEmpty(TextField textField) {
        return textField.getText().isEmpty();
    }

    private boolean isCapaciteValid() {
        try {
            Integer.parseInt(TFcapaciteM.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isDateFinValid() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateDeb = LocalDate.parse(TFdateDebM.getText(), formatter);
        LocalDate dateFin = LocalDate.parse(TFdateFinM.getText(), formatter);
        return dateFin.isAfter(dateDeb);
    }

    private void setFieldStyle(TextField textField, boolean isInvalid) {
        if (isInvalid) {
            textField.setStyle("-fx-background-color: red;");
        } else {
            textField.setStyle("-fx-background-color: lime;");
        }
    }

    private boolean isAnyFieldEmpty() {
        return isFieldEmpty(TFnomM) || isFieldEmpty(TFdateDebM) || isFieldEmpty(TFdateFinM) ||
                isFieldEmpty(TFcapaciteM) || isFieldEmpty(TFcategorieM);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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

}
