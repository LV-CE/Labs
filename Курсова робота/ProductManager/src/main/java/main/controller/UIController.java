package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.product.Product;
import main.service.Service;
import main.io.Reader;
import main.io.View;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class UIController {

    boolean mainTable = true;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> idCol;
    @FXML
    private TableColumn<Product, String> nameCol;
    @FXML
    private TableColumn<Product, Double> priceCol;
    @FXML
    private TableColumn<Product, LocalDate> receiptCol;
    @FXML
    private TableColumn<Product, String> unitCol;
    @FXML
    private TableColumn<Product, String> manufacturerCol;
    @FXML
    private TableColumn<Product, Integer> quantityCol;
    @FXML
    private TableColumn<Product, String> serialNumCol;
    @FXML
    private ComboBox<String> operationsChoiceBox;

    private final Service service = new Service();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        priceCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        receiptCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getReceiptDate()));
        unitCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUnit()));
        manufacturerCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getManufacturer()));
        quantityCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        serialNumCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSerialNumber()));

        View.changePropetry(true,idCol,nameCol,priceCol,receiptCol,unitCol,manufacturerCol,quantityCol,serialNumCol,operationsChoiceBox);
        service.loadFromTextFile("ProductList.txt", true);
        service.loadFromTextFile("ReceiptList.txt", false);
        service.resetAllProducts();
        updateTable(service.getAllProducts(true));

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
                String name = Reader.TextInputWindow("Введіть назву продукту:");
                if (name != null) {
                    List<Product> results = service.findByName(name);
                    updateTable(results);
                }
            }
            case "2. Пошук за ціною" -> {
                String priceStr = Reader.TextInputWindow("Введіть максимальну ціну:");
                if (priceStr != null) {
                    try {
                        double price = Double.parseDouble(priceStr);
                        List<Product> results = service.findByPrice(price);
                        updateTable(results);
                    } catch (NumberFormatException e) {
                        View.showMessage("Помилка", "Невірний формат ціни.");
                    }
                }
            }
            case "3. Пошук за датой надходження" -> {
                String dateStr = Reader.TextInputWindow("Введіть дату (РРРР-ММ-ДД) або залиште пустим для сьогоднішньої:");
                if (dateStr != null) {
                    try {
                        LocalDate date = LocalDate.parse(dateStr);
                        List<Product> results = service.findByReceiptDate(date);
                        updateTable(results);
                    } catch (Exception e) {
                        List<Product> results = service.findByReceiptDate(LocalDate.now());
                        updateTable(results);
                    }
                }
            }
            case "4. Пошук за типом продукту" -> {
                ComboBox<String> productType = new ComboBox<>();
                List<Product> products = service.getAllProducts(true);
                for (Product product : products) {
                    String unit = product.getUnit();
                    if (!productType.getItems().contains(unit)) {
                        productType.getItems().add(unit);
                    }
                }
                Optional<String> result = Reader.CreateChooseVindow("Вибір типу", "Введіть тип продукту", "ОК", productType, "Тип:");
                result.ifPresent(value -> {
                    List<Product> results = service.findByType(value);
                    updateTable(results);
                });
            }
            case "5. Пошук за виробником" -> {
                ComboBox<String> manufacturerList = new ComboBox<>();
                List<Product> products = service.getAllProducts(true);
                for (Product product : products) {
                    String manufacturer = product.getManufacturer();
                    if (!manufacturerList.getItems().contains(manufacturer)) {
                        manufacturerList.getItems().add(manufacturer);
                    }
                }
                Optional<String> result = Reader.CreateChooseVindow("Вибір виробника", "Введіть виробника", "ОК", manufacturerList, "Виробник:");
                result.ifPresent(value -> {
                    List<Product> results = service.findByManufacturer(value);
                    updateTable(results);
                });
            }
            case "6. Пошук за серійним номером" -> {
                String serialNum = Reader.TextInputWindow("Введіть серійний номер:");
                if (serialNum != null) {
                    List<Product> results = service.findBySerialNumber(serialNum);
                    updateTable(results);
                }
            }
            case "7. Сортування за виробником" -> {
                List<Product> results = service.sortByManufacturer();
                updateTable(results);
            }
            case "8. Сортування за датой надходження" -> {
                List<Product> results = service.sortByReceiptDate();
                updateTable(results);
            }
            case "9. Сортовані продукти за найменуванням" -> {
                List<Product> grouped = service.sortByNameSortedByReceipt();
                updateTable(grouped);
            }
            case "10. Сортовані найкоштовніші продукти за найменуванням" -> {
                List<Product> grouped = service.mostExpensiveByName();
                updateTable(grouped);
            }
            default -> View.showMessage("Помилка", "Невідома операція.");
        }
    }

    @FXML
    private void saveAs() {
        Optional<String> result = Reader.CreateSaveVindow();
        result.ifPresent(value -> {
            String[] parts = value.split(";");
            String newFileName = parts[0];
            String extension = parts[1];
            boolean loadResult;
            if (extension.equals(".txt")) {
                loadResult = service.saveToTextFile(newFileName+extension, mainTable);
            } else {
                loadResult = service.saveToBinFile(newFileName+extension, mainTable);
            }
            if (loadResult)
                View.showMessage("Успіх", "Збереження данних пройшло успішно."); else
                View.showMessage("Помилка", "Помилка збереження данних.");
        });
    }

    @FXML
    private void readFrom() {
        ComboBox<String> fileChoice = new ComboBox<>();
        try (Stream<Path> paths = Files.walk(Paths.get("src/files"))) {
            paths
                    .filter(Files::isRegularFile)
                    .map(Path -> Path.getFileName().toString())
                    .forEach(fileChoice.getItems()::add);
        } catch (IOException e) {
            View.showMessage("Помилка", "Не вдалося завантажити файли.");
        }
        Optional<String> result = Reader.CreateChooseVindow("Завантаження файлів", "Виберіть файл з списку", "Завантаження", fileChoice, "Файл:");
        result.ifPresent(value -> {
            String[] parts = value.split("\\.");
            String extension = parts[1];
            boolean loadResult;
            if (extension.equals("txt")) {
                loadResult = service.loadFromTextFile(value, mainTable);
            } else {
                loadResult = service.loadFromBinFile(value, mainTable);
            }
            if (loadResult)
                View.showMessage("Успіх", "Завантаження данних пройшло успішно."); else
                View.showMessage("Помилка", "Помилка завантаження данних.");
            updateTable(service.getAllProducts(mainTable));
        });
    }

    @FXML
    private void addProduct() {
        try {
            String name = Reader.TextInputWindow("Найменування:");
            if (name == null) return; else if (name.isEmpty()) {View.showMessage("Помилка", "Помилка введення даних. Спробуйте ще раз."); return;}

            String priceStr = Reader.TextInputWindow("Ціна:");
            if (priceStr == null) return; else if (priceStr.isEmpty()) {View.showMessage("Помилка", "Помилка введення даних. Спробуйте ще раз."); return;}
            double price = Double.parseDouble(priceStr);

            String receiptStr = Reader.TextInputWindow("Дата надходження (yyyy-MM-dd)(або залиште пустим для сьогоднішньої):");
            LocalDate receipt = LocalDate.now();
            if (receiptStr == null) return;
            else if (!receiptStr.isEmpty()) {
                receipt = LocalDate.parse(receiptStr);
            }

            String unit = Reader.TextInputWindow("Тип продукту:");
            if (unit == null) return; else if (unit.isEmpty()) {View.showMessage("Помилка", "Помилка введення даних. Спробуйте ще раз."); return;}

            String manufacturer = Reader.TextInputWindow("Виробник:");
            if (manufacturer == null) return; else if (manufacturer.isEmpty()) {View.showMessage("Помилка", "Помилка введення даних. Спробуйте ще раз."); return;}

            String quantityStr = Reader.TextInputWindow("Кількість на складі:");
            if (quantityStr == null) return; else if (quantityStr.isEmpty()) {View.showMessage("Помилка", "Помилка введення даних. Спробуйте ще раз."); return;}
            int quantity = Integer.parseInt(quantityStr);

            String serialNum = Reader.TextInputWindow("Серійний номер:");
            if (serialNum == null) return; else if (serialNum.isEmpty()) {View.showMessage("Помилка", "Помилка введення даних. Спробуйте ще раз."); return;}

            int id = service.IDGenerator(true);

            Product product = new Product(id, name, price, receipt, unit, manufacturer, quantity, serialNum);
            service.addProduct(product, true);

            View.showMessage("Успіх", "Продукт додано.");
            updateTable(service.getAllProducts(mainTable));
        } catch (Exception e) {
            View.showMessage("Помилка", "Помилка введення даних. Спробуйте ще раз.");
        }
    }

    @FXML
    private void addReceipt() {
        try {
            String idStr = Reader.TextInputWindow("ID квитанції:");
            int id = service.IDGenerator(false);
            if (idStr == null) return; else if (!idStr.isEmpty()) {id = Integer.parseInt(idStr);}

            String name = Reader.TextInputWindow("Прізвище І.Б. клієнта:");
            if (name == null) return; else if (name.isEmpty()) {View.showMessage("Помилка", "Помилка введення даних. Спробуйте ще раз."); return;}

            String priceStr = Reader.TextInputWindow("Ціна покупки:");
            if (priceStr == null) return; else if (priceStr.isEmpty()) {View.showMessage("Помилка", "Помилка введення даних. Спробуйте ще раз."); return;}
            double price = Double.parseDouble(priceStr);

            String receiptStr = Reader.TextInputWindow("Дата покупки (yyyy-MM-dd)(або залиште пустим для сьогоднішньої):");
            LocalDate receipt = LocalDate.now();
            if (receiptStr == null) return;
            else if (!receiptStr.isEmpty()) {
                receipt = LocalDate.parse(receiptStr);
            }

            String unit = Reader.TextInputWindow("Тип видачі:");
            if (unit == null) return; else if (unit.isEmpty()) {View.showMessage("Помилка", "Помилка введення даних. Спробуйте ще раз."); return;}

            String manufacturer = Reader.TextInputWindow("Продавець (Призвіще І.Б.):");
            if (manufacturer == null) return; else if (manufacturer.isEmpty()) {View.showMessage("Помилка", "Помилка введення даних. Спробуйте ще раз."); return;}

            Product product = new Product(id, name, price, receipt, unit, manufacturer, 0, "0");
            service.addProduct(product, false);

            View.showMessage("Успіх", "Квитанцію додано.");
            updateTable(service.getAllProducts(mainTable));
        } catch (Exception e) {
            View.showMessage("Помилка", "Помилка введення даних. Спробуйте ще раз.");
        }
    }

    @FXML
    private void delete() {
        String type;
        if (mainTable) type = "продукт"; else type = "квитанцію";
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean removed = service.removeProductById(selected.getId(), mainTable);
            if (removed) {
                updateTable(service.getAllProducts(mainTable));
                View.showMessage("Видалення", type+" успішно видалено.");
            } else {
                View.showMessage("Видалення", "Не вдалося видалити " +type+ ".");
            }
        } else {
            View.showMessage("Видалення", "Оберіть " +type+ " для видалення.");
        }
    }

    @FXML
    private void showProduct() {
        mainTable = true;
        View.changePropetry(true,idCol,nameCol,priceCol,receiptCol,unitCol,manufacturerCol,quantityCol,serialNumCol,operationsChoiceBox);
        service.resetAllProducts();
        updateTable(service.getAllProducts(mainTable));
    }

    @FXML
    private void showReceipt() {
        mainTable = false;
        View.changePropetry(false,idCol,nameCol,priceCol,receiptCol,unitCol,manufacturerCol,quantityCol,serialNumCol,operationsChoiceBox);
        updateTable(service.getAllProducts(mainTable));
    }
}