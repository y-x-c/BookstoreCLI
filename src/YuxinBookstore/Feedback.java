package YuxinBookstore;

/**
 * Created by Orthocenter on 5/17/15.
 */

import sun.org.mozilla.javascript.internal.EcmaError;

import java.io.*;
import java.sql.ResultSet;

public class Feedback {
    public static void recordDesc() {
        System.out.println("Record your record for a book");
    }

    public static void record(int cid) {
        String isbn;
        int score = 0;
        String comment;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Please enter isbn : ");
            isbn = in.readLine();
            System.out.println("Please enter score(1 - 10) : ");
            score = Integer.parseInt(in.readLine());
            System.out.println("Please enter your comment(optional) : ");
            comment = in.readLine();
        } catch(Exception e) {
            System.out.println("Failed to read feedback information");
            System.err.println(e.getMessage());
            return;
        }

        try {
            String sql = "INSERT INTO Feedback (isbn, cid, score, comment, time) VALUES (";
            sql += "'" + isbn + "'," + cid + "," + score + ",";
            if(comment.length() > 0) sql += "'" + comment + "',"; else sql += null + ",";
            sql += "NOW()" + ")";
            System.err.println(sql);

            Connector con = new Connector();
            con.stmt.execute(sql);
            System.out.println("Successfully");
        } catch(Exception e) {
            System.out.println("Failed to record the feedback");
            System.err.println(e.getMessage());
            return;
        }
    }

    public static void assessFeedbackDesc() {
        System.out.println("Assess a feed back record");
    }

    public static void assessFeedback(int cid) {
        int fid = -1, rating = 0;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.println("Please enter fid of the feedback which you want to assess");
            fid = Integer.parseInt(in.readLine());
            System.out.println("Please enter rating(0, 1 or 2)");
            rating = Integer.parseInt(in.readLine());
        } catch(Exception e) {
            System.out.println("Failed to read");
            System.err.println(e.getMessage());
            return;
        }

        try {
            String sql = "SELECT cid FROM Feedback WHERE fid = " + fid;
            Connector con = new Connector();
            ResultSet rs = con.stmt.executeQuery(sql);
            rs.next();

            if(rs.getInt("cid") == cid) {
                System.out.println("Sorry, you are not allow to assess your own feedback");
                return;
            }
        } catch(Exception e) {
            System.out.println("Failed to check");
            System.err.println(e.getMessage());
            return;
        }

        try {
            String sql = "INSERT INTO Usefulness (fid, cid, rating) VALUES (";
            sql += fid + "," + cid + "," + rating + ")";
            Connector con = new Connector();
            con.stmt.execute(sql);

            System.out.println("Successfully");
        } catch (Exception e) {
            System.out.println("Failed to insert");
            System.err.println(e.getMessage());
            return;
        }
    }
}
