package service;

import domain.Flight;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlightResultCleaner {

    public List<Flight> clean(List<WebElement> searchResults, String departure, String date) {
        List<Flight> flightsList = new ArrayList<>();

        for (WebElement searchResult : searchResults) {

            String destination = searchResult.findElement(By.xpath("div/div/span[1]")).getAttribute("innerHTML");

            String price = searchResult.findElement(By.xpath("div/div/span[2]")).getAttribute("innerHTML");

            double onlyPrice = Double.parseDouble(cleanPrice(price));

            Flight flight = new Flight(departure, destination, onlyPrice, date);
            flightsList.add(flight);
        }
        return flightsList;
    }

    private String cleanPrice(String price) {

        String cleanedPrice = price.replace("span", "")
            .replace("<", "")
            .replace(">", "")
            .replace("\\", "")
            .replace("/", "")
            .replace(" class=\"strike-through\"", "")
            .replace("Non-stop,", "")
            .replace(" TL", "");


        // Count the commas, if there are 2 commas, take the part after the first comma and remove other commas
        // If there are not 2 commas, remove all commas
        Pattern pattern = Pattern.compile(",");
        Matcher matcher = pattern.matcher(cleanedPrice);

        int commaCount = 0;
        while (matcher.find()) {
            commaCount++;
        }

        String finalPrice;

        if (commaCount == 2) {

            int firstCommaIndex = cleanedPrice.indexOf(",");
            String pricePart = cleanedPrice.substring(firstCommaIndex + 1);

            finalPrice = pricePart.replace(",", "");
        } else {

            finalPrice = cleanedPrice.replace(",", "");
        }

        return finalPrice;
    }
}
