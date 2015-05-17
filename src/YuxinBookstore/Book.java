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

        String sql = "SELECT * FROM Book B NATURAL JOIN Publisher P NATURAL JOIN WrittenBy W NATURAL JOIN Author A WHERE ";
        sql += conditions;
        sql += " GROUP BY B.isbn ";
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

        String sql = "SELECT COUNT(*) FROM Book B NATURAL JOIN Publisher P NATURAL JOIN WrittenBy W NATURAL JOIN Author A WHERE ";
        sql += conditions;
        sql += " GROUP BY B.isbn ";
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
                        + rs.getString("A.authname") + " " + rs.getString("isbn");
                final String isbn = rs.getString("isbn");

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
            System.out.println("Failed to print search result");
            System.err.println(e.getMessage());
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

    public static void showDetails(final int cid, final String isbn) {
        Connector con = null;
        try {
            con = new Connector();
        } catch (Exception e) {
            System.out.println("Cannot connect to database");
            return ;
        }

        String sql = "SELECT * FROM Book B NATURAL JOIN Publisher P NATURAL JOIN Author A"
                        + " WHERE isbn = " + isbn;

        ResultSet rs = null;
        try {
            rs = con.stmt.executeQuery(sql);
        } catch(Exception e) {
            System.out.println("Failed to get details");
            System.err.println(e.getMessage());
            return ;
        }

        try {
            rs.next();
            System.out.format("|-Title : %s\n", rs.getString("title"));
            System.out.format("|-ISBN : %s\n", rs.getString("isbn"));
            //System.out.format("|-Author : %s\n", rs.getString("A.name"));
            //System.out.format("Translator : %s\n", rs.getString("translator"));
            System.out.format("|-Publisher : %s\n", rs.getString("P.pubname"));
            System.out.format("|-Pubdate : %s\n", rs.getString("pubdate"));
            System.out.format("|-Format : %s\n", rs.getString("format"));
            System.out.format("|-Price : %f\n", rs.getFloat("price"));
            System.out.format("|-Copies : %d\n", rs.getInt("copies"));
        } catch (Exception e) {
            System.out.println("Failed to print details");
            System.err.println(e.getMessage());
        }

        sql = "SELECT * FROM WrittenBy W NATURAL JOIN Author WHERE W.isbn = '" + isbn + "'";
        //System.err.println(sql);
        try {
            rs = con.stmt.executeQuery(sql);
        } catch(Exception e) {
            System.out.println("Failed to get author(s)");
            System.err.println(e.getMessage());
        }
        try {
            while(rs.next()) {
                System.out.format("|-Author : %s\n", rs.getString("authname"));
            }
        } catch (Exception e) {
            System.out.println("Failed to print author(s)");
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
            String sql = "INSERT INTO Book (isbn, title, pid, copies, price, format) VALUES ";
            sql += "('" + isbn + "','" + title + "'," + pid + "," + copies + "," + price + ",";
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

        try {
            Author.writtenBy(isbn);
        } catch (Exception e) {
            System.out.println("Failed to add authors");
            System.err.println(e.getMessage());
        }
    }

    public static void editDetailsDesc() {
        System.out.println("Edit details for a book");
    }

    public static void editDetails(int cid, String isbn) {

    }

    public static void replenishDesc() {
        System.out.println("Arrival of more copies");
    }

    public static void replenish() {
        int num = 0;
        String isbn;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Please enter isbn : ");
            isbn = in.readLine();

            System.out.println("Please enter the amount of new copies : ");
            num = Integer.parseInt(in.readLine());
        } catch (Exception e) {
            System.out.println("Failed to read amount");
            System.err.println(e.getMessage());
            return;
        }

        try {
            String sql = "UPDATE Book SET copies = copies + " + num + " WHERE isbn = '" + isbn + "'";
            Connector con = new Connector();
            con.stmt.execute(sql);
        } catch(Exception e) {
            System.out.println("Failed to update the amount");
            System.err.println(e.getMessage());
            return ;
        }
    }
}
