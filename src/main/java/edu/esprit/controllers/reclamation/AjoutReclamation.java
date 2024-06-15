package edu.esprit.controllers.reclamation;

import edu.esprit.controllers.user.Login;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Reclamation;
import edu.esprit.services.ServiceReclamation;
import edu.esprit.services.ServiceUser;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.sql.SQLException;
import java.util.*;
import java.util.prefs.Preferences;

import edu.esprit.services.HateSpeechChecker;

public class AjoutReclamation implements Initializable {
    private static final String USER_PREF_KEY = "current_user";

    ServiceUser serviceUser = new ServiceUser();

    int userId  = Integer.parseInt(getCurrentUser());

    EndUser user = serviceUser.getOneByID(userId);
    @FXML
    private TextArea TAdescription_reclamation;

    @FXML
    private ComboBox<String> typeReclamationComboBox;

    @FXML
    private String value1;

    @FXML
    private TextField TFsujet_reclamation;

    @FXML
    private TextField TFadresse_reclamation;

    @FXML
    private ImageView imgView_reclamation;

    @FXML
    private Button uploadbutton;
    @FXML
    private Label label;
    @FXML
    private Label checkadressereclamation;

    @FXML
    private Label checkdescriptionreclamation;

    @FXML
    private Label checksujetreclamation;

    @FXML
    private Label checktypereclamation;
    @FXML
    private Label sujetexist;

    @FXML
    private Label descriptionexist;

    private final ServiceReclamation sr = new ServiceReclamation();
    java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
    private String imagePath;
    private boolean type_reclamation;

    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private VBox MainLeftSidebar;

    @FXML
    private BorderPane SecondBorderPane;
    private boolean isSidebarVisible = true;

    public boolean getType_reclamation() {
        return type_reclamation;
    }
    private HateSpeechChecker hateSpeechChecker;

    public AjoutReclamation() {
        hateSpeechChecker = new HateSpeechChecker(); // Assurez-vous que vous initialisez correctement l'objet ici
    }

    public void setTypesReclamation(boolean isUrgent) {
        ObservableList<String> typesReclamation = FXCollections.observableArrayList();
        if (isUrgent) {
            // Ajouter des éléments à la liste pour les cas urgents
            typesReclamation.addAll("Urgences médicales", "Incendies", "Fuites de gaz", "Inondations", "Sécurité publique", "Défaillances des infrastructures critiques");
        } else {
            // Ajouter des éléments à la liste pour les cas non urgents
            typesReclamation.addAll("Réparations de voirie", "Collecte des déchets", "Environnement", "Aménagement paysager", "Problèmes de logement", "Services municipaux");
        }
        // Définir les éléments du ComboBox en utilisant la liste observable
        typeReclamationComboBox.setItems(typesReclamation);
    }

    @FXML
    void uploadimg(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Image", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                new FileChooser.ExtensionFilter("All image files", "*.jpg", "*.png")
        );
        Stage stage = (Stage) uploadbutton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // Store just the file name with its extension
            imagePath = selectedFile.getName();

            // Display the file name
            label.setText(imagePath);

            // Load and display the image
            Image image = new Image(selectedFile.toURI().toString());
            imgView_reclamation.setImage(image);
        }
    }

    @FXML
    void ajouterReclamationAction(ActionEvent event) {
        boolean sujetValid = validateTextField(TFsujet_reclamation, checksujetreclamation);
        boolean descriptionValid = validateTextArea(TAdescription_reclamation, checkdescriptionreclamation);
        boolean typeValid = validateComboBox(typeReclamationComboBox, checktypereclamation);

        if (sujetValid && descriptionValid && typeValid) {
            boolean hasBadWords = checkForBadWords(TFsujet_reclamation.getText(), TAdescription_reclamation.getText());
            if (hasBadWords) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Contenu inapproprié");
                alert.setContentText("Votre réclamation contient des mots inappropriés. Veuillez modifier le contenu et réessayer.");
                alert.showAndWait();
            } else {
                if (sr.reclamationExists(TFsujet_reclamation.getText(), TAdescription_reclamation.getText())) {
                    sujetexist.setVisible(true);
                    descriptionexist.setVisible(true);
                    return;
                } else {
                    sujetexist.setVisible(false);
                    descriptionexist.setVisible(false);

                    // Ajouter la réclamation avec le nom de l'image
                    // Assurez-vous de définir l'imagePath pour enregistrer le chemin absolu de l'image dans la base de données
                    sr.ajouter(new Reclamation(user, user.getMuni(), TFsujet_reclamation.getText(), sqlDate, typeReclamationComboBox.getValue(), TAdescription_reclamation.getText(), imagePath, TFadresse_reclamation.getText()));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Reclamation a été ajoutée");
                    alert.setContentText("GG");
                    alert.show();
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/ReclamationGui.fxml"));
                        MainAnchorPaneBaladity.getScene().setRoot(root);
                    } catch (IOException e) {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setContentText("Une erreur s'est produite lors de la redirection.");
                        errorAlert.setTitle("Erreur de redirection");
                        errorAlert.show();
                    }
                }
            }
        }
    }

    @FXML
    void cancelReclamationAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/ReclamationTypeGui.fxml"));
            TFsujet_reclamation.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Ajoutez un ChangeListener pour le champ TFsujet_reclamation
        TFsujet_reclamation.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Votre logique ici pour limiter les caractères et autoriser uniquement les lettres
                // Par exemple :
                if (!newValue.matches("^[a-zA-Z]*$")) {
                    TFsujet_reclamation.setText(newValue.replaceAll("[^a-zA-Z]", ""));
                }
                // Limiter la longueur du champ à un certain nombre de caractères
                if (newValue.length() > 50) { // Par exemple, limitez à 50 caractères
                    TFsujet_reclamation.setText(newValue.substring(0, 50));
                }
            }
        });

        // Ajoutez un ChangeListener pour le champ TAdescription_reclamation
        TAdescription_reclamation.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("^[a-zA-Z]*$")) {
                    TAdescription_reclamation.setText(newValue.replaceAll("[^a-zA-Z]", ""));
                }
                // Par exemple :
                // Limiter la longueur du champ à un certain nombre de caractères
                if (newValue.length() > 200) { // Par exemple, limitez à 200 caractères
                    TAdescription_reclamation.setText(newValue.substring(0, 200));
                }
            }
        });


    }

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

    @FXML
    void BTNGestionUser(ActionEvent event) {

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
    private boolean validateComboBox(ComboBox<String> comboBox, Label label) {
        // Vérifie si aucune option n'est sélectionnée dans le ComboBox
        if (comboBox.getValue() == null || comboBox.getValue().isEmpty()) {
            label.setText("Veuillez sélectionner une option");
            label.getStyleClass().add("warning-text");
            return false;
        }

            label.setText("Valide");
            label.getStyleClass().removeAll("warning-text");
            label.getStyleClass().add("success-text");
            return true;

    }

    private boolean validateTextField(TextField textField, Label label) {
        // Vérifie si le champ de texte est vide
        if (textField.getText().isEmpty()) {
            label.setText("Veuillez remplir ce champ");
            label.getStyleClass().add("warning-text");
            return false;
        }

        // Vérifie les mots inappropriés uniquement si le champ est non vide
        if (hateSpeechChecker.containsBadWord(textField.getText())) {
            label.setText("Votre champ contient des mots inappropriés. Veuillez modifier le contenu et réessayer.");
            label.getStyleClass().add("warning-text");
            return false;
        } else {
            label.setText("Valide");
            label.getStyleClass().removeAll("warning-text");
            label.getStyleClass().add("success-text");
            return true;
        }
    }

    // Méthode pour valider le champ de texte et mettre à jour le label associé
    private boolean validateTextArea(TextArea textArea, Label label) {
        // Vérifie si la zone de texte est vide
        if (textArea.getText().isEmpty()) {
            label.setText("Veuillez remplir ce champ");
            label.getStyleClass().add("warning-text");
            return false;
        }

        if (hateSpeechChecker.containsBadWord(textArea.getText())) {
            label.setText("Votre champ contient des mots inappropriés. Veuillez modifier le contenu et réessayer.");
            label.getStyleClass().add("warning-text");
            return false;
        } else {
            label.setText("Valide");
            label.getStyleClass().removeAll("warning-text");
            label.getStyleClass().add("success-text");
            return true;
        }
    }

    private boolean checkForBadWords(String sujet, String description) {
        // Vérifier le sujet de la réclamation
        if (hateSpeechChecker.containsBadWord(sujet)) {
            return true;
        }

        // Vérifier la description de la réclamation
        if (hateSpeechChecker.containsBadWord(description)) {
            return true;
        }

        return false;
    }
    public boolean bad_words(String text) {
        List<String> badListW = Arrays.asList("fuck", "din", "khra", "bhim", "hayawen", "kaleb", "putain");
        for (String str : badListW) {
            if (text.contains(str)) {
                return true;
            }
        }
        return false;
    }
    private void openNewScene(String latitude, String longitude, AjoutReclamation controller) {
        try {
            if (latitude != null && longitude != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamationGui/Map.fxml"));
                Parent root = loader.load();
                Maps mapController = loader.getController();
                mapController.loadGoogleMaps(latitude, longitude);

                // Mettre à jour le champ d'adresse dans le contrôleur AjoutReclamation
                controller.setAddressFields(latitude, longitude);

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } else {
                // Gérer le cas où les coordonnées sont nulles
                System.err.println("Les coordonnées de latitude ou de longitude sont nulles.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onMapsClicked(ActionEvent event) {
        String latitude = "36.8065"; // Latitude de San Francisco
        String longitude = "10.1815"; // Longitude de San Francisco
        // Ouvrir une nouvelle scène pour Google Maps avec les coordonnées fournies
        openNewScene(latitude, longitude, this);
    }
    public void setAddressFields(String latitude, String longitude) {
        TFadresse_reclamation.setText("Latitude: " + latitude + ", Longitude: " + longitude);
    }
    private String getCurrentUser() {
        Preferences preferences = Preferences.userNodeForPackage(Login.class);
        return preferences.get(USER_PREF_KEY, "DefaultUser");
    }
}
