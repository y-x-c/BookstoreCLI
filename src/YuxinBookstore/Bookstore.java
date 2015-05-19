 /**
 * Created by Orthocenter on 5/11/15.
 */

// format: ChenYuxin13307130248
// ddl 19th May, 10pm

package YuxinBookstore;

 import java.awt.*;
 import java.util.ArrayList;

 public class Bookstore {

    public static Connector con = null;

    public static void displayMenu() {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        int[] maxSizes = {30};
        String[] manners = {"c"};

        menuItems.add(new MenuItem() {
            @Override
            public ArrayList<String> getDescs() {
                return Customer.mainMenuDescs();
            }

            @Override
            public void run() {
                Customer.mainMenu();
            }
        });

        menuItems.add(new MenuItem() {
            @Override
            public ArrayList<String> getDescs() {
                return Admin.mainMenuDescs();
            }

            @Override
            public void run() {
                Admin.mainMenu();
            }
        });

        MenuDisplay menuDisplay = new MenuDisplay();
        menuDisplay.chooseAndRun(menuItems, null, maxSizes, manners, true);
    }

    public static void main(String[] args) {
        try {
            con = new Connector();
        } catch (Exception e) {
            System.out.println("Cannot connect to the database");
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        displayMenu();
    }
}