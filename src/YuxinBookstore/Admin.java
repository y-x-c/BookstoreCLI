package YuxinBookstore;

import java.awt.*;

/**
 * Created by Orthocenter on 5/14/15.
 */
public class Admin {

    public static void mainMenuDesc() {
        System.out.println("For administrator");
    }

    public static void mainMenu() {
        MenuItem[] menuItems = new MenuItem[] {
            new MenuItem(){
                public void showDesc() {
                    Book.addBookDesc();
                }
                public void run() {
                    Book.addBook();
                }
            },
            new MenuItem() {
                public void showDesc() {
                    System.out.println("Return");
                }
                public void run() {
                    return;
                }
            }
        };

        MenuDisplay.choose(menuItems);
    }

}
