package main.io;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import java.util.Optional;

import javafx.scene.control.TextInputDialog;

public class Reader {
    public static String TextInputWindow(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ввід даних");
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        return dialog.showAndWait().orElse(null);
    }
    public static Optional<String> CreateSaveVindow() {
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

        return dialog.showAndWait();
    }
    public static Optional<String> CreateChooseVindow(String title, String header, String buttonName, ComboBox<String> newComboBox, String comboBoxText) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        ButtonType okButtonType = new ButtonType(buttonName, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Відміна", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        dialog.getDialogPane().getButtonTypes().add(cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label(comboBoxText), 0, 0);
        grid.add(newComboBox, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == okButtonType) {
                return newComboBox.getValue();
            }else if (button == cancelButtonType) {
                return null;
            }
            return null;
        });

        return dialog.showAndWait();
    }
}
