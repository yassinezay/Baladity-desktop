package edu.esprit.controllers.equipement;

import edu.esprit.controllers.user.Login;
import edu.esprit.entities.Avis;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Equipement;
import edu.esprit.services.ServiceAvis;
import edu.esprit.services.ServiceEquipement;
import edu.esprit.services.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.sql.Date;
import java.util.Objects;
import java.util.prefs.Preferences;

public class AjouterAvisGuiFront {
    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private Button ajouterButton;

    @FXML
    private TextArea commentaireeq;

    @FXML
    private DatePicker dateaviseq;

    @FXML
    private BorderPane firstborderpane;

    @FXML
    private Rating noteeq;

    @FXML
    private Button retourButton;
    @FXML
    private Label commentaireErrorLabel;

    @FXML
    void BTNGestionAct(ActionEvent event) {

    }

    @FXML
    void BTNGestionEquipement(ActionEvent event) {

    }

    @FXML
    void BTNGestionEvennement(ActionEvent event) {

    }

    @FXML
    void BTNGestionRec(ActionEvent event) {

    }

    @FXML
    void BTNGestionTache(ActionEvent event) {

    }

    private final ServiceAvis serviceAvis = new ServiceAvis();
    private static final String USER_PREF_KEY = "current_user";

    ServiceUser serviceUser = new ServiceUser();
    int userId  = Integer.parseInt(getCurrentUser());
    //  int userId = 48;
    EndUser user = serviceUser.getOneByID(userId);
    java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
    @FXML
    void ajouterAvisAction(ActionEvent event) {
        String commentaire_avis = commentaireeq.getText().trim();
        String[] mots = commentaire_avis.split("\\s+");

        if (mots.length > 50) {
            commentaireErrorLabel.setText("Le commentaire ne doit pas dépasser 50 mots.");
            commentaireErrorLabel.setVisible(true);
            return; // Arrête la méthode si le commentaire dépasse 50 mots
        } else {
            commentaireErrorLabel.setVisible(false);
        }
        try {
            // Récupérer les valeurs saisies par l'utilisateur
            int note_avis = (int) noteeq.getRating();

            Date date_avis = Date.valueOf(dateaviseq.getValue()); // Convertissez la valeur du DatePicker en objet Date
            // Créer un nouvel objet Avis avec ces valeurs
            Avis nouvelAvis = new Avis(equipement,user, user.getMuni(), note_avis, commentaireeq.getText(), date_avis);

            // Appeler la méthode de service appropriée pour ajouter cet avis
            serviceAvis.ajouter(nouvelAvis);

            // Afficher un message d'alerte en cas de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Avis ajouté avec succès !");
            alert.show();
        } catch (NumberFormatException e) {
            // Afficher un message d'alerte si la note saisie n'est pas un nombre valide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("La note doit être un nombre valide !");
            alert.showAndWait();
        }

    }

    @FXML
    void retourAvisAction(ActionEvent event) {
        try {
            System.out.println("Resource URL: " + getClass().getResource("/equipementGui/AfficherEquipementGuiFront.fxml"));
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/equipementGui/AfficherEquipementGuiFront.fxml")));
            retourButton.getScene().setRoot(root);
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
     ServiceEquipement serviceEquipement= new ServiceEquipement();
    Equipement equipement;
    public void setServiceEquipement(ServiceEquipement serviceEquipement) {
        this.serviceEquipement = serviceEquipement;

    }

    public void setData(Equipement equipement) {
        this.equipement = equipement;
        System.out.println(equipement);
    }
}
