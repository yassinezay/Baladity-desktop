package edu.esprit.controllers.tache;

import edu.esprit.controllers.ChartContainerController;
import edu.esprit.controllers.user.Login;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Tache;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class AfficherTacheController implements Initializable {
    private final ServiceTache sr = new ServiceTache();
    public BorderPane firstborderpane;
    @FXML
    private TextField searchbar;
    @FXML
    private AnchorPane MainAnchorPaneBaladity;
    @FXML
    private BorderPane SecondBorderPane;
    @FXML
    private VBox MainLeftSidebar;
    private boolean isSidebarVisible = true;
    @FXML
    private GridPane grid;
    private Stage stage; // Define a stage variable
    private static final String USER_PREF_KEY = "current_user";

    ServiceUser serviceUser = new ServiceUser();
    int userId  = Integer.parseInt(getCurrentUser());
    //  int userId = 48;
    EndUser user = serviceUser.getOneByID(userId);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchbar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Trigger searchTacheLabel method whenever text changes
                searchTacheLabel(new ActionEvent());
            }
        });
        loadTasks();
    }

    private void loadTasks() {
        Set<Tache> tacheList = sr.getAll();
        try {
            for (Tache tache : tacheList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tacheGui/Tache.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                TacheController itemController = fxmlLoader.getController();
                itemController.setData(tache);
                grid.addRow(grid.getRowCount(), anchorPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Erreur Chargement Des Taches.");
        }
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

    public void ajouterButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tacheGui/AjouterTache.fxml"));
            Parent root = loader.load();
            AjouterTacheController controller = loader.getController();
            // Get the stage of the current scene
            Stage currentStage = (Stage) grid.getScene().getWindow();
            // Set the stage to the controller
            controller.setStage(currentStage);
            // Create a new scene with the root and set it on the current stage
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Erreur Chargement interface");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void MeilleurEmpTLabel(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tacheGui/ChartContainer.fxml"));
            Parent root = loader.load();
            ChartContainerController controller = loader.getController();
            // Retrieve an EndUser object representing the user
            // Get the tasks assigned to the user
            Set<Tache> userTasks = sr.getAll();
            // Pass the tasks to the controller
            controller.displayChart(userTasks);
            // Show the stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void PDFTacheLabel(ActionEvent actionEvent) {
        // Get all tasks from the service
        Set<Tache> allTasks = sr.getAll();
        try {
            // Create a new PDF document
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Load the image
            PDImageXObject pdImage = PDImageXObject.createFromFile("src/main/resources/assets/logoiconT.png", document);

            // Create a content stream for writing to the PDF
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Set font and position for writing
            contentStream.setFont(PDType1Font.COURIER_BOLD, 12);

            // Set text color
            contentStream.setNonStrokingColor(Color.BLACK);

            // Add image to the top-left corner
            float imageWidth = (float) pdImage.getWidth() / 4;
            float imageHeight = (float) pdImage.getHeight() / 4;
            float startX = 25; // Adjust as needed
            float startY = page.getMediaBox().getHeight() - 25 - imageHeight; // Adjust as needed
            contentStream.drawImage(pdImage, startX, startY, imageWidth, imageHeight);

            // Write column headers
            float yPosition = page.getMediaBox().getHeight() - imageHeight - 50; // Start below the image
            float margin = 50; // Left margin
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, startY - 25); // Start below the image
            contentStream.showText("Titre");
            contentStream.newLineAtOffset(100, 0); // Move to the next column
            contentStream.showText("Date Debut");
            contentStream.newLineAtOffset(100, 1); // Move to the next column
            contentStream.showText("DateFin");
            contentStream.newLineAtOffset(100, 2); // Move to the next column
            contentStream.showText("Etat");
            contentStream.newLineAtOffset(100, 3); // Move to the next column
            contentStream.endText();

            // Move to the next line
            yPosition -= 30; // Increased spacing between lines

            // Write tasks to the PDF
            for (Tache task : allTasks) {
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(task.getTitre_T());
                contentStream.newLineAtOffset(100, 0); // Move to the next column
                contentStream.showText(task.getDate_DT().toString());
                contentStream.newLineAtOffset(100, 1); // Move to the next column
                contentStream.showText(task.getDate_FT().toString());
                contentStream.newLineAtOffset(100, 2); // Move to the next column
                contentStream.showText(task.getEtat_T().toString());
                contentStream.endText();

                // Draw borders between elements
                contentStream.moveTo(margin, yPosition - 5); // Move to the starting point
                contentStream.lineTo(margin + 400, yPosition - 5); // Draw a line to create a border between columns
                contentStream.stroke();

                // Move to the next line for the next task
                yPosition -= 20; // Adjust line spacing as needed
            }

            contentStream.close();

            // Default file name based on today's date and time
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String defaultFileName = "tache_" + timeStamp + ".pdf";

            // Prompt the user to choose where to save the PDF file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer Fichier PDF");
            fileChooser.setInitialDirectory(new File("src/main/resources"));
            fileChooser.setInitialFileName(defaultFileName);
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile != null) {
                // Save the document
                document.save(selectedFile);
                document.close();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Tache Exporter avec succees!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Aucun fichier selectionnee, exportation annulee.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Erreur exportation pdf");
        }
    }



    @FXML
    void EXCELTacheLabel(ActionEvent event) {
        // Get all tasks from the service
        Set<Tache> allTasks = sr.getAll();
        try {
            // Create a new Excel workbook
            Workbook workbook = new XSSFWorkbook();
            CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Taches");

            // Create a header row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Titre", "Date Debut","Date Fin", "Etat"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            // Populate data rows
            int rowNum = 1;
            for (Tache task : allTasks) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(task.getTitre_T());
                row.createCell(1).setCellValue(task.getDate_DT().toString());
                row.createCell(2).setCellValue(task.getDate_FT().toString());
                row.createCell(3).setCellValue(task.getEtat_T().toString());

                // Apply borders to the cells
                CellStyle style = workbook.createCellStyle();
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                for (int i = 0; i < columns.length; i++) {
                    row.getCell(i).setCellStyle(style);
                }
            }
            // Autosize columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            // Default file name based on today's date and time
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String defaultFileName = "tache_" + timeStamp + ".xlsx";

            // Initialize the stage
            Stage stage = (Stage) grid.getScene().getWindow();

            // Prompt the user to choose where to save the Excel file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer Fichier Excel");
            fileChooser.setInitialDirectory(new File("src/main/resources"));
            fileChooser.setInitialFileName(defaultFileName);
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showSaveDialog(stage);
            if (selectedFile != null) {
                // Write the workbook to the file
                FileOutputStream fileOut = new FileOutputStream(selectedFile);
                workbook.write(fileOut);
                fileOut.close();
                workbook.close();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Tache Exporter avec succees!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Aucun fichier selectionnee, exportation annulee.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Erreur exportation excel");
        }
    }

    @FXML
    void searchTacheLabel(ActionEvent actionEvent) {
        String searchText = searchbar.getText().toLowerCase().trim();
        Set<Tache> allTasks = sr.getAll();

        Set<Tache> filteredTasks = allTasks.stream()
                .filter(task -> task.getTitre_T().toLowerCase().contains(searchText))
                .collect(Collectors.toSet());

        displayTasks(filteredTasks);
    }

    private void displayTasks(Set<Tache> tasks) {
        grid.getChildren().clear(); // Clear existing tasks
        try {
            for (Tache tache : tasks) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tacheGui/Tache.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                TacheController itemController = fxmlLoader.getController();
                itemController.setData(tache);
                grid.addRow(grid.getRowCount(), anchorPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                searchbar.getScene().setRoot(root);
            } else {
                System.out.println("Resource URL: " + getClass().getResource("/MainGui.fxml"));
                Parent root = FXMLLoader.load(getClass().getResource("/MainGui.fxml"));
                searchbar.getScene().setRoot(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
    }

