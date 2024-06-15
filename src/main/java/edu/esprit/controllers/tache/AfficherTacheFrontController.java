package edu.esprit.controllers.tache;

import edu.esprit.controllers.user.Login;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Tache;
import edu.esprit.services.EtatTache;
import edu.esprit.services.ServiceTache;
import edu.esprit.services.ServiceUser;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.prefs.Preferences;
public class AfficherTacheFrontController implements Initializable {
    private static final String USER_PREF_KEY = "current_user";
    ServiceUser serviceUser = new ServiceUser();

    int userId = Integer.parseInt(getCurrentUser());
    EndUser user = serviceUser.getOneByID(userId);
    private final ServiceTache ST = new ServiceTache();
    public TextField todoCount;
    public TextField doingCount;
    public TextField doneCount;
    @FXML
    private TextField searchTacheLabel;
    @FXML
    private GridPane TODO;
    @FXML
    private GridPane DOING;
    @FXML
    private GridPane DONE;
    public BorderPane firstborderpane;
    @FXML
    private AnchorPane MainAnchorPaneBaladity;
    @FXML
    private BorderPane SecondBorderPane;
    @FXML
    private VBox MainLeftSidebar;
    private boolean isSidebarVisible = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enableGridDropping(TODO);
        enableGridDropping(DOING);
        enableGridDropping(DONE);
        loadTasks();
        // Add listener to searchTextField
        searchTacheLabel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Trigger searchTacheLabel method whenever text changes
                searchTacheLabel(new ActionEvent());
            }
        });
    }

    private void refreshAllGrids() {
        TODO.getChildren().clear();
        DOING.getChildren().clear();
        DONE.getChildren().clear();
        loadTasks();
    }

    private void loadTasks() {
        Set<Tache> tacheList = ST.getAll();
        int todoCounter = 0;
        int doingCounter = 0;
        int doneCounter = 0;

        try {
            for (Tache tache : tacheList) {
                // Check if the task belongs to the category type of the current user
                if (tache.getCategorie().equals(user.getType())) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tacheGui/TacheFront.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();
                    TacheFrontController itemController = fxmlLoader.getController();
                    itemController.setData(tache);

                    switch (tache.getEtat_T()) {
                        case TODO:
                            TODO.addRow(TODO.getRowCount(), anchorPane);
                            todoCounter++;
                            break;
                        case DOING:
                            DOING.addRow(DOING.getRowCount(), anchorPane);
                            doingCounter++;
                            break;
                        case DONE:
                            DONE.addRow(DONE.getRowCount(), anchorPane);
                            doneCounter++;
                            break;
                    }
                }
            }
            todoCount.setText("TO DO | " + todoCounter);
            doingCount.setText("DOING | " + doingCounter);
            doneCount.setText("DONE | " + doneCounter);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Erreur Chargement Des Taches.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void enableGridDropping(GridPane gridPane) {
        final GridPane finalGridPane = gridPane; // Declare as effectively final

        if (!gridPane.getId().equals("doneGrid")) {
            gridPane.setOnDragOver(event -> {
                if (event.getGestureSource() != finalGridPane && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            gridPane.setOnDragEntered(event -> {
                if (event.getGestureSource() != finalGridPane && event.getDragboard().hasString()) {
                    finalGridPane.setStyle("-fx-background-color: red;");
                }
                event.consume();
            });

            gridPane.setOnDragExited(event -> {
                finalGridPane.setStyle("-fx-background-color: transparent;");
                event.consume();
            });

            gridPane.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                final boolean[] success = {false};
                if (db.hasString()) {
                    int taskId = Integer.parseInt(db.getString());
                    Tache task = ST.getOneByID(taskId);
                    if (task != null) {
                        // Assuming the grid's ID corresponds to the new state of the task
                        EtatTache newState = EtatTache.valueOf(finalGridPane.getId());
                        if (newState == EtatTache.TODO && task.getEtat_T() == EtatTache.DONE ) {
                            // Task is already in the "Done" state, prevent moving
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning");
                            alert.setHeaderText(null);
                            alert.setContentText("Cette tâche est déjà dans l'état 'DONE' et ne peut pas être déplacée.");
                            alert.showAndWait();
                        } else if (newState == EtatTache.DOING && task.getEtat_T() == EtatTache.DONE) {
                            // Task is already in the "Done" state, prevent moving
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning");
                            alert.setHeaderText(null);
                            alert.setContentText("Cette tâche est déjà dans l'état 'DONE' et ne peut pas être déplacée.");
                            alert.showAndWait();
                        } else if (newState == EtatTache.DONE && task.getEtat_T() == EtatTache.DONE) {
                            // Task is already in the "Done" state, prevent moving
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning");
                            alert.setHeaderText(null);
                            alert.setContentText("Cette tâche est déjà dans l'état 'DONE' et ne peut pas être déplacée.");
                            alert.showAndWait();
                        } else {
                            if (newState == EtatTache.DONE) {
                                // Show confirmation dialog for irreversible action
                                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                                confirmationAlert.setTitle("Confirmation");
                                confirmationAlert.setHeaderText(null);
                                confirmationAlert.setContentText("Êtes-vous sûr de vouloir déplacer cette tâche à 'DONE' ? Cette action est irréversible.");
                                confirmationAlert.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.OK) {
                                        // Proceed with the action
                                        task.setEtat_T(newState);
                                        task.setUser(user);
                                        ST.modifier(task); // Update the task state in the database
                                        success[0] = true;
                                    }
                                });
                            } else {
                                // For states other than DONE, proceed directly
                                task.setEtat_T(newState);
                                task.setUser(user);
                                ST.modifier(task); // Update the task state in the database
                                success[0] = true;
                            }
                        }
                    }
                }
                event.setDropCompleted(success[0]);
                event.consume();
                if (success[0]) {
                    refreshAllGrids(); // Refresh all grids after successful drop
                }
            });
        } else {
            gridPane.setOnDragOver(event -> {
                event.acceptTransferModes(TransferMode.NONE); // Disable drag over
                event.consume();
            });
            gridPane.setOnDragDropped(event -> {
                // Do nothing, as dragging into this grid is disabled
                event.setDropCompleted(false);
                event.consume();
            });
        }
    }


    @FXML
    void searchTacheLabel(ActionEvent event) {
        String query = searchTacheLabel.getText().trim().toLowerCase(); // Get search query
        Set<Tache> tacheList = ST.getAll();
        int[] counters = new int[EtatTache.values().length];
        try {
            TODO.getChildren().clear(); // Clear existing rows
            DOING.getChildren().clear();
            DONE.getChildren().clear();

            tacheList.stream()
                    .filter(tache -> tache.getTitre_T().toLowerCase().contains(query))
                    .forEach(tache -> {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tacheGui/TacheFront.fxml"));
                            AnchorPane anchorPane = fxmlLoader.load();
                            TacheFrontController itemController = fxmlLoader.getController();
                            itemController.setData(tache);

                            GridPane targetGrid = switch (tache.getEtat_T()) {
                                case TODO -> TODO;
                                case DOING -> DOING;
                                case DONE -> DONE;
                                default -> null;
                            };

                            if (targetGrid != null) {
                                targetGrid.addRow(targetGrid.getRowCount(), anchorPane);
                                counters[tache.getEtat_T().ordinal()]++;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            showAlert(Alert.AlertType.ERROR, "Error", "Erreur Chargement Des Taches.");
                        }
                    });
            // Update counts
            for (EtatTache etatTache : EtatTache.values()) {
                int index = etatTache.ordinal();
                switch (etatTache) {
                    case TODO:
                        todoCount.setText("TO DO | " + counters[index]);
                        break;
                    case DOING:
                        doingCount.setText("DOING | " + counters[index]);
                        break;
                    case DONE:
                        doneCount.setText("DONE | " + counters[index]);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load tasks.");
        }
    }

    public void chatBot(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tacheGui/ChatBotTache.fxml"));
            Parent root = loader.load();
            ChatBotTacheController controller = loader.getController();
            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getCurrentUser() {
        Preferences preferences = Preferences.userNodeForPackage(Login.class);
        return preferences.get(USER_PREF_KEY, "DefaultUser");
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
            TranslateTransition borderPaneTransition = new TranslateTransition(Duration.millis(250), SecondBorderPane);
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
    public void buttonreturnTache(ActionEvent actionEvent) {
        try {
            if( user.getType().equals("Admin")){
                System.out.println("Resource URL: " + getClass().getResource("/MainGuiBack.fxml"));
                Parent root = FXMLLoader.load(getClass().getResource("/MainGuiBack.fxml"));
            } else {
                System.out.println("Resource URL: " + getClass().getResource("/MainGui.fxml"));
                Parent root = FXMLLoader.load(getClass().getResource("/MainGui.fxml"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
}
