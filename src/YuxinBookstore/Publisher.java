package YuxinBookstore;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Orthocenter on 5/14/15.
 */
public class Publisher {
    public static int choose() {
        int i = 0;

        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

        menuItems.add(new MenuItem() {
            @Override
            public void showDesc() {
                System.out.println("Select an existing publisher");
            }

            @Override
            public void run() {
                return;
            }
        });

        menuItems.add(new MenuItem() {
            @Override
            public void showDesc() {
                System.out.println("Add a new publisher");
            }

            @Override
            public void run() {
                return;
            }
        });

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

        int pid = -1;

        while (pid == -1) {
            int c = MenuDisplay.getChoice(menuItems);

            if (c == 0)
                pid = search();
            else if (c == 1)
                pid = add();
        }

        return pid;
    }

    public static int search() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String name = null;

        try {
            do {
                System.out.print("Please enter publisher's name : ");
            }
            while ((name = in.readLine()) == null || name.length() == 0);
        } catch(Exception e) {
            System.out.println("Failed to read publisher's name");
            System.err.println(e.getMessage());
            return -1;
        }

        String sql = "SELECT P.pubname, B.title, P.pid FROM Publisher P NATURAL JOIN Book B WHERE P.pubname LIKE";
        sql += "'%" + name + "%'";
        sql += " GROUP BY P.pid";
        System.err.println(sql);

        ArrayList<Integer> pids = new ArrayList<Integer>();
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

        int i = 0;
        try {
            Connector con = Bookstore.con;
            con.newStatement();

            ResultSet rs = con.stmt.executeQuery(sql);

            while(rs.next()) {
                final String desc = rs.getString("P.pubname") + rs.getString("B.title");
                menuItems.add(new MenuItem() {
                    @Override
                    public void showDesc() {
                        System.out.println(desc);
                    }

                    @Override
                    public void run() {
                        return;
                    }
                });

                pids.add(new Integer(rs.getInt("P.pid")));
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

        } catch (Exception e) {
            System.out.println("Failed to search author");
            System.err.println(e.getMessage());
        }


        int choice = MenuDisplay.getChoice(menuItems);
        if(choice == menuItems.size() - 1) return -1;

        return pids.get(choice).intValue();
    }

    public static int add() {
        String pubname;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.println("Enter publisher's name : ");
            pubname = in.readLine();
        } catch(Exception e) {
            System.out.println("Failed to read publisher's information");
            System.err.println(e.getMessage());
            return -1;
        }

        try {
            String sql = "INSERT INTO Publisher (pubname) VALUES (";
            sql += Utility.genStringAttr(pubname, "");
            sql += ")";

            Connector con = Bookstore.con;
            con.newStatement();
            con.stmt.execute(sql);

            sql = "SELECT LAST_INSERT_ID()";
            ResultSet rs = con.stmt.executeQuery(sql);
            if(rs.next()) return rs.getInt(1);

        } catch(Exception e) {
            System.out.println("Failed to add publisher");
            System.err.println(e.getMessage());
            return -1;
        }

        return -1;
    }

    public static void showPopularPublishersDesc() {
        System.out.println("Show most popular publishers in a certain period");
    }

    public static void showPopularPublishers() {
        int m;
        String st, ed;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Please enter the amount of the most popular publishers you want to see");
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
            String sql = "SELECT B.pid, SUM(I.amount) as sales FROM ItemInOrder I, Book B, Orders O " +
                    "WHERE I.isbn = B.isbn AND O.orderid = I.orderid AND O.time >= '" + st + "' AND O.time <= '" + ed +
                    "' GROUP BY B.pid ORDER BY SUM(I.amount) DESC";

            Connector con = Bookstore.con;
            con.newStatement();
            ResultSet rs = con.stmt.executeQuery(sql);

            while(rs.next() && m-- > 0) {
                System.out.format("Publisher id: %d  Sales: %s\n", rs.getInt("B.pid"), rs.getInt("sales"));
            }
        } catch (Exception e) {
            System.out.println("Failed to query");
            System.err.println(e.getMessage());
            return;
        }
    }
}
