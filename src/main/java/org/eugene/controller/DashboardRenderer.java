package org.eugene.controller;

import org.eugene.ui.Dashboard;

import java.util.Map;

public class DashboardRenderer {
    private Dashboard dashboard;

    public void setDashboard(Dashboard dashboard){
        this.dashboard = dashboard;
    }

    public void refreshMetaInfo(String schema, String path, int rowNumber, int columnNumber, boolean init){
        dashboard.refresh(schema, path, rowNumber, columnNumber, init);
    }

    public void refreshAggregationPane(String columnName, Map<String, String> keyToValue){
        dashboard.refreshAggregationPane(columnName, keyToValue);
    }

    public void refreshProportionPane(String columnName, Map<String, Integer> itemToCount){
        dashboard.refreshProportionPane(columnName, itemToCount);
    }
}
