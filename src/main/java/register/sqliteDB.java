package register;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class sqliteDB {
    Connection c = null;
    Statement stmt = null;
    //constructor
    public sqliteDB(){
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void closeConnection(){
        try {
            this.c.close();
        } catch (Exception e) {
            //error on closing
        }
    }
}
