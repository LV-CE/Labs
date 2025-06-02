package scripts;
import java.io.Serializable;
import java.time.LocalDate;

public class Product implements Serializable {
    private int id;
    private String name;
    private double price;
    private LocalDate expiryDate;
    private String unit;
    private String manufacturer;
    private int quantity;
    private double recommendedTemperature;

    public Product(int id, String name, double price, LocalDate expiryDate, String unit,
                   String manufacturer, int stockQuantity, double recommendedTemperature) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.expiryDate = expiryDate;
        this.unit = unit;
        this.manufacturer = manufacturer;
        this.quantity = stockQuantity;
        this.recommendedTemperature = recommendedTemperature;
    }

    public int getId() { return id; }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getUnit() { return unit; }
    public String getManufacturer() { return manufacturer; }
    public int getQuantity() { return quantity; }
    public double getRecommendedTemperature() { return recommendedTemperature; }

    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    @Override
    public String toString() {
        return String.format("ID: %d | Назва: %s | Ціна: %.2f | Термін: %s дн. | Од.вим: %s | Виробник: %s | К-сть: %d | Темп: %.1f°C",
                id, name, price, expiryDate, unit, manufacturer, quantity, recommendedTemperature);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        Product p = (Product) obj;
        return id == p.id;
    }
}
