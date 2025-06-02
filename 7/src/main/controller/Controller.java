package main.controller;
import main.resource.Product;
import main.service.Service;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Controller {
    private final Service service = new Service();
    private final Scanner scanner = new Scanner(System.in);
    public void run() {
        boolean exit = false;
        service.loadFromTextFile();
        while (!exit) {
            System.out.println("\nМеню:");
            System.out.println("1. Додати продукт");
            System.out.println("2. Видалити продукт за ID");
            System.out.println("3. Показати всі продукти");
            System.out.println("4. Пошук за найменуванням");
            System.out.println("5. Пошук за найменуванням і ціною");
            System.out.println("6. Пошук за терміном зберігання");
            System.out.println("7. Сортування за температурою і ціною");
            System.out.println("8. Перевірка можливості зберігання");
            System.out.println("9. Список виробників за ціною");
            System.out.println("10. Map — продукти за найменуванням");
            System.out.println("11. Map — найкоштовніший продукт за найменуванням");
            System.out.println("12. Зберегти в текстовий файл");
            System.out.println("13. Зберегти в бінарний файл");
            System.out.println("14. Загрузити з текстового файлу");
            System.out.println("15. Загрузити з бінарного файлу");
            System.out.println("0. Вийти");
            System.out.print("Вибір: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": addProduct(); break;
                case "2": removeProduct(); break;
                case "3": showAllProducts(); break;
                case "4": findByName(); break;
                case "5": findByNameAndPrice(); break;
                case "6": findByExpiryDate(); break;
                case "7": sortByTempAndPrice(); break;
                case "8": checkStorageTemp(); break;
                case "9": showManufacturersByPrice(); break;
                case "10": showGroupedByName(); break;
                case "11": showMostExpensiveByName(); break;
                case "12": saveToText(); break;
                case "13": saveToBin(); break;
                case "14": service.loadFromTextFile(); break;
                case "15": service.loadFromBinFile(); break;
                case "0": exit = true; break;
                default:
                    System.out.print("Неправильний ввод, повторіть ввод.");
            }
        }
    }
    private void addProduct() {
        try {
            System.out.print("ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Найменування: ");
            String name = scanner.nextLine();

            System.out.print("Ціна: ");
            double price = Double.parseDouble(scanner.nextLine());

            System.out.print("Дата закінчення терміну (yyyy-MM-dd): ");
            LocalDate expiry = LocalDate.parse(scanner.nextLine());

            System.out.print("Одиниця вимірювання: ");
            String unit = scanner.nextLine();

            System.out.print("Виробник: ");
            String manufacturer = scanner.nextLine();

            System.out.print("Кількість на складі: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            System.out.print("Рекомендована температура зберігання: ");
            double temp = Double.parseDouble(scanner.nextLine());

            Product product = new Product(id, name, price, expiry, unit, manufacturer, quantity, temp);
            service.addProduct(product);

            System.out.println("Продукт додано.");
        } catch (Exception e) {
            System.out.println("Помилка введення даних. Спробуйте ще раз.");
        }
    }

    private void removeProduct() {
        try {
            System.out.print("ID для видалення: ");
            int id = Integer.parseInt(scanner.nextLine());
            if (service.removeProductById(id)) {
                System.out.println("Видалено.");
            } else {
                System.out.println("Продукт не знайдено.");
            }
        } catch (Exception e) {
            System.out.println("Помилка введення ID.");
        }
    }

    private void showAllProducts() {
        List<Product> all = service.getAllProducts();
        if (all.isEmpty()) {
            System.out.println("Список порожній.");
        } else {
            all.forEach(System.out::println);
        }
    }

    private void findByName() {
        System.out.print("Введіть найменування: ");
        String name = scanner.nextLine();
        List<Product> found = service.findByName(name);
        if (found.isEmpty()) {
            System.out.println("Не знайдено.");
        } else {
            found.forEach(System.out::println);
        }
    }

    private void findByNameAndPrice() {
        try {
            System.out.print("Найменування: ");
            String name = scanner.nextLine();
            System.out.print("Максимальна ціна: ");
            double maxPrice = Double.parseDouble(scanner.nextLine());

            List<Product> found = service.findByNameAndPrice(name, maxPrice);
            if (found.isEmpty()) {
                System.out.println("Не знайдено.");
            } else {
                found.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Помилка введення ціни.");
        }
    }

    private void findByExpiryDate() {
        LocalDate date = LocalDate.now();
        System.out.print("Дата (yyyy-MM-dd)(оставьте пустою для сьогоднішньої дати): ");
        try {
            date = LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            System.out.println("Помилка введення дати. Використана сьогоднішня дата");
        }
        List<Product> found = service.findByExpiry(date);
        if (found.isEmpty()) {
            System.out.println("Не знайдено.");
        } else {
            found.forEach(System.out::println);
        }
    }

    private void sortByTempAndPrice() {
        List<Product> sorted = service.sortByTemperatureAndPrice();
        if (sorted.isEmpty()) {
            System.out.println("Список порожній.");
        } else {
            sorted.forEach(System.out::println);
        }
    }

    private void checkStorageTemp() {
        try {
            System.out.print("Найменування товару: ");
            String name = scanner.nextLine();
            System.out.print("Температура зберігання: ");
            double temp = Double.parseDouble(scanner.nextLine());

            boolean canStore = service.canStoreProductAtTemperature(name, temp);
            if (canStore) {
                System.out.println("Товар можна зберігати при цій температурі.");
            } else {
                System.out.println("Товар не можна зберігати при цій температурі або товар не знайдено.");
            }
        } catch (Exception e) {
            System.out.println("Помилка введення даних.");
        }
    }

    private void showManufacturersByPrice() {
        try {
            System.out.print("Максимальна ціна: ");
            double price = Double.parseDouble(scanner.nextLine());

            Set<String> manufacturers = service.findManufacturersByPriceLessThan(price);
            if (manufacturers.isEmpty()) {
                System.out.println("Не знайдено виробників.");
            } else {
                manufacturers.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Помилка введення ціни.");
        }
    }

    private void showGroupedByName() {
        Map<String, List<Product>> map = service.groupByNameSortedByExpiry();
        if (map.isEmpty()) {
            System.out.println("Порожній список.");
            return;
        }
        map.forEach((name, list) -> {
            System.out.println("Найменування: " + name);
            list.forEach(System.out::println);
        });
    }

    private void showMostExpensiveByName() {
        Map<String, Optional<Product>> map = service.mostExpensiveByName();
        if (map.isEmpty()) {
            System.out.println("Порожній список.");
            return;
        }
        map.forEach((name, productOpt) -> {
            System.out.println("Найменування: " + name);
            productOpt.ifPresent(
                    System.out::println
            );
        });
    }

    private void saveToText() {
        boolean saved = service.saveToTextFile();
        if (saved) {
            System.out.println("Дата успішно збережена!");
        }else{
            System.out.println("Збереження дати не вийшло!");
        }
    }
    private void saveToBin() {
        boolean saved = service.saveToBinFile();
        if (saved) {
            System.out.println("Дата успішно збережена!");
        }else{
            System.out.println("Збереження дати не вийшло!");
        }
    }
}