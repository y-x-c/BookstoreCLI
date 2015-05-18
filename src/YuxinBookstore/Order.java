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

    public static void confirmOrderDesc() {
        System.out.println("Confirm your order");
    }

    public static void confirmOrder(final int cid) {
        Connector con = Bookstore.con;
        try {
            con.newStatement();
        } catch(Exception e) {
            System.out.println("Failed to create new statement");
            System.err.println(e.getMessage());
            return;
        }

        // check available amount
        try {
            String sql = "SELECT B.isbn, B.title FROM Book B, Cart C WHERE C.cid = " + cid +
                    " AND B.isbn = C.isbn AND B.copies < C.amount" ;
            System.err.println(sql);
            ResultSet rs = con.stmt.executeQuery(sql);

            if(rs.next()) {
                System.out.print("No enough books ");
                System.out.println(rs.getString("title"));
                return;
            }
        } catch(Exception e) {
            System.out.println("Failed to check amount limitation");
            System.err.println(e.getMessage());
            return;
        }

        //TBD
        int addrid = 1;

        // modify amount and record order
        try {
            con.con.setAutoCommit(false);

            String sql = "UPDATE Book SET copies = copies - " +
                    "(SELECT C.amount FROM Cart C WHERE Book.isbn = C.isbn AND C.cid = " + cid + ") " +
                    "WHERE Book.isbn IN (SELECT C.isbn FROM Cart C WHERE C.cid = " + cid + ")";
            System.err.println(sql);
            con.stmt.addBatch(sql);

            sql = "INSERT INTO Orders (time, cid, addrid) VALUES (NOW(), " + cid + "," + addrid + ")";
            System.err.println(sql);
            con.stmt.addBatch(sql);

            sql = "INSERT INTO ItemInOrder (orderid, isbn, price, amount) " +
                    "SELECT LAST_INSERT_ID(), C.isbn, B.price, C.amount FROM Cart C, Book B WHERE " +
                    "C.isbn = B.isbn AND C.cid = " + cid;
            System.err.println(sql);
            con.stmt.addBatch(sql);

            sql = "DELETE FROM Cart WHERE cid = " + cid;
            System.err.println(sql);
            con.stmt.addBatch(sql);

            con.stmt.executeBatch();
        } catch(Exception e) {
            System.out.println("Failed to update");
            System.err.println(e.getMessage());

            try {
                con.con.rollback();
            } catch(Exception e2) {
                System.out.println("Failed to roll back");
                System.err.println(e2);
            }
        } finally {
            try {
                con.con.setAutoCommit(true);
            } catch(Exception e3) {
                System.err.println(e3);
            }
        }

    }
}
