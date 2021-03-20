package org.eugene.persistent;

import java.io.File;
import java.sql.*;

public class SqlliteWrapper {
    private Connection connection;

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
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
            }else{
                System.out.println("Database create failed.");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
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
