package org.eugene.persistent;

import org.eugene.model.CommonData;

import java.io.File;
import java.sql.*;
import java.util.*;

public class SqlliteWrapper {
    private Connection connection;

    static final String INTEGER = "integer";
    static final String REAL = "real";
    static final String TEXT = "text";

    public static final String MAX = "MAX";
    public static final String MIN = "MIN";
    public static final String SUM = "SUM";
    public static final String AVG = "AVG";

    public void createNewDatabase(String fileName) {
        String home_dir = System.getProperty("user.home");
        try{
            Class.forName("org.sqlite.JDBC");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        String url = "jdbc:sqlite:" + home_dir + File.separator + fileName;

        try{
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
            }else{
                System.out.println("Database create failed.");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private String convertType(String type){
        switch(type.toUpperCase()){
            case "INT":
            case "INTEGER":
            case "LONG":
                return SqlliteWrapper.INTEGER;
            case "DOUBLE":
            case "FLOAT":
                return SqlliteWrapper.REAL;
        }
        return SqlliteWrapper.TEXT;
    }

    private boolean createData(CommonData commonData){
        System.out.println("The table name is: " + commonData.getName());
        String sql = "CREATE TABLE IF NOT EXISTS " + commonData.getName() + "(\n";

        int i = 0;
        int size = commonData.getColumnToType().size();
        for(Map.Entry<String, String> entry : commonData.getColumnToType().entrySet()){
            i++;
            String name = entry.getKey();
            String type = entry.getValue();
            if(i == size){
                sql += name + " " + convertType(type) + "\n";
            }else{
                sql += name + " " + convertType(type) + ",\n";
            }
        }
        sql += ");";

        System.out.println("Create data, sql: " + sql);

        try{
            Statement stmt = connection.createStatement();
            boolean res = stmt.execute(sql);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    private void setItem(PreparedStatement pstmt, String item, int index, List<String> typeList) throws SQLException {
        String type = typeList.get(index);
        switch(type) {
            case SqlliteWrapper.INTEGER:
                if(item != null){
                    pstmt.setInt(++index, Integer.parseInt(item));
                }else{
                    pstmt.setNull(++index, Types.INTEGER);
                }
            case SqlliteWrapper.REAL:
                if(item != null){
                    pstmt.setDouble(++index, Double.parseDouble(item));
                }else{
                    pstmt.setNull(++index, Types.DOUBLE);
                }
            default:
                if(item != null){
                    pstmt.setString(++index, item);
                }else{
                    pstmt.setNull(++index, Types.VARCHAR);
                }
        }
    }

    public boolean insertData(CommonData commonData){
        List<String> typeList = new ArrayList<String>();
        String sql = "INSERT INTO " + commonData.getName();
        int i = 0;
        int size = commonData.getColumnToType().size();
        sql += "(";
        for(Map.Entry<String, String> entry : commonData.getColumnToType().entrySet()){
            i++;
            String name = entry.getKey();
            String type = entry.getValue();
            if(i == size){
                sql += name + ")";
            }else{
                sql += name + ",";
            }
            typeList.add(type);
        }

        int j = 0;
        sql += " VALUES (";
        for(Map.Entry<String, String> entry : commonData.getColumnToType().entrySet()){
            j++;
            String name = entry.getKey();
            String type = entry.getValue();
            if(j == size){
                sql += "?" + ")";
            }else{
                sql += "?" + ",";
            }
        }
        try{
            System.out.println("The sql: " + sql);
            PreparedStatement pstmt = connection.prepareStatement(sql);
            for(List<String> record: commonData.getData()){
                for(int k = 0; k < record.size(); k++){
                    setItem(pstmt, record.get(k), k, typeList);
                }
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.commit();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Map<String, Integer> getProportion(CommonData commonData, String columnName){
        Map<String, Integer> itemToCount = new LinkedHashMap<String, Integer>();
        String sql = "SELECT " + columnName + ", COUNT(" + columnName + ") from " + commonData.getName() + " group by " + columnName + " order by COUNT(" + columnName + ") DESC";
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                String column = rs.getString(1);
                int count = rs.getInt(2);
                itemToCount.put(column,count);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return itemToCount;
    }

    private String getAggregation(CommonData commonData, String columnName, String aggregation){
        String sql = "SELECT " + aggregation + " (" + columnName + ") as r_" + aggregation + " from " + commonData.getName() + " where " + columnName + " != \"NULL\"";
        String result = null;
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                result = rs.getString("r_" + aggregation);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public Map<String,String> getAggregations(CommonData commonData, String columnName){
        String max = getAggregation(commonData, columnName, SqlliteWrapper.MAX);
        String min = getAggregation(commonData, columnName, SqlliteWrapper.MIN);
        String sum = getAggregation(commonData, columnName, SqlliteWrapper.SUM);
        String avg = getAggregation(commonData, columnName, SqlliteWrapper.AVG);
        System.out.println("max: "+ max + " min: " + min + "sum: " + sum + "avg: " + avg);
        Map<String, String> keyToValue= new HashMap<String, String>();
        keyToValue.put(SqlliteWrapper.MAX, max);
        keyToValue.put(SqlliteWrapper.MIN, min);
        keyToValue.put(SqlliteWrapper.SUM, sum);
        keyToValue.put(SqlliteWrapper.AVG, avg);
        return keyToValue;
    }

    private boolean tableExists(String name){
        try{
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet rs = dbm.getTables(null, null, name, null);
            if(rs.next()){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean persistData(CommonData commonData){
        if(tableExists(commonData.getName())){
            return true;
        }
        createData(commonData);
        insertData(commonData);
        return true;
    }

    private boolean createLocation(){
        String sql = "CREATE TABLE IF NOT EXISTS Location(\n"
                + "	id integer NOT NULL PRIMARY KEY,\n"
                + "	path text NOT NULL\n"
                + ");";
        try{
            Statement stmt = connection.createStatement();
            boolean res = stmt.execute(sql);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    private int countLocation(){
        int count = 0;
        String sql = "SELECT COUNT(id)\n"
                + "FROM Location;";
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            count = rs.getInt(1);
        }catch(SQLException e){
            e.printStackTrace();
        }
        System.out.println("The count is: " + count);
        return count;
    }

    private boolean insertLocation(String path){
        String sql = "INSERT INTO Location(path) VALUES (?)";
        try{
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, path);
            pstmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateLocation(String path){
        int count = countLocation();
        if (count == 0){
            createLocation();
            insertLocation(path);
            return true;
        }

        String sql = "UPDATE Location SET path = ?";

        try{
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, path);
            pstmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    public String getLocation(){
        createLocation();
        String sql = "SELECT path FROM Location";
        String path = "";
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                path = rs.getString("path");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return path;
    }
}
