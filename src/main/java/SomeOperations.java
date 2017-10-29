import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class SomeOperations {
    BufferedReader br;
    LibWorker libWorker;
    private String userLogin, booksName, booksYear, takeOrRetur = null;
    private int userID = 0;

    public SomeOperations() throws SQLException {
        br = new BufferedReader(new InputStreamReader(System.in));
        libWorker = new LibWorker();
    }

    public void runOperations() {

        try {
            System.out.println("\nPleace, enter your login");
            userLogin = br.readLine();
            userID = libWorker.getIdUserbyLogin(userLogin);
            if (userID >= 0) {

                System.out.println("\nTo take book enter 't'. To return book enter 'r'");
                takeOrRetur = br.readLine();

                while (!(takeOrRetur.equals("t") | takeOrRetur.equals("r"))) {
                    System.out.println("\nYou've entered incorrect value. Try again");
                    takeOrRetur = br.readLine();
                }

                switch (takeOrRetur) {
                    case "t":
                        takeBook();
                        break;
                    case "r":
                        returnBook();
                        break;
                }

            } else {
                System.out.println("\nSorry. but login '" + userLogin + "' is not registered. You should register first");
                new Main().runProgram();
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void returnBook() throws SQLException, IOException {

        if (libWorker.doesUserBorrowBooks(userID) > 0) {
            enterBookNameAndYear();

            while (!libWorker.userBorrowThisBook(userID, booksName, booksYear)) {
                System.out.println("You entered wrong book's title or year. Try again");
                enterBookNameAndYear();
            }
            libWorker.returnUserBook(userID, booksName, booksYear);
            System.out.println("\nThe book was successfully returned!");

        } else {
            System.out.println("\nYou don't have to return anything");
        }
    }


    private void takeBook() throws IOException, SQLException {

        try {
            libWorker.showAllFreeBooksFromDb();
            System.out.println("\nThese are all free books in our library at this time. \nYou can choose one of them by name.\n");
            enterBookNameAndYear();

            while (!libWorker.bookIsFree(booksName, booksYear)) {
                System.out.println("Sorry, but you can't take this book. Try another one.");
                enterBookNameAndYear();
            }
            while (libWorker.userBorrowThisBook(userID, booksName, booksYear)) {
                System.out.println("Sorry, but you can't take this book. Try another one.");
                enterBookNameAndYear();
            }
            libWorker.giveNewBookToUser(userID, booksName, booksYear);
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("There is no such book in our library");
        }
    }


    private void enterBookNameAndYear() throws IOException {

        System.out.println("\nEnter book's name");
        booksName = br.readLine();
        System.out.println("\nEnter book's year release in format YYYY");
        booksYear = br.readLine();

        while (booksYear.length() != 4 || !booksYear.chars().allMatch(Character::isDigit)) {
            System.out.println("Value invalid. Try again");
            booksYear = br.readLine();
        }
    }

}
