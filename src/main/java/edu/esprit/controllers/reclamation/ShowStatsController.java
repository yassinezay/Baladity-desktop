package edu.esprit.controllers.reclamation;

import edu.esprit.entities.Reclamation;
import edu.esprit.services.ServiceReclamation;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javafx.embed.swing.SwingNode;


public class ShowStatsController implements Initializable {

    @FXML
    private AnchorPane MainAnchorPaneBaladity;

    @FXML
    private BorderPane SecondBorderPane;

    @FXML
    private VBox MainLeftSidebar;

    private boolean isSidebarVisible = true;

    @FXML
    private AnchorPane Showstats;

    @FXML
    private AnchorPane showstatsnbr;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser la taille du SecondBorderPane avec la même largeur que la barre latérale
        double sidebarWidth = MainLeftSidebar.getWidth();
        SecondBorderPane.setPrefWidth(SecondBorderPane.getWidth() + sidebarWidth);
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(createSwingContent());
        Showstats.getChildren().add(swingNode);
        SwingNode lineChartSwingNode = new SwingNode();
        lineChartSwingNode.setContent(createLineChartSwingContent());
        showstatsnbr.getChildren().add(lineChartSwingNode);;

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
    private JComponent createSwingContent() {
        // Create your JFreeChart here (e.g., using a method)
        JFreeChart jFreeChart = createPieChart();

        // Wrap the JFreeChart ChartPanel inside a SwingNode
        ChartPanel chartPanel = new ChartPanel(jFreeChart);

        // Set the preferred size for the ChartPanel
        chartPanel.setPreferredSize(new java.awt.Dimension(400, 300));

        return chartPanel;
    }

    private JFreeChart createPieChart() {
        // Create a dataset
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Obtenez les réclamations à partir du service de réclamation
        ServiceReclamation serviceReclamation = new ServiceReclamation();

        // Compter le nombre total de réclamations
        int total = serviceReclamation.getAll().size();

        // Compter le nombre de réclamations par statut
        int traitees = serviceReclamation.getReclamationsTraitees().size();
        int enCours = serviceReclamation.getReclamationsEnCoursTraitees().size();
        int nonTraitees = serviceReclamation.getReclamationsNonTraitees().size();

        // Calculer les pourcentages
        double pourcentageTraitees = (double) traitees / total * 100;
        double pourcentageEnCours = (double) enCours / total * 100;
        double pourcentageNonTraitees = (double) nonTraitees / total * 100;

        // Ajoutez les données au dataset
        dataset.setValue("Traitées (" + String.format("%.2f", pourcentageTraitees) + "%)", traitees);
        dataset.setValue("En cours de traitement (" + String.format("%.2f", pourcentageEnCours) + "%)", enCours);
        dataset.setValue("Non traitées (" + String.format("%.2f", pourcentageNonTraitees) + "%)", nonTraitees);

        // Create a pie chart based on the dataset
        return ChartFactory.createPieChart(
                "Statistiques des réclamations par statut",
                dataset,
                true,
                true,
                false
        );
    }
    private JFreeChart createLineChart() {
        // Créez un dataset pour stocker les données de la courbe
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Obtenez les réclamations à partir du service de réclamation
        ServiceReclamation serviceReclamation = new ServiceReclamation();

        // Obtenez toutes les réclamations
        Set<Reclamation> reclamationSet = serviceReclamation.getAll();
        List<Reclamation> reclamations = new ArrayList<>(reclamationSet);

        // Utilisez un format de date pour regrouper les réclamations par date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Map pour stocker le nombre de réclamations par date
        Map<String, Integer> reclamationsParDate = new HashMap<>();

        // Comptez le nombre de réclamations par date
        for (Reclamation reclamation : reclamations) {
            String date = sdf.format(reclamation.getDate_reclamation());
            reclamationsParDate.put(date, reclamationsParDate.getOrDefault(date, 0) + 1);
        }

        // Ajoutez les données au dataset
        for (Map.Entry<String, Integer> entry : reclamationsParDate.entrySet()) {
            dataset.addValue(entry.getValue(), "Nombre de réclamations", entry.getKey());
        }

        // Créez le graphique en ligne avec les axes appropriés
        JFreeChart chart = ChartFactory.createLineChart(
                "Nombre de réclamations par date",
                "Date",
                "Nombre de réclamations",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        return chart;
    }
    private JComponent createLineChartSwingContent() {
        // Créez votre graphique de ligne ici
        JFreeChart lineChart = createLineChart();

        // Créez le panel de chart JFreeChart
        ChartPanel chartPanel = new ChartPanel(lineChart);

        // Définissez la taille préférée pour le panel de chart
        chartPanel.setPreferredSize(new java.awt.Dimension(400, 300));

        return chartPanel;
    }

    @FXML
    void buttonReturn(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/reclamationGui/DirecteurReclamationGui.fxml"));
            MainAnchorPaneBaladity.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }



}
