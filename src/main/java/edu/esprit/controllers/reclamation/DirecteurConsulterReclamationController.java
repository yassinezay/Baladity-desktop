package edu.esprit.controllers.reclamation;

import edu.esprit.entities.Reclamation;
import edu.esprit.services.ServiceReclamation;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class DirecteurConsulterReclamationController implements Initializable {

    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private VBox MainLeftSidebar;

    private boolean isSidebarVisible = true;
    @FXML
    private Text TFadresseReclamationDetail;

    @FXML
    private Text TFdateReclamationDetail;

    @FXML
    private Text TFdescriptionReclamationDetail;

    @FXML
    private ImageView Imagedetail;

    @FXML
    private Text TFsujetReclamationDetail;

    @FXML
    private Text TFtypeReclamationDetail;
    private ServiceReclamation serviceReclamation;
    private Reclamation reclamation;
    private String imagePath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser la taille du SecondBorderPane avec la même largeur que la barre latérale
        double sidebarWidth = MainLeftSidebar.getWidth();
        SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() + sidebarWidth);
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

    public void setData(Reclamation reclamation) {
        this.reclamation = reclamation;
        TFsujetReclamationDetail.setText(reclamation.getSujet_reclamation());
        TFtypeReclamationDetail.setText(reclamation.getType_reclamation());
        TFdateReclamationDetail.setText(String.valueOf(reclamation.getDate_reclamation()));
        TFdescriptionReclamationDetail.setText(reclamation.getDescription_reclamation());
        TFadresseReclamationDetail.setText(reclamation.getAdresse_reclamation());
        String imageUrl = reclamation.getImage_reclamation();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                // Create an instance of File using the file path
                File file = new File("C:\\Users\\amine\\Desktop\\PiDev\\DevMasters-Baladity\\public\\uploads\\" + imageUrl);
                // Check if the file exists
                if (file.exists()) {
                    // Create an Image instance from the file path
                    Image image = new Image(file.toURI().toString());
                    // Set the image in the ImageView
                    Imagedetail.setImage(image);
                } else {
                    System.err.println("File not found: " + file.getPath());
                }
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
                Imagedetail.setImage(defaultImage);
            } else {
                System.err.println("Default image not found!");
            }
        }
    }
    @FXML
    void modifytraitereclamation(ActionEvent event) {
        if (reclamation != null && serviceReclamation != null) {
            try {
                // Mettre à jour le statut de la réclamation
                reclamation.setStatus_reclamation("traité");

                // Appeler la méthode de modification du service de réclamation
                serviceReclamation.modifier(reclamation);
                Notifications.create()
                        .title("Modification réussie")
                        .text("Réclamation traitée avec succès !")
                        .darkStyle() // Utilise un style sombre pour la notification
                        .graphic(new ImageView("assets/done.png"))
                        .hideAfter(Duration.seconds(5)) // Masque automatiquement la notification après 5 secondes
                        .position(Pos.BOTTOM_RIGHT) // Positionne la notification en bas à droite de l'écran
                        .showInformation();

                // Rediriger l'utilisateur vers la vue précédente (par exemple, la liste des réclamations)
                Stage stage = (Stage) TFadresseReclamationDetail.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/DirecteurReclamationStatusGui.fxml"));
                stage.setScene(new Scene(root));

            } catch (IOException e) {
                // Gérer l'exception si la redirection échoue
                showAlert(Alert.AlertType.ERROR, "Erreur de redirection", "Une erreur s'est produite lors de la redirection : " + e.getMessage());
            }
        } else {
            // Afficher un message d'erreur si la réclamation est null ou si le service de réclamation est null
            showAlert(Alert.AlertType.ERROR, "Erreur de modification", "Impossible de modifier la réclamation car aucune réclamation n'est sélectionnée ou le service de réclamation est null.");
        }
    }


    public void setServiceReclamation(ServiceReclamation serviceReclamation) {
        this.serviceReclamation = serviceReclamation;
    }
    @FXML
    void buttonReturnConsulterReclamation(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/DirecteurReclamationStatusGui.fxml"));
            TFsujetReclamationDetail.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }

    }
    @FXML
    void encoursReclamationModifier(ActionEvent event) {
        if (reclamation != null && serviceReclamation != null) {
            try {
                // Mettre à jour le statut de la réclamation
                reclamation.setStatus_reclamation("en cours");

                // Appeler la méthode de modification du service de réclamation
                serviceReclamation.modifier(reclamation);
                Notifications.create()
                        .title("Modification réussie")
                        .text("Réclamation En cours de traitement !")
                        .darkStyle() // Utilise un style sombre pour la notification
                        .graphic(new ImageView("assets/done.png"))
                        .hideAfter(Duration.seconds(5)) // Masque automatiquement la notification après 5 secondes
                        .position(Pos.BOTTOM_RIGHT) // Positionne la notification en bas à droite de l'écran
                        .showInformation();

                // Rediriger l'utilisateur vers la vue précédente (par exemple, la liste des réclamations)
                Stage stage = (Stage) TFadresseReclamationDetail.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/DirecteurAfficherReclamationECGui.fxml"));
                stage.setScene(new Scene(root));

            } catch (IOException e) {
                // Gérer l'exception si la redirection échoue
                showAlert(Alert.AlertType.ERROR, "Erreur de redirection", "Une erreur s'est produite lors de la redirection : " + e.getMessage());
            }
        } else {
            // Afficher un message d'erreur si la réclamation est null ou si le service de réclamation est null
            showAlert(Alert.AlertType.ERROR, "Erreur de modification", "Impossible de modifier la réclamation car aucune réclamation n'est sélectionnée ou le service de réclamation est null.");
        }
    }



}
