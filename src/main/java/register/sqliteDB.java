package register;

import java.sql.*;

public class sqliteDB {
    Connection c = null;
    //Statement stmt = null;

    //constructor
    public sqliteDB(){
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement stmt = this.c.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS login (id INTEGER, login TEXT, password TEXT, uuid TEXT, ip TEXT)");
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


    public boolean check(String login, String pwd, String uuid, String IP){
        try {
            PreparedStatement pstm = this.c.prepareStatement("SELECT * FROM login WHERE login = ? AND password = ?");
            pstm.setString(1,login);
            pstm.setString(2, pwd);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()){
                //set uuid and IP
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


}
