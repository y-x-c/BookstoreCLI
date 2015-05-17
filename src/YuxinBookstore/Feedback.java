package YuxinBookstore;

/**
 * Created by Orthocenter on 5/17/15.
 */

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

            Connector con = Bookstore.con;
            try {
                con.newStatement();
            } catch(Exception e) {
                return ;
            }
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
            Connector con = Bookstore.con;
            try {
                con.newStatement();
            } catch(Exception e) {
                return ;
            }
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
            Connector con = Bookstore.con;
            try {
                con.newStatement();
            } catch(Exception e) {
                return ;
            }
            con.stmt.execute(sql);

            System.out.println("Successfully");
        } catch (Exception e) {
            System.out.println("Failed to insert");
            System.err.println(e.getMessage());
            return;
        }
    }

    public static void showFeedbacksDesc() {
        System.out.println("Show feedbacks for a given book");
    }

    public static void showFeedbacks() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int m;
        String isbn;

        try {
            System.out.println("Please enter the isbn : ");
            isbn = in.readLine();
            System.out.println("The amount of the most useful feedbacks you want to see : ");
            m = Integer.parseInt(in.readLine());
        } catch (Exception e) {
            System.out.println("Failed to read");
            System.err.println(e.getMessage());
            return;
        }

        try {
            String sql = "SELECT C.username, F.score, F.comment, (SELECT SUM(U.rating) FROM Usefulness U WHERE U.fid = F.fid) AS usefulness FROM Feedback F NATURAL JOIN Customer C WHERE isbn = '" + isbn + "'";
            sql += " ORDER BY (SELECT SUM(U.rating) FROM Usefulness U WHERE U.fid = F.fid) DESC";

            Connector con = Bookstore.con;
            try {
                con.newStatement();
            } catch(Exception e) {
                return ;
            }
            ResultSet rs = con.stmt.executeQuery(sql);

            while(rs.next() && m-- > 0) {
                System.out.format("User: %s Score: %d  Comment: %s Usefulness: %d\n", rs.getString("C.username"), rs.getInt("F.score"),
                        rs.getString("F.comment"), rs.getInt("usefulness"));
            }

        } catch(Exception e) {
            System.out.println("Failed to query");
            System.err.println(e.getMessage());
            return;
        }
    }
}
