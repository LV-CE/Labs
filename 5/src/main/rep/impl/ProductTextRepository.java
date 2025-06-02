package main.rep.impl;
import main.resource.Product;
import main.rep.ProductRep;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductTextRepository implements ProductRep {
    @Override
    public void saveToFile(Product[] array, File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (Product p : array) {
                writer.printf("%d,%s,%.2f,%s,%s,%d\n",
                        p.getId(), p.getName(), p.getPrice(), p.getExpiryDate(), p.getUnit(), p.getQuantity());
            }
        } catch (IOException e) {
            System.out.println("Error writing to text file: " + e.getMessage());
        }
    }
    @Override
    public Product[] loadFromFile(File file) {
        List<Product> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    LocalDate expiry = LocalDate.parse(parts[3]);
                    String unit = parts[4];
                    int qty = Integer.parseInt(parts[5]);
                    list.add(new Product(id, name, price, expiry, unit, qty));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from text file: " + e.getMessage());
        }
        return list.toArray(new Product[0]);
    }
    @Override
    public void outputArray(Product[] array, File file) {
        saveToFile(array, file);
    }
    @Override
    public void outputArray(Product[] array, String fileName) {
        outputArray(array, new File(fileName));
    }
    @Override
    public Product[] readArray(File file) {
        return loadFromFile(file);
    }
    @Override
    public Product[] readArray(String fileName) {
        return readArray(new File(fileName));
    }
}