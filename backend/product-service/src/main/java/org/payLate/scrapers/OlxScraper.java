package org.payLate.scrapers;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OlxScraper {

    private static final String BASE_URL = "https://www.olx.bg";
    private static final String CATEGORIES_URL = "https://www.olx.bg/";

    public List<Map<String, Object>> scrapeAllProducts() throws IOException {
        List<Map<String, Object>> allCategoriesDataList = new ArrayList<>();

        Document categoriesDoc = Jsoup.connect(CATEGORIES_URL).get();

        Elements categoryElements = categoriesDoc.select("a[data-cy^='cat-']");

        for (Element category : categoryElements) {
            String categoryPath = category.attr("href");
            String categoryName = category.text();
            String categoryUrl = BASE_URL + categoryPath;

            System.out.println("Scraping category: " + categoryName + " url: " + categoryUrl);

            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("categoryName", categoryName);

            List<Map<String, Object>> productDataList = scrapeCategoryAndSubcategories(categoryUrl);
            categoryData.put("categoryProducts", productDataList);

            allCategoriesDataList.add(categoryData);
        }

        return allCategoriesDataList;
    }

    private List<Map<String, Object>> scrapeCategoryAndSubcategories(String categoryUrl) throws IOException {

        List<Map<String, Object>> allProductDataList = new ArrayList<>();
        try {
            List<Map<String, Object>> productDataList = scrapeProducts(categoryUrl);
            allProductDataList.addAll(productDataList);

            Document doc = Jsoup.connect(categoryUrl).get();

            // Select subcategory links
            Elements subCategoryElements = doc.select("a[data-testid^='sub-cat-']");

            for (Element subCategory : subCategoryElements) {
                String subCategoryPath = subCategory.attr("href");
                String subCategoryName = subCategory.text();
                String subCategoryUrl = BASE_URL + subCategoryPath;

                System.out.println("Scraping subcategory: " + subCategoryName + " at " + subCategoryUrl);

                // Scrape products in the subcategory
                List<Map<String, Object>> subCategoryProductDataList = scrapeProducts(subCategoryUrl);
                allProductDataList.addAll(subCategoryProductDataList);
            }
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + e.getMessage() + ". Skipping this URL: " + categoryUrl);
        } catch (IOException e) {
            System.err.println("Error fetching URL: " + categoryUrl + " - " + e.getMessage());
        }

        return allProductDataList;
    }

    public List<Map<String, Object>> scrapeProducts(String url) throws IOException {
        List<Map<String, Object>> productDataList = new ArrayList<>();

        Document doc = Jsoup.connect(url).get();

        Elements productElements = doc.select("div[data-cy='ad-card-title']");

        for (Element product : productElements) {
            Map<String, Object> productData = new HashMap<>();

            Element titleElement = product.selectFirst("h6.css-1wxaaza");
            if (titleElement != null) {
                productData.put("title", titleElement.text());
            }

            Element priceElement = product.selectFirst("p[data-testid='ad-price']");
            if (priceElement != null) {
                productData.put("price", priceElement.text());
            }

            Element imgElement = doc.selectFirst("div.css-gl6djm img");
            if (imgElement != null) {
                String imgUrl = imgElement.attr("src");
                productData.put("img_url", imgUrl);
            }

            Element productLinkElement = product.selectFirst("a[href]");
            if (productLinkElement != null) {
                String productLink = BASE_URL + productLinkElement.attr("href");

                String productDescription = getProductDescription(productLink);
                productData.put("product_details", productDescription);

                Map<String, String> authorDetails = getAuthorDetails(productLink);
                productData.put("author_details", authorDetails);
            }

            if (productData.size() >= 4) {
                productDataList.add(productData);
            } else {
                System.out.println("Incomplete data for product: " + productData);
            }
        }

        return productDataList;
    }

    private String getProductDescription(String productUrl) {
        try {
            Document productDoc = Jsoup.connect(productUrl).get();
            Element descriptionElement = productDoc.selectFirst("div.css-1o924a9");
            if (descriptionElement != null) {
                return descriptionElement.text();
            }
        } catch (IOException e) {
            System.out.println("Error fetching product description: " + e.getMessage());
        }
        return "";
    }

    private Map<String, String> getAuthorDetails(String productUrl) throws IOException {
        Map<String, String> authorDetails = new HashMap<>();
        Document productDoc = Jsoup.connect(productUrl).get();

        // Extracting user details
        Element userNameElement = productDoc.selectFirst("h4.css-1lcz6o7");
        if (userNameElement != null) {
            authorDetails.put("name", userNameElement.text());
        }

        Element userProfileLink = productDoc.selectFirst("a[data-testid='user-profile-link']");
        if (userProfileLink != null) {
            authorDetails.put("profile_link", BASE_URL + userProfileLink.attr("href"));
        }

        Element lastSeenElement = productDoc.selectFirst("p[data-testid='lastSeenBox']");
        if (lastSeenElement != null) {
            authorDetails.put("last_seen", lastSeenElement.text());
        }

        return authorDetails;
    }
}
