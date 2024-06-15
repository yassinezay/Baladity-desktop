package edu.esprit.controllers.user;

import edu.esprit.entities.Municipality;
import edu.esprit.services.ServiceMuni;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifierMunicipality implements Initializable {

    @FXML
    private ImageView ImageM;

    @FXML
    private PasswordField pfConfirmMdp;

    @FXML
    private PasswordField pfMdp;

    @FXML
    private Button pickImageButton;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfNom;

    File selectedFile = null;

    private Municipality muni;

    ServiceMuni serviceMuni = new ServiceMuni();

    public void setData(Municipality muni) {
        this.muni = muni;
        // Afficher les données de l'événement dans les champs de texte
        tfNom.setText(muni.getNom_muni());
        tfEmail.setText(muni.getEmail_muni());
//        pfMdp.setText(endUser.getPassword());

        // Afficher l'image de l'événement
        String imagePath = muni.getImage();
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

        // Récupérer les nouvelles valeurs des champs de texte
        String nom = tfNom.getText();
        String email = tfEmail.getText();
        String pwd = pfMdp.getText();
        String confirmPwd = pfConfirmMdp.getText();

        if (isAnyFieldEmpty()) {
            showAlert("Veuillez remplir tous les champs!");
        } else if (!pwd.isEmpty() || !confirmPwd.isEmpty()) {
            if(pwd.isEmpty() || confirmPwd.isEmpty()){
                showAlert("Veuillez remplir tous les champs!");
            }else {
                if(!pwd.equals(confirmPwd)){
                    showAlert("Vérifier votre mot de passe!");
                } else {
                    // Mettre à jour les données de la muni avec le mot de passe
                    muni.setNom_muni(nom);
                    muni.setEmail_muni(email);
                    muni.setPassword_muni(pwd);


                    if(selectedFile == null){
                        muni.setImage(muni.getImage());
                    }else {
                        muni.setImage(selectedFile.getAbsolutePath());
                    }
                    serviceMuni.modifier(muni);

                    // Afficher une alerte pour confirmer la modification
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("L'événement a été modifié avec succès.");
                    alert.setTitle("Événement modifié");
                    alert.show();
                }
            }
        } else {
            // Mettre à jour les données de l'événement
            muni.setNom_muni(nom);
            muni.setEmail_muni(email);


            if(selectedFile == null){
                muni.setImage(muni.getImage());
            }else {
                muni.setImage(selectedFile.getAbsolutePath());
            }
            serviceMuni.modifier(muni);

            // Afficher une alerte pour confirmer la modification
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("L'événement a été modifié avec succès.");
            alert.setTitle("Événement modifié");
            alert.show();
        }

    }

    @FXML
    void imagePicker(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
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

    private boolean isFieldEmpty(TextField textField) {
        return textField.getText().isEmpty();
    }

    private boolean isAnyFieldEmpty() {
        return isFieldEmpty(tfNom) || isFieldEmpty(tfEmail) || ImageM == null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
