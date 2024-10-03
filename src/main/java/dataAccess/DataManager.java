package dataAccess;
import domain.DestinationAvgPrice;
import domain.Flight;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DataManager {

    private EntityManagerFactory emf;
    private EntityManager em;

    public DataManager() {
        this.emf = Persistence.createEntityManagerFactory("examplePU");
        this.em = emf.createEntityManager();
    }

    public void deleteFlightTableData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {

            em.createNativeQuery("DELETE FROM flights").executeUpdate();
            em.getTransaction().commit();
            System.out.println("Flight table data deleted!");
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void deleteAvgPriceTableData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            // Save flight entity to the database
            em.createNativeQuery("DELETE FROM DestinationAvgPrice").executeUpdate();
            em.getTransaction().commit();
            System.out.println("DestinationAvgPrice table data deleted!");
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void saveToPostgre(List<Flight> cleanedResults) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            for (Flight flight : cleanedResults) {
                em.persist(flight);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void createAVGTable() {

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            // Group by destination and calculate the average price

            List<Object[]> avgResults = em.createQuery(
                "SELECT f.destination, AVG(CAST(f.price AS double)) " +
                    "FROM Flight f " +
                    "GROUP BY f.destination"
            ).getResultList();

            // Save result to avg table
            for (Object[] result : avgResults) {
                String destination = (String) result[0];
                double avgPrice = (Double) result[1];

                DestinationAvgPrice avgTable = new DestinationAvgPrice();
                avgTable.setDestination(destination);
                avgTable.setAvgPrice(avgPrice);

                em.persist(avgTable);
            }

            em.getTransaction().commit();
            System.out.println("Average prices saved!");
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void comparePrices() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            // Compare prices between flights and destinationavgprice tables
            List<Object[]> results = em.createQuery(
                "SELECT f, avgp.avgPrice " +
                    "FROM Flight f, DestinationAvgPrice avgp " +
                    "WHERE f.destination = avgp.destination"
            ).getResultList();

            for (Object[] result : results) {
                Flight flight = (Flight) result[0];
                double avgPrice = (double) result[1];

                flight.setAveragePrice(avgPrice);

                double price = (flight.getPrice());
                double priceDiffPercentage = ((price - avgPrice) / avgPrice) * 100;
                flight.setPriceDiffPercentage(priceDiffPercentage);

                em.merge(flight);
            }


            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void saveToExcel(String filePath) {
        // Fetch data from database
        EntityManager em = emf.createEntityManager();
        List<Flight> flights = em.createQuery("SELECT f FROM Flight f", Flight.class).getResultList();
        em.close();

        // Create excell
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Flights");

        // Column headers add
        String[] columnHeaders = {"Average Price", "Departure", "Destination", "Price", "Price Diff Percentage", "Date"};
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
        }

        // Add rows
        int rowNum = 1;
        for (Flight flight : flights) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(flight.getAveragePrice());
            row.createCell(1).setCellValue(flight.getDeparture() != null ? flight.getDeparture() : "");
            row.createCell(2).setCellValue(flight.getDestination());
            row.createCell(3).setCellValue((flight.getPrice()));
            row.createCell(4).setCellValue(flight.getPriceDiffPercentage());
            row.createCell(5).setCellValue(flight.getDate());
        }

        // Save to excell
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            workbook.close();
            System.out.println("Excel file created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
