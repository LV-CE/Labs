package main.repository;
import main.resource.Product;
import java.util.List;

public interface Repository {
    void save(List<Product> products);
    List<Product> load();
}
