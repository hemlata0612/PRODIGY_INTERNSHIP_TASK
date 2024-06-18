import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class data {

    public static void main(String[] args) 
    {
        // Replace this URL with the actual URL of the e-commerce website you want to scrape
        String url = "https://www.myntra.com/dresses/aask/aask-striped-midi-fit-and-flare-dresses/22283762/buy";
        
        try {
            Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get();

            // Print the fetched document for debugging
            System.out.println("Fetched Document: " + document.title());

            // Extract all JSON-LD script tags
            Elements jsonLdElements = document.select("script[type=application/ld+json]");
            if (jsonLdElements.isEmpty()) {
                System.err.println("No JSON-LD script tags found");
                return;
            }

            String productName = "";
            String price = "";
            String brand = "";

            for (Element jsonLdElement : jsonLdElements) {
                String jsonLd = jsonLdElement.html();
                if (jsonLd.contains("\"@type\" : \"Product\"")) {
                    System.out.println("Extracted JSON-LD for Product: " + jsonLd);

                    // Manually parse the JSON-LD data
                    productName = extractValue(jsonLd, "name");
                    price = extractValue(jsonLd, "price");
                    brand = extractNestedValue(jsonLd, "brand", "name");
                    break;
                }
            }

            // Print the scraped data for verification
            System.out.println("Product Name: " + productName);
            System.out.println("Price: " + price);
            System.out.println("Brand: " + brand);

            // Specify the CSV file path
            String csvFilePath = "product_data.csv";

            // Write the CSV header and data
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
                writer.write("Product Name,Price,Brand\n");
                writer.write(productName + "," + price + "," + brand + "\n");
                System.out.println("Data has been successfully scraped and saved to " + csvFilePath);
            } catch (IOException e) {
                System.err.println("Error writing to CSV file: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Error connecting to the website: " + e.getMessage());
        }
    }

    // Helper method to extract values from the JSON-LD string
    private static String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) {
            return "";
        }
        int colonIndex = json.indexOf(":", startIndex);
        int commaIndex = json.indexOf(",", colonIndex);
        int endIndex = json.indexOf("}", colonIndex);
        int quoteIndex = json.indexOf("\"", colonIndex + 1);

        // Determine the end index
        if (commaIndex == -1 || (endIndex != -1 && endIndex < commaIndex)) {
            commaIndex = endIndex;
        }
        if (quoteIndex != -1 && quoteIndex < commaIndex) {
            commaIndex = json.indexOf("\"", quoteIndex + 1) + 1;
        }

        String value = json.substring(colonIndex + 1, commaIndex).trim();
        // Remove any surrounding quotes
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value;
    }
    // Helper method to extract nested values from the JSON-LD string
    private static String extractNestedValue(String json, String parentKey, String childKey) {
        int parentStartIndex = json.indexOf("\"" + parentKey + "\"");
        if (parentStartIndex == -1) {
            return "";
        }

        int braceIndex = json.indexOf("{", parentStartIndex);
        if (braceIndex == -1) {
            return "";
        }

        int endBraceIndex = json.indexOf("}", braceIndex);
        if (endBraceIndex == -1) {
            return "";
        }

        String nestedJson = json.substring(braceIndex, endBraceIndex + 1);
        return extractValue(nestedJson, childKey);
    }
}

//javac -cp .;.\jsoup-1.17.2.jar task5.java

//java -cp .;.\jsoup-1.17.2.jar data