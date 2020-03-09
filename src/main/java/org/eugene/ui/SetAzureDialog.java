package org.eugene.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eugene.controller.Renderer;

import java.util.HashMap;
import java.util.Map;

public class SetAzureDialog {
    private Dialog<Map<String, String>> dialog;

    public void init(Stage stage, Renderer renderer) {
        dialog = new Dialog<>();
        dialog.setWidth(200);
        VBox vBox = new VBox();
        Label connectionStringLabel = new Label();
        connectionStringLabel.setText("Connection String:  ");
        connectionStringLabel.setPadding(new Insets(5,150,5,5));
        TextField connectionStringTF  = new TextField();
        connectionStringTF.setPromptText("connection string");

        Label containerLabel = new Label();
        containerLabel.setText("Container:  ");
        containerLabel.setPadding(new Insets(5,0,5,5));
        TextField containerTF  = new TextField();
        containerTF.setPromptText("container");

        Label blobLabel = new Label();
        blobLabel.setText("Blob:  ");
        blobLabel.setPadding(new Insets(5,0,5,5));
        TextField blobTF = new TextField();
        blobTF.setPromptText("blob");

        vBox.getChildren().add(connectionStringLabel);
        vBox.getChildren().add(connectionStringTF);
        vBox.getChildren().add(containerLabel);
        vBox.getChildren().add(containerTF);
        vBox.getChildren().add(blobLabel);
        vBox.getChildren().add(blobTF);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Map<String, String > result = new HashMap<>();
                String connectionString = connectionStringTF.getText();
                result.put(Constants.CONNECTION_STRING, connectionString);
                String container = containerTF.getText();
                result.put(Constants.CONTAINER, container);
                String blob = blobTF.getText();
                result.put(Constants.BLOB, blob);
                return result;
            }
            return null;
        });
        dialog.getDialogPane().setContent(vBox);
        dialog.setTitle("Set Azure Credential");
    }

    public Dialog<Map<String, String>> getDialog() {
        return dialog;
    }
}
