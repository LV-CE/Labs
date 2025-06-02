package main.repository;
import main.resource.Product;
import java.util.List;

public interface Repository {
    boolean save(List<Product> products);
    List<Product> load();
}
