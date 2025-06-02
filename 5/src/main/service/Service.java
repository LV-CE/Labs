package main.service;
import main.resource.Product;
import java.time.LocalDate;

public class Service {

    public Product[] filterByName(Product[] products, String name) {
        Product[] temp = new Product[products.length];
        int count = 0;
        String lowerName = name.toLowerCase();

        for (Product p : products) {
            if (p.getName().toLowerCase().contains(lowerName)) {
                temp[count++] = p;
            }
        }

        return copyToExactSize(temp, count);
    }

    public Product[] filterByNameAndPrice(Product[] products, String name, double maxPrice) {
        Product[] temp = new Product[products.length];
        int count = 0;
        String lowerName = name.toLowerCase();

        for (Product p : products) {
            if (p.getName().toLowerCase().contains(lowerName) && p.getPrice() <= maxPrice) {
                temp[count++] = p;
            }
        }

        return copyToExactSize(temp, count);
    }

    public Product[] filterByExpiry(Product[] products, LocalDate date) {
        Product[] temp = new Product[products.length];
        int count = 0;

        for (Product p : products) {
            if (p.getExpiryDate().isAfter(date)) {
                temp[count++] = p;
            }
        }

        return copyToExactSize(temp, count);
    }

    private Product[] copyToExactSize(Product[] source, int size) {
        Product[] result = new Product[size];
        for (int i = 0; i < size; i++) {
            result[i] = source[i];
        }
        return result;
    }
}
