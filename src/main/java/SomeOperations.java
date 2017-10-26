import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class SomeOperations {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private String userLogin, booksName, booksYear, takeOrRetur = null;
    private int userID = 0;
    LibWorker libWorker = new LibWorker();

    public SomeOperations() throws SQLException {
    }

    public void runOperation() throws IOException, SQLException {
        System.out.println("\nPleace, enter your login");
        userLogin = br.readLine();
        userID = libWorker.getIdUserbyLogin(userLogin);
        if (userID >= 0) {
            System.out.println("\nTo take book enter 't'. To return book enter 'r'");
            takeOrRetur = br.readLine();
            switch (takeOrRetur) {
                case "t":
                    showAllFreeBooksAndGiveOneToUser();
                    break;
                case "r":
                    bookReturn();
                    break;
                default:
                    System.out.println("\nYou've entered incorrect value. Try again");
                    runOperation();
            }

        } else {
            System.out.println("\nSorry. but login '" + userLogin + "' is not registered. You should register first");
            new Main().runProgram();
        }
    }

    private void bookReturn() {
        try {

            if (libWorker.borrowedUsersBooks(userID) > 0) {
                System.out.println("\n Enter book's name you want to return");
                booksName = br.readLine();
                System.out.println("\n Enter book's year release");
                booksYear = br.readLine();
                if (libWorker.doesUserBorrowTheBook(userID, booksName, booksYear)) {//
                    libWorker.returnUserBook(userID, booksName, booksYear);
                    System.out.println("\nThe book was successfully returned!");
                } else {
                    System.out.println("\nIt's look like you've wrote incorrect book's name, year or you've already returned this book. Try again");
                    bookReturn();
                }
            } else {
                System.out.println("\nYou don't have to return anything");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAllFreeBooksAndGiveOneToUser() {
        try {
            libWorker.showAllFreeBooksFromDb();
            System.out.println("\nThese are all free books in our library at this time. \nYou can choose one of them by name.\n");
            givingBookProcess();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void givingBookProcess() throws IOException, SQLException {
        booksName = br.readLine();
        int numberOfBooksDates = libWorker.getDatafromAllFreeBooksByNameFromDb(booksName);   /// show all book with the same title but different dates and record the number of dates
        if (libWorker.bookIsInLibrary(booksName)) {

            if (numberOfBooksDates == 0) {
                System.out.println("Sorry, but this book is already busy. Try enter another name");
                givingBookProcess();
            } else if (numberOfBooksDates == 1) {                          /// here is no requirement to enter the date of the book because there is only one date
                booksYear = libWorker.getDateOfTheBook(booksName);
                libWorker.giveNewBookToUser(userID, booksName, booksYear);
            } else {                                                        /// this variant, when there is more than one date of book with the same title

                System.out.println("Pleace, choose the book's year release");
                booksYear = br.readLine();
                if (libWorker.bookIsFree(booksName, booksYear)) {
                    libWorker.giveNewBookToUser(userID, booksName, booksYear);
                } else {
                    System.out.println("Soryy, but you entered incorrect books release year. Try with another one. \nEnter the name of the book");
                    givingBookProcess();
                }
            }

        } else {
            System.out.println("This book '" + booksName + "' doesn't exist in our library. Try enter enother book's title");
            givingBookProcess();
        }
    }


}
