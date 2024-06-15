package edu.esprit.controllers.Actualite;

import com.itextpdf.text.*;
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

import com.itextpdf.text.pdf.PdfWriter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;


import javafx.scene.control.Alert;



public class PayerPubliciteController implements Initializable {

    private AjouterPubliciteController ajouterPubliciteController;
    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private VBox MainLeftSidebar;

    @FXML
    private Button PayerButtonAction;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private Button exportPDFAction;

    @FXML
    private BorderPane firstborderpane;

    @FXML
    private TextField nompaiement;

    @FXML
    private TextField numCarte;

    @FXML
    private ComboBox<String> offrePubCombo1;

    @FXML
    private TextField prenompaiement;

    @FXML
    private Button toAdd;

    private boolean isSidebarVisible = true;
    private final ServicePublicite sp = new ServicePublicite();
    private String imagePath;

    private static final String USER_PREF_KEY = "current_user";

    ServiceUser serviceUser = new ServiceUser();

    EndUser endUser = new EndUser();

    @FXML
    private Button buttonMain1;
    private String selectedOffer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // Allow only digits and limit to 16 characters
            if (newText.matches("\\d{0,16}")) {
                return change;
            }

            return null;
        });
        numCarte.setTextFormatter(formatter);

// Add an event handler to check for exactly 16 digits
        numCarte.setOnKeyTyped(event -> {
            // Allow backspace and delete keys
            if (!Character.isDigit(event.getCharacter().charAt(0)) && !event.getCharacter().matches("[\b\t]")) {
                event.consume();
            }

            // Check if the length exceeds 16 characters
            if (numCarte.getText().length() >= 16) {
                event.consume();
            }
        });

        ajouterPubliciteController = new AjouterPubliciteController();
        ajouterPubliciteController.setPayerPubliciteController(this);
        offrePubCombo1.getItems().addAll("3 mois :50dt", "6 mois :90dt", "9 mois :130dt");
        // Initialiser la taille du SecondBorderPane avec la même largeur que la barre latérale
        double sidebarWidth = MainLeftSidebar.getWidth();
        SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() + sidebarWidth);

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


    @FXML
    public void PayerButtonAction(ActionEvent actionEvent) throws IOException {
        String contactText = numCarte.getText();

        // Check if numCarte has exactly 16 digits
        if (contactText.length() != 16 || !contactText.matches("\\d{16}")) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer exactement 16 chiffres dans le numéro de carte.");
            return;
        }

        this.selectedOffer = offrePubCombo1.getValue();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ActualiteGui/AjouterPubliciteGui.fxml"));
        Parent root = loader.load();

// Access the AjouterPubliciteController instance
        AjouterPubliciteController ajouterPubliciteController = loader.getController();

        // Pass the selected offer to AjouterPubliciteController
        ajouterPubliciteController.setSelectedOffer(this.selectedOffer);
        this.imagePath = ajouterPubliciteController.getImagePath();
        Image image = ajouterPubliciteController.getImage();
        // Make sure a value is selected in offrePubCombo
        if (ajouterPubliciteController.offrePubCombo.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Sélection manquante", "Veuillez sélectionner une offre.");
            return;
        }

        String selectedOffer = ajouterPubliciteController.offrePubCombo.getValue();
        double amount = ajouterPubliciteController.getAmountFromOffer(selectedOffer);
        boolean paymentSuccessful = ajouterPubliciteController.processPayment(amount);

        if (paymentSuccessful) {
            // Retrieve information from the FXML fields directly in this controller
            //String contactText = numCarte.getText().trim();


            try {
                // Debugging: Print values before attempting to add to the database
                System.out.println("Titre: " + nompaiement.getText());
                System.out.println("Description: " + nompaiement.getText());
                System.out.println("Contact: " + contactText);
                System.out.println("Localisation: " + nompaiement.getText());
                System.out.println("Image Path: " + imagePath);
                System.out.println("Selected Offer: " + selectedOffer);
                int userId = Integer.parseInt(getCurrentUser());
                endUser = serviceUser.getOneByID(userId);

                // Attempt to add the publicite to the database
                sp.ajouter(new Publicite(
                        nompaiement.getText(),
                        nompaiement.getText(),
                        contactText,
                        nompaiement.getText(),
                        imagePath,
                        selectedOffer,
                        endUser,
                        new Actualite(102, endUser)));

                // Additional logic for handling the contactText
                if (!contactText.isEmpty()) {
                    try {
                        int contactValue = Integer.parseInt(contactText);
                        // Now you can use contactValue without the risk of a NumberFormatException
                    } catch (NumberFormatException e) {
                        // Handle the case where the text is not a valid integer

                    }
                } else {
                    // Handle the case where the text is empty
                    showAlert(Alert.AlertType.ERROR, "Error", "Contact field cannot be empty.");
                }

                // Additional logic...

                showAlert(Alert.AlertType.INFORMATION, "Paiement ", "Paiement effectué avec succès , merci ");

                // If you're using transactions, commit the transaction here

            } catch (Exception e) {
                // Log or print the exception details
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while adding the publicite to the database.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur de paiement", "Le paiement a échoué. Veuillez réessayer.");
        }
    }

    private double getAmountFromOffer(String offer) {
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

    private boolean processPayment(double amount) {
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

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }


    public void BTNGestionEvennement(ActionEvent actionEvent) {
    }


    public void buttonMain1(ActionEvent actionEvent) {
    }

    @FXML
    public void toAdd(ActionEvent actionEvent) {
        try {
            System.out.println("Resource URL: " + getClass().getResource("/ActualiteGui/AjouterPubliciteGui.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/ActualiteGui/AjouterPubliciteGui.fxml"));
            numCarte.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }

    @FXML
    public void exportPDFAction(ActionEvent actionEvent) {
        String nom = nompaiement.getText();
        String prenom = prenompaiement.getText();
        String numCarteText = numCarte.getText();
        int numCarteInt = 0;

        try {
            numCarteInt = Integer.parseInt(numCarteText);
        } catch (NumberFormatException e) {
        //    showAlert(Alert.AlertType.WARNING, "Avertissement", "Le numéro de carte n'est pas un entier valide.");
        }

        String selectedOffer = offrePubCombo1.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || numCarteText.isEmpty() || selectedOffer == null) {
            showAlert(Alert.AlertType.ERROR, "Données manquantes", "Veuillez remplir tous les champs avant d'exporter en PDF.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir l'emplacement pour sauvegarder le PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        // Show save file dialog
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            Document document = new Document();
            PdfWriter writer = null;

            try {
                writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // Add logo
                Image logoImage = new Image(getClass().getResource("/ActualiteGui/assets/BaladityTrans.png").toExternalForm());
                com.itextpdf.text.Image itextLogoImage = com.itextpdf.text.Image.getInstance(logoImage.getUrl());
                itextLogoImage.setAlignment(com.itextpdf.text.Image.ALIGN_LEFT);
                itextLogoImage.scaleAbsolute(100, 100);
                document.add(itextLogoImage);

                // Add title
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
                Paragraph title = new Paragraph("Vérification de Paiement", titleFont);
                title.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(title);

                // Add space
                document.add(Chunk.NEWLINE);

                // Add payment information with styles
                Font boldFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 12);
                Font contentFont = FontFactory.getFont(FontFactory.COURIER, 12);

                Paragraph paymentInfo = new Paragraph();
                paymentInfo.add(new Chunk("Nom: ", boldFont));
                paymentInfo.add(new Chunk(nom, contentFont));
                paymentInfo.add(Chunk.NEWLINE);
                paymentInfo.add(new Chunk("Prénom: ", boldFont));
                paymentInfo.add(new Chunk(prenom, contentFont));
                paymentInfo.add(Chunk.NEWLINE);
                paymentInfo.add(new Chunk("Numéro de carte: ", boldFont));
                paymentInfo.add(new Chunk(numCarteText, contentFont));
                paymentInfo.add(Chunk.NEWLINE);
                paymentInfo.add(new Chunk("Offre sélectionnée: ", boldFont));
                paymentInfo.add(new Chunk(selectedOffer, contentFont));
                paymentInfo.add(Chunk.NEWLINE);

                document.add(paymentInfo);

                // Add payment received and verification message
                Paragraph verificationInfo = new Paragraph();
                verificationInfo.add(new Chunk("Paiement reçu avec succès.\n", contentFont));
                verificationInfo.add(new Chunk("La vérification de paiement est confirmée.\n\n", contentFont));
                document.add(verificationInfo);

                showAlert(Alert.AlertType.INFORMATION, "Export PDF", "Les données ont été exportées avec succès en PDF.");

            } catch (DocumentException | IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur d'exportation PDF", "Une erreur s'est produite lors de l'exportation en PDF.");
            } finally {
                if (document != null) {
                    document.close();
                }
            }
        }
    }

    private String getCurrentUser() {
        Preferences preferences = Preferences.userNodeForPackage(Login.class);
        return preferences.get(USER_PREF_KEY, "DefaultUser");
    }

}




