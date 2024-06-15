package edu.esprit.controllers.Actualite;

import edu.esprit.entities.Actualite;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Publicite;
import edu.esprit.services.ServicePublicite;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifierPubliciteController implements Initializable {

    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private VBox MainLeftSidebar;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private TextField TFcontactpubModif;

    @FXML
    private TextField TFdescriptionpubModif;

    @FXML
    private TextField TFlocalisationpubModif;

    @FXML
    private TextField TFtitrepubModif;

    @FXML
    private BorderPane firstborderpane;

    @FXML
    private ImageView imgView_pubModif;

    @FXML
    private ImageView imgView_pub;

    @FXML
    private Label labelPubModif;

    @FXML
    private Button modifierPubliciteAction;
    private boolean isSidebarVisible = true;
    @FXML
    private Label numeroexiste;

    @FXML
    private ComboBox<String> offrePubComboModif;

    @FXML
    private Button retourToAfficherPub;

    @FXML
    private Button uploadbuttonPModif;
    private EndUser user;
    private Actualite a;

    private ServicePublicite servicePublicite;
    private Publicite publicite;
    private String imagePath;
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    @FXML
    void modifierPubliciteAction(ActionEvent event) {

            if (publicite != null && servicePublicite != null) {
                // Créer une boîte de dialogue de confirmation
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setContentText("Êtes-vous sûr de vouloir modifier cette pub ?");
                confirmationAlert.setTitle("Confirmation de modification");

                // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
                Optional<ButtonType> result = confirmationAlert.showAndWait();

                // Vérifier si l'utilisateur a cliqué sur le bouton OK
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Mettre à jour les données de la réclamation avec les valeurs des champs de texte
                    publicite.setTitre_pub(TFtitrepubModif.getText());
                    publicite.setDescription_pub(TFdescriptionpubModif.getText());
                    publicite.setContact_pub(Integer.parseInt(TFcontactpubModif.getText()));
                    publicite.setLocalisation_pub(TFlocalisationpubModif.getText());
                    publicite.setOffre_pub(offrePubComboModif.getValue());
                    publicite.setImage_pub(imagePath);
                    publicite.setEndUser(user);
                    publicite.setActualite(a);
                   // imagePath peut être nul si aucune image n'est sélectionnée
                    try {
                        // Appeler la méthode de modification du service de réclamation
                        servicePublicite.modifier(publicite);

                        // Afficher un message de succès
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setContentText("pub modifiée avec succès !");
                        successAlert.setTitle("Modification réussie");
                        successAlert.show();

                        // Rediriger l'utilisateur vers la vue précédente (par exemple, la liste des réclamations)
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("/ActualiteGui/AfficherPubliciteCitoyenGui.fxml"));
                            TFtitrepubModif.getScene().setRoot(root);
                        } catch (IOException e) {
                            // Gérer l'exception si la redirection échoue
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setContentText("Une erreur s'est produite lors de la redirection.");
                            errorAlert.setTitle("Erreur de redirection");
                            errorAlert.show();
                        }
                    } catch (Exception e) {
                        // Afficher un message d'erreur en cas d'échec de la modification
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setContentText("Erreur lors de la modification de la pub : " + e.getMessage());
                        errorAlert.setTitle("Erreur de modification");
                        errorAlert.show();
                    }
                }
            } else {
                // Afficher un message d'erreur si la réclamation est null ou si le service de réclamation est null
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText("Impossible de modifier la pub car aucune pub n'est sélectionnée ou le service de pub est null.");
                errorAlert.setTitle("Erreur de modification");
                errorAlert.show();
            }

    }



    @FXML
    void uploadimgP(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\amine\\Desktop\\PiDev\\DevMasters-Baladity\\public\\uploads"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Image", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                new FileChooser.ExtensionFilter("All image files", "*.jpg", "*.png")
        );
        Stage stage = (Stage) uploadbuttonPModif.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // Store just the file name with its extension
            imagePath = selectedFile.getName();

            labelPubModif.setText(imagePath);
            String fileUrl = selectedFile.toURI().toString();
            Image image = new Image(fileUrl);
            imgView_pubModif.setImage(image);

            // Update the image name in the publicite object
            if (publicite != null) {
                publicite.setImage_pub(imagePath);
            }
        }
    }

    public void setData(Publicite publicite) {
        this.publicite = publicite;
        if (publicite != null) {
            TFtitrepubModif.setText(publicite.getTitre_pub());
            TFdescriptionpubModif.setText(publicite.getDescription_pub());
            TFcontactpubModif.setText(String.valueOf(publicite.getContact_pub()));
            TFlocalisationpubModif.setText(publicite.getLocalisation_pub());
            offrePubComboModif.setValue(publicite.getOffre_pub());
            String imageUrl = publicite.getImage_pub();
            user = publicite.getEndUser();
            a = publicite.getActualite();
            System.out.println(user);
            System.out.println(a);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    // Create an instance of File using just the image name
                    File file = new File("C:\\Users\\amine\\Desktop\\PiDev\\DevMasters-Baladity\\public\\uploads\\" + imageUrl);
                    // Check if the file exists
                    if (file.exists()) {
                        // Create an Image instance from the file path
                        Image image = new Image(file.toURI().toString());
                        // Set the image in the ImageView
                        imgView_pubModif.setImage(image);
                    } else {
                        System.err.println("File not found: " + file.getPath());
                    }
                } catch (Exception e) {
                    // Handle any exception
                    e.printStackTrace();
                }
            }
        }
    }


    public void setServicePublicite(ServicePublicite servicePublicite) {
        this.servicePublicite = servicePublicite;
    }

    @FXML
    void retourToAfficherPub(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ActualiteGui/AfficherPubliciteCitoyenGui.fxml"));
            Parent root = loader.load();

            // Update the scene to display the list of advertisements
            TFtitrepubModif.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors du retour à la liste des publicités.");
        }
    }

    private void showErrorAlert(String errorMessage) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setContentText(errorMessage);
        errorAlert.setTitle("Erreur");
        errorAlert.show();
    }



    @FXML
    public void BTNToggleSidebar(ActionEvent event) {
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser la taille du SecondBorderPane avec la même largeur que la barre latérale
        double sidebarWidth = MainLeftSidebar.getWidth();
        SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() + sidebarWidth);
    }
}
