package org.payLate.services;

import org.payLate.scrapers.OlxScraper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class OlxScraperService {

    private final OlxScraper olxScraper = new OlxScraper();

    @Value("${csv.file.pathOlx}")
    private String csvFilePath;

    public void scrapeAndSaveToCsv() {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(csvFilePath), StandardCharsets.UTF_8)) {
            // Write the header, including the category
            writer.write("Category,Title,Price,Image URL,Product Details,Author Name,Author Profile Link,Author Last Seen\n");

            // Fetch all category data
            List<Map<String, Object>> scrapedData = olxScraper.scrapeAllProducts();
            if (scrapedData.isEmpty()) {
                System.out.println("No products scraped. Exiting CSV writing process.");
                return;
            }

            for (Map<String, Object> categoryData : scrapedData) {
                String categoryName = (String) categoryData.get("categoryName");
                List<Map<String, Object>> categoryProducts = (List<Map<String, Object>>) categoryData.get("categoryProducts");

                if (categoryProducts != null) {
                    for (Map<String, Object> productData : categoryProducts) {
                        writeProductToCsv(writer, categoryName, productData);
                    }
                }
            }

            System.out.println("CSV file has been updated with scraped data.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeProductToCsv(Writer writer, String categoryName, Map<String, Object> productData) throws IOException {
        // Retrieve product details
        String title = (String) productData.get("title");
        String price = (String) productData.get("price");
        String imgUrl = (String) productData.get("img_url");
        String productDetails = (String) productData.get("product_details");

        // Retrieve author details
        Map<String, String> authorDetails = (Map<String, String>) productData.get("author_details");
        String authorName = authorDetails != null ? authorDetails.get("name") : "";
        String authorProfileLink = authorDetails != null ? authorDetails.get("profile_link") : "";
        String authorLastSeen = authorDetails != null ? authorDetails.get("last_seen") : "";

        // Write the row with category included
        writer.write(escapeCsvField(categoryName) + ","
                + escapeCsvField(title) + ","
                + escapeCsvField(price) + ","
                + escapeCsvField(imgUrl) + ","
                + escapeCsvField(productDetails) + ","
                + escapeCsvField(authorName) + ","
                + escapeCsvField(authorProfileLink) + ","
                + escapeCsvField(authorLastSeen) + "\n");
    }

    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        return "\"" + field.replace("\"", "\"\"") + "\"";
    }
}