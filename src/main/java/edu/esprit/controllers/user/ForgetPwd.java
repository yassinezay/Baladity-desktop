package edu.esprit.controllers.user;

import edu.esprit.services.GMailer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.security.SecureRandom;
import java.util.ResourceBundle;

public class ForgetPwd implements Initializable {

    String email;
    String nom;
    private static final int OTP_LENGTH = 6;

    String otp;

    @FXML
    private TextField OTPField;


//    public void setData(String email, String nom) throws Exception {
//        this.email = email;
//        this.nom = nom;
//        otp = generateOTP();
//        String content = String.format("""
//
//                Cher(e) %s,
//
//                Vous avez récemment demandé la réinitialisation de votre mot de passe. Pour procéder à cette réinitialisation, veuillez utiliser le code de vérification ci-dessous :
//
//                Code de Vérification : %s
//
//                Si vous n'avez pas demandé de réinitialisation de mot de passe, veuillez ignorer ce message.
//
//                Cordialement,
//                Baladity
//                """,nom, otp);
//        new GMailer(email).sendMail("Code de Vérification", content);
//    }

    @FXML
    void VerifierOTPButton(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/modifierPassword.fxml"));
            Parent root = loader.load();

            // Access the controller
            ModifierMdp modifierMdpController = loader.getController();

            // Set the email
            modifierMdpController.setData(email);

            // Show the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Oubliez mot de passe");
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        if (OTPField.getText().equals(otp)) {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/modifierPassword.fxml"));
//                Parent root = loader.load();
//
//                // Access the controller
//                ModifierMdp modifierMdpController = loader.getController();
//
//                // Set the email
//                modifierMdpController.setData(email);
//
//                // Show the scene
//                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                Scene scene = new Scene(root);
//                stage.setScene(scene);
//                stage.setTitle("Oubliez mot de passe");
//                stage.show();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            showAlert("Veuillez vérifier votre code");
//        }
    }

    @FXML
    void reEnvoyerOTP(ActionEvent event) throws Exception {
        otp = generateOTP();
        String content = String.format("""
                                
                Cher(e) %s,
                                
                Vous avez récemment demandé la réinitialisation de votre mot de passe. Pour procéder à cette réinitialisation, veuillez utiliser le code de vérification ci-dessous :
                                
                Code de Vérification : %s
                                
                Si vous n'avez pas demandé de réinitialisation de mot de passe, veuillez ignorer ce message.
                                
                Cordialement,
                Baladity
                """,nom, otp);
        new GMailer(email).sendMail("Code de Vérification", content);
    }

    public static String generateOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }

    private void showAlert(String message) {
        // Affiche une boîte de dialogue d'information avec le message donné
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        System.out.println(email);
//        try {
//            new GMailer( "wertatanifadi@gmail.com").sendMail("My First Message", """
//                    Dear reader,
//
//                    Nshallah yakhtef l mail.
//
//                    Best regards,
//                    Fadi
//                    """);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}
