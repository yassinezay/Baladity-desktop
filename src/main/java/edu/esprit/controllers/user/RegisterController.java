package edu.esprit.controllers.user;

import edu.esprit.controllers.camera.Camera;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Municipality;
import edu.esprit.services.GMailer;
import edu.esprit.services.ServiceMuni;
import edu.esprit.services.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

public class RegisterController implements Initializable {

    @FXML
    private ImageView ImageF = null;

    @FXML
    private ComboBox<String> muniSelectionComboBox;

    @FXML
    private PasswordField pfConfirmMotDePasse;

    @FXML
    private PasswordField pfMotDePasse;

    @FXML
    private Button pickImageButton;

    @FXML
    private Button takePictureButton;

    @FXML
    private TextField tfAddresse;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfTel;

    String imagePath;

    File selectedFile = null;

    Municipality muni;
    ServiceUser serviceUser = new ServiceUser();

    ServiceMuni serviceMuni = new ServiceMuni();

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");

    private static final Pattern PHONE_PATTERN = Pattern.compile("[0-9]");

    private static final int OTP_LENGTH = 6;
    String otp;
    private static final String USER_PREF_KEY = "current_user";

    @FXML
    void registerAction(ActionEvent event) throws Exception {
        String nom = tfNom.getText();
        String email = tfEmail.getText();
        String motDePasse = pfMotDePasse.getText();
        String confirmMotDePasse = pfConfirmMotDePasse.getText();
        String numTel = tfTel.getText();
        String location = tfAddresse.getText();
        String selectedMuni = muniSelectionComboBox.getValue();

        // Votre logique de traitement d'inscription ici
        // Vous pouvez appeler votre service pour enregistrer l'utilisateur, par exemple
        if (nom.isEmpty() || email.isEmpty() || motDePasse.isEmpty() || confirmMotDePasse.isEmpty() || numTel.isEmpty() || location.isEmpty() || selectedMuni == null) {
            showAlert("Veuillez remplir tous les champs!");
        } else if (ImageF == null) {
            showAlert("Veuillez entrer une image!");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            showAlert("Veuillez entrer un email valid!");
        } else if (serviceUser.getOneByEmail(email) != null) {
            showAlert("Email existe déjà!");
        } else if (numTel.length() != 8 && !PHONE_PATTERN.matcher(numTel).matches()) {
            showAlert("Le numéro de téléphone doit être composé de 8 chiffres");
        } else if (!motDePasse.equals(confirmMotDePasse)) {
            showAlert("Vérifier votre mot de passe!");
        } else {
            otp = generateOTP();
            String content = String.format("""
            
            Cher(e) %s,
                     
            Merci de vous être inscrit(e) sur Baladity. Pour finaliser votre inscription, veuillez utiliser le code de validation ci-dessous:
                     
            Code de Validation : %s
                     
            Veuillez ne pas partager ce code avec d'autres personnes.
                     
            Si vous n'avez pas créé de compte sur Baladity, veuillez ignorer ce message.
                     
            Cordialement,
            Baladity
            """,nom,otp);
            String hashedPwd = hashPassword(motDePasse);
            String imageName = selectedFile.getName(); // Get the name of the selected file
            if(selectedFile != null){
                imagePath = selectedFile.getAbsolutePath();
            }
//        new GMailer(email).sendMail("Code de Validation", content);
            EndUser user = new EndUser(nom, email, motDePasse, "Citoyen", numTel, muni, location, imageName); // Pass the image filename
            serviceUser.ajouter(user);
            int userId = serviceUser.getOneByEmail(email).getId();
            setCurrentUser(userId);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainGui.fxml"));
            Parent root = loader.load();

            // Continue with your navigation code
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("User Account");
            stage.show();

        }
    }

    @FXML
    void pickImageAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            // Handle the selected image file (e.g., display it, process it, etc.)
            System.out.println("Selected image: " + selectedFile.getName()); // Print the name of the selected file
            Image image = new Image(selectedFile.toURI().toString());
            ImageF.setImage(image);
        }
    }


    @FXML
    void muniSelection(ActionEvent event) {

        // Access the selected item in muniSelectionComboBox
        String selectedMuni = muniSelectionComboBox.getValue();
        System.out.println("Selected Muni: " + selectedMuni);
        muni = serviceMuni.getOneByName(selectedMuni);
        System.out.println(muni);

    }



    Camera camera;
    @FXML
    void takePictureAction(ActionEvent event) {

        // Load OpenCV DLL
        System.load("C:\\Users\\werta\\Documents\\GitHub\\baladity\\src\\main\\java\\edu\\esprit\\services\\opencv_java490.dll");

        // Run camera in the Event Dispatch Thread
        EventQueue.invokeLater(() -> {
            Camera camera = new Camera();

            // Set the callback to handle the image URL
            camera.setCallback(imageUrl -> {
                // Handle the image URL (e.g., update UI, use it in further logic)
                System.out.println("Image captured: " + imageUrl);
                imagePath = imageUrl;
                // Load the image and set it to ImageF
                File imageFile = new File(imageUrl);
                Image image = new Image(imageFile.toURI().toString());
                ImageF.setImage(image);

                // If needed, you can trigger additional actions or UI updates here
            });

            // Start camera in a new thread
            new Thread(() -> camera.startCamera()).start();
            camera.isCameraOpen = true;


        });
    }

    @FXML
    void handleLabelClick(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/user/Login.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Se connecter");
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
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

    public void openForm(ActionEvent event, EndUser user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainGui.fxml"));
            Parent root = loader.load();

            // Access the controller
            VerifierEmail verifierEmailController = loader.getController();

            // Set the email
            verifierEmailController.setData(otp, user);

            // Show the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Vérifier Email");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
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

    private void setCurrentUser(int userId) {
        Preferences preferences = Preferences.userNodeForPackage(Login.class);
        preferences.put(USER_PREF_KEY, String.valueOf(userId));
        System.out.println("Current User saved: " + userId);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        ObservableList<String> typesProfile = FXCollections.observableArrayList();
        ObservableList<String> muniSelection = FXCollections.observableArrayList();

//        typesProfile.addAll("Citoyen", "Employé", "Responsable employé");
        muniSelection.addAll("Ariana Ville", "La Soukra", "Mnihla", "Raoued", "Ettadhamen", "Sidi Thabet");

        // Définir les éléments du ComboBox en utilisant la liste observable
//        profilTypeComboBox.setItems(typesProfile);
        muniSelectionComboBox.setItems(muniSelection);
    }
}
