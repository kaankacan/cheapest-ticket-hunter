package domain;

import jakarta.persistence.*;

@Table(name = "flights")
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String departure;
    String destination;
    double price;
    double averagePrice;
    double priceDiffPercentage;

    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Flight() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public double getPriceDiffPercentage() {
        return priceDiffPercentage;
    }

    public void setPriceDiffPercentage(double priceDiffPercentage) {
        this.priceDiffPercentage = priceDiffPercentage;
    }

    public Flight(String departure, String destination, double price, String date) {
        this.departure = departure;
        this.destination = destination;
        this.price = price;
        this.date = date;
    }
}
