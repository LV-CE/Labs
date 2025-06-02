package main.resource;
import java.io.Serializable;
import java.time.LocalDate;

public class Product implements Serializable {
    private int id;
    private String name;
    private double price;
    private LocalDate expiryDate;
    private String unit;
    private int quantity;
    private double recommendedTemperature;
    public Product(int id, String name, double price, LocalDate expiryDate, String unit, int quantity, double recommendedTemperature) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.expiryDate = expiryDate;
        this.unit = unit;
        this.quantity = quantity;
        this.recommendedTemperature = recommendedTemperature;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getUnit() { return unit; }
    public int getQuantity() { return quantity; }
    public double getRecommendedTemperature() { return recommendedTemperature; }
    @Override
    public String toString() {
        return String.format("[id = %-2d name: %-15s price: %-6.2f expiry: %-10s unit: %-6s qty: %-3d temp: %-5.1f]",
                id, name, price, expiryDate, unit, quantity, recommendedTemperature);
    }
}
