package YuxinBookstore;

import java.io.*;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Orthocenter on 5/14/15.
 */
public class Author {
    public static void writtenBy(String isbn) {
        while(true) {
            int i = 0;

            System.out.format("%3d : ", i++);
            System.out.println("Search an existing author");
            System.out.format("%3d : ", i++);
            System.out.println("Add a new author");
            System.out.format("%3d : ", i++);
            System.out.println("Return");

            int c = Utility.getChoice(3);

            int authid = c;

            if (c == 0)
                authid = search();
            else if (c == 1)
                authid = add();
            else return;

            String sql = "INSERT INTO WrittenBy (isbn, authid) VALUES (";
            sql += "'" + isbn + "'," + authid + ")";
            System.err.println(sql);

            try {
                Connector con = new Connector();
                con.stmt.executeUpdate(sql);
            } catch(Exception e) {
                System.out.println("Cannot add to relation WrittenBy");
                System.err.println(e.getMessage());
            }
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

        ArrayList<Integer> authids = new ArrayList<Integer>();

        int i = 0;
        try {
            Connector con = new Connector();
            ResultSet rs = con.stmt.executeQuery(sql);

            while(rs.next()) {
                System.out.format("%3d : ", i++);
                System.out.format("%s %s\n", rs.getString("A.authname"), Utility.getShortString(rs.getString("A.intro"), 50));
                authids.add(new Integer(rs.getInt("A.authid")));
            }
        } catch (Exception e) {
            System.out.println("Failed to search author");
            System.err.println(e.getMessage());
        }

        System.out.format("%3d : Return\n", i++);
        authids.add(new Integer(-1));
        int c = Utility.getChoice(authids.size() + 1);

        return authids.get(c).intValue(); // should I handle exception here?
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

        try {
            Connector con = new Connector();
            con.stmt.executeUpdate(sql);

            sql = "SELECT * FROM Author WHERE authname = '" + name + "'" + " ORDER BY authid DESC";
            ResultSet rs = con.stmt.executeQuery(sql);
            rs.next();
            return rs.getInt("authid");
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
            Connector con = new Connector();
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
            Connector con = new Connector();
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
}
