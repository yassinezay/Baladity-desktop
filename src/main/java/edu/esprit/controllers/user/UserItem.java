package edu.esprit.controllers.user;

import edu.esprit.entities.EndUser;
import edu.esprit.services.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserItem implements Initializable {

    @FXML
    private Label email;

    @FXML
    private ImageView img;

    @FXML
    private Label name;

    @FXML
    private Label phone;

    @FXML
    private Label type;

    ServiceUser serviceUser = new ServiceUser();

    int userId;

    public void setData(EndUser user) {
        userId = user.getId();
        String imageUrl = user.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                // Create an instance of File using the file path
                File file = new File("C:\\Users\\amine\\Desktop\\PiDev\\DevMasters-Baladity\\public\\uploads\\" + imageUrl);
                // Check if the file exists
                if (file.exists()) {
                    // Create an Image instance from the file path
                    Image image = new Image(file.toURI().toString());
                    // Set the image in the ImageView
                    img.setImage(image);
                } else {
                    System.err.println("File not found: " + file.getPath());
                }
            } catch (Exception e) {
                // Handle the exception if the file URL is not valid
                e.printStackTrace();
            }
        } else {
            // If the image URL is empty or null, display a default image
            URL defaultImageUrl = getClass().getResource("/assets/man.png");
            if (defaultImageUrl != null) {
                Image defaultImage = new Image(defaultImageUrl.toString());
                img.setImage(defaultImage);
            } else {
                System.err.println("Default image not found!");
            }
        }

        name.setText(user.getNom());
        phone.setText(user.getPhoneNumber());
        email.setText(user.getEmail());
        type.setText(user.getType());
    }


    @FXML
    void editUser(ActionEvent event) {
        try {
            EndUser endUser = serviceUser.getOneByID(userId);
            // Charger la vue de modification d'événement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/ModifierUser.fxml"));
            Parent root = loader.load();

            // Passer l'événement à modifier au contrôleur de modification
            ModifierUser controller = loader.getController();
            controller.setData(endUser);

            // Créer une nouvelle fenêtre (stage) pour afficher la vue de modification
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Rend la fenêtre modale
            stage.setTitle("Modifier utilisateur");
            stage.setScene(new Scene(root));

            // Afficher la fenêtre de modification
            stage.showAndWait();
        } catch (IOException e) {
            // Gérer les exceptions liées au chargement de la vue de modification
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur lors de la modification de l'événement.");
            alert.setTitle("Erreur de modification");
            alert.show();
        }
    }

    @FXML
    void deleteUser(ActionEvent event) {
        serviceUser.supprimer(userId);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void refreshScene(Scene scene) {
        if (scene != null) {
            // If you want to refresh the entire scene
            scene.getRoot().requestLayout();

            // If you want to refresh specific components, find them by ID or other means
            // For example, if you have a TextField with the ID "textField"
            // TextField textField = (TextField) scene.lookup("#textField");
            // textField.setText("Updated Data");
        }
    }

}
