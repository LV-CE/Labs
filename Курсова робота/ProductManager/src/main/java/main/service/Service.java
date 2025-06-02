package main.service;

import main.product.Product;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.*;
import java.util.Random;

public class Service {
    private List<Product> products = new ArrayList<>();
    private List<Product> editedProducts = new ArrayList<>();
    private List<Product> receiptList = new ArrayList<>();
    private ProductTextRepository PTR = new ProductTextRepository();
    private ProductBinaryRepository PBR = new ProductBinaryRepository();
    public int IDGenerator(boolean mainTable) {
        if (mainTable) {return products.stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0) + 1;} else {
            Random random = new Random();
            return 1_000_000 + random.nextInt(9000000);}
    }
    public void addProduct(Product product, boolean mainTable) {
        if (mainTable) {
            products.add(product);
            resetAllProducts();
            } else receiptList.add(product);
    }
    public boolean removeProductById(int id, boolean mainTable) {
        if (mainTable) return products.removeIf(p -> p.getId() == id);
        else return receiptList.removeIf(p -> p.getId() == id);
    }
    public List<Product> getAllProducts(boolean mainTable) {
        if (mainTable) return Collections.unmodifiableList(editedProducts);
        else return Collections.unmodifiableList(receiptList);
    }
    private void sortByID(List<Product> productx) {
        productx.sort(Comparator.comparingInt(Product::getId));
        int newId = 1;
        for (Product product : productx) {
            product.setId(newId++);
        }
    }
    public void resetAllProducts() {sortByID(products); editedProducts = products;}

    public List<Product> findByName(String name) {
        editedProducts = editedProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        return editedProducts;
    }
    public List<Product> findByPrice(double maxPrice) {
        editedProducts = editedProducts.stream()
                .filter(p -> p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
        return editedProducts;
    }
    public List<Product> findByReceiptDate(LocalDate date) {
        editedProducts = editedProducts.stream()
                .filter(p -> p.getReceiptDate().isBefore(date))
                .collect(Collectors.toList());
        return editedProducts;
    }
    public List<Product> findByType(String type) {
        editedProducts = editedProducts.stream()
                .filter(p -> p.getUnit().toLowerCase().contains(type.toLowerCase()))
                .collect(Collectors.toList());
        return editedProducts;
    }
    public List<Product> findByManufacturer(String manufacturer) {
        editedProducts = editedProducts.stream()
                .filter(p -> p.getManufacturer().toLowerCase().contains(manufacturer.toLowerCase()))
                .collect(Collectors.toList());
        return editedProducts;
    }
    public List<Product> findBySerialNumber(String serialNumber) {
        editedProducts = editedProducts.stream()
                .filter(p -> p.getSerialNumber().contains(serialNumber))
                .collect(Collectors.toList());
        return editedProducts;
    }
    public List<Product> sortByReceiptDate() {
        editedProducts = editedProducts.stream()
                .sorted(Comparator.comparing(Product::getReceiptDate))
                .collect(Collectors.toList());
        return editedProducts;
    }
    public List<Product> sortByManufacturer() {
        editedProducts = editedProducts.stream()
                .sorted(Comparator.comparing(Product::getManufacturer))
                .collect(Collectors.toList());
        return editedProducts;
    }

    public List<Product> sortByNameSortedByReceipt() {
        editedProducts = editedProducts.stream()
                .sorted(Comparator.comparing(Product::getName)
                        .thenComparing(Product::getReceiptDate))
                .collect(Collectors.toList());
        return editedProducts;
    }

    public List<Product> mostExpensiveByName() {
        editedProducts = editedProducts.stream()
                .collect(Collectors.groupingBy(Product::getName,
                        Collectors.maxBy(Comparator.comparing(Product::getPrice))))
                .values().stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
        return editedProducts;
    }

    public boolean saveToTextFile(String fileName, boolean mainTable) {
        if (mainTable) return PTR.PTRsave(products, fileName);
        else return PTR.PTRsave(receiptList, fileName);
    }
    public boolean saveToBinFile(String fileName, boolean mainTable) {
        if (mainTable) return PBR.PBRsave(products, fileName);
        else return PBR.PBRsave(receiptList, fileName);
    }
    public boolean loadFromTextFile(String fileName, boolean mainTable) {
        List<Product> newproducts = PTR.PTRload(fileName);
        if (!newproducts.isEmpty()) {
            if (mainTable) products = newproducts;
            else receiptList = newproducts;
            return true;} else {return false;}
    }
    public boolean loadFromBinFile(String fileName, boolean mainTable) {
        List<Product> newproducts = PBR.PBRload(fileName);
        if (!newproducts.isEmpty()) {
            if (mainTable) products = newproducts;
            else receiptList = newproducts;
            return true;} else {return false;}
    }
}
