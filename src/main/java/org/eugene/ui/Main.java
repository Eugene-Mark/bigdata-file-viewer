package org.eugene.ui;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
public class Main {
  private final Stage stage;
  @Getter
  private final Table table;
  @Getter
  private final Dashboard dashboard;

  @Value("${split-pane-ratio}")
  private int splitPaneRatio;

  public void initUI() {
	VBox mainVBox = (VBox) stage.getScene().getRoot();
	SplitPane splitPane = new SplitPane();
	VBox leftVBox = new VBox();
	VBox rightVBox = new VBox();
	table.setVBox(rightVBox);
	dashboard.setVBox(leftVBox);
	splitPane.getItems().addAll(leftVBox, rightVBox);
	splitPane.setDividerPosition(0, splitPaneRatio);
	mainVBox.getChildren().add(splitPane);
  }
}
