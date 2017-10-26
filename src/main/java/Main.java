import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class Main {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private String userBookOperationsQuit, yesNo;

    public void doMore() throws IOException {
        System.out.println("Do you want to do something else? y/n");
        yesNo = br.readLine();
        if (yesNo.equals("y")) {
            runProgram();
        } else if (yesNo.equals("n")) {
            System.exit(0);
        } else {
            doMore();
        }
    }

    void runProgram() {

        System.out.println("Enter 'u' - to register as a new user \n      'b' - to register new book  \n      'o' - do some operation \n      'q' - to quit");

        try {
            RegBookInDb regBookInDb = new RegBookInDb();
            SomeOperations someOperations = new SomeOperations();
            RegUserInDb regUserInDb = new RegUserInDb();
            userBookOperationsQuit = br.readLine();

            switch (userBookOperationsQuit) {
                case "u":
                    regUserInDb.addNewUser();
                    doMore();
                    break;
                case "b":
                    regBookInDb.addNewBook();
                    doMore();
                    break;
                case "o":
                    someOperations.runOperation();
                    doMore();
                    break;
                case "q":
                    break;
                default:
                    System.out.println("You've entered incorrect value.");
                    runProgram();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main().runProgram();
    }
}
