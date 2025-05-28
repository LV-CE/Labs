package main.service;
import main.resource.Product;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    public List<Product> filterByName(List<Product> products, String name) {
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    public List<Product> filterByNameAndPrice(List<Product> products, String name, double maxPrice) {
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()) && p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
    public List<Product> filterByExpiry(List<Product> products, LocalDate date) {
        return products.stream()
                .filter(p -> p.getExpiryDate().isAfter(date))
                .collect(Collectors.toList());
    }
    public List<Product> sortByTemperatureAndPrice(List<Product> products) {
        return products.stream()
                .sorted(Comparator.comparingDouble(Product::getRecommendedTemperature)
                        .thenComparing(Comparator.comparingDouble(Product::getPrice).reversed()))
                .collect(Collectors.toList());
    }
    public boolean canStoreProductAtTemperature(List<Product> products, String productName, double temperature) {
        return products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(productName))
                .anyMatch(p -> temperature >= p.getRecommendedTemperature());
    }
}
