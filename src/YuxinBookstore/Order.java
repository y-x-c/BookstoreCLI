package YuxinBookstore;

import javax.xml.transform.Result;
import java.awt.*;
import java.io.*;
import java.sql.ResultSet;
import java.util.ArrayList;


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

            Connector con = Bookstore.con;
            try {
                con.newStatement();
            } catch(Exception e) {
                return ;
            }
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
            Connector con = Bookstore.con;
            try {
                con.newStatement();
            } catch(Exception e) {
                return ;
            }
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

    public static void showOrderDetailsDesc(final int orderid) {
        String sql = "SELECT * FROM Orders WHERE orderid = " + orderid;

        try {
            Connector con = Bookstore.con;
            con.newStatement();

            ResultSet rs = con.stmt.executeQuery(sql);
            rs.next();
            System.out.format("Order id: %d, Time: %s, Customer id: %d\n", rs.getInt("orderid"), rs.getString("time"),
                    rs.getInt("cid"));
        } catch(Exception e) {
            System.out.println("Failed to print order description");
            System.err.println(e.getMessage());
            return;
        }
    }

    public static void showOrderDetails(final int orderid) {
        try {
            String sql = "SELECT * FROM Orders O WHERE orderid = " + orderid;
            Connector con = Bookstore.con;
            con.newStatement();

            ResultSet rs = con.stmt.executeQuery(sql);
            rs.next();
            System.out.format("|--Order id : %d\n", rs.getInt("orderid"));
            System.out.format("|--Time : %s\n", rs.getString("time"));

            final int cid = rs.getInt("cid");
            final int addrid = rs.getInt("addrid");

            sql = "SELECT * FROM Customer C WHERE cid = " + cid;
            rs = con.stmt.executeQuery(sql);
            rs.next();

            System.out.format("|--Username : %s\n", rs.getString("username"));
            System.out.format("|--Name : %s\n", rs.getString("name"));

            sql = "SELECT * FROM Address A WHERE addrid = " + addrid;
            rs = con.stmt.executeQuery(sql);
            rs.next();

            System.out.format("|--Address : %s\n", Utility.getFullAddress(rs));
            System.out.format("|--Receiver Phone : %s\n", rs.getString("phone"));

            sql = "SELECT * FROM ItemInOrder I, Book B WHERE orderid = " + orderid +
                " AND I.isbn = B.isbn";
            rs = con.stmt.executeQuery(sql);

            int i = 0;
            float totalPrices = 0;
            ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
            while(rs.next()) {
                String row = "";
                row += "Item " + i++ + ", ";
                row += "Title: " + rs.getString("B.title") + ", ";
                row += "Price: " + rs.getString("I.price") + ", ";
                row += "ISBN: " + rs.getString("B.isbn") + ", ";
                row += "Amount: " + rs.getString("I.amount");

                final String _row = row;
                final String isbn = rs.getString("B.isbn");

                menuItems.add(new MenuItem() {
                    @Override
                    public void showDesc() {
                        Book.showDetailsDesc(_row);
                    }

                    @Override
                    public void run() {
                        Book.showDetails(cid, isbn);
                    }
                });

                totalPrices += rs.getFloat("I.price") * rs.getInt("I.amount");
            }

            menuItems.add(new MenuItem() {
                @Override
                public void showDesc() {
                    System.out.println("Return");
                }

                @Override
                public void run() {
                    return;
                }
            });

            System.out.format("|--Total Prices : %f\n", totalPrices);

            MenuDisplay.choose(menuItems, false);
        } catch(Exception e) {
            System.out.println("Failed to print order details");
            System.err.println(e.getMessage());
            return;
        }
    }

    public static void showAllOrdersDesc() {
        System.out.println("Show all orders for a certain customer");
    }

    public static void showAllOrder(int cid) {
        try {
            String sql = "SELECT orderid FROM Orders O WHERE O.cid = " + cid;
            System.err.println(sql);
            Connector con = Bookstore.con;
            try {
                con.newStatement();
            } catch(Exception e) {
                return ;
            }
            ResultSet rs = con.stmt.executeQuery(sql);

            ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
            while(rs.next()) {
                final int orderid = rs.getInt("orderid");
                menuItems.add(new MenuItem() {
                    @Override
                    public void showDesc() {
                        showOrderDetailsDesc(orderid);
                    }

                    @Override
                    public void run() {
                        showOrderDetails(orderid);
                    }
                });
            }

            menuItems.add(new MenuItem() {
                @Override
                public void showDesc() {
                    System.out.print("Return");
                }

                @Override
                public void run() {
                    return;
                }
            });

            MenuDisplay.choose(menuItems);
        } catch (Exception e) {
            System.out.println("Failed to query");
            System.err.println(e.getMessage());
            return;
        }
    }
}
