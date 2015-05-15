package YuxinBookstore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Orthocenter on 5/14/15.
 */
public class Publisher {
    public static int choose() {
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

        String sql = "SELECT P.name, B.title, P.pid FROM Publisher P JOIN Book B ON P.pid = B.bid WHERE P.name LIKE";
        sql += "'%" + name + "%'";
        sql += " GROUP BY P.pid";

        ArrayList<Integer> pids = new ArrayList<Integer>();

        int i = 0;
        try {
            Connector con = new Connector();
            ResultSet rs = con.stmt.executeQuery(sql);

            while(rs.next()) {
                System.out.format("%3d : ", i++);
                System.out.format("%s %s\n", rs.getString("P.name"), rs.getString("B.title"));
                pids.add(new Integer(rs.getInt("P.pid")));
            }
        } catch (Exception e) {
            System.out.println("Failed to search author");
            System.err.println(e.getMessage());
        }

        System.out.format("%3d : Return\n", i++);
        pids.add(new Integer(-1));
        int c = Utility.getChoice(pids.size() + 1);

        return pids.get(c).intValue();
    }
}
