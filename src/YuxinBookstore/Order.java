package YuxinBookstore;

import apple.laf.JRSUIUtils;

import java.awt.*;
import java.io.*;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Orthocenter on 5/13/15.
 */


public class Order {

    public static void buyNowDesc() {
        System.out.println("Buy it now!");
    }

    public static void buyNow(final int cid, final String isbn) {

    }

    public static void add2CartDesc() {
        System.out.println("Add it into the cart");
    }

    public static void add2Cart(final int cid, final String isbn) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String amount = null;

        try {
            do{ System.out.println("Please enter the amount : "); }
            while ((amount = in.readLine()) == null || amount.length() == 0) ;
        } catch(Exception e) {
            System.out.println("Failed to read");
            System.err.println(e.getMessage());
            return ;
        }

        try {
            String sql = "INSERT INTO Cart (cid, isbn, amount) VALUES (" + cid + ",'" + isbn + "'," + amount + ") " +
                    "ON DUPLICATE KEY UPDATE amount = amount + " + amount;
            System.err.println(sql);

            Connector con = new Connector();
            con.stmt.execute(sql);

            System.out.println("Successfully");
        } catch(Exception e) {
            System.out.println("Failed to add into shopping cart");
            System.err.println(e.getMessage());
            return ;
        }

        try {
            System.out.println("Added to the shopping cart successfully");

            MenuItem[] menuItems = new MenuItem[] {
                new MenuItem() {
                    public void showDesc() {
                        showCartDesc();
                    }
                    public void run() {
                        showCart(cid);
                    }
                },
                    new MenuItem() {
                        public void showDesc() {
                            System.out.println("Return");
                        }
                        public void run() {
                            return ;
                        }
                    }
            };

            MenuDisplay menuDisplay = new MenuDisplay();
            menuDisplay.choose(menuItems);

        } catch(Exception e) {

        }
    }

    public static void showCartDesc() {
        System.out.println("Show all items in your cart");
    }

    public static void showCart(final int cid) {
        try {
            String sql = "SELECT * FROM Cart C NATURAL JOIN Book B WHERE C.cid = " + cid;
            System.err.println(sql);
            Connector con = new Connector();
            ResultSet rs = con.stmt.executeQuery(sql);

            while(rs.next()) {
                System.out.format("Title: %s  Price: %f  ISBN: %s  Amount: %d\n", rs.getString("B.title"),
                        rs.getFloat("B.price"), rs.getString("isbn"), rs.getInt("C.amount"));
            }
        } catch (Exception e) {
            System.out.println("Failed to query");
            System.err.println(e.getMessage());
            return;
        }
    }

    public static void editItemDesc(final String isbn, final Integer amount, final float price) {
        System.out.println();
    }

    public static void editItem(final int cid, final String isbn, final Integer amount, final float price) {

    }
}
