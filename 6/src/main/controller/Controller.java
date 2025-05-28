package main.controller;
import main.resource.Product;
import main.service.Service;
import main.view.View;
import main.repository.Repository;
import main.repository.ProductTextRepository;
import main.repository.ProductBinaryRepository;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private final Service service = new Service();
    private final View view = new View();
    private Repository repository;
    private List<Product> products = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    public void run() {
        repository = new ProductTextRepository();
        products = repository.load();
        boolean exit = false;
        while (!exit) {
            view.printMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    view.print(products);
                    break;
                case "2":
                    view.print("Enter product name to filter:");
                    String name = scanner.nextLine();
                    view.print(service.filterByName(products, name));
                    break;
                case "3":
                    view.print("Enter product name:");
                    String name3 = scanner.nextLine();
                    view.print("Enter max price:");
                    double price3 = readDouble();
                    view.print(service.filterByNameAndPrice(products, name3, price3));
                    break;
                case "4":
                    view.print("Enter date (YYYY-MM-DD, left empty for today's date):");
                    LocalDate date = readDate();
                    if (date != null) {
                        view.print(service.filterByExpiry(products, date));
                    }
                    break;
                case "5":
                    view.print(service.sortByTemperatureAndPrice(products));
                    break;
                case "6":
                    view.print("Enter product name:");
                    String name6 = scanner.nextLine();
                    view.print("Enter temperature:");
                    double temp = readDouble();
                    boolean canStore = service.canStoreProductAtTemperature(products, name6, temp);
                    view.print(canStore ? "Product can be stored at this temperature." : "Cannot store product at this temperature.");
                    break;
                case "7":
                    repository = new ProductTextRepository();
                    repository.save(products);
                    view.print("Saved to text file.");
                    break;
                case "8":
                    repository = new ProductBinaryRepository();
                    repository.save(products);
                    view.print("Saved to binary file.");
                    break;
                case "9":
                    repository = new ProductTextRepository();
                    products = repository.load();
                    view.print("Loaded from text file.");
                    break;
                case "10":
                    repository = new ProductBinaryRepository();
                    products = repository.load();
                    view.print("Loaded from binary file.");
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    view.print("Invalid option, try again.");
            }
        }
    }
    private double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                view.print("Invalid number, try again:");
            }
        }
    }
    private LocalDate readDate() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                return LocalDate.now();
            }
        }
    }
}
