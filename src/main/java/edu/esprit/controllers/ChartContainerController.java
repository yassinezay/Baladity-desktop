package edu.esprit.controllers;

import edu.esprit.entities.EndUser;
import edu.esprit.entities.Tache;
import edu.esprit.services.EtatTache;
import edu.esprit.services.ServiceTache;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChartContainerController {
    private final ServiceTache ST = new ServiceTache();
    @FXML
    private AnchorPane chartContainer;

    public void displayChart(Set<Tache> userTasks) {
        Set<Tache> allTasks = ST.getTachesByState(EtatTache.DONE);
        Map<EndUser, Long> userTaskCounts = allTasks.stream()
                .collect(Collectors.groupingBy(Tache::getUser, Collectors.counting()));
        Map<EndUser, Long> sortedUserTaskCounts = userTaskCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        DefaultPieDataset dataset = new DefaultPieDataset();
        sortedUserTaskCounts.forEach((user, count) -> {
            dataset.setValue(user.getNom() + " (" + count + ")", (Number) count); // Including task count in label
        });
        JFreeChart chart = ChartFactory.createPieChart(
                "Top 10 Utilisateurs avec plus taches Done",
                dataset,
                true,
                true,
                false);
        // Customizations for chart
        chart.setBackgroundPaint(new Color(240, 240, 240)); // Background color
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 18)); // Title font
        chart.getTitle().setPaint(Color.DARK_GRAY); // Title color
        chart.getLegend().setItemFont(new Font("Arial", Font.PLAIN, 12)); // Legend font
        chart.getLegend().setBorder(0, 0, 0, 0); // Remove legend border
        // Adding the chart to SwingNode
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        chartPanel.setBackground(Color.WHITE); // Chart panel background color
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(chartPanel);
        // Clear existing content and add the new chart
        chartContainer.getChildren().clear();
        chartContainer.getChildren().add(swingNode);
    }
}