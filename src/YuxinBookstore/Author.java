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
        System.out.println("Search an author");
        System.out.format("%3d : ", i++);
        System.out.println("Add an author");
        System.out.format("%3d : ", i++);
        System.out.println("Return");

        int c = Utility.getChoice(3);

        if(c == 0) return search();
        if(c == 1) return add();

        return -1;
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

        String sql = "SELECT A.name, B.title, A.authid FROM Author A " +
                "JOIN Book B ON A.authid = B.authid WHERE A.name LIKE";
        sql += "'%" + name + "%'";
        sql += " GROUP BY A.authid";

        ArrayList<Integer> authids = new ArrayList<Integer>();

        int i = 0;
        try {
            Connector con = new Connector();
            ResultSet rs = con.stmt.executeQuery(sql);

            while(rs.next()) {
                System.out.format("%3d : ", i++);
                System.out.format("%s %s\n", rs.getString("A.name"), rs.getString("B.title"));
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

        String sql = "INSERT INTO Author (name) VALUES";
        sql += "('" + name + "')";

        try {
            Connector con = new Connector();
            con.stmt.executeUpdate(sql);

            sql = "SELECT * FROM Author WHERE name = '" + name + "'" + " ORDER BY authid DESC";
            ResultSet rs = con.stmt.executeQuery(sql);
            rs.next();
            return rs.getInt("authid");
        } catch (Exception e) {
            System.out.println("Failed to add author");
            System.err.println(e.getMessage());
            return -1;
        }
    }
}
