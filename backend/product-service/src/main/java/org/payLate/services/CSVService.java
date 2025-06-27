package org.payLate.services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.payLate.dto.AliExpressProductDTO;
import org.payLate.dto.AmazonProductDTO;
import org.payLate.dto.OlxProductDTO;
import org.payLate.utils.CSVRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CSVService {

    private final CSVRunner csvRunner;

    @Value("${csv.file.pathAli}")
    private String csvFilePathOne;

    @Value("${csv.file.pathOlx}")
    private String csvFilePathTwo;

    @Value("${csv.file.pathAmazon}")
    private String csvFilePathThree;

    public CSVService(CSVRunner csvRunner) {
        this.csvRunner = csvRunner;
    }

    public JSONArray getCsvDataAsJsonAli() {
        return csvRunner.readCsvToJson(csvFilePathOne);
    }

    public JSONArray getCsvDataAsJsonOlx() {
        return csvRunner.readCsvToJson(csvFilePathTwo);
    }

    public JSONArray getCsvDataAsJsonAmazon() {
        return csvRunner.readCsvToJson(csvFilePathThree);
    }

    public List<AliExpressProductDTO> readAliexpressCsvFile() {
        List<AliExpressProductDTO> aliexpressProducts = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePathOne, StandardCharsets.UTF_8))) {
            String[] values;
            csvReader.readNext();

            while ((values = csvReader.readNext()) != null) {
                AliExpressProductDTO dto = new AliExpressProductDTO();
                dto.setProductName(values[2]);
                dto.setPrice(parsePriceAli(values[5]));
                dto.setDescription(values[6]);
                dto.setImgUrl(values[7]);
                dto.setCategory(checkCategory(values[4]));
                dto.setQuantity(values[5] != null && !values[2].isEmpty() ? parseInt(values[2]) : 0);
                dto.setAuthorName(values[0]);
                dto.setAuthorUrl(values[1]);

                aliexpressProducts.add(dto);
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return aliexpressProducts;
    }

    public List<OlxProductDTO> readOlxCsvFile() {
        List<OlxProductDTO> olxProducts = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePathTwo, StandardCharsets.UTF_8))) {
            String[] values;
            csvReader.readNext();

            while ((values = csvReader.readNext()) != null) {
                OlxProductDTO dto = new OlxProductDTO();
                dto.setCategory(checkCategory(values[0]));
                dto.setProductName(values[1]);
                dto.setPrice(parseDouble(values[2]));
                dto.setImgUrl(values[3]);
                dto.setDescription(values[4]);
                dto.setQuantity(values[5] != null && !values[5].isEmpty() ? parseInt(values[5]) : 0);
                dto.setAuthorName(values[6]);

                olxProducts.add(dto);
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return olxProducts;
    }


    public List<AmazonProductDTO> readAmazonCsvFile() {
        List<AmazonProductDTO> amazonProducts = new ArrayList<>();

        JSONArray jsonArray = csvRunner.readCsvToJson(csvFilePathThree);
        jsonArray.forEach(item -> {
            if (item instanceof JSONObject jsonObject) {
                AmazonProductDTO dto = new AmazonProductDTO();

                dto.setProductName(jsonObject.optString("title", "No Product Name"));
                dto.setCategory(jsonObject.optString("categoryPageData/category", "Basic"));
                dto.setImgUrl(jsonObject.optString("galleryThumbnails/0", "img"));
                dto.setAuthorName(jsonObject.optString("seller/name", "Unknown"));
                dto.setAuthorUrl(jsonObject.optString("url", "No Url"));
                dto.setPrice(parseDouble(jsonObject.optString("price/value", "0.0")));
                dto.setDescription(jsonObject.optString("description", "This is an automatically assigned description" +
                        "."));
                dto.setQuantity(parseInt(jsonObject.optString("quantity", "0")));
                dto.setRating(parseDouble(jsonObject.optString("stars", "0.0")));

                amazonProducts.add(dto);
            }
        });

        return amazonProducts;
    }

    private Double parseDouble(String value) {
        value = value.replaceAll("[^\\d.]", "");
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.out.println("Non-numeric price encountered: " + value + ". Setting price to 0.");
            return 0.0;
        }
    }

    private Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Non-numeric quantity encountered: " + value + ". Setting quantity to 0.");
            return 0;
        }
    }

    private Double parsePriceAli(String value) {
        try {
            return Double.parseDouble(value.substring(3));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Non-numeric price encountered: " + value + ". Setting price to 0.");
            return 0.0;
        }
    }

    private String checkCategory(String category) {

        String tempCategory = "Basic";

        if (category != null && !category.isEmpty()) {
            try {
                Pattern pattern = Pattern.compile("\"theme\":\"(.*?)\"");
                Matcher matcher = pattern.matcher(category);

                if (matcher.find()) {
                    tempCategory = matcher.group(1);
                }
            } catch (Exception e) {
                System.out.println("Error extracting category!");
            }
        }

        return tempCategory;
    }
}
