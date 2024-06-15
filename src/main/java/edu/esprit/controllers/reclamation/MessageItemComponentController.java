package edu.esprit.controllers.reclamation;
import edu.esprit.entities.Messagerie;
import edu.esprit.entities.Reclamation;
import edu.esprit.services.ServiceMessagerie;
import edu.esprit.services.ServiceReclamation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;


public class MessageItemComponentController {


    @FXML
    private Label contenuReciver;

    @FXML
    private ImageView userphotomsg;

    private Messagerie messagerie;

    ServiceMessagerie sm =  new ServiceMessagerie();
    java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());



    public void setData(Messagerie messagerie){
        this.messagerie = messagerie;
        contenuReciver.setText(messagerie.getContenu_message());
        contenuReciver.setWrapText(true);
        contenuReciver.setPrefHeight(40);
        String imageUrl = messagerie.getSender_message().getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                // Créer une instance de File à partir du chemin d'accès à l'image
                File file = new File(imageUrl);
                // Convertir le chemin de fichier en URL
                String fileUrl = file.toURI().toURL().toString();
                // Créer une instance d'Image à partir de l'URL de fichier
                Image image = new Image(fileUrl);
                // Définir l'image dans l'ImageView
                userphotomsg.setImage(image);
            } catch (MalformedURLException e) {
                // Gérer l'exception si le chemin d'accès à l'image n'est pas valide
                e.printStackTrace();
            }
        } else {
            // Si l'URL de l'image est vide ou null, afficher une image par défaut
            // Par exemple, si vous avez une image "imageblanche.png" dans votre dossier src/main/resources
            // Vous pouvez utiliser getClass().getResource() pour obtenir son URL
            URL defaultImageUrl = getClass().getResource("/assets/profile.png");
            if (defaultImageUrl != null) {
                Image defaultImage = new Image(defaultImageUrl.toString());
                userphotomsg.setImage(defaultImage);
            } else {
                System.err.println("L'image par défaut n'a pas été trouvée !");
            }
        }
    }
    @FXML
    void deleteMessageAction(ActionEvent event) {
        if (messagerie != null) {
            // Créer une boîte de dialogue de confirmation
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce message ?");
            confirmationAlert.setTitle("Confirmation de suppression");

            // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            // Vérifier si l'utilisateur a cliqué sur le bouton OK
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer le message
                ServiceMessagerie serviceMessagerie = new ServiceMessagerie();
                serviceMessagerie.supprimer(messagerie.getId_message());

                // Afficher une alerte pour informer l'utilisateur que le message a été supprimé avec succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Le message a été supprimé avec succès.");
                alert.setTitle("Message supprimé");
                alert.show();

                // Rafraîchir la vue pour refléter les changements
                try {
                    // Rediriger l'utilisateur vers la vue précédente (par exemple, la liste des messages)
                    Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/AfficherReclamationGui.fxml"));
                    contenuReciver.getScene().setRoot(root);
                } catch (IOException e) {
                    // Gérer l'exception si la redirection échoue
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Une erreur s'est produite lors de la redirection.");
                    errorAlert.setTitle("Erreur de redirection");
                    errorAlert.show();
                }
            }
        } else {
            // Afficher un message d'erreur si le message est null
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Impossible de supprimer le message car aucun message n'est sélectionné.");
            errorAlert.setTitle("Erreur de suppression");
            errorAlert.show();
        }
    }

    @FXML
    void editMessageAction(ActionEvent event) {
        openEditMessagePopup(messagerie);
    }
    private void openEditMessagePopup(Messagerie message) {
        // Create a new stage for the pop-up window
        Stage editStage = new Stage();
        editStage.setTitle("Edit Message");

        // Create a TextArea to allow the user to edit the message content
        TextArea editTextArea = new TextArea(message.getContenu_message());
        editTextArea.setWrapText(true); // Enable text wrapping
        editTextArea.setMaxWidth(300); // Set a max width to trigger wrapping
        editTextArea.setPrefRowCount(5);
        editTextArea.setPrefColumnCount(20);

        // Create a button to save the edited message
        Button saveButton = new Button("Update", new ImageView(new Image("/assets/edit.png")));
        ((ImageView) saveButton.getGraphic()).setFitWidth(15);
        ((ImageView) saveButton.getGraphic()).setFitHeight(15);

        saveButton.setStyle("-fx-background-color:  #008000; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 10; -fx-font-size: 13;");
        saveButton.setOnAction(saveEvent -> {
            // Update the message content in the database
            message.setContenu_message(editTextArea.getText());
            message.setDate_message(sqlDate);
            // message.setDate(new Timestamp(System.currentTimeMillis())); // Vous pouvez utiliser ceci pour définir la date actuelle
           sm.modifier(message);



            // Close the pop-up window
            editStage.close();
            // Update the chat messages
            try {
                // Rediriger l'utilisateur vers la vue précédente (par exemple, la liste des messages)
                Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/AfficherReclamationGui.fxml"));
                contenuReciver.getScene().setRoot(root);
            } catch (IOException e) {
                // Gérer l'exception si la redirection échoue
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText("Une erreur s'est produite lors de la redirection.");
                errorAlert.setTitle("Erreur de redirection");
                errorAlert.show();
            }
            //updateChatMessages();
        });

        // Create a VBox to hold the TextArea and the Save button
        VBox editVBox = new VBox(editTextArea, saveButton);
        editVBox.setSpacing(10.0);
        editVBox.setPadding(new Insets(10.0));

        // Create a scene and set it on the stage
        Scene editScene = new Scene(editVBox);
        editStage.setScene(editScene);

        // Show the pop-up window
        editStage.show();
    }





}
