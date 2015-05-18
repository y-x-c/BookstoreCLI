/**
 * Created by Orthocenter on 5/11/15.
 */

package YuxinBookstore;

import java.io.*;
import java.util.ArrayList;

interface MenuItem {
    public void showDesc();
    public void run();
}

public class MenuDisplay {
    public static void showMenu(MenuItem[] menuItems) {
        int i = 0;
        for(MenuItem item : menuItems) {
            System.out.format("%3d : ", i++);
            item.showDesc();
        }
    }

    public static void showMenu(ArrayList<MenuItem> menuItems) {
        int i = 0;
        for(MenuItem item : menuItems) {
            System.out.format("%3d : ", i++);
            item.showDesc();
        }
    }

    public static void choose(MenuItem[] menuItems, boolean flush) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String choice;
        int c = -1;

        try {
            while (true) {
                if(flush) {
                    System.out.print("\u001b[2J");
                    System.out.flush();
                }
                int i = 0;
                for(MenuItem item : menuItems) {
                    System.out.format("%3d : ", i++);
                    item.showDesc();
                }

                c = Utility.getChoice(menuItems.length - 1);
//                do { System.out.print("Please enter your choice : "); }
//                while ((choice = in.readLine()) == null || choice.length() == 0) ;
//
//                try {
//                    c = Integer.parseInt(choice);
//                    if (c < 0 || c >= menuItems.length) throw new Exception();
//                } catch (Exception e) {
//                    System.out.println("Your choice is invalid, please try again.");
//                    continue;
//                }

                if(c == menuItems.length - 1) return;
                menuItems[c].run();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void choose(ArrayList<MenuItem> menuItems, boolean flush) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String choice;
        int c = -1;

        try {
            while (true) {
                if(flush) {
                    System.out.print("\u001b[2J");
                    System.out.flush();
                }
                int i = 0;
                for(MenuItem item : menuItems) {
                    System.out.format("%3d : ", i++);
                    item.showDesc();
                }

                c = Utility.getChoice(menuItems.size() - 1);

//                do { System.out.print("Please enter your choice : "); }
//                while ((choice = in.readLine()) == null || choice.length() == 0) ;
//
//                try {
//                    c = Integer.parseInt(choice);
//                    if (c < 0 || c >= menuItems.size()) throw new Exception();
//                } catch (Exception e) {
//                    System.out.println("Your choice is invalid, please try again.");
//                    continue;
//                }

                if(c == menuItems.size() - 1) return;
                menuItems.get(c).run();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void choose(ArrayList<MenuItem> menuItems) {
        choose(menuItems, true);
    }

    public static void choose(MenuItem[] menuItems) {
        choose(menuItems, true);
    }

    public static int getChoice(ArrayList<MenuItem> menuItems, boolean flush) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String choice;
        int c = -1;

        try {
            while (true) {
                if(flush) {
                    System.out.print("\u001b[2J");
                    System.out.flush();
                }
                int i = 0;
                for(MenuItem item : menuItems) {
                    System.out.format("%3d : ", i++);
                    item.showDesc();
                }

                c = Utility.getChoice(menuItems.size() - 1);

//                do { System.out.print("Please enter your choice : "); }
//                while ((choice = in.readLine()) == null || choice.length() == 0) ;
//
//                try {
//                    c = Integer.parseInt(choice);
//                    if (c < 0 || c >= menuItems.size()) throw new Exception();
//                } catch (Exception e) {
//                    System.out.println("Your choice is invalid, please try again.");
//                    continue;
//                }

                return c;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static int getChoice(ArrayList<MenuItem> menuItems) {
        return getChoice(menuItems, true);
    }
}
