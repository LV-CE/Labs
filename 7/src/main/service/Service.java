package main.service;
import main.repository.ProductTextRepository;
import main.resource.Product;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import main.repository.Repository;
import main.repository.ProductTextRepository;
import main.repository.ProductBinaryRepository;
import java.util.*;

public class Service {
    private Repository repository;
    private List<Product> products = new ArrayList<>();
    public void addProduct(Product product) {
        products.add(product);
    }
    public boolean removeProductById(int id) {
        return products.removeIf(p -> p.getId() == id);
    }
    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(products);
    }
    public List<Product> findByName(String name) {
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    public List<Product> findByNameAndPrice(String name, double maxPrice) {
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()) && p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
    public List<Product> findByExpiry(LocalDate date) {
        return products.stream()
                .filter(p -> p.getExpiryDate().isAfter(date))
                .collect(Collectors.toList());
    }
    public List<Product> sortByTemperatureAndPrice() {
        return products.stream()
                .sorted(Comparator.comparingDouble(Product::getRecommendedTemperature)
                        .thenComparing(Comparator.comparingDouble(Product::getPrice).reversed()))
                .collect(Collectors.toList());
    }
    public boolean canStoreProductAtTemperature(String productName, double temperature) {
        return products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(productName))
                .anyMatch(p -> Math.abs(temperature - p.getRecommendedTemperature()) <= 3.0);
    }

    public Set<String> findManufacturersByPriceLessThan(double price) {
        return products.stream()
                .filter(p -> p.getPrice() < price)
                .map(Product::getManufacturer)
                .collect(Collectors.toSet());
    }

    public Map<String, List<Product>> groupByNameSortedByExpiry() {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getName,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Product::getExpiryDate))
                                        .toList()
                        )));
    }

    public Map<String, Optional<Product>> mostExpensiveByName() {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getName,
                        Collectors.maxBy(Comparator.comparing(Product::getPrice))));
    }

    public boolean saveToTextFile() {
        repository = new ProductTextRepository();
        return repository.save(products);
    }
    public boolean saveToBinFile() {
        repository = new ProductBinaryRepository();
        return repository.save(products);
    }
    public void loadFromTextFile() {
        repository = new ProductTextRepository();
        products = repository.load();
    }
    public void loadFromBinFile() {
        repository = new ProductBinaryRepository();
        products = repository.load();
    }
}
