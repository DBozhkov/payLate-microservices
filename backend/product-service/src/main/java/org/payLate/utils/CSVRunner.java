package org.payLate.utils;

import com.opencsv.CSVReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class CSVRunner {
    public JSONArray readCsvToJsonFromClasspath(String classpathLocation) {
        JSONArray jsonArray = new JSONArray();
        try {
            ClassPathResource resource = new ClassPathResource(classpathLocation);
            try (
                    InputStream is = resource.getInputStream();
                    InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                    CSVReader csvReader = new CSVReader(reader)
            ) {
                String[] headers = csvReader.readNext();
                if (headers == null) return jsonArray;

                String[] values;
                while ((values = csvReader.readNext()) != null) {
                    JSONObject obj = new JSONObject();
                    for (int i = 0; i < headers.length && i < values.length; i++) {
                        obj.put(headers[i], values[i]);
                    }
                    jsonArray.put(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONArray readCsvToJson(Reader reader) {
        JSONArray jsonArray = new JSONArray();
        try (CSVReader csvReader = new CSVReader(reader)) {
            String[] headers = csvReader.readNext();
            if (headers == null) return jsonArray;
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                JSONObject obj = new JSONObject();
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    obj.put(headers[i], values[i]);
                }
                jsonArray.put(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}