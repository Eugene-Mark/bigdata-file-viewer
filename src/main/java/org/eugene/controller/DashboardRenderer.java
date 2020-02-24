package org.eugene.controller;

import org.eugene.ui.Dashboard;

public class DashboardRenderer {
    private Dashboard dashboard;

    public void setDashboard(Dashboard dashboard){
        this.dashboard = dashboard;
    }

    public void refreshMetaInfo(String schema, String path, int rowNumber, int columnNumber){
        dashboard.refresh(schema, path, rowNumber, columnNumber);
    }
}
