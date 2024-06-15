package edu.esprit.services;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class chatbot {
    private String ta= "";

    private Map<String, String> questionResponseMap = new HashMap<>();

    public chatbot() {
        loadResponsesFromExcel();
    }

    public String repondre(String message) {
        try {
            return chatTest(message);
        } catch (IOException e) {
            e.printStackTrace();
            return "Une erreur s'est produite lors du traitement de la demande.";
        }
    }

    private void loadResponsesFromExcel() {
        try {
            FileInputStream file = new FileInputStream("src/main/resources/datasets/dataset.xlsx");
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell questionCell = row.getCell(0);
                Cell responseCell = row.getCell(1);

                if (questionCell != null && responseCell != null) {
                    String question = questionCell.getStringCellValue().toLowerCase();
                    String response = responseCell.getStringCellValue();

                    questionResponseMap.put(question, response);
                }
            }

            workbook.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception de manière appropriée en fonction de votre application
        }
    }

    public String chatTest(String userInput) throws IOException {
        String mostSimilarQuestion = findMostSimilarQuestion(userInput);

        if (!mostSimilarQuestion.isEmpty()) {
            String response = questionResponseMap.getOrDefault(mostSimilarQuestion, "Je suis désolé, je ne comprends pas.");
            return response;
        } else {
            return "Je suis désolé, je ne comprends pas.";
        }
    }

    private String findMostSimilarQuestion(String userInput) {
        String mostSimilarQuestion = "";
        int minDistance = Integer.MAX_VALUE;

        for (String question : questionResponseMap.keySet()) {
            int distance = LevenshteinDistance.getDefaultInstance().apply(userInput, question);
            if (distance < minDistance) {
                minDistance = distance;
                mostSimilarQuestion = question;
            }
        }

        return mostSimilarQuestion;
    }
    public void addText(String str) {
        ta = ta + str;
    }
    public String firstText() {
        addText("-->baladity: Good day! Welcome to Baladity, How can we assist you today? \n");
        return this.ta;
    }
}
