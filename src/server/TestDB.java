package server;

import java.sql.Connection;

public class TestDB {
    public static void main(String[] args){
        try(Connection conn = DBConnection.getConnection()) {
            System.out.println("Connection Successful");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
