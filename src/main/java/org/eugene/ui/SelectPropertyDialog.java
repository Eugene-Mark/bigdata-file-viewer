package org.eugene.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eugene.controller.Renderer;
import org.eugene.persistent.VirtualDB;

import java.util.ArrayList;
import java.util.List;

class SelectPropertyDialog {
    private Dialog<List<String>> dialog;

    public void init(Stage stage, Renderer renderer){
        List<String> properties = new ArrayList<String>(VirtualDB.getInstance().getCommonData().getColumnToType().keySet());
        dialog = new Dialog<>();
        VBox vBox = new VBox();
        VBox checkBoxGroup = new VBox(2);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(checkBoxGroup);
        checkBoxGroup.setAlignment(Pos.CENTER_LEFT);
        RadioButton selectAll = new RadioButton("Select All Properties");
        RadioButton deSelectAll = new RadioButton("Deselect All Properties");
        HBox hBox = new HBox(50);
        hBox.getChildren().add(selectAll);
        hBox.getChildren().add(deSelectAll);
        ToggleGroup toggleGroup = new ToggleGroup();
        selectAll.setToggleGroup(toggleGroup);
        selectAll.setSelected(true);
        selectAll.setOnAction(event -> {
            for (Node node: checkBoxGroup.getChildren()) {
                CheckBox checkBox = (CheckBox)node;
                checkBox.setSelected(true);
            }
        });
        deSelectAll.setToggleGroup(toggleGroup);
        deSelectAll.setOnAction(event -> {
            for (Node node: checkBoxGroup.getChildren()) {
                CheckBox checkBox = (CheckBox)node;
                checkBox.setSelected(false);
            }
        });
        hBox.setPadding(new Insets(20,0,20,0));
        vBox.getChildren().add(hBox);

        for (String property:properties) {
            CheckBox checkBox = new CheckBox();
            checkBox.setText(property);
            checkBoxGroup.getChildren().add(checkBox);
            checkBox.setSelected(true);
        }
        vBox.getChildren().add(scrollPane);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        List<String> result = new ArrayList<>();
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType){
                for (Node node: checkBoxGroup.getChildren()) {
                    CheckBox checkBox = (CheckBox)node;
                    if (checkBox.isSelected()){
                        result.add(checkBox.getText());
                    }
                }
                return result;
            }
            return null;
        });
        dialog.getDialogPane().setContent(vBox);
        dialog.setTitle("Select properties to show");
    }

    public Dialog<List<String>> getDialog(){
        return dialog;
    }

}
