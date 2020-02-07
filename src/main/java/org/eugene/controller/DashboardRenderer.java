package org.eugene.controller;

import org.apache.avro.Schema;
import org.eugene.ui.Dashboard;

import java.io.File;

public class DashboardRenderer {
    private Dashboard dashboard;

    public void setDashboard(Dashboard dashboard){
        this.dashboard = dashboard;
    }

    public void refreshMetaInfo(Schema schema, File selectedFile, int rowNumber, int columnNumber){
        dashboard.refresh(schema, selectedFile, rowNumber, columnNumber);
    }
}
