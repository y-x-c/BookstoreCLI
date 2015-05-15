 /**
 * Created by Orthocenter on 5/11/15.
 */

// format: ChenYuxin13307130248
// ddl 19th May, 10pm

package YuxinBookstore;

public class Bookstore {

    public static void displayMenu() {
        final Customer customer = new Customer();
        final Admin admin = new Admin();

        MenuItem[] menuItems = new MenuItem[] {
                new MenuItem() {
                    public void showDesc() {customer.mainMenuDesc();}
                    public void run() {customer.mainMenu();}
                },
                new MenuItem() {
                    public void showDesc() {admin.mainMenuDesc();}
                    public void run() {admin.mainMenu();}
                },
                new MenuItem() {
                    public void showDesc() { System.out.println("Exit"); }
                    public void run() { return; }
                }
        };

        MenuDisplay menuDisplay = new MenuDisplay();
        menuDisplay.choose(menuItems);
    }

    public static void main(String[] args) {
        displayMenu();
    }

//    public static void displayMenu()
//    {
//        System.out.println("        Book Store Management System     ");
//        System.out.println("1. search a course by cname and dname:");
//        System.out.println("2. enter your onw query:");
//        System.out.println("3. exit:");
//        System.out.println("pleasse enter your choice:");
//    }
//
//    public static void main(String[] args) {
//        // TODO Auto-generated method stub
//        System.out.println("Example for cs5530");
//        Connector con=null;
//        String choice;
//        String cname;
//        String dname;
//        String sql=null;
//        int c=0;
//        try
//        {
//            //remember to replace the password
//            con= new Connector();
//            System.out.println ("Database connection established");
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//
//            while(true)
//            {
//                displayMenu();
//                while ((choice = in.readLine()) == null && choice.length() == 0);
//                try{
//                    c = Integer.parseInt(choice);
//                }catch (Exception e)
//                {
//
//                    continue;
//                }
//                if (c<1 | c>3)
//                    continue;
//                if (c==1)
//                {
//                    System.out.println("please enter a cname:");
//                    while ((cname = in.readLine()) == null && cname.length() == 0);
//                    System.out.println("please enter a dname:");
//                    while ((dname = in.readLine()) == null && dname.length() == 0);
//                    Course course=new Course();
//                    System.out.println(course.getCourse(cname, dname, con.stmt));
//                }
//                else if (c==2)
//                {
//                    System.out.println("please enter your query below:");
//                    while ((sql = in.readLine()) == null && sql.length() == 0)
//                        System.out.println(sql);
//                    ResultSet rs=con.stmt.executeQuery(sql);
//                    ResultSetMetaData rsmd = rs.getMetaData();
//                    int numCols = rsmd.getColumnCount();
//                    while (rs.next())
//                    {
//                        //System.out.print("cname:");
//                        for (int i=1; i<=numCols;i++)
//                            System.out.print(rs.getString(i)+"  ");
//                        System.out.println("");
//                    }
//                    System.out.println(" ");
//                    rs.close();
//                }
//                else
//                {
//                    System.out.println("Remeber to pay us!");
//                    con.stmt.close();
//
//                    break;
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            System.err.println ("Cannot connect to database server");
//        }
//        finally
//        {
//            if (con != null)
//            {
//                try
//                {
//                    con.closeConnection();
//                    System.out.println ("Database connection terminated");
//                }
//
//                catch (Exception e) { /* ignore close errors */ }
//            }
//        }
//    }
}
