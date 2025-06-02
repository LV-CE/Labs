package main.io;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import main.product.Product;

public class View {
    public static void showMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public static void changePropetry(boolean trueTable, TableColumn<Product, ?> id, TableColumn<Product, ?> name, TableColumn<Product, ?> price, TableColumn<Product, ?> receipt, TableColumn<Product, ?> unit, TableColumn<Product, ?> manufacturer, TableColumn<Product, ?> quantity, TableColumn<Product, ?> serialNum, ComboBox<String> operationsChoiceBox) {
        if (trueTable) {
            name.setText("Назва");
            receipt.setText("Дата надходження");
            manufacturer.setText("Виробник");
            unit.setText("Категорія");
            serialNum.setVisible(true);
            quantity.setVisible(true);
            operationsChoiceBox.setVisible(true);
            id.setPrefWidth(46);
            name.setPrefWidth(142);
            price.setPrefWidth(75);
            receipt.setPrefWidth(129);
            unit.setPrefWidth(105);
            manufacturer.setPrefWidth(132);
            quantity.setPrefWidth(60);
            serialNum.setPrefWidth(103);
        } else {
            name.setText("ПІБ");
            receipt.setText("Дата покупки");
            manufacturer.setText("Продавець");
            unit.setText("Тип видачі");
            serialNum.setVisible(false);
            quantity.setVisible(false);
            operationsChoiceBox.setVisible(false);
            id.setPrefWidth(86);
            name.setPrefWidth(160);
            price.setPrefWidth(94);
            receipt.setPrefWidth(150);
            unit.setPrefWidth(144);
            manufacturer.setPrefWidth(160);
        }
    }
}