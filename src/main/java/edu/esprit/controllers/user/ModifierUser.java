package edu.esprit.controllers.user;

import edu.esprit.entities.EndUser;
import edu.esprit.services.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifierUser implements Initializable {

    @FXML
    private ImageView ImageM;

    @FXML
    private ImageView banStatus;

    @FXML Button banUserButton;

    @FXML
    private TextField tfAdresse;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfNom;

//    @FXML
//    private PasswordField pfMdp;

    @FXML
    private TextField tfTel;

    @FXML
    private ComboBox<String> roleSelectionComboBox;

    File selectedFile;

    String role;

    boolean isBanned;

    private ServiceUser serviceUser = new ServiceUser();
    private EndUser endUser;

    public void setData(EndUser endUser) {
        this.isBanned = endUser.isBanned();
        if(isBanned) banStatus.setImage(new Image("/assets/check.png"));
        else banStatus.setImage(new Image("/assets/cross-sign.png"));
        this.endUser = endUser;
        // Afficher les données de l'événement dans les champs de texte
        tfNom.setText(endUser.getNom());
        tfEmail.setText(endUser.getEmail());
//        pfMdp.setText(endUser.getPassword());
        tfAdresse.setText(endUser.getLocation());
        tfTel.setText(endUser.getPhoneNumber());

        // Afficher l'image de l'événement
        String imagePath = endUser.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                ImageM.setImage(image);
            }
        }
    }

    @FXML
    void ModifierEventClick(ActionEvent event) {
        // Vérifier si le serviceEvenement est initialisé
//        if (serviceUser == null) {
//            serviceUser = new serviceUser();
//        }

        // Récupérer les nouvelles valeurs des champs de texte
        String nom = tfNom.getText();
        String email = tfEmail.getText();
//        String password = pfMdp.getText();
        String phone = tfTel.getText();
        String localisation = tfAdresse.getText();

        if (isAnyFieldEmpty()) {
            updateTextFieldState();
        } else {
            // Mettre à jour les données de l'événement
            endUser.setNom(nom);
            endUser.setEmail(email);
//            endUser.setPassword(password);
            endUser.setPhoneNumber(phone);
            endUser.setLocation(localisation);
            endUser.setType(role);

            if (nom.isEmpty() || email.isEmpty() || phone.isEmpty() || localisation.isEmpty() || roleSelectionComboBox.getValue() == null) {
                showAlert("Veuillez remplir tous les champs!");
            } else {
                if(selectedFile == null){
                    endUser.setImage(endUser.getImage());
                } else {
                    endUser.setImage(selectedFile.getAbsolutePath());
                }
                // Appeler le service pour mettre à jour le user dans la base de données
                serviceUser.modifier(endUser);

                // Afficher une alerte pour confirmer la modification
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("L'événement a été modifié avec succès.");
                alert.setTitle("Événement modifié");
                alert.show();
            }
        }
    }

    @FXML
    void banUser(ActionEvent event) {


        isBanned = !isBanned;

//        if(isBanned) {
//
//        }


        if(isBanned){
            serviceUser.modifierUserToBanned(endUser.getId(),false);
            banStatus.setImage(new Image("/assets/cross-sign.png"));
        } else {
            serviceUser.modifierUserToBanned(endUser.getId(),true);
            banStatus.setImage(new Image("/assets/check.png"));
        }
    }

    @FXML
    void navigateOnClick(ActionEvent event) {
        try {
            // Fermer la fenêtre actuelle
            Stage stage = (Stage) tfNom.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer les exceptions liées à la fermeture de la fenêtre
        }
    }

    @FXML
    void imagePicker(ActionEvent event) {
        // Configurer le FileChooser pour sélectionner une image
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            // Handle the selected image file (e.g., display it, process it, etc.)
            System.out.println("Selected image: " + selectedFile.getAbsolutePath());
            Image image = new Image(selectedFile.toURI().toString());
            ImageM.setImage(image);
        }
    }

    private void showAlert(String message) {
        // Affiche une boîte de dialogue d'information avec le message donné
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateTextFieldState() {
        setFieldState(tfNom, isFieldEmpty(tfNom));
        setFieldState(tfEmail, isFieldEmpty(tfEmail));
        setFieldState(tfAdresse, isFieldEmpty(tfAdresse));
        setFieldState(tfTel, isFieldEmpty(tfTel));
//        setFieldState(pfMdp, isFieldEmpty(pfMdp));
    }

    private boolean isFieldEmpty(TextField textField) {
        return textField.getText().isEmpty();
    }

    private void setFieldState(TextField textField, boolean isEmpty) {
        if (isEmpty) {
            textField.setStyle("-fx-border-color: red;");
        } else {
            textField.setStyle("-fx-border-color: lime;");
        }
    }

    private boolean isAnyFieldEmpty() {
        return isFieldEmpty(tfNom) || isFieldEmpty(tfEmail) || isFieldEmpty(tfAdresse) ||
                isFieldEmpty(tfTel);
    }

    @FXML
    void roleSelection(ActionEvent event) {

        // Access the selected item in muniSelectionComboBox
        role = roleSelectionComboBox.getValue();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //        ObservableList<String> typesProfile = FXCollections.observableArrayList();
        ObservableList<String> roleSelection = FXCollections.observableArrayList();

//        typesProfile.addAll("Citoyen", "Employé", "Responsable employé");
        roleSelection.addAll("Citoyen", "Responsable employé", "Employé", "Directeur");

        // Définir les éléments du ComboBox en utilisant la liste observable
//        profilTypeComboBox.setItems(typesProfile);
        roleSelectionComboBox.setItems(roleSelection);
    }
}



