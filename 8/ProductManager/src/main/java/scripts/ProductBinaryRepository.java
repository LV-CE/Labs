package scripts;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductBinaryRepository {
    private static final String FILE_PATH = "src/files/";
    public boolean PBRsave(List<Product> products, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH + fileName))) {
            oos.writeObject(products);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public List<Product> PBRload(String fileName) {
        File file = new File(FILE_PATH + fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH + fileName))) {
            return (List<Product>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
