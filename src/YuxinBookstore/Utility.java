package YuxinBookstore;

/**
 * Created by Orthocenter on 5/14/15.
 */

import java.io.*;

public class Utility {
    public static int getChoice(int bound) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        String choice;
        int c;
        do {
            try {
                do {
                    System.out.print("Please enter your choice : ");
                }
                while ((choice = in.readLine()) == null || choice.length() == 0);

                c = Integer.parseInt(choice);
                if (c < 0 || c > bound) throw new Exception();
            } catch (Exception e) {
                System.out.println("Your choice is invalid, please try again.");
                c = -1;
            }
        } while (c == -1);

        return c;
    }

    public static String getShortString(String str, int bound) {
        String ret = "";
        if(str.length() > bound) {
            ret = str.substring(bound) + "...";
        } else {
            ret = str.substring(str.length());
        }
        return ret;
    }


}
