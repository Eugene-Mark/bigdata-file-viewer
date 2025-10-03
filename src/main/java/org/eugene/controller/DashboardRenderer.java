package org.eugene.controller;

import java.util.Map;

import lombok.Setter;

import org.eugene.ui.Dashboard;

@Setter
public class DashboardRenderer {
  private Dashboard dashboard;

  public void refreshMetaInfo(String schema, String path, int rowNumber, int columnNumber, boolean init) {
	dashboard.refresh(schema, path, rowNumber, columnNumber, init);
  }

  public void refreshAggregationPane(String columnName, Map<String, String> keyToValue) {
	dashboard.refreshAggregationPane(columnName, keyToValue);
  }

  public void refreshProportionPane(String columnName, Map<String, Integer> itemToCount) {
	dashboard.refreshProportionPane(columnName, itemToCount);
  }
}
