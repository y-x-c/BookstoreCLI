package YuxinBookstore;

import apple.laf.JRSUIUtils;

import java.awt.*;
import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Orthocenter on 5/13/15.
 */


public class Order {

    public static TreeMap<Long, Integer> itemList = new TreeMap<Long, Integer>();

    public static void buyNowDesc() {
        System.out.println("Buy it now!");
    }

    public static void buyNow(final int cid, final long isbn) {

    }

    public static void add2CartDesc() {
        System.out.println("Add it into the cart");
    }

    public static void add2Cart(final int cid, final long isbn) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String amount = null;

        try {
            do{ System.out.println("Please enter the amount : "); }
            while ((amount = in.readLine()) == null || amount.length() == 0) ;

            Integer oldAmount = 0;
            oldAmount = itemList.get(new Long(isbn));
            Integer newAmount = (oldAmount == null ? 0 : oldAmount) + new Integer(amount);
            itemList.put(new Long(isbn), newAmount);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return ;
        }

        try {
            System.out.println("Added to the shopping cart successfully");

            MenuItem[] menuItems = new MenuItem[] {
                new MenuItem() {
                    public void showDesc() {
                        showCartDesc();
                    }
                    public void run() {
                        showCart(cid);
                    }
                },
                    new MenuItem() {
                        public void showDesc() {
                            System.out.println("Return");
                        }
                        public void run() {
                            return ;
                        }
                    }
            };

            MenuDisplay menuDisplay = new MenuDisplay();
            menuDisplay.choose(menuItems);

        } catch(Exception e) {

        }
    }

    public static void showCartDesc() {
        System.out.println("Show all items in your cart");
    }

    public static void showCart(final int cid) {
        MenuItem[] menuItems = new MenuItem[itemList.size() + 1];
        menuItems[itemList.size()] = new MenuItem() {
            public void showDesc() {
                System.out.println("Return");
            }
            public void run() {
                return ;
            }
        };

        int i = 0;
        for(final Map.Entry<Long, Integer> entry : itemList.entrySet()) {
            menuItems[i++] = new MenuItem() {
                @Override
                public void showDesc() {
                    editItemDesc(entry.getKey(), entry.getValue(), 0.0f);
                }

                @Override
                public void run() {
                    editItem(cid, entry.getKey(), entry.getValue(), 0.0f);
                }
            };
        }

        MenuDisplay menuDisplay = new MenuDisplay();
        menuDisplay.choose(menuItems);
    }

    public static void editItemDesc(final Long isbn, final Integer amount, final float price) {
        System.out.println();
    }

    public static void editItem(final int cid, final Long isbn, final Integer amount, final float price) {

    }
}
