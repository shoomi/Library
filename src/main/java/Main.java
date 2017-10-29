import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class Main {

    private String userChoice, yesNo;
    BufferedReader br;

    public Main() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

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
            userChoice = br.readLine();

            while (!(userChoice.equals("u") | userChoice.equals("b") | (userChoice.equals("o") | userChoice.equals("q")))) {
                System.out.println("\nYou've entered incorrect value! Try again");
                userChoice = br.readLine();
            }

            switch (userChoice) {
                case "u":
                    regUserInDb.addNewUser();
                    doMore();
                    break;
                case "b":
                    regBookInDb.addNewBook();
                    doMore();
                    break;
                case "o":
                    someOperations.runOperations();
                    doMore();
                    break;
                case "q":
                    break;
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main().runProgram();
    }
}
