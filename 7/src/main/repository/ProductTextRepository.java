package main.repository;
import main.resource.Product;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

public class ProductTextRepository implements Repository {
    private static final String FILE_NAME = "src/files/file.txt";
    @Override
    public boolean save(List<Product> products) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Product p : products) {
                writer.printf(Locale.US, "%d, %s, %.2f, %s, %s, %s, %d, %.1f%n",
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getExpiryDate(),
                        p.getManufacturer(),
                        p.getUnit(),
                        p.getQuantity(),
                        p.getRecommendedTemperature());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public List<Product> load() {
        List<Product> products = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return products;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 8) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    LocalDate expiryDate = LocalDate.parse(parts[3]);
                    String unit = parts[5];
                    String manufacturer = parts[4];
                    int quantity = Integer.parseInt(parts[6]);
                    double recommendedTemperature = Double.parseDouble(parts[7]);
                    products.add(new Product(id, name, price, expiryDate, unit, manufacturer, quantity, recommendedTemperature));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }
}
