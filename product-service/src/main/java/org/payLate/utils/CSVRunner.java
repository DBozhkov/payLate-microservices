package org.payLate.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Component
public class CSVRunner {

    public JSONArray readCsvToJson(String filePath) {
        JSONArray jsonArray = new JSONArray();

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            List<String[]> csvData = csvReader.readAll();
            String[] headers = csvData.get(0);

            for (int i = 1; i < csvData.size(); i++) {
                String[] row = csvData.get(i);
                JSONObject jsonObject = new JSONObject();

                for (int j = 0; j < headers.length; j++) {
                    jsonObject.put(headers[j], row[j]);
                }
                jsonArray.put(jsonObject);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }
}
