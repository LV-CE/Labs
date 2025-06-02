package main.controller;
import main.resource.Product;
import main.rep.ProductRep;
import main.rep.impl.ProductBinaryRepository;
import main.rep.impl.ProductTextRepository;
import main.service.Service;
import main.view.View;
import java.io.File;
import java.time.LocalDate;
import java.util.Scanner;

public class Controller {
    private final View view = new View();
    private final Service service = new Service();
    private final ProductRep textRepo = new ProductTextRepository();
    private final ProductRep binaryRepo = new ProductBinaryRepository();
    private final Scanner scanner = new Scanner(System.in);
    private Product[] products = new Product[0];
    private final File textFile = new File("src/files/file.txt");
    private final File binaryFile = new File("src/files/file2.bin");
    public void run() {
        int option;
        do {
            view.printMenu();
            option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1 -> view.print(products);
                case 2 -> {
                    view.print("Enter name:");
                    String name = scanner.nextLine();
                    view.print(service.filterByName(products, name));
                }
                case 3 -> {
                    view.print("Enter name:");
                    String name = scanner.nextLine();
                    view.print("Enter max price:");
                    double price = Double.parseDouble(scanner.nextLine());
                    view.print(service.filterByNameAndPrice(products, name, price));
                }
                case 4 -> {
                    view.print("Enter date (yyyy-mm-dd):");
                    LocalDate date = LocalDate.parse(scanner.nextLine());
                    view.print(service.filterByExpiry(products, date));
                }
                case 5 -> textRepo.saveToFile(products, textFile);
                case 6 -> binaryRepo.saveToFile(products, binaryFile);
                case 7 -> {
                    products = textRepo.loadFromFile(textFile);
                    view.print(products);
                }
                case 8 -> {
                    products = binaryRepo.loadFromFile(binaryFile);
                    view.print(products);
                }
                case 0 -> view.print("Exiting...");
                default -> view.print("Invalid choice");
            }
        } while (option != 0);
    }
}