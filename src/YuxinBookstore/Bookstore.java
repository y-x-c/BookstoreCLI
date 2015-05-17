 /**
 * Created by Orthocenter on 5/11/15.
 */

// format: ChenYuxin13307130248
// ddl 19th May, 10pm

package YuxinBookstore;

public class Bookstore {

    public static Connector con = null;

    public static void displayMenu() {
        final Customer customer = new Customer();
        final Admin admin = new Admin();

        MenuItem[] menuItems = new MenuItem[]{
                new MenuItem() {
                    public void showDesc() {
                        customer.mainMenuDesc();
                    }

                    public void run() {
                        customer.mainMenu();
                    }
                },
                new MenuItem() {
                    public void showDesc() {
                        admin.mainMenuDesc();
                    }

                    public void run() {
                        admin.mainMenu();
                    }
                },
                new MenuItem() {
                    public void showDesc() {
                        System.out.println("Exit");
                    }

                    public void run() {
                        return;
                    }
                }
        };

        MenuDisplay menuDisplay = new MenuDisplay();
        menuDisplay.choose(menuItems);
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