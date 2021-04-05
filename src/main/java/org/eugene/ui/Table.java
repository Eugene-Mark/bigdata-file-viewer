package org.eugene.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eugene.controller.Renderer;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private Stage stage;
    private VBox vBox;
    private TableView<List<StringProperty>> tableView;
    private Pagination pagination;
    private Renderer renderer;

    public Table(Stage stage, Renderer renderer){
        this.stage = stage;
        this.renderer = renderer;
    }

    public void setVBox(VBox vBox){
        this.vBox = vBox;
    }

    public void initTable(){
        if (tableView != null)
            vBox.getChildren().remove(tableView);
        if (pagination != null)
            vBox.getChildren().remove(pagination);
        tableView = new TableView();
        final ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
        selectedCells.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change change) {
                for (TablePosition pos : selectedCells) {
                    String columnName = pos.getTableColumn().getText();
                    if(columnName != null){
                        renderer.refreshAggregationPane(pos.getTableColumn().getText());
                        renderer.refreshProportionPane(pos.getTableColumn().getText());
                    }
                    System.out.println("Cell selected in row " + pos.getRow() + " and column " + pos.getTableColumn().getText());
                }
            }
        });
        pagination = new Pagination();
        tableView.prefHeightProperty().bind(stage.heightProperty());
        tableView.prefWidthProperty().bind(stage.widthProperty());
        vBox.getChildren().add(tableView);
        vBox.getChildren().add(pagination);
    }



    public void refresh(List<String> showingList, List<String> propertyList, int rowNumber, int columnNumber, List<List<String>> data){
        initTable();

        int index = 0;
        for (String property: propertyList){
            if (!showingList.contains(property)){
                continue;
            }
            TableColumn<List<StringProperty>, String> tableColumn = new TableColumn<>(property);
            int finalIndex = index;
            tableColumn.setCellValueFactory(colData -> colData.getValue().get(finalIndex));
            tableColumn.setSortable(false);
            tableColumn.setEditable(false);
            tableView.getColumns().add(tableColumn);
            index++;
        }

        int pageCount = rowNumber / Constants.MAX_ROW_NUM + 1;
        if (rowNumber % Constants.MAX_ROW_NUM == 0) {
            pageCount--;
        }
        int colNumber = propertyList.size();
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
        pagination.setMaxPageIndicatorCount(10);

        pagination.setPageFactory((pageIndex) -> {
            generatePage(data, tableView, pageIndex, Constants.MAX_ROW_NUM, colNumber, showingList, propertyList);
            VBox vbox = new VBox();
            vbox.getChildren().add(tableView);
            return vbox;
        });
    }

    private void generatePage(List<List<String>> data, TableView tableView, int pageIndex, int pageRowNum, int colNumber, List<String> showingList, List<String> propertyList){
        ObservableList<List<StringProperty>> content = FXCollections.observableArrayList();
        int start = pageIndex * pageRowNum;
        int end = start + pageRowNum;
        if (end > data.size()){
            end = data.size();
        }
        for (int i = start; i < end; i++) {
            List<String> r = data.get(i);
            List<StringProperty> row = new ArrayList<StringProperty>();
            int index = 0;
            for (int j = 0; j < colNumber; j++){
                if(showingList.contains(propertyList.get(j))){
                    if (r.get(j) == null){
                        row.add(index, new SimpleStringProperty(Constants.NULL));
                    }
                    else{
                        row.add(index, new SimpleStringProperty(r.get(j).toString()));
                    }
                    index++;
                }
            }
            content.add(row);

        }
        tableView.setItems(content);
    }
}
