package main.service;

import main.product.Product;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

public class ProductTextRepository {
    private static final String FILE_PATH = "src/files/";
    public boolean PTRsave(List<Product> products, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH + fileName))) {
            for (Product p : products) {
                writer.printf(Locale.US, "%d, %s, %.2f, %s, %s, %s, %d, %s%n",
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getReceiptDate(),
                        p.getUnit(),
                        p.getManufacturer(),
                        p.getQuantity(),
                        p.getSerialNumber());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Product> PTRload(String fileName) {
        List<Product> products = new ArrayList<>();
        File file = new File(FILE_PATH + fileName);
        if (!file.exists()) {
            return products;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH + fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 8) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    LocalDate receiptDate = LocalDate.parse(parts[3]);
                    String unit = parts[4];
                    String manufacturer = parts[5];
                    int quantity = Integer.parseInt(parts[6]);
                    String serialNumber = parts[7];
                    products.add(new Product(id, name, price, receiptDate, unit, manufacturer, quantity, serialNumber));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }
}
