package main.view;

import main.resource.Product;

public class View {
    public void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Show all products");
        System.out.println("2. Products by name");
        System.out.println("3. Products by name and max price");
        System.out.println("4. Products with expiry after date");
        System.out.println("5. Save to text file");
        System.out.println("6. Save to binary file");
        System.out.println("7. Load from text file");
        System.out.println("8. Load from binary file");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    public void print(String message) {
        System.out.println(message);
    }

    public void print(Product[] products) {
        if (products.length == 0) {
            System.out.println("No products found.");
        } else {
            for (Product p : products) {
                System.out.println(p);
            }
        }
    }
}