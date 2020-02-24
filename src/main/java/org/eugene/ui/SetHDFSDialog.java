package org.eugene.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eugene.controller.Renderer;
import javafx.scene.paint.Color;

public class SetHDFSDialog {
    private Dialog<String> dialog;

    public void init(Stage stage, Renderer renderer) {
        dialog = new Dialog<>();
        VBox vBox = new VBox();
        Label label = new Label();
        label.setText("Input full HDFS path:  ");
        label.setPadding(new Insets(5,0,5,5));
        Label info = new Label();
        info.setText("E.g. hdfs://localhost:9000/user/eugene/sample.parquet");
        info.setPadding(new Insets(5, 0,5,5));
        info.setTextFill(Color.gray(0.3));
        TextField textField = new TextField();
        textField.setPromptText("hdfs://");
        vBox.getChildren().add(label);
        vBox.getChildren().add(info);
        vBox.getChildren().add(textField);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return textField.getText();
            }
            return null;
        });
        dialog.getDialogPane().setContent(vBox);
        dialog.setTitle("Set HDFS path");
    }

    public Dialog<String> getDialog() {
        return dialog;
    }
}
