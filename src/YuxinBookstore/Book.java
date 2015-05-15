package YuxinBookstore;

/**
 * Created by Orthocenter on 5/12/15.
 */

import java.awt.*;
import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;


public class Book {
    public static void searchMenuDesc() {
        System.out.println("Search books");
    }

    public static ResultSet search(String conditions) {
        Connector con = null;
        try {
            con = new Connector();
        } catch (Exception e) {
            System.out.println("Cannot connect to database");
            return null;
        }

        String sql = "SELECT * FROM Book B JOIN Publisher P ON B.pid = P.pid " +
                "JOIN Author A ON B.authid = A.authid WHERE ";
        sql += conditions;
        //System.out.println(sql);

        try {
            ResultSet rs = con.stmt.executeQuery(sql);
            return rs;
        } catch(Exception e) {
            System.out.println("Query failed");
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static int countSearchResult(String conditions) {
        Connector con = null;
        try {
            con = new Connector();
        } catch (Exception e) {
            System.out.println("Cannot connect to database");
            return 0;
        }

        String sql = "SELECT COUNT(*) FROM Book B JOIN Publisher P ON B.pid = P.pid " +
                "JOIN Author A ON B.authid = A.authid WHERE ";
        sql += conditions;
        //System.out.println(sql);

        try {
            ResultSet rs = con.stmt.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch(Exception e) {
            System.out.println("Query failed");
            System.err.println(e.getMessage());
            return 0;
        }
    }

    public static void searchMenu(final boolean forEdit, final int cid) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String conditions, sortBy;

        try {
            do {
                System.out.print("Please enter search conditions : ");
            }
            while ((conditions = in.readLine()) == null || conditions.length() == 0);

            ResultSet rs = search(conditions);
            int cnt = countSearchResult(conditions);

            MenuItem[] menuItems = new MenuItem[cnt + 1];

            menuItems[cnt] = new MenuItem() {
                public void showDesc() { System.out.println("Return"); }
                public void run() { return; }
            };

            rs.next();
            for(int i = 0; i < cnt; i++, rs.next()) {
                final String row = rs.getString("title") + " " + rs.getString("price") + " "
                        + rs.getString("A.name") + " " + rs.getString("isbn");
                final long isbn = rs.getLong("isbn");

                MenuItem menuItem = new MenuItem() {
                    public void showDesc() {
                        showDetailsDesc(row);
                    }
                    public void run() {
                        if(!forEdit) showDetails(cid, isbn); else editDetails(cid, isbn);
                    }
                };

                menuItems[i] = menuItem;
            }
            MenuDisplay menuDisplay = new MenuDisplay();
            menuDisplay.choose(menuItems);

            // TBD: turn the page

        } catch (Exception e) {

        }

//        System.out.println("Sort by (a) by year, or (b) by the average numerical score of the feedbacks, \n" +
//                "or (c) by the average numerical score of the trusted user feedbacks :");
//        while ((sortBy = in.readLine()) == null || sortBy.length() == 0) ;
    }

    public static void searchMenu(final int cid) {
        searchMenu(false, cid);
    }

    public static void showDetailsDesc(final String row) {
        System.out.println(row);
    }

    public static void showDetails(final int cid, final long isbn) {
        Connector con = null;
        try {
            con = new Connector();
        } catch (Exception e) {
            System.out.println("Cannot connect to database");
            return ;
        }

        String sql = "SELECT * FROM Book B JOIN Publisher P ON B.pid = P.pid " +
                "JOIN Author A ON B.authid = A.authid WHERE isbn = " + isbn;

        ResultSet rs = null;
        try {
            rs = con.stmt.executeQuery(sql);
            rs.next();
        } catch(Exception e) {
            System.out.println("Failed to get details");
            System.err.println(e.getMessage());
            return ;
        }

        try {
            System.out.format("|-Title : %s\n", rs.getString("title"));
            System.out.format("|-ISBN : %s\n", rs.getString("isbn"));
            System.out.format("|-Author : %s\n", rs.getString("A.name"));
            //System.out.format("Translator : %s\n", rs.getString("translator"));
            System.out.format("|-Publisher : %s\n", rs.getString("P.name"));
            System.out.format("|-Year : %d\n", rs.getInt("year"));
            System.out.format("|-Format : %s\n", rs.getString("format"));
            System.out.format("|-Price : %f\n", rs.getFloat("price"));
            System.out.format("|-Copies : %d\n", rs.getInt("copies"));
        } catch (Exception e) {
            System.out.println("Failed to print details");
            System.err.println(e.getMessage());
        }

        try {
            final Order order = new Order();

            MenuItem[] menuItems = new MenuItem[] {
                new MenuItem() {
                    public void showDesc() {
                        order.buyNowDesc();
                    }
                    public void run() {
                        order.buyNow(cid, isbn);
                    }
                },
                new MenuItem() {
                    public void showDesc() {
                        order.add2CartDesc();
                    }
                    public void run() {
                        order.add2Cart(cid, isbn);
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
            System.out.println("Failed to show more options");
            System.err.println(e.getMessage());
        }
    }

    public static void addBookDesc() {
        System.out.println("Add a book");
    }

    public static void addBook() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String isbn = null, title = null, format = null, price_s = null, copies_s = null;
        int authid, pid;
        float price, copies;

        try {
            do {
                System.out.print("Please enter ISBN : ");
            }
            while ((isbn = in.readLine()) == null || isbn.length() == 0);

            do {
                System.out.print("Please enter title : ");
            }
            while ((title = in.readLine()) == null || title.length() == 0);

            Author author = new Author();
            while((authid = author.choose()) == -1);

            Publisher publisher = new Publisher();
            while((pid = publisher.choose()) == -1);

            do {
                System.out.print("Please enter copies : ");
            }
            while ((copies_s = in.readLine()) == null || copies_s.length() == 0);
            copies = Integer.parseInt(copies_s);

            do {
                System.out.print("Please enter price : ");
            }
            while ((price_s = in.readLine()) == null || price_s.length() == 0);
            price = Float.parseFloat(price_s);

            do {
                System.out.print("Please enter format(optional) : ");
            }
            while ((format = in.readLine()) == null);
        } catch(Exception e) {
            System.out.println("Failed to read details");
            System.err.println(e.getMessage());
            return;
        }

        try {
            String sql = "INSERT INTO Book (isbn, title, authid, pid, copies, price, format) VALUES ";
            sql += "(" + isbn + ",'" + title + "'," + authid + "," + pid + "," + copies + "," + price + ",";
            if(format != null && format.length() > 0) sql += "'" + format + "'"; else sql += "null";
            sql += ")";
            System.err.println(sql);

            Connector con = new Connector();
            con.stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Failed to add the book into database");
            System.err.println(e.getMessage());
            return;
        }
    }

    public static void editDetailsDesc() {
        System.out.println("Edit details for a book");
    }

    public static void editDetails(int cid, long isbn) {

    }
}
