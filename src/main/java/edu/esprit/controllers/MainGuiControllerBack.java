package edu.esprit.controllers;

import edu.esprit.controllers.user.Login;
import edu.esprit.controllers.user.UserAccount;
import edu.esprit.entities.EndUser;
import edu.esprit.services.ServiceUser;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class MainGuiControllerBack {

    @FXML
    private Button BTNGestionAct;
    @FXML
    private BorderPane MainAnchorPaneBaladity;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private VBox MainLeftSidebar;

    private static final String USER_PREF_KEY = "current_user";

    ServiceUser serviceUser = new ServiceUser();

    int userId = Integer.parseInt(getCurrentUser());

    EndUser endUser = serviceUser.getOneByID(userId);

    private boolean isSidebarVisible = true;

    @FXML
    void BTNToggleSidebar(ActionEvent event) {
        TranslateTransition sideBarTransition = new TranslateTransition(Duration.millis(200), MainLeftSidebar);

        double sidebarWidth = 308;
        double hiddenWidth = 0; // Width when sidebar is hidden

        if (isSidebarVisible) {
            // Hide sidebar
            sideBarTransition.setToX(-sidebarWidth);
            isSidebarVisible = false;
            // Set the width of the MainLeftSidebar when sidebar is hidden
            MainLeftSidebar.setPrefWidth(hiddenWidth);
            // Set the width of the SecondBorderPane when sidebar is hidden
            SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() + sidebarWidth);
        } else {
            // Show sidebar
            sideBarTransition.setToX(0);
            isSidebarVisible = true;
            // Set the width of the MainLeftSidebar back to its original value
            MainLeftSidebar.setPrefWidth(MainLeftSidebar.getWidth() + sidebarWidth);
            // Set the width of the SecondBorderPane back to its original value
            SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() - sidebarWidth);
        }

        sideBarTransition.play();
    }


    public void BTNGestionEvennement(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/evenementGui/EventDashboard.fxml"));
            MainAnchorPaneBaladity.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    public void BTNGestionUser(ActionEvent actionEvent) {
    }

    public void BTNGestionRec(ActionEvent actionEvent) {
    }

    public void BTNGestionAct(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ActualiteGui/AfficherActualiteAdminGui.fxml"));
            MainAnchorPaneBaladity.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    public void BTNGestionEquipement(ActionEvent actionEvent) {

    }

    public void BTNGestionTache(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/TacheGui/AfficherTache.fxml"));
            MainAnchorPaneBaladity.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void BTNGestionReclamation(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/AdminAfficherReclamationGui.fxml"));
            MainAnchorPaneBaladity.getScene().setRoot(root);
        } catch (IOException e) {
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

    @FXML
    void chatbotAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ChatbotGui.fxml"));
            MainAnchorPaneBaladity.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    public void BTNGestionEquipements(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/equipementGui/AfficherEquipementGui.fxml"));
            MainAnchorPaneBaladity.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    public void BTNGestionEvent(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/evenementGui/EventDashboard.fxml"));
            MainAnchorPaneBaladity.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    public void BTNGestionuser(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/user/UserGui.fxml"));
            MainAnchorPaneBaladity.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    public void BTNGestiontaches(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/TacheGui/AfficherTache.fxml"));
            MainAnchorPaneBaladity.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    @FXML
    void logoutButton(ActionEvent event) throws IOException {
        // Logging out
        Preferences preferences = Preferences.userNodeForPackage(UserAccount.class);
        preferences.remove("current_user");

        // After logging out, show the login screen
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/user/Login.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Se connecter");
        stage.show();

    }
}