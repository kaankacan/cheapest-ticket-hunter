package service;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class FlightSearcher {

    private WebDriver webDriver;

    public FlightSearcher() {
        ChromeOptions options = new ChromeOptions();
        webDriver = new ChromeDriver(options);
    }

    public List<WebElement> search(String departure, String date) {

        WebElement searchButton;
        WebElement allFlightParent = null;

        String url = String.format("https://panflights.com/en/oneway/?v2=%s_1200_%s", departure, date);
        webDriver.get(url);

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        try {
            searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='xyzdeepsearch']")));
            searchButton.click();
        } catch (TimeoutException e) {
            System.out.println("Searchbutton not found!");
        }

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='inmap_div']/div[1]/div[4]")));
            Thread.sleep(5000);
            allFlightParent = webDriver.findElement(By.xpath("//*[@id='inmap_div']/div[1]/div[4]"));
        } catch (Exception e) {
            System.out.println("Result not found!");
        }

        if (allFlightParent != null) {
            List<WebElement> allFlightChild = allFlightParent.findElements(By.xpath("*"));
            return allFlightChild;
        } else {
            return null;
        }
    }


}
