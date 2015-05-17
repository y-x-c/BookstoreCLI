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
                        Book.replenishDesc();
                    }
                    public void run() {
                        Book.replenish();
                    }
                },
                new MenuItem() {
                    @Override
                    public void showDesc() {
                        Author.showDegreesOfSeperationDesc();
                    }

                    @Override
                    public void run() {
                        Author.showDegreesOfSeperation();
                    }
                },
                new MenuItem() {
                    @Override
                    public void showDesc() {
                        Book.showPopularBooksDesc();
                    }

                    @Override
                    public void run() {
                        Book.showPopularBooks();
                    }
                },
                new MenuItem() {
                    @Override
                    public void showDesc() {
                        Publisher.showPopularPublishersDesc();
                    }

                    @Override
                    public void run() {
                        Publisher.showPopularPublishers();
                    }
                },
                new MenuItem() {
                    @Override
                    public void showDesc() {
                        Author.showPopularAuthorsDesc();
                    }

                    @Override
                    public void run() {
                        Author.showPopularAuthors();
                    }
                },
                new MenuItem() {
                    @Override
                    public void showDesc() {
                        Customer.trustedUsersDesc();
                    }

                    @Override
                    public void run() {
                        Customer.trustedUsers();
                    }
                },
                new MenuItem() {
                    @Override
                    public void showDesc() {
                        Customer.usefulUsersDesc();
                    }

                    @Override
                    public void run() {
                        Customer.usefulUsers();
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
