package edu.esprit.controllers.tache;

import edu.esprit.controllers.user.Login;
import edu.esprit.entities.CommentaireTache;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Tache;
import edu.esprit.services.ServiceCommentaireTache;
import edu.esprit.services.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

public class ModifierCommentaireTacheController {

    @FXML
    private TextArea commentField;
    private CommentaireTache commentaireTache;
    private ServiceCommentaireTache serviceCommentaireTache;
    private Tache tache;
    private static final String USER_PREF_KEY = "current_user";
    ServiceUser serviceUser = new ServiceUser();

    int userId = Integer.parseInt(getCurrentUser());
    EndUser user = serviceUser.getOneByID(userId);

    public void setData(CommentaireTache commentaireTache) {
        this.commentaireTache = commentaireTache;
        commentField.setText(commentaireTache.getText_C());
    }

    public void setServiceCommentaireTache(ServiceCommentaireTache serviceCommentaireTache) {
        this.serviceCommentaireTache = serviceCommentaireTache;
    }

    @FXML
    void modifierCommentaire(ActionEvent event) {
        if (serviceCommentaireTache == null) {
            System.err.println("Service CommentaireTache pas definie.");
            return;
        }
        String newCommentText = commentField.getText();
        // Update the comment text
        commentaireTache.setText_C(newCommentText);
        // Modify the comment using the service
        serviceCommentaireTache.modifier(commentaireTache);
        // Show confirmation message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Modification reussie");
        alert.setHeaderText(null);
        alert.setContentText("Commentaire modifie avec succees.");
        alert.showAndWait();
        // Close the window
        Stage stage = (Stage) commentField.getScene().getWindow();
        stage.close();
    }
    private String getCurrentUser() {
        Preferences preferences = Preferences.userNodeForPackage(Login.class);
        return preferences.get(USER_PREF_KEY, "DefaultUser");
    }
}