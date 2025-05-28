package main.repository;
import main.resource.Product;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductBinaryRepository implements Repository {
    private static final String FILE_NAME = "src/files/file2.bin";
    @Override
    public void save(List<Product> products) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(products);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    @SuppressWarnings("unchecked")
    public List<Product> load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Product>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
