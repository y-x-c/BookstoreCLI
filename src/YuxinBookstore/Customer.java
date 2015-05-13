/**
 * Created by Orthocenter on 5/11/15.
 */

package YuxinBookstore;

import java.io.*;
import java.sql.*;

public class Customer {

    public static void mainMenuDesc() {
        System.out.println("For customer");
    }

    public static void mainMenu() {
        MenuItem[] menuItems = new MenuItem[] {
                new MenuItem() {
                    public void showDesc() { loginMenuDesc(); }
                    public void run() { loginMenu(); }
                },
                new MenuItem() {
                    public void showDesc() { signupMenuDesc(); }
                    public void run() { signupMenu(); }
                },
                new MenuItem() {
                    public void showDesc() { System.out.println("Return"); }
                    public void run() { return; }
                }
        };

        MenuDisplay menuDisplay = new MenuDisplay();
        menuDisplay.choose(menuItems);
    }

    public static void loginMenuDesc() {
        System.out.println("Login");
    }

    public static void loginMenu() {
        //System.out.println("loginMenu");
        Connector con = null;
        try {
            con = new Connector();
        } catch (Exception e) {
            System.out.println("Cannot connect to database");
            return;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            String username, password;

            do { System.out.print("Please enter your username : "); }
            while ((username = in.readLine()) == null || username.length() == 0) ;
            do { System.out.print("Please enter your password : "); }
            while ((password = in.readLine()) == null || password.length() == 0) ;

            String sql = "SELECT cid FROM Customer WHERE";
            sql += " username = \"" + username + "\"";
            sql += " AND password = SHA1(\"" + password + "\")";
            //System.out.println(sql);

            ResultSet rs = con.stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
//            int numCols = rsmd.getColumnCount();
//            while (rs.next())
//            {
//                for (int i=1; i<=numCols;i++)
//                    System.out.print(rs.getString(i)+"  ");
//            }
//            System.out.println("");

            if(rs.next()) {
                int cid;
                cid = rs.getInt(1);

                System.out.println("Logged in!");
                userhomeMenu(cid);
            } else
                System.err.println("Failed");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void signupMenuDesc() {
        System.out.println("Sign Up");
    }

    public static void signupMenu() {
        Connector con = null;
        try {
            con = new Connector();
        } catch (Exception e) {
            return;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            String username, password, name, email, phone, confirmPassword;

            do { System.out.print("Please enter your username : "); }
            while ((username = in.readLine()) == null || username.length() == 0) ;
            do { System.out.print("Please enter your password : "); }
            while ((password = in.readLine()) == null || password.length() == 0) ;
            do { System.out.print("Please confirm your password : "); }
            while ((confirmPassword = in.readLine()) == null || confirmPassword.length() == 0) ;
            if(!password.equals(confirmPassword)) {
                System.out.println("Two passwords are not equal");
                return;
            }

            do { System.out.print("Please enter your name : "); }
            while ((name = in.readLine()) == null || name.length() == 0) ;
            System.out.print("Please enter your email : ");
            email = in.readLine();
            System.out.print("Please enter your phone : ");
            phone = in.readLine();

            String sql = "INSERT INTO Customer (username, password, name, email, phone) VALUES (";
            sql += "\"" + username + "\",";
            sql += "SHA1(\"" + password + "\")" + ",";
            sql += "\"" + name + "\",";
            if(email.length() == 0) sql += "NULL,"; else sql += "\"" + email + "\",";
            if(phone.length() == 0) sql += "NULL"; else sql += "\"" + phone + "\"";
            sql += ")";
            //System.out.println(sql);

            try {
                int status = con.stmt.executeUpdate(sql);
                System.out.println("Registered");

                sql = "SELECT cid FROM Customer WHERE username = \"" + username + "\"";
                //System.err.println(sql);

                ResultSet rs = con.stmt.executeQuery(sql);
                ResultSetMetaData rsmd = rs.getMetaData();

                rs.next();
                int cid = rs.getInt(1);

                userhomeMenu(cid);
            } catch (Exception e) {
                System.out.println("Failed to find the user who registered just now");
                System.err.println(e.getMessage());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void userhomeMenuDesc() {
        System.out.println("");
    }

    public static void userhomeMenu(final int cid) {
        final Book book = new Book();

        MenuItem[] menuItems = new MenuItem[] {
                new MenuItem() {
                    public void showDesc() { book.searchMenuDesc(); }
                    public void run() { book.searchMenu(cid); }
                },
                new MenuItem() {
                    public void showDesc() { System.out.println("Return"); }
                    public void run() { return; }
                }
        };

        MenuDisplay display = new MenuDisplay();
        display.choose(menuItems);
    }
}
