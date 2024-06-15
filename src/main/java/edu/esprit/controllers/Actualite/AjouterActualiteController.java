package edu.esprit.controllers.Actualite;

import edu.esprit.controllers.user.Login;
import edu.esprit.entities.Actualite;
import edu.esprit.entities.EndUser;

import edu.esprit.services.ServiceActualite;
import edu.esprit.services.ServiceUser;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class AjouterActualiteController implements Initializable {

    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private VBox MainLeftSidebar;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private TextField TFdescription;

    @FXML
    private TextField TFtitre;

    @FXML
    private Button ajouterActualiteAction;

    @FXML
    private BorderPane firstborderpane;

    @FXML
    private ImageView imgView_actualite;

    @FXML
    private Label labelA;

    @FXML
    private Button uploadbuttonA;

    @FXML
    private Label verifierDescription;

    @FXML
    private Label verifierTitre;
    private boolean isSidebarVisible = true;
    private static final String USER_PREF_KEY = "current_user";

    ServiceUser serviceUser = new ServiceUser();

    int userId  = Integer.parseInt(getCurrentUser());

    EndUser user = serviceUser.getOneByID(userId);
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




    private final ServiceActualite sp = new ServiceActualite();
    java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());


    Actualite actualite = new Actualite(68,user);
    private String imagePath;


    public void initialize() {
        // Add a listener to the text property of TFtitre
        TFtitre.textProperty().addListener((observable, oldValue, newValue) -> {
            // Validate the length of the text
            if (newValue.length() > 6) {
                // If length is greater than 6, set the background color to a light color
                TFtitre.setStyle("-fx-background-color: #ffffff;"); // Light green
            } else {
                // If length is 6 or less, set the background color to a light red color
                TFtitre.setStyle("-fx-background-color: #e83939;"); // Light pink
            }
        });

        // Add a listener to the text property of TFdescription
        TFdescription.textProperty().addListener((observable, oldValue, newValue) -> {
            // Validate the length of the text
            if (newValue.length() < 15) {
                // If length is less than 15, set the background color to a light red color
                TFdescription.setStyle("-fx-background-color: #e83939;"); // Light pink
            } else {
                // If length is 15 or more, set the background color to a light color
                TFdescription.setStyle("-fx-background-color: #ffffff;"); // Light green
            }
        });
    }
    public void uploadimgA(ActionEvent actionEvent) {
        uploadbuttonA.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("C:\\Users\\amine\\Desktop\\PiDev\\DevMasters-Baladity\\public\\uploads"));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPEG Image", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                    new FileChooser.ExtensionFilter("All image files", "*.jpg", "*.png")
            );
            Stage stage = (Stage) uploadbuttonA.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                // Store just the file name with its extension
                imagePath = selectedFile.getName();

                // Display the file name
                labelA.setText(imagePath);

                // Load and display the image
                Image image = new Image(selectedFile.toURI().toString());
                imgView_actualite.setImage(image);
            }
        });
    }

    @FXML
    void ajouterActualiteAction() {
        String titre = TFtitre.getText();
        String description = TFdescription.getText();

        if (titre.length() > 6 && description.length() >= 15) {
            // Validate selected file
            if (imagePath != null && !imagePath.isEmpty()) {
                // Assuming sqlDate is defined elsewhere
                // Assuming user is defined elsewhere

                // Add the actualite with the image file name
                sp.ajouter(new Actualite(titre, description, sqlDate, imagePath, user));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("GG");
                alert.show();
            } else {
                // Handle invalid or missing image file
                showAlert(Alert.AlertType.ERROR, "Error", "Il faut ajouter une image, s'il vous plaît.");
            }
        } else {
            // Show validation message for titre and description with a red background
            if (titre.length() <= 6) {
                verifierTitre.setVisible(true);
                TFtitre.requestFocus();
                showAlert(Alert.AlertType.ERROR, "Error", "Le titre doit avoir plus de 6 caractères.");
            }
            if (description.length() < 15) {
                verifierDescription.setVisible(true);
                TFdescription.requestFocus();
                showAlert(Alert.AlertType.ERROR, "Error", "La description doit avoir au moins 15 caractères.");
            }
        }
    }

    private Stage stage ;
    public void setStage(Stage stage) {

        this.stage = stage;
    }
    // Other methods...

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }


    public void navigatetoAfficherActualiteAction(ActionEvent actionEvent) {
        try {
            System.out.println("Resource URL: " + getClass().getResource("/ActualiteGui/AfficherActualiteGui.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/ActualiteGui/AfficherActualiteGui.fxml"));
            TFtitre.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }


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


    public void buttonmain2(ActionEvent actionEvent) {
        try {
            System.out.println("Resource URL: " + getClass().getResource("/MainGui.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/MainGui.fxml"));
            TFtitre.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
    private String getCurrentUser() {
        Preferences preferences = Preferences.userNodeForPackage(Login.class);
        return preferences.get(USER_PREF_KEY, "DefaultUser");
    }
}
