package main.product;
import java.io.Serializable;
import java.time.LocalDate;

public class Product implements Serializable {
    private int id;
    private String name;
    private double price;
    private LocalDate receiptDate;
    private String unit;
    private String manufacturer;
    private int quantity;
    private String serialNumber;

    public Product(int id, String name, double price, LocalDate receiptDate, String unit,
                   String manufacturer, int stockQuantity, String serialNumber) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.receiptDate = receiptDate;
        this.unit = unit;
        this.manufacturer = manufacturer;
        this.quantity = stockQuantity;
        this.serialNumber = serialNumber;
    }

    public int getId() { return id; }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public LocalDate getReceiptDate() { return receiptDate; }
    public String getUnit() { return unit; }
    public String getManufacturer() { return manufacturer; }
    public int getQuantity() { return quantity; }
    public String getSerialNumber() { return serialNumber; }
}
