package YuxinBookstore;

import java.io.*;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Orthocenter on 5/14/15.
 */
public class Author {
    public static int choose() {
        int i = 0;

        System.out.format("%3d : ", i++);
        System.out.println("Search an existing author");
        System.out.format("%3d : ", i++);
        System.out.println("Add a new author");
        System.out.format("%3d : ", i++);
        System.out.println("Return");

        int c = Utility.getChoice(3);

        if (c == 0)
            return search();
        else if (c == 1)
            return add();

        return -2;
    }

    public static void writtenBy(String isbn) {
        int i = 0;
        while(true) {
            int authid = choose();

            if(i > 0 && authid == -2) return;

            String sql = "INSERT INTO WrittenBy (isbn, authid) VALUES (";
            sql += "'" + isbn + "'," + authid + ")";
            //System.err.println(sql);

            try {
                Connector con = Bookstore.con;
                try {
                    con.newStatement();
                } catch(Exception e) {
                    return ;
                }
                con.stmt.executeUpdate(sql);
            } catch(Exception e) {
                System.out.println("Cannot add to relation WrittenBy");
                System.err.println(e.getMessage());
            }

            i++;
        }
    }

    public static int search() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String name = null;

        try {
            do {
                System.out.print("Please enter author's name : ");
            }
            while ((name = in.readLine()) == null || name.length() == 0);
        } catch(Exception e) {
            System.out.println("Failed to read author's name");
            System.err.println(e.getMessage());
            return -1;
        }

        String sql = "SELECT A.authname, A.authid, A.intro FROM Author A " +
                "WHERE A.authname LIKE";
        sql += "'%" + name + "%'";
        sql += " GROUP BY A.authid";
        System.err.println(sql);

        ArrayList<Integer> authids = new ArrayList<Integer>();

        int i = 0;
        try {
            Connector con = Bookstore.con;
            try {
                con.newStatement();
            } catch(Exception e) {
                return -1;
            }
            ResultSet rs = con.stmt.executeQuery(sql);

            while(rs.next()) {
                System.out.format("%3d : ", i++);
                System.out.format("%s %s\n", rs.getString("A.authname"), Utility.getShortString(rs.getString("A.intro"), 50));
                authids.add(rs.getInt("A.authid"));
            }
        } catch (Exception e) {
            System.out.println("Failed to search author");
            System.err.println(e.getMessage());
        }

        System.out.format("%3d : Return\n", i++);
        authids.add(new Integer(-1));
        int c = Utility.getChoice(authids.size() + 1);

        return authids.get(c); // should I handle exception here?
    }

    public static int add() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String name = null;

        try {
            do {
                System.out.print("Please enter author's name : ");
            }
            while ((name = in.readLine()) == null || name.length() == 0);
        } catch(Exception e) {
            System.out.println("Failed to read author's name");
            System.err.println(e.getMessage());
            return -1;
        }

        String sql = "INSERT INTO Author (authname) VALUES";
        sql += "('" + name + "')";
        System.err.println(sql);

        try {
            Connector con = Bookstore.con;
            try {
                con.newStatement();
            } catch(Exception e) {
                return -1;
            }
            con.stmt.execute(sql);

            //sql = "SELECT * FROM Author WHERE authname = '" + name + "'" + " ORDER BY authid DESC";
            sql = "SELECT LAST_INSERT_ID()";
            ResultSet rs = con.stmt.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            System.out.println("Failed to add author");
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public static void showDegreesOfSeperationDesc() {
        System.out.println("Determine the degree of separation of two given authors");
    }

    public static void showDegreesOfSeperation() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int authid1, authid2;

        try {
            System.out.println("Please enter the first author id : ");
            authid1 = Integer.parseInt(in.readLine());
            System.out.println("Please enter the second author id :");
            authid2 = Integer.parseInt(in.readLine());
        } catch(Exception e) {
            System.out.println("Failed to read");
            System.err.println(e.getMessage());
            return;
        }

        try {
            String sql = "SELECT W1.isbn FROM WrittenBy W1, WrittenBy W2 WHERE W1.isbn = W2.isbn " +
                    "AND W1.authid = " + authid1 + " AND W2.authid = " + authid2;
            Connector con = Bookstore.con;
            try {
                con.newStatement();
            } catch(Exception e) {
                return ;
            }
            ResultSet rs = con.stmt.executeQuery(sql);

            if(rs.next()) {
                System.out.format("%d and %d is 1-degree away\n", authid1, authid2);
                return;
            }
        } catch(Exception e) {
            System.out.println("Failed to query");
            System.err.println(e.getMessage());
            return;
        }

        try {
            String sql = "SELECT W1.isbn, W4.isbn FROM WrittenBy W1, WrittenBy W2, WrittenBy W3, WrittenBy W4 WHERE " +
                    "W1.isbn = W2.isbn AND W3.isbn = W4.isbn AND W1.authid = " + authid1 +
                    " AND W2.authid = W3.authid AND W4.authid = " + authid2;
            Connector con = Bookstore.con;
            try {
                con.newStatement();
            } catch(Exception e) {
                return ;
            }
            ResultSet rs = con.stmt.executeQuery(sql);

            if(rs.next()) {
                System.out.format("%d and %d is 2-degrees away\n", authid1, authid2);
                return;
            }
        } catch(Exception e) {
            System.out.println("Failed to query");
            System.err.println(e.getMessage());
            return;
        }

        System.out.format("The degree between %d and %d is too far to be determined. > <\n", authid1, authid2);
    }

    public static void showPopularAuthorsDesc() {
        System.out.println("Show most popular authors in a certain period");
    }

    public static void showPopularAuthors() {
        int m;
        String st, ed;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Please enter the amount of the most popular authors you want to see");
            m = Integer.parseInt(in.readLine());

            System.out.println("Please enter the start time: ");
            st = in.readLine();
            System.out.println("Please enter the end time: ");
            ed = in.readLine();
        } catch(Exception e) {
            System.out.println("Failed to read");
            System.err.println(e.getMessage());
            return;
        }

        try {
            String sql = "SELECT W.authid, SUM(I.amount) as sales FROM ItemInOrder I, WrittenBy W, Orders O " +
                    "WHERE I.isbn = W.isbn AND O.orderid = I.orderid AND O.time >= '" + st + "' AND O.time <= '" + ed +
                    "' GROUP BY W.authid ORDER BY SUM(I.amount) DESC";

            Connector con = Bookstore.con;
            try {
                con.newStatement();
            } catch(Exception e) {
                return ;
            }
            ResultSet rs = con.stmt.executeQuery(sql);

            while(rs.next() && m-- > 0) {
                System.out.format("Author id: %d  Sales: %s\n", rs.getInt("W.authid"), rs.getInt("sales"));
            }
        } catch (Exception e) {
            System.out.println("Failed to query");
            System.err.println(e.getMessage());
            return;
        }
    }
}
