package YuxinBookstore;

import org.omg.IOP.TAG_MULTIPLE_COMPONENTS;

import javax.rmi.CORBA.Util;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.io.*;

/**
 * Created by Orthocenter on 5/18/15.
 */
public class Address {
    public static int choose(int cid) {
        int i = 0;

        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

        menuItems.add(new MenuItem() {
            @Override
            public void showDesc() {
                System.out.println("Select an existing address");
            }

            @Override
            public void run() {
                return;
            }
        });

        menuItems.add(new MenuItem() {
            @Override
            public void showDesc() {
                System.out.println("Add a new address");
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

        int addrid = -1;

        while (addrid == -1) {
            int c = MenuDisplay.getChoice(menuItems);

            if (c == 0)
                addrid = search(cid);
            else if (c == 1)
                addrid = add(cid);
        }

        return addrid;
    }

    public static int search(int cid) {
        Connector con = Bookstore.con;
        try {
            con.newStatement();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return -1;
        }

        try {
            String sql = "SELECT * FROM Address A WHERE A.cid = " + cid;
            ResultSet rs = con.stmt.executeQuery(sql);

            ArrayList<Integer> addrids = new ArrayList<Integer>();
            ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

            while(rs.next()) {
                addrids.add(rs.getInt("addrid"));
                final String desc = Utility.getFullAddress(rs);

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

            int choice = MenuDisplay.getChoice(menuItems);
            if(choice == menuItems.size() - 1) {
                return -1;
            }

            return addrids.get(choice);

        } catch(Exception e) {
            System.out.println("Failed to query");
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public static int add(int cid) {
        String rname, rphone, country, state, city, district, street, room;
        int zip;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter receiver's name : ");
            rname = in.readLine();
            System.out.println("Enter receiver's phone number : ");
            rphone = in.readLine();
            System.out.println("Enter country : ");
            country = in.readLine();
            System.out.println("Enter state : ");
            state = in.readLine();
            System.out.println("Enter city : ");
            city = in.readLine();
            System.out.println("Enter district : ");
            district = in.readLine();
            System.out.println("Enter street : ");
            street = in.readLine();
            System.out.println("Enter room : ");
            room = in.readLine();
            System.out.println("Enter zip : ");
            zip = Integer.parseInt(in.readLine());
        } catch(Exception e) {
            System.out.println("Failed to read address information");
            System.err.println(e.getMessage());
            return -1;
        }

        try {
            String sql = "INSERT INTO Address (cid, rname, rphone, country, state, city, district, street, room, zip) VALUES (";
            sql += cid + ",";
            sql += Utility.genStringAttr(rname, ",");
            sql += Utility.genStringAttr(rphone, ",");
            sql += Utility.genStringAttr(country, ",");
            sql += Utility.genStringAttr(state, ",");
            sql += Utility.genStringAttr(city, ",");
            sql += Utility.genStringAttr(district, ",");
            sql += Utility.genStringAttr(street, ",");
            sql += Utility.genStringAttr(room, ",");
            sql += zip + ")";

            Connector con = Bookstore.con;
            con.newStatement();
            con.stmt.execute(sql);

            sql = "SELECT LAST_INSERT_ID()";
            ResultSet rs = con.stmt.executeQuery(sql);
            if(rs.next()) return rs.getInt(1);

        } catch(Exception e) {
            System.out.println("Failed to add address");
            System.err.println(e.getMessage());
            return -1;
        }

        return -1;
    }

}
