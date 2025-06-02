package main.view;
import main.resource.Product;
import java.util.List;

public class View {
    public void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Show all products");
        System.out.println("2. Products by name");
        System.out.println("3. Products by name and max price");
        System.out.println("4. Products with expiry after date");
        System.out.println("5. Products sorted by recommended temperature and price");
        System.out.println("6. Check if product can be stored at given temperature");
        System.out.println("7. Save to text file");
        System.out.println("8. Save to binary file");
        System.out.println("9. Load from text file");
        System.out.println("10. Load from binary file");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }
    public void print(String message) {
        System.out.println(message);
    }
    public void print(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            for (Product p : products) {
                System.out.println(p);
            }
        }
    }
}