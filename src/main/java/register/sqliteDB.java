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
            stmt.execute("CREATE TABLE IF NOT EXISTS login (id INTEGER, login TEXT, password TEXT, uuid TEXT, ip TEXT, isAdmin INTEGER, loggedIn INTEGER)");
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
                pstm = this.c.prepareStatement("UPDATE login SET uuid = ?, loggedIn = 1 WHERE login = ? and password = ?");
                pstm.setString(1, uuid);
                pstm.setString(2, login);
                pstm.setString(3, pwd);
                pstm.executeUpdate();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean isAdmin(String uuid){
        try {
            PreparedStatement pstm = this.c.prepareStatement("SELECT * FROM login WHERE uuid = ? AND isAdmin = 1");
            pstm.setString(1, uuid);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean isAdmin(String login, String password){
        try {
            PreparedStatement pstm = this.c.prepareStatement("SELECT * FROM login WHERE login = ? AND  password = ? AND isAdmin = 1");
            pstm.setString(1, login);
            pstm.setString(2, password);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean uuidCheck(String uuid){
        try{
            PreparedStatement pstm = this.c.prepareStatement("SELECT * FROM login WHERE uuid = ?");
            pstm.setString(1, uuid);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()){
                pstm = this.c.prepareStatement("UPDATE login SET loggedIn = 1 WHERE uuid = ?");
                pstm.setString(1, uuid);
                pstm.executeUpdate();
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void playerLeave(String uuid){
        try{
            PreparedStatement pstm = this.c.prepareStatement("UPDATE login SET loggedIn = 0 WHERE uuid = ?");
            pstm.setString(1, uuid);
            pstm.executeUpdate();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
