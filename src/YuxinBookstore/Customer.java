/**
 * Created by Orthocenter on 5/11/15.
 */

package YuxinBookstore;

import java.io.*;
import java.sql.*;

public class Customer {

    public static void mainMenuDesc() {
        System.out.println("For customer");
    }

    public static void mainMenu() {
        MenuItem[] menuItems = new MenuItem[] {
                new MenuItem() {
                    public void showDesc() { loginMenuDesc(); }
                    public void run() { loginMenu(); }
                },
                new MenuItem() {
                    public void showDesc() { signupMenuDesc(); }
                    public void run() { signupMenu(); }
                },
                new MenuItem() {
                    public void showDesc() { System.out.println("Return"); }
                    public void run() { return; }
                }
        };

        MenuDisplay menuDisplay = new MenuDisplay();
        menuDisplay.choose(menuItems);
    }

    public static void loginMenuDesc() {
        System.out.println("Login");
    }

    public static void loginMenu() {
        //System.out.println("loginMenu");
        Connector con = Bookstore.con;
        try {
            con.newStatement();
        } catch(Exception e) {
            return ;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            String username, password;

            do { System.out.print("Please enter your username : "); }
            while ((username = in.readLine()) == null || username.length() == 0) ;
            do { System.out.print("Please enter your password : "); }
            while ((password = in.readLine()) == null || password.length() == 0) ;

            String sql = "SELECT cid FROM Customer WHERE";
            sql += " username = \"" + username + "\"";
            sql += " AND password = SHA1(\"" + password + "\")";
            //System.out.println(sql);

            ResultSet rs = con.stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();

            if(rs.next()) {
                int cid;
                cid = rs.getInt(1);

                System.out.println("Logged in!");
                userhomeMenu(cid);
            } else
                System.err.println("Failed");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void signupMenuDesc() {
        System.out.println("Sign Up");
    }

    public static void signupMenu() {
        Connector con = Bookstore.con;
        try {
            con.newStatement();
        } catch(Exception e) {
            return ;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            String username, password, name, email, phone, confirmPassword;

            do { System.out.print("Please enter your username : "); }
            while ((username = in.readLine()) == null || username.length() == 0) ;
            do { System.out.print("Please enter your password : "); }
            while ((password = in.readLine()) == null || password.length() == 0) ;
            do { System.out.print("Please confirm your password : "); }
            while ((confirmPassword = in.readLine()) == null || confirmPassword.length() == 0) ;
            if(!password.equals(confirmPassword)) {
                System.out.println("Two passwords are not equal");
                return;
            }

            do { System.out.print("Please enter your name : "); }
            while ((name = in.readLine()) == null || name.length() == 0) ;
            System.out.print("Please enter your email : ");
            email = in.readLine();
            System.out.print("Please enter your phone : ");
            phone = in.readLine();

            String sql = "INSERT INTO Customer (username, password, name, email, phone) VALUES (";
            sql += "\"" + username + "\",";
            sql += "SHA1(\"" + password + "\")" + ",";
            sql += "\"" + name + "\",";
            if(email.length() == 0) sql += "NULL,"; else sql += "\"" + email + "\",";
            if(phone.length() == 0) sql += "NULL"; else sql += "\"" + phone + "\"";
            sql += ")";
            //System.out.println(sql);

            try {
                int status = con.stmt.executeUpdate(sql);
                System.out.println("Registered");

                sql = "SELECT cid FROM Customer WHERE username = \"" + username + "\"";
                //System.err.println(sql);

                ResultSet rs = con.stmt.executeQuery(sql);
                ResultSetMetaData rsmd = rs.getMetaData();

                rs.next();
                int cid = rs.getInt(1);

                userhomeMenu(cid);
            } catch (Exception e) {
                System.out.println("Failed to find the user who registered just now");
                System.err.println(e.getMessage());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void declareUserDesc() {
        System.out.println("Declare other users as 'trusted' or 'not-trusted'");
    }

    public static void declareUser(int cid) {
        int dcid = -1;
        boolean trust;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.println("Please enter cid : ");
            dcid = Integer.parseInt(in.readLine());
            System.out.println("Please enter trusted(true) or not-trusted(false) : ");
            trust = Boolean.parseBoolean(in.readLine());
        } catch (Exception e) {
            System.out.println("Failed to read");
            System.err.println(e.getMessage());
            return;
        }

        if (dcid == cid) {
            System.out.println("Youself are always trusted!");
            return;
        }

        try {
            String sql = "INSERT INTO TrustRecords (cid1, cid2, trust) VALUES (";
            sql += cid + "," + dcid + "," + trust + ")";

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

    public static void userhomeMenuDesc() {
        System.out.println("");
    }

    public static void userhomeMenu(final int cid) {
        final Book book = new Book();

        MenuItem[] menuItems = new MenuItem[] {
                new MenuItem() {
                    public void showDesc() { book.simpleSearchMenuDesc(); }
                    public void run() { book.simpleSearchMenu(cid); }
                },
                new MenuItem() {
                    @Override
                    public void showDesc() {
                        Feedback.recordDesc();
                    }

                    @Override
                    public void run() {
                        Feedback.record(cid);
                    }
                },
                new MenuItem() {
                    @Override
                    public void showDesc() {
                        Feedback.assessFeedbackDesc();
                    }

                    @Override
                    public void run() {
                        Feedback.assessFeedback(cid);
                    }
                },
                new MenuItem() {
                    @Override
                    public void showDesc() {
                        Customer.declareUserDesc();
                    }

                    @Override
                    public void run() {
                        Customer.declareUser(cid);
                    }
                },
                new MenuItem() {
                    @Override
                    public void showDesc() {
                        Feedback.showFeedbacksDesc();
                    }

                    @Override
                    public void run() {
                        Feedback.showFeedbacks();
                    }
                },
                new MenuItem() {
                    @Override
                    public void showDesc() {
                        Order.showAllOrdersDesc();
                    }

                    @Override
                    public void run() {
                        Order.showAllOrder(cid);
                    }
                },
                new MenuItem() {
                    @Override
                    public void showDesc() {
                        Order.showCartDesc();
                    }

                    @Override
                    public void run() {
                        Order.showCart(cid);
                    }
                },
                new MenuItem() {
                    @Override
                    public void showDesc() {
                        Order.confirmOrderDesc();
                    }

                    @Override
                    public void run() {
                        Order.confirmOrder(cid);
                    }
                },
                new MenuItem() {
                    public void showDesc() { System.out.println("Return"); }
                    public void run() { return; }
                }
        };

        MenuDisplay display = new MenuDisplay();
        display.choose(menuItems);
    }

    public static void trustedUsersDesc() {
        System.out.println("Print the top m most 'trusted' users");
    }

    public static void trustedUsers() {
        int m;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Please enter the amount of the most popular authors you want to see");
            m = Integer.parseInt(in.readLine());
        } catch(Exception e) {
            System.out.println("Failed to read");
            System.err.println(e.getMessage());
            return;
        }

        try {
            String sql = "SELECT *, " +
                    "(SELECT COUNT(*) FROM TrustRecords T1 WHERE T1.cid2 = C.cid AND T1.trust = 1) - (SELECT COUNT(*) FROM TrustRecords T2 WHERE T2.cid2 = C.cid AND T2.trust = 0) AS score " +
                    "FROM Customer C ORDER BY " +
                    "score DESC";
            System.err.println(sql);
            Connector con = Bookstore.con;
            try {
                con.newStatement();
            } catch(Exception e) {
                return ;
            }
            ResultSet rs = con.stmt.executeQuery(sql);

            while(rs.next() && m-- > 0) {
                System.out.format("Customer id: %d  Trust score: %d\n", rs.getInt("C.cid"), rs.getInt("score"));
            }

        } catch(Exception e) {
            System.out.println("Failed to query");
            System.out.println(e.getMessage());
            return;
        }
    }

    public static void usefulUsersDesc() {
        System.out.println("Print the top m most 'useful' users");
    }

    public static void usefulUsers() {
        int m;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Please enter the amount of the most popular authors you want to see");
            m = Integer.parseInt(in.readLine());
        } catch(Exception e) {
            System.out.println("Failed to read");
            System.err.println(e.getMessage());
            return;
        }

        try {
            String sql = "SELECT cid, AVG(rating) AS avgRating FROM Usefulness U GROUP BY U.cid ORDER BY avgRating DESC";
            System.err.println(sql);
            Connector con = Bookstore.con;
            try {
                con.newStatement();
            } catch(Exception e) {
                return ;
            }
            ResultSet rs = con.stmt.executeQuery(sql);

            while(rs.next() && m-- > 0) {
                System.out.format("Customer id: %d  Average usefulness: %f\n", rs.getInt("cid"), rs.getFloat("avgRating"));
            }

        } catch(Exception e) {
            System.out.println("Failed to query");
            System.out.println(e.getMessage());
            return;
        }
    }
}
