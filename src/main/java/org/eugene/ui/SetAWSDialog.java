package org.eugene.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eugene.controller.Renderer;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class SetAWSDialog {
    private Dialog<Map<String, String>> dialog;
    @Value("${aws-dialog-width}")
    private int dialogWidth;
    public void init(Stage stage, Renderer renderer) {
        dialog = new Dialog<>();
        dialog.setWidth(dialogWidth);
        VBox vBox = new VBox();
        Label bucketNameLabel = new Label();
        bucketNameLabel.setText("Bucket Name:  ");
        bucketNameLabel.setPadding(new Insets(5,150,5,5));
        TextField bucketNameTF  = new TextField();
        bucketNameTF.setPromptText("bucket name");

        Label fileNameLabel = new Label();
        fileNameLabel.setText("File Name:  ");
        fileNameLabel.setPadding(new Insets(5,0,5,5));
        TextField fileNameTF  = new TextField();
        fileNameTF.setPromptText("file name");

        Label accessKeyLabel = new Label();
        accessKeyLabel.setText("Access Key:  ");
        accessKeyLabel.setPadding(new Insets(5,0,5,5));
        TextField accessKeyTF = new TextField();
        accessKeyTF.setPromptText("access key");

        Label secretKeyLabel = new Label();
        secretKeyLabel.setText("Secret Key:  ");
        secretKeyLabel.setPadding(new Insets(5,0,5,5));
        TextField secretKeyTF = new TextField();
        secretKeyTF.setPromptText("secret key");

        Label regionLabel = new Label();
        regionLabel.setText("Region:  ");
        regionLabel.setPadding(new Insets(5,0,5,5));
        TextField regionTF = new TextField();
        regionTF.setPromptText("region");

        vBox.getChildren().add(bucketNameLabel);
        vBox.getChildren().add(bucketNameTF);
        vBox.getChildren().add(fileNameLabel);
        vBox.getChildren().add(fileNameTF);
        vBox.getChildren().add(accessKeyLabel);
        vBox.getChildren().add(accessKeyTF);
        vBox.getChildren().add(secretKeyLabel);
        vBox.getChildren().add(secretKeyTF);
        vBox.getChildren().add(regionLabel);
        vBox.getChildren().add(regionTF);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Map<String, String > result = new HashMap<>();
                String bucketName = bucketNameTF.getText();
                result.put(Constants.BUCKET, bucketName);
                String fileName = fileNameTF.getText();
                result.put(Constants.FILE, fileName);
                String accessKey = accessKeyTF.getText();
                result.put(Constants.ACCESSKEY, accessKey);
                String secretKey = secretKeyTF.getText();
                result.put(Constants.SECRETKEY, secretKey);
                String region = regionTF.getText();
                result.put(Constants.REGION, region);
                return result;
            }
            return null;
        });
        dialog.getDialogPane().setContent(vBox);
        dialog.setTitle("Set AWS Credential");
    }

    public Dialog<Map<String, String>> getDialog() {
        return dialog;
    }
}
