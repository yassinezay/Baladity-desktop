package edu.esprit.controllers.Actualite;

import edu.esprit.entities.Actualite;
import edu.esprit.entities.EndUser;
import edu.esprit.services.ServiceActualite;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import java.util.Date;
import java.util.ResourceBundle;

public class ModifierActualiteController implements Initializable {

    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private VBox MainLeftSidebar;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private TextField TFdescriptionModifier;

    @FXML
    private TextField TFtitreModifier;

    @FXML
    private BorderPane firstborderpane;

    @FXML
    private ImageView imgView_actualiteModifier;

    @FXML
    private Label labelAModifier;

    @FXML
    private Button modifierActualiteAction;

    @FXML
    private Button uploadbuttonAModifier;


    private boolean isSidebarVisible = true;
    private ServiceActualite serviceActualite;
    private Actualite actualite;
    private String imagePath;

    @FXML
    void modifierActualiteAction(ActionEvent event) {
        if (actualite != null && serviceActualite != null) {
            String nouveauTitre = TFtitreModifier.getText();
            String nouvelleDescription = TFdescriptionModifier.getText();

            // Validate titre and description
            if (nouveauTitre.length() > 6 && nouvelleDescription.length() >= 15) {
                actualite.setTitre_a(nouveauTitre);
                actualite.setDescription_a(nouvelleDescription);
                actualite.setDate_a(java.sql.Date.valueOf(java.time.LocalDate.now()));

                try {
                    // Check if imagePath is not null and update actualite's image path
                    if (imagePath != null) {
                        actualite.setImage_a(imagePath);
                    }

                    serviceActualite.modifier(actualite);

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setContentText("Actualité modifiée avec succès !");
                    successAlert.setTitle("Modification réussie");
                    successAlert.show();
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Erreur lors de la modification de l'actualité : " + e.getMessage());
                    errorAlert.setTitle("Erreur de modification");
                    errorAlert.show();
                }
            } else {
                // Show validation message for titre and description with a red background
                if (nouveauTitre.length() <= 6) {
                    // Additional check for titre length
                    showAlert(Alert.AlertType.ERROR, "Error", "Le titre doit avoir plus de 6 caractères.");
                    TFtitreModifier.requestFocus();
                }
                if (nouvelleDescription.length() < 15) {
                    // Additional check for description length
                    showAlert(Alert.AlertType.ERROR, "Error", "La description doit avoir au moins 15 caractères.");
                    TFdescriptionModifier.requestFocus();
                }
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Impossible de modifier l'actualité car aucune actualité n'est sélectionnée ");
            errorAlert.setTitle("Erreur de modification");
            errorAlert.show();
        }
    }

    @FXML
    void uploadimgAModifer(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\amine\\Desktop\\PiDev\\DevMasters-Baladity\\public\\uploads"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Image", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                new FileChooser.ExtensionFilter("All image files", "*.jpg", "*.png")
        );
        Stage stage = (Stage) uploadbuttonAModifier.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // Store just the file name with its extension
            imagePath = selectedFile.getName();

            // Display the file name
            labelAModifier.setText(imagePath);

            // Load and display the image
            Image image = new Image(selectedFile.toURI().toString());
            imgView_actualiteModifier.setImage(image);
        }
    }



    public void setData(Actualite actualite) {
        this.actualite = actualite;
        if (actualite != null) {
            TFtitreModifier.setText(actualite.getTitre_a());
            TFdescriptionModifier.setText(actualite.getDescription_a());

            String imageUrl = actualite.getImage_a();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    File file = new File(imageUrl);
                    String fileUrl = file.toURI().toURL().toString();
                    Image image = new Image(fileUrl);
                    imgView_actualiteModifier.setImage(image);
                } catch (MalformedURLException e) {
                    System.err.println("Malformed URL: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                URL defaultImageUrl = getClass().getResource("/edu/esprit/img/imageblanche.png");
                Image defaultImage = new Image(defaultImageUrl.toString());
                imgView_actualiteModifier.setImage(defaultImage);
            }
        }
    }


    public void setServiceActualite(ServiceActualite serviceActualite) {
        this.serviceActualite = serviceActualite;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser la taille du SecondBorderPane avec la même largeur que la barre latérale
        double sidebarWidth = MainLeftSidebar.getWidth();
        SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() + sidebarWidth);
    }
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






    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
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



    public void setMainAnchorPaneContent(AnchorPane ajouterAP) {
        MainAnchorPaneBaladity.getChildren().setAll(ajouterAP);
    }


    public void navigatetoAfficherActualiteModifierAction(ActionEvent actionEvent) {
        try {
            System.out.println("Resource URL: " + getClass().getResource("/ActualiteGui/AfficherActualiteGui.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/ActualiteGui/AfficherActualiteGui.fxml"));
            TFtitreModifier.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
}
