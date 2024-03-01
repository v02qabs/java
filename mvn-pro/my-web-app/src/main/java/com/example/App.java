package com.example;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.sqlite.*;
import java.sql.*;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        try{
        	new App().Select_SQL("data.db");
        }catch(Exception e){
        	System.err.println(e.toString());
        }
    }
    public static void showTableList(String dbFilePath) throws SQLException {
        System.out.println("Connecting to database...");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
        System.out.println("Connection to database established.");
        
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[] { "TABLE" });

            System.out.println("Tables in the database:");
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println(tableName);
            }
} finally {
            connection.close();
        }
    }
	private void Select_SQL(String dbFilePath){
        	final String URL = "jdbc:sqlite:" + dbFilePath;
//        final String USER = "";
//        final String PASS = "";
        final String SQL = "select * from user where pass = '0624';";
        
        try(Connection conn = 
                DriverManager.getConnection(URL);
            PreparedStatement ps = conn.prepareStatement(SQL)){
            
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    System.out.println(
                    	rs.getInt("pass"));
                }           
            };
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("処理が完了しました");
        }
    }
}
