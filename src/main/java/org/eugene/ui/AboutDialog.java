package org.eugene.ui;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.net.URI;

public class AboutDialog {
    Dialog dialog = new Dialog();

    public void init(){
        Label intro = new Label();
        intro.setText("   Currently supported format: ");
        Label parquet = new Label("      - Parquet");
        Label orc = new Label("      - ORC");
        Label avro = new Label("      - AVRO");
        avro.setPadding(new Insets(0,0,5, 0));
        Label author = new Label();
        author.setText("   Author: " );
        Hyperlink github = new Hyperlink();
        github.setText("      github: Eugene");
        github.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/Eugene-Mark/"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Hyperlink so = new Hyperlink();
        so.setText("      stack overflow: Eugene");
        so.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://stackoverflow.com/users/3378204/eugene"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        dialog.getDialogPane().setContent(new VBox(intro, parquet, orc, avro, author, github, so));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
    }

    public Dialog getDialog(){
        return dialog;
    }
}
