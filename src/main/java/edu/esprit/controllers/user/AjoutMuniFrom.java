package edu.esprit.controllers.user;

import edu.esprit.entities.Municipality;
import edu.esprit.services.ServiceMuni;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class AjoutMuniFrom {

    @FXML
    private TextField TFnom;

    @FXML
    private TextField TFemail;

    @FXML
    private PasswordField TFpassword;

    @FXML
    private ImageView ImageF;

    File selectedFile;

    private final ServiceMuni serviceMuni = new ServiceMuni();

    @FXML
    void ajouterMuniAction(ActionEvent event) {
        serviceMuni.ajouter(new Municipality(TFnom.getText(),TFemail.getText(),TFpassword.getText(),selectedFile.getAbsolutePath()));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText("GG");
        alert.show();
    }

    @FXML
    void pickImageAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            // Handle the selected image file (e.g., display it, process it, etc.)
            System.out.println("Selected image: " + selectedFile.getAbsolutePath());
            Image image = new Image(selectedFile.toURI().toString());
            ImageF.setImage(image);
        }
    }

    public void navigatetoAfficherMuniAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/user/AfficherMuni.fxml"));
            TFnom.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }

}
