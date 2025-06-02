package main.service;

import main.resource.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Service {

    public List<Product> filterByName(List<Product> products, String name) {
        List<Product> result = new ArrayList<>();
        String lowerName = name.toLowerCase();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(lowerName)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Product> filterByNameAndPrice(List<Product> products, String name, double maxPrice) {
        List<Product> result = new ArrayList<>();
        String lowerName = name.toLowerCase();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(lowerName) && p.getPrice() <= maxPrice) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Product> filterByExpiry(List<Product> products, LocalDate date) {
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getExpiryDate().isAfter(date)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Product> sortByTemperatureAndPrice(List<Product> products) {
        List<Product> result = new ArrayList<>(products);
        result.sort(
                Comparator.comparingDouble(Product::getRecommendedTemperature)
                        .thenComparing(Comparator.comparingDouble(Product::getPrice).reversed())
        );
        return result;
    }

    public boolean canStoreProductAtTemperature(List<Product> products, String productName, double temperature) {
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(productName)) {
                if (Math.abs(temperature - p.getRecommendedTemperature()) <= 3.0) {
                    return true;
                }
            }
        }
        return false;
    }
}