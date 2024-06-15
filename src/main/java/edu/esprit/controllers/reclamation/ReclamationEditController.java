package edu.esprit.controllers.reclamation;

import edu.esprit.entities.Reclamation;
import edu.esprit.services.ServiceReclamation;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReclamationEditController implements Initializable {

    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private VBox MainLeftSidebar;

    private boolean isSidebarVisible = true;

    @FXML
    private TextField TFmodifieradresse_reclamation;

    @FXML
    private TextField TFmodifiersujet_reclamation;

    @FXML
    private TextArea TmodifierAdescription_reclamation;

    @FXML
    private ImageView modifierimgView_reclamation;

    @FXML
    private Label label;

    @FXML
    private ComboBox<String> modifiertypeReclamationComboBox;

    @FXML
    private Button uploadimgmodifier;
    @FXML
    private Label sujetmodifier;

    @FXML
    private Label typemodifier;
    @FXML
    private Label adressemodifier;

    @FXML
    private Label descriptionmodifier;


    @FXML
    private Button uploadbutton_modifier;
    private ServiceReclamation serviceReclamation;
    private Reclamation reclamation;
    private String imagePath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser la taille du SecondBorderPane avec la même largeur que la barre latérale
        double sidebarWidth = MainLeftSidebar.getWidth();
        SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() + sidebarWidth);
        TFmodifiersujet_reclamation.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Votre logique ici pour limiter les caractères et autoriser uniquement les lettres
                // Par exemple :
                if (!newValue.matches("^[a-zA-Z]*$")) {
                    TFmodifiersujet_reclamation.setText(newValue.replaceAll("[^a-zA-Z]", ""));
                }
                // Limiter la longueur du champ à un certain nombre de caractères
                if (newValue.length() > 50) { // Par exemple, limitez à 50 caractères
                    TFmodifiersujet_reclamation.setText(newValue.substring(0, 50));
                }
            }
        });

        // Ajoutez un ChangeListener pour le champ TAdescription_reclamation
        TmodifierAdescription_reclamation.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("^[a-zA-Z]*$")) {
                    TmodifierAdescription_reclamation.setText(newValue.replaceAll("[^a-zA-Z]", ""));
                }
                // Par exemple :
                // Limiter la longueur du champ à un certain nombre de caractères
                if (newValue.length() > 200) { // Par exemple, limitez à 200 caractères
                    TmodifierAdescription_reclamation.setText(newValue.substring(0, 200));
                }
            }
        });
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

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setMainAnchorPaneContent(AnchorPane ajouterAP) {
        MainAnchorPaneBaladity.getChildren().setAll(ajouterAP);
    }
    @FXML
    void modifierReclamationAction(ActionEvent event) {
        boolean sujetValid = validateTextField(TFmodifiersujet_reclamation, sujetmodifier);
        boolean descriptionValid = validateTextArea(TmodifierAdescription_reclamation, descriptionmodifier);
        boolean adresseValid = validateTextField(TFmodifieradresse_reclamation, adressemodifier);
        boolean typeValid = validateComboBox(modifiertypeReclamationComboBox, typemodifier);

        // Vérifier si tous les champs sont valides
        if (sujetValid && descriptionValid && adresseValid && typeValid) {
        if (reclamation != null && serviceReclamation != null) {
            // Créer une boîte de dialogue de confirmation
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir modifier cette réclamation ?");
            confirmationAlert.setTitle("Confirmation de modification");

            // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            // Vérifier si l'utilisateur a cliqué sur le bouton OK
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Mettre à jour les données de la réclamation avec les valeurs des champs de texte
                reclamation.setSujet_reclamation(TFmodifiersujet_reclamation.getText());
                reclamation.setType_reclamation(modifiertypeReclamationComboBox.getValue());
                reclamation.setDescription_reclamation(TmodifierAdescription_reclamation.getText());
                reclamation.setAdresse_reclamation(TFmodifieradresse_reclamation.getText());
                reclamation.setImage_reclamation(imagePath); // imagePath peut être nul si aucune image n'est sélectionnée
                try {
                    // Appeler la méthode de modification du service de réclamation
                    serviceReclamation.modifier(reclamation);

                    // Afficher un message de succès
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setContentText("Réclamation modifiée avec succès !");
                    successAlert.setTitle("Modification réussie");
                    successAlert.show();

                    // Rediriger l'utilisateur vers la vue précédente (par exemple, la liste des réclamations)
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/AfficherReclamationGui.fxml"));
                        TFmodifiersujet_reclamation.getScene().setRoot(root);
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
                    errorAlert.setContentText("Erreur lors de la modification de la réclamation : " + e.getMessage());
                    errorAlert.setTitle("Erreur de modification");
                    errorAlert.show();
                }
            }
        } else {
            // Afficher un message d'erreur si la réclamation est null ou si le service de réclamation est null
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Impossible de modifier la réclamation car aucune réclamation n'est sélectionnée ou le service de réclamation est null.");
            errorAlert.setTitle("Erreur de modification");
            errorAlert.show();
        }
        }
    }



    @FXML
    void cancelModifierReclamationAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/AfficherReclamationGui.fxml"));
            TFmodifiersujet_reclamation.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    @FXML
    void uploadbutton_modifier(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Image", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                new FileChooser.ExtensionFilter("All image files", "*.jpg", "*.png")
        );
        Stage stage = (Stage) uploadimgmodifier.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // Store just the file name with its extension
            imagePath = selectedFile.getName();

            // Display the file name
            label.setText(imagePath);

            // Load and display the image
            Image image = new Image(selectedFile.toURI().toString());
            modifierimgView_reclamation.setImage(image);
        }
    }

    // Méthode pour initialiser les champs avec les données de la réclamation
    public void setData(Reclamation reclamation) {
        this.reclamation = reclamation;
        if (reclamation != null) {
            TFmodifiersujet_reclamation.setText(reclamation.getSujet_reclamation());
            modifiertypeReclamationComboBox.setValue(reclamation.getType_reclamation());
            TmodifierAdescription_reclamation.setText(reclamation.getDescription_reclamation());
            TFmodifieradresse_reclamation.setText(reclamation.getAdresse_reclamation());
            String imageUrl = reclamation.getImage_reclamation();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    // Create an instance of File using the file path
                    File file = new File("C:\\Users\\amine\\Desktop\\PiDev\\DevMasters-Baladity\\public\\uploads\\" + imageUrl);
                    // Get the file name
                    String fileName = file.getName();
                    // Update the Label with the file name
                    label.setText(fileName);

                    // Create an Image instance from the file path
                    Image image = new Image(file.toURI().toString());
                    // Set the image in the ImageView
                    modifierimgView_reclamation.setImage(image);
                } catch (Exception e) {
                    // Handle any exception
                    e.printStackTrace();
                }
            } else {
                // If the image URL is empty or null, display a default image
                // For example, if you have an image "default_image.png" in your resources folder
                // You can use getClass().getResource() to get its URL
                URL defaultImageUrl = getClass().getResource("/assets/default_image.png");
                if (defaultImageUrl != null) {
                    Image defaultImage = new Image(defaultImageUrl.toString());
                    modifierimgView_reclamation.setImage(defaultImage);
                } else {
                    System.err.println("Default image not found!");
                }
            }
        }
    }



    public void setServiceReclamation(ServiceReclamation serviceReclamation) {
        this.serviceReclamation = serviceReclamation;
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

    // Méthode pour valider le champ de texte et mettre à jour le label associé
    private boolean validateTextField(TextField textField, Label label) {
        // Vérifie si le champ de texte est vide
        if (textField.getText().isEmpty()) {
            label.setText("Veuillez remplir ce champ");
            label.getStyleClass().add("warning-text");
            return false;
        }

        // Vérifie les mots inappropriés uniquement si le champ est non vide
        if (bad_words(textField.getText())) {
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

        // Vérifie les mots inappropriés uniquement si la zone de texte est non vide
        if (bad_words(textArea.getText())) {
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

    private boolean checkForBadWords() {
        // Vérifier le sujet de la réclamation
        if (bad_words(sujetmodifier.getText())) {
            return true;
        }

        // Vérifier la description de la réclamation
        if (bad_words(descriptionmodifier.getText())) {
            return true;
        }

        // Vérifier l'adresse de la réclamation
        if (bad_words(adressemodifier.getText())) {
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

}
