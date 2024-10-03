package coordinator;

import org.openqa.selenium.WebDriverException;
import service.DateGenerator;
import service.FlightSearcher;
import service.FlightResultCleaner;
import dataAccess.DataManager;
import domain.Flight;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessCoordinator {

    private final FlightResultCleaner flightResultCleaner;
    private final DateGenerator dateGenerator;
    private final DataManager dataManager;

    public ProcessCoordinator() {
        this.flightResultCleaner = new FlightResultCleaner();
        this.dateGenerator = new DateGenerator();
        this.dataManager = new DataManager();
    }

    public void coordinateWorkFlow() {

        //Generating 250 dates from tomorrow
        List<String> generatedDates = dateGenerator.generateDates(250);

        // Deleting old flight and destination avg price data
        dataManager.deleteFlightTableData();
        dataManager.deleteAvgPriceTableData();

        // Departure airport codes
        List<String> departures = new ArrayList<>();
        departures.add("ADB");
        departures.add("SAW");
        departures.add("IST");

        // Running multiple search processes
        ExecutorService executorService = Executors.newFixedThreadPool(departures.size());

        for (String departure : departures) {
            executorService.submit(() -> {
                // Each thread will use its own flightsearcher and webdriver

                FlightSearcher flightSearcher = new FlightSearcher();

                for (String date : generatedDates) {
                    try {
                        // Flight Searching
                        List<WebElement> searchResults = flightSearcher.search(departure, date);

                        // Cleaning searched flight result
                        if (searchResults != null && !searchResults.isEmpty()) {
                            List<Flight> cleanedResults = flightResultCleaner.clean(searchResults, departure, date);

                            //Saving cleaned searched flight result
                            dataManager.saveToPostgre(cleanedResults);
                        } else {
                            System.out.println("No results for departure: " + departure + " on date: " + date);
                        }

                    } catch (Exception e) {
                        System.out.println("Exception for departure: " + departure + " on date: " + date + " -> " + e.getMessage());
                    }
                }
            });
        }

        // Shutting down after all tasks are done
        executorService.shutdown();

        // Waiting until all tasks are finished
        while (!executorService.isTerminated()) {
        }

        // Creating destination avg price data
        dataManager.createAVGTable();

        // Creating data for comparing all flight destinations with their average prices
        dataManager.comparePrices();

        // Save to excel
        dataManager.saveToExcel("Flights.xlsx");
    }
}
