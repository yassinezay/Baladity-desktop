package edu.esprit.services;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HateSpeechChecker {

    private List<String> badWordsList = new ArrayList<>();

    public HateSpeechChecker() {
        loadBadWordsFromExcel();
    }

    private void loadBadWordsFromExcel() {
        try {
            FileInputStream file = new FileInputStream("src/main/resources/datasets/hatespeech.xlsx");
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cell = row.getCell(0); // Suppose que les mots clés sont dans la première colonne

                if (cell != null) {
                    String badWord = cell.getStringCellValue();
                    badWordsList.add(badWord);
                }
            }

            workbook.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception de manière appropriée en fonction de votre application
        }
    }

    public boolean containsBadWord(String text) {
        for (String badWord : badWordsList) {
            if (text.toLowerCase().contains(badWord.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
