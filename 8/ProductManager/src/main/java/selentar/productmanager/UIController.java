package selentar.productmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import scripts.Product;
import scripts.Service;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;
import java.util.ArrayList;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class UIController {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> idCol;
    @FXML
    private TableColumn<Product, String> nameCol;
    @FXML
    private TableColumn<Product, Double> priceCol;
    @FXML
    private TableColumn<Product, LocalDate> expiryCol;
    @FXML
    private TableColumn<Product, String> unitCol;
    @FXML
    private TableColumn<Product, String> manufacturerCol;
    @FXML
    private TableColumn<Product, Integer> quantityCol;
    @FXML
    private TableColumn<Product, Double> tempCol;
    @FXML
    private ComboBox<String> operationsChoiceBox;

    private final Service service = new Service();

    @FXML
    public void initialize() {
        // Ініціалізація колонок таблиці
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        priceCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        expiryCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getExpiryDate()));
        unitCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUnit()));
        manufacturerCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getManufacturer()));
        quantityCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        tempCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getRecommendedTemperature()).asObject());

        // Завантаження початкових даних
        service.loadFromTextFile("file.txt");
        updateTable(service.getAllProducts());

        // Обробка вибору операції
        operationsChoiceBox.setOnAction(event -> handleOperationSelection());
    }

    private void updateTable(List<Product> products) {
        ObservableList<Product> data = FXCollections.observableArrayList(products);
        productTable.setItems(data);
        productTable.refresh();
    }

    private void handleOperationSelection() {
        String selectedOperation = operationsChoiceBox.getValue();
        if (selectedOperation == null) return;

        switch (selectedOperation) {
            case "1. Пошук за найменуванням" -> {
                String name = promptForInput("Введіть назву продукту:");
                if (name != null) {
                    List<Product> results = service.findByName(name);
                    updateTable(results);
                }
            }
            case "2. Пошук за найменуванням і ціною" -> {
                String name = promptForInput("Введіть назву продукту:");
                String priceStr = promptForInput("Введіть максимальну ціну:");
                if (name != null && priceStr != null) {
                    try {
                        double price = Double.parseDouble(priceStr);
                        List<Product> results = service.findByNameAndPrice(name, price);
                        updateTable(results);
                    } catch (NumberFormatException e) {
                        showAlert("Помилка", "Невірний формат ціни.");
                    }
                }
            }
            case "3. Пошук за терміном зберігання" -> {
                String dateStr = promptForInput("Введіть дату (РРРР-ММ-ДД) або залиште пустим для сьогоднішнього:");
                if (dateStr != null) {
                    try {
                        LocalDate date = LocalDate.parse(dateStr);
                        List<Product> results = service.findByExpiry(date);
                        updateTable(results);
                    } catch (Exception e) {
                        List<Product> results = service.findByExpiry(LocalDate.now());
                        updateTable(results);
                    }
                }
            }
            case "4. Сортування за температурою і ціною" -> {
                List<Product> results = service.sortByTemperatureAndPrice();
                updateTable(results);
            }
            case "5. Перевірка можливості зберігання" -> {
                String name = promptForInput("Введіть назву продукту:");
                String tempStr = promptForInput("Введіть температуру:");
                if (name != null && tempStr != null) {
                    try {
                        double temp = Double.parseDouble(tempStr);
                        boolean canStore = service.canStoreProductAtTemperature(name, temp);
                        showAlert("Результат", canStore ? "Можна зберігати." : "Не можна зберігати.");
                    } catch (NumberFormatException e) {
                        showAlert("Помилка", "Невірний формат температури.");
                    }
                }
            }
            case "6. Список виробників за ціною" -> {
                String priceStr = promptForInput("Введіть максимальну ціну:");
                if (priceStr != null) {
                    try {
                        double price = Double.parseDouble(priceStr);
                        Set<String> manufacturers = service.findManufacturersByPriceLessThan(price);
                        showAlert("Виробники", String.join(", ", manufacturers));
                    } catch (NumberFormatException e) {
                        showAlert("Помилка", "Невірний формат ціни.");
                    }
                }
            }
            case "7. Сортовані продукти за найменуванням" -> {
                Map<String, List<Product>> grouped = service.groupByNameSortedByExpiry();
                List<Product> flatList = new ArrayList<>();
                grouped.values().forEach(flatList::addAll);
                updateTable(flatList);
            }
            case "8. Сортовані найкоштовніші продукти за найменуванням" -> {
                Map<String, Optional<Product>> grouped = service.mostExpensiveByName();
                List<Product> flatList = new ArrayList<>();
                grouped.values().forEach(opt -> opt.ifPresent(flatList::add));
                updateTable(flatList);
            }
            default -> showAlert("Помилка", "Невідома операція.");
        }
    }

    private String promptForInput(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ввід даних");
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        return dialog.showAndWait().orElse(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private String saveAs() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Збереження продуктів");
        dialog.setHeaderText("Введіть назву та тип для збереження файлу.");

        ButtonType okButtonType = new ButtonType("Збереження", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Відміна", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        dialog.getDialogPane().getButtonTypes().add(cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> choice = new ComboBox<>();
        choice.getItems().addAll(".txt", ".bin");

        TextField fileName = new TextField();

        grid.add(new Label("Назва:"), 0, 0);
        grid.add(fileName, 1, 0);
        grid.add(new Label("Тип файлу:"), 0, 1);
        grid.add(choice, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == okButtonType) {
                return fileName.getText() + ";"+ choice.getValue();
            }else if (button == cancelButtonType) {
                return null;
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(value -> {
            String[] parts = value.split(";");
            String newFileName = parts[0];
            String extension = parts[1];
            if (extension.equals(".txt")) {
                service.saveToTextFile(newFileName+extension);
            } else {
                service.saveToBinFile(newFileName+extension);
            }
        });
        return null;
    }

    @FXML
    private String readFrom() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Завантаження файлів");
        dialog.setHeaderText("Виберіть файл з списку");

        ButtonType okButtonType = new ButtonType("Завантаження", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Відміна", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        dialog.getDialogPane().getButtonTypes().add(cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> fileChoice = new ComboBox<>();

        try (Stream<Path> paths = Files.walk(Paths.get("src/files"))) {
            paths
                    .filter(Files::isRegularFile)
                    .map(Path -> Path.getFileName().toString())
                    .forEach(fileChoice.getItems()::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        grid.add(new Label("Файл:"), 0, 0);
        grid.add(fileChoice, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == okButtonType) {
                return fileChoice.getValue();
            }else if (button == cancelButtonType) {
                return null;
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(value -> {
            String[] parts = value.split("\\.");
            String extension = parts[1];
            if (extension.equals("txt")) {
                service.loadFromTextFile(value);
            } else {
                service.loadFromBinFile(value);
            }
            updateTable(service.getAllProducts());
        });
        return null;
    }

    @FXML
    private void add() {
        try {
            String name = promptForInput("Найменування:");
            if (name == null) return;

            String priceStr = promptForInput("Ціна:");
            if (priceStr == null) return;
            double price = Double.parseDouble(priceStr);

            String expiryStr = promptForInput("Дата закінчення терміну (yyyy-MM-dd):");
            if (expiryStr == null) return;
            LocalDate expiry = LocalDate.parse(expiryStr);

            String unit = promptForInput("Одиниця вимірювання:");
            if (unit == null) return;

            String manufacturer = promptForInput("Виробник:");
            if (manufacturer == null) return;

            String quantityStr = promptForInput("Кількість на складі:");
            if (quantityStr == null) return;
            int quantity = Integer.parseInt(quantityStr);

            String tempStr = promptForInput("Рекомендована температура зберігання:");
            if (tempStr == null) return;
            double temp = Double.parseDouble(tempStr);

            int id = service.IDGenerator();

            Product product = new Product(id, name, price, expiry, unit, manufacturer, quantity, temp);
            service.addProduct(product);

            showAlert("Успіх", "Продукт додано.");
            updateTable(service.getAllProducts());
        } catch (Exception e) {
            showAlert("Помилка", "Помилка введення даних. Спробуйте ще раз.");
        }
    }

    @FXML
    private void delete() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean removed = service.removeProductById(selected.getId());
            if (removed) {
                updateTable(service.getAllProducts());
                showAlert("Видалення", "Продукт успішно видалено.");
            } else {
                showAlert("Видалення", "Не вдалося видалити продукт.");
            }
        } else {
            showAlert("Видалення", "Оберіть продукт для видалення.");
        }
    }

    @FXML
    private void show() {
        updateTable(service.getAllProducts());
    }

    @FXML
    private void resort() {
        service.sortByID();
        updateTable(service.getAllProducts());
    }
}