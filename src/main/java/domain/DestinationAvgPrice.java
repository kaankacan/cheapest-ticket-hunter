package domain;
import jakarta.persistence.*;

@Table(name = "DestinationAvgPrice")
@Entity
public class DestinationAvgPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String destination;
    private double avgPrice;

    public DestinationAvgPrice() {
    }

    public DestinationAvgPrice(String destination, double avgPrice) {
        this.destination = destination;
        this.avgPrice = avgPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }
}
