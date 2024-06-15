package edu.esprit.controllers.equipement;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import edu.esprit.entities.Equipement;
import edu.esprit.services.ServiceEquipement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class EquipementItemGuiFront {
    @FXML
    private Button avisFButton;

    @FXML
    private Text categorieitemTFF;

    @FXML
    private Text dateajoutitemTFF;

    @FXML
    private Text descriptionitemTAF;

    @FXML
    private ImageView imageViewaffiche;

    @FXML
    private Text nomitemTFF;

    @FXML
    private Text quantiteitemTFF;

    @FXML
    private Text referenceitemTFF;

    @FXML
    private Button useButton;
    @FXML
    private Button rendreButton;
    private int quantiteInitiale;

    @FXML
    void utiliserEquipementAction(ActionEvent event) {
        // Vérifier si la quantité d'équipement est supérieure à zéro
        if (equipement.getQuantite_eq() > 0) {
            int nouvelleQuantite = equipement.getQuantite_eq() - 1;
            equipement.setQuantite_eq(nouvelleQuantite);
            serviceEquipement.modifier(equipement);
            quantiteitemTFF.setText(String.valueOf(nouvelleQuantite));

            if (nouvelleQuantite == 0) {
                envoyerSmsAdmin();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Stock en rupture pour l'équipement : " + equipement.getNom_eq());
                alert.setTitle("Stock épuisé");
                alert.show();
            }

            // Désactiver le bouton si la quantité est nulle
            useButton.setDisable(nouvelleQuantite == 0);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Stock déjà épuisé pour l'équipement : " + equipement.getNom_eq());
            alert.setTitle("Stock épuisé");
            alert.show();
        }
    }

    // Votre identifiant Twilio
    private static final String ACCOUNT_SID = "ACc889cd2b52f6a09ca714c967c8c33cd1";
    // Votre token d'authentification Twilio
    private static final String AUTH_TOKEN = "03899d558cdf5574b4566b0f644c7781";
    // Votre numéro Twilio (numéro de téléphone Twilio)
    private static final String TWILIO_NUMBER = "+19284400733";

    // Méthode pour envoyer un SMS à l'administrateur
    public void envoyerSmsAdmin() {
        try {
            // Initialiser la connexion à l'API Twilio avec votre identifiant et token d'authentification
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            // Numéro de téléphone de l'administrateur (à remplacer par le numéro de téléphone réel)
            String adminPhoneNumber = "+21655907840";

            // Message à envoyer à l'administrateur
            String messageBody = "Stock en rupture pour l'équipement : " + equipement.getReference_eq();

            // Envoyer le SMS à l'administrateur
            Message message = Message.creator(
                            new PhoneNumber(adminPhoneNumber), // Numéro de téléphone de l'administrateur
                            new PhoneNumber(TWILIO_NUMBER), // Votre numéro Twilio
                            messageBody)
                    .create();

            // Afficher le SID du message Twilio envoyé (facultatif)
            System.out.println("Message SID: " + message.getSid());
        } catch (ApiException e) {
            e.printStackTrace();
            // Gérer l'exception si l'API Twilio renvoie une erreur
            // Afficher un message d'erreur ou effectuer une action appropriée
        }
    }
    @FXML
    void rendreEquipementAction(ActionEvent event) {
        // Incrémenter la quantité d'équipement disponible
        int nouvelleQuantite = equipement.getQuantite_eq() + 1;

        // Vérifier si la quantité rendue ne dépasse pas la quantité initiale des équipements
        if (nouvelleQuantite <= quantiteInitiale) {
            // Mettre à jour la quantité dans la base de données
            equipement.setQuantite_eq(nouvelleQuantite);
            serviceEquipement.modifier(equipement);

            // Mettre à jour l'affichage de la quantité
            quantiteitemTFF.setText(String.valueOf(nouvelleQuantite));

            // Activer le bouton 'useButton'
            useButton.setDisable(false);

            // Afficher une notification à l'administrateur
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("L'équipement " + equipement.getReference_eq() + " a été rendu avec succès.");
            alert.setTitle("Équipement rendu");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("La quantité rendue dépasse la quantité initiale des équipements.");
            alert.setTitle("Quantité rendue excessive");
            alert.show();
        }
    }
    private Equipement equipement;
    ServiceEquipement serviceEquipement = new ServiceEquipement();
    public void setData(Equipement equipement) {
        this.equipement = equipement;
        nomitemTFF.setText(equipement.getNom_eq());
        referenceitemTFF.setText(equipement.getReference_eq());
        quantiteitemTFF.setText(String.valueOf(equipement.getQuantite_eq()));
        categorieitemTFF.setText(equipement.getCategorie_eq());
        descriptionitemTAF.setText(equipement.getDescription_eq());
        dateajoutitemTFF.setText(String.valueOf(equipement.getDate_ajouteq()));
        String imageUrl = equipement.getImage_eq();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                // Construct the complete file path
                String basePath = "C:\\Users\\amine\\Desktop\\PiDev\\DevMasters-Baladity\\public\\uploads\\";
                String filePath = basePath + imageUrl;
                // Create an instance of File using the complete file path
                File file = new File(filePath);
                // Check if the file exists
                if (file.exists()) {
                    // Create an Image instance from the file path
                    Image image = new Image(file.toURI().toString());
                    // Set the image in the ImageView
                    imageViewaffiche.setImage(image);
                } else {
                    System.err.println("File not found: " + file.getPath());
                }
            } catch (Exception e) {
                // Handle any exception
                e.printStackTrace();
            }
        }
        quantiteInitiale = equipement.getQuantite_eq();
    }


    @FXML
    void avisFEquipementAction(ActionEvent event) {
        if (equipement != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/equipementGui/AfficherAvisGuiFront.fxml"));
                Parent root = loader.load();
                AfficherAvisGuiFront controller = loader.getController();
                controller.setServiceEquipement(serviceEquipement);
                controller.setData(equipement); // Set the data before loading the controller
                nomitemTFF.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Sorry");
                alert.setTitle("Error");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("L'objet Equipement passé en argument est null.");
            alert.setTitle("Error");
            alert.show();
        }
    }
}

