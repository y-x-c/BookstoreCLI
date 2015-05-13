package YuxinBookstore;

/**
 * Created by Orthocenter on 5/12/15.
 */

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

        String sql = "SELECT * FROM Book B JOIN Publisher P JOIN Author A WHERE ";
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

        String sql = "SELECT COUNT(*) FROM Book B JOIN Publisher P JOIN Author A WHERE ";
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

    public static void searchMenu(final int cid) {
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
                        showDetails(cid, isbn);
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

        String sql = "SELECT * FROM Book B JOIN Publisher P JOIN Author A WHERE isbn = " + isbn;

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


    }

}
