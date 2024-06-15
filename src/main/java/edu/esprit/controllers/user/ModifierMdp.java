package edu.esprit.controllers.user;

import edu.esprit.entities.EndUser;
import edu.esprit.services.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

public class ModifierMdp {

    @FXML
    private PasswordField pfConfirmerNouveauMdp;

    @FXML
    private PasswordField pfNouveauMdp;

    ServiceUser serviceUser = new ServiceUser();

    EndUser endUser = new EndUser();
    String confirmNouveauPwd ;

    String nouveauPwd ;

    String email;

    public void setData(String email) throws Exception {
        this.email = email;
    }

    @FXML
    void ModifierNouveauMdp(ActionEvent event) {

        confirmNouveauPwd = pfConfirmerNouveauMdp.getText();

        nouveauPwd = pfNouveauMdp.getText();

        endUser = serviceUser.getOneByEmail(email);

        if(nouveauPwd.isEmpty() || confirmNouveauPwd.isEmpty()){
            showAlert("Veuillez remplir tous les champs!");
        } else if (!nouveauPwd.equals(confirmNouveauPwd)) {
            showAlert("Vérifier votre mot de passe!");
        } else {
//            String hashedPassword = hashPassword(nouveauPwd);
            endUser.setPassword(nouveauPwd);
            serviceUser.modifier(endUser);
            showAlert("Le mot de passe a étè modifié");
        }
    }

    @FXML
    void handleLabelClick(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/user/Login.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Se connecter");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    private String hashPassword(String password) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hashedBytes = md.digest(password.getBytes());
//
//            // Convert byte array to hexadecimal representation
//            StringBuilder stringBuilder = new StringBuilder();
//            for (byte b : hashedBytes) {
//                stringBuilder.append(String.format("%02x", b));
//            }
//
//            return stringBuilder.toString();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            // Handle the exception appropriately
//            return null;
//        }
//    }
    public static String hashPassword(String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(13));
        return hashedPassword;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Status");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
