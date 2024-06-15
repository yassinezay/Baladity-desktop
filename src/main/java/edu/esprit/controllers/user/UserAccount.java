package edu.esprit.controllers.user;

import com.twilio.rest.chat.v1.service.User;
import edu.esprit.controllers.user.Login;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Municipality;
import edu.esprit.services.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

public class UserAccount implements Initializable{

    @FXML
    private Label label;

    @FXML
    private ImageView imageF;

    @FXML
    private PasswordField pfMotDePasse;

    @FXML
    private PasswordField pfConfirmMotDePasse;

    @FXML
    private TextField tfAddresse;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfNumTel;

    @FXML
    private Button uploadbutton_modifier;

    private static final String USER_PREF_KEY = "current_user";

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");

    File selectedFile;

    ServiceUser serviceUser = new ServiceUser();
    private final int userId = Integer.parseInt(getCurrentUser());

    EndUser user = serviceUser.getOneByID(userId);

    @FXML
    void modifierUser(ActionEvent event) {
        String nom = tfNom.getText();
        String email = tfEmail.getText();
        String password = pfMotDePasse.getText();
        String confirmPassword = pfConfirmMotDePasse.getText();
        String numTel = tfNumTel.getText();
        String addresse = tfAddresse.getText();
        String type = user.getType();
        Municipality muni = user.getMuni();

        if(nom.isEmpty() || email.isEmpty() || numTel.isEmpty() || addresse.isEmpty() || imageF == null){
            System.out.println(selectedFile.getAbsolutePath());
            showAlert("Veuillez remplir tous les champs!");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            showAlert("Veuillez entrer un email valid!");
        }if(!password.isEmpty() || !confirmPassword.isEmpty()){
            if(!password.isEmpty() && !confirmPassword.isEmpty()){
                if(password.equals(confirmPassword)){
//                    String hashedPassword = hashPassword(password);
                    serviceUser.modifier(new EndUser(userId, email,nom,password,type,numTel,muni,addresse,selectedFile.getAbsolutePath(),false));
                    showAlert("User Updated");
                } else showAlert("Vérifier votre mot de passe!");
            } else showAlert("Veuillez remplir les deux champs");
        }  else {
            EndUser endUser = new EndUser(userId, email,nom,user.getPassword(),type,numTel,muni,addresse,selectedFile.getAbsolutePath(),false);
            serviceUser.modifier(endUser);
            showAlert("User Updated");
        }

    }

    @FXML
    void pickImage(ActionEvent event) {
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
            imageF.setImage(image);
        }
    }


    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            // Convert byte array to hexadecimal representation
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception appropriately
            return null;
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Status");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getCurrentUser() {
        Preferences preferences = Preferences.userNodeForPackage(Login.class);
        return preferences.get(USER_PREF_KEY, "DefaultUser");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfNom.setText(user.getNom());
        tfEmail.setText(user.getEmail());
//        pfMotDePasse.setText(user.getPassword());
//        pfConfirmMotDePasse.setText(user.getPassword());
        tfNumTel.setText(user.getPhoneNumber());
        tfAddresse.setText(user.getLocation());

        // Afficher l'image de l'événement
        String imagePath = user.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageF.setImage(image);
                selectedFile = file;
            }
        }

    }
}
