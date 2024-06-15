package edu.esprit.controllers.Actualite;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import edu.esprit.controllers.user.Login;
import edu.esprit.entities.Actualite;
import edu.esprit.entities.EndUser;

import edu.esprit.entities.Publicite;
import edu.esprit.services.ServicePublicite;
import edu.esprit.services.ServiceUser;
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
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class AjouterPubliciteController implements Initializable {
    private PayerPubliciteController payerPubliciteController;

    public void setPayerPubliciteController(PayerPubliciteController controller) {
        this.payerPubliciteController = controller;
    }

    @FXML
    private AnchorPane MainAnchorPaneBaladity;
    @FXML
    private Label numeroexiste;
    @FXML
    private VBox MainLeftSidebar;
    @FXML
    private Label localisationAlerte;
    @FXML
    private Label descriptionAlerte;
    @FXML
    private Label titreAlerte;
    @FXML
    private BorderPane SecondBorderPane;

    public String getImagePath() {
        return imagePath;
    }

    public Image getImage() {
        return imgView_pub.getImage();
    }

    @FXML
    TextField TFcontactpub;

    @FXML
    TextField TFdescriptionpub;

    @FXML
    TextField TFlocalisationpub;

    @FXML
    TextField TFtitrepub;

    @FXML
    private Button ajouterPubliciteAction;

    @FXML
    private BorderPane firstborderpane;

    @FXML
    private ImageView imgView_actualite;

    @FXML
    private ImageView imgView_pub;

    @FXML
    private Label labelPub;
    @FXML
    private Label contactAlerte;

    @FXML
    ComboBox<String> offrePubCombo;

    @FXML
    private Button tolistActualite;

    @FXML
    private Button uploadbuttonP;

    private boolean isSidebarVisible = true;
    final ServicePublicite sp = new ServicePublicite();
    String imagePath;

    java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
    private static final String USER_PREF_KEY = "current_user";

    ServiceUser serviceUser = new ServiceUser();
    int userId  = Integer.parseInt(getCurrentUser());
  //  int userId = 48;
    EndUser user = serviceUser.getOneByID(userId);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        offrePubCombo.getItems().addAll("3 mois :50dt", "6 mois :90dt", "9 mois :130dt");
        // Initialiser la taille du SecondBorderPane avec la même largeur que la barre latérale
        double sidebarWidth = MainLeftSidebar.getWidth();
        SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() + sidebarWidth);

        // Add a listener to the text property of TFtitrepub
        TFtitrepub.textProperty().addListener((observable, oldValue, newValue) -> {
            // Validate the length of the text
            if (newValue.length() < 6) {
                // If length is less than 6, show the alert
                titreAlerte.setVisible(true);
                TFtitrepub.requestFocus();
            } else {
                // If length is 6 or more, hide the alert
                titreAlerte.setVisible(false);
            }
        });

        // Add a listener to the text property of TFdescriptionpub
        TFdescriptionpub.textProperty().addListener((observable, oldValue, newValue) -> {
            // Validate the length of the text
            if (newValue.length() < 15) {
                // If length is less than 15, show the alert
                descriptionAlerte.setVisible(true);
                TFdescriptionpub.requestFocus();
            } else {
                // If length is 15 or more, hide the alert
                descriptionAlerte.setVisible(false);
            }
        });

        // Add a listener to the text property of TFlocalisationpub
        TFlocalisationpub.textProperty().addListener((observable, oldValue, newValue) -> {
            // Validate the length of the text
            if (newValue.length() < 3) {
                // If length is less than 3, show the alert
                localisationAlerte.setVisible(true);
                TFlocalisationpub.requestFocus();
            } else {
                // If length is 3 or more, hide the alert
                localisationAlerte.setVisible(false);
            }

        });

        TFcontactpub.textProperty().addListener((observable, oldValue, newValue) -> {
            // Allow only numeric input
            if (!newValue.matches("\\d*")) {
                // If non-numeric characters are entered, remove them
                TFcontactpub.setText(newValue.replaceAll("[^\\d]", ""));
            }

            // Limit the length to 8 characters
            if (newValue.length() > 8) {
                TFcontactpub.setText(newValue.substring(0, 8));
            }
        });

    }


    private boolean validateContact(String contactValue) {
        try {
            int contact = Integer.parseInt(contactValue);

            // Check if the contact number already exists in the database
            if (sp.numeroExists(contact)) {
                // Display the warning message
                numeroexiste.setVisible(true);
                return false; // Exit the method to prevent further processing
            } else {
                // Hide the warning message if it was previously shown
                numeroexiste.setVisible(false);
            }

            if (contact <= 0 || contactValue.length() != 8) {
                showAlert(Alert.AlertType.ERROR, "Error", "Veuillez saisir un numéro de contact valide (8 chiffres).");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Veuillez saisir un numéro de contact valide (8 chiffres).");
            return false;
        }
    }


    void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void uploadimgP(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\amine\\Desktop\\PiDev\\DevMasters-Baladity\\public\\uploads"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Image", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                new FileChooser.ExtensionFilter("All image files", "*.jpg", "*.png")
        );
        Stage stage = (Stage) uploadbuttonP.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // Store just the file name with its extension
            imagePath = selectedFile.getName();

            // Display the file name
            labelPub.setText(imagePath);

            // Load and display the image
            Image image = new Image(selectedFile.toURI().toString());
            imgView_pub.setImage(image);
        }
    }



    @FXML
    public void tolistActualite(ActionEvent actionEvent) {
        try {
            System.out.println("Resource URL: " + getClass().getResource("/ActualiteGui/AfficherActualiteCitoyenGui.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/ActualiteGui/AfficherActualiteCitoyenGui.fxml"));
            TFtitrepub.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
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

    public void setSelectedOffer(String selectedOffer) {
        offrePubCombo.setValue(selectedOffer);
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

    public void setTextFieldValues(String titre, String description, String contact, String localisation, String imagePath) {
        TFtitrepub.setText(titre);
        TFdescriptionpub.setText(description);
        TFcontactpub.setText(contact);
        TFlocalisationpub.setText(localisation);
        this.imagePath = imagePath;

        // Load and display the image (assuming imgView_pub is the ImageView for the image)
        Image image = new Image("file:" + imagePath);
        imgView_pub.setImage(image);
    }

    @FXML
    public void ajouterPubliciteAction(ActionEvent actionEvent) {
        Actualite actualite = new Actualite(146, user);
        String selectedOffer = offrePubCombo.getValue();

        // No payment processing check here

        sp.ajouter(new Publicite(
                TFtitrepub.getText(),
                TFdescriptionpub.getText(),
                Integer.parseInt(TFcontactpub.getText()),
                TFlocalisationpub.getText(),
                imagePath, // Use the file name with its extension
                selectedOffer,
                user,
                actualite
        ));

        showAlert(Alert.AlertType.INFORMATION, "procéder au paiement ", "procéder au paiement ");
        try {
            System.out.println("Resource URL: " + getClass().getResource("/ActualiteGui/PayerPubliciteGui.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/ActualiteGui/PayerPubliciteGui.fxml"));
            TFtitrepub.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }



    double getAmountFromOffer(String offer) {
        // Implement the logic to get the amount based on the selected offer
        // For now, let's assume a simple mapping for demonstration purposes
        switch (offer) {
            case "3 mois :50dt":
                return 16.0;
            case "6 mois :90dt":
                return 28.80;
            case "9 mois :130dt":
                return 41.60;
            default:
                return 0.0;
        }
    }

    boolean processPayment(double amount) {
        try {
            Stripe.apiKey = "sk_test_51OpeMeI3VcdValufdQQI5nr0PLI1jmQ9YCCa6Xu4ozS5Qv9IBoaTSvqMtzZXaZf0edfdRkNVVLixMKfo8CtYx3PW00MLcbGNSd";
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (amount * 100)) // Amount in cents
                    .setCurrency("usd")
                    .build();
            PaymentIntent intent = PaymentIntent.create(params);
            System.out.println("Payment successful. PaymentIntent ID: " + intent.getId());
            return true;
        } catch (StripeException e) {
            System.out.println("Payment failed. Error: " + e.getMessage());
            return false;
        }
    }


    public void buttonMain1(ActionEvent actionEvent) {
        try {
            System.out.println("Resource URL: " + getClass().getResource("/MainGui.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/MainGui.fxml"));
            TFtitrepub.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    public void paiement(ActionEvent actionEvent) {

    }

    public void TOpayment(ActionEvent actionEvent) {
        try {
            System.out.println("Resource URL: " + getClass().getResource("/ActualiteGui/PayerPubliciteGui.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/ActualiteGui/PayerPubliciteGui.fxml"));
            TFtitrepub.getScene().setRoot(root);
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