package service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateGenerator {
    public List<String> generateDates(int dayCount) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        List<String> generatedDates = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (int i = 1; i < dayCount; i++) {
            LocalDate futureDate = today.plusDays(i);
            generatedDates.add(futureDate.format(dateTimeFormatter));
        }
        return generatedDates;
    }
}
