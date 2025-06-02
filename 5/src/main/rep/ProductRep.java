package main.rep;
import main.resource.Product;
import java.io.File;

public interface ProductRep extends Repository<Product> {
    void saveToFile(Product[] array, File file);
    Product[] loadFromFile(File file);
}