import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RegBookInDb {
    DBWarker dbWarker = new DBWarker();
    Statement statement = dbWarker.getConnection().createStatement();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private Book book = new Book();
    private String title = null, author = null, releaseDate = null;
    private int stock = 1;

    public RegBookInDb() throws SQLException {
    }

    public void addNewBook() {

        try {
            System.out.println("Enter book's title");
            title = br.readLine();
            System.out.println("Enter book's author");
            author = br.readLine();
            enteringDateStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enteringDateStage() throws IOException {
        System.out.println("Enter book's release year");
        releaseDate = br.readLine();
        if (dateIsValide(releaseDate)) {
            book.setReleaseDate(releaseDate);
            enteringAmountOfBooksStage();
        } else {
            System.out.println("You entered the date in wrong format");
            enteringDateStage();
        }
    }

    public void enteringAmountOfBooksStage() {
        try {
            if (!doesBookAlreadyExist(title, author, releaseDate)) {
                System.out.println("How many books do you want to register? Enter the number");
                stock = Integer.parseInt(br.readLine());
                book.setTitle(title);
                book.setAuthor(author);
                book.setStock(stock);

                LibWorker libWarker = new LibWorker();
                libWarker.addNewBookToDbBooks(book);
            } else {
                System.out.println("How many books do you want to register? Enter the number");
                stock = Integer.parseInt(br.readLine());
                statement.execute("UPDATE mylibrary.books SET available = available + " + stock + ", stock = stock + " + stock + " WHERE books.title = '" + title + "' AND books.author = '" + author + "' and release_date = '" + releaseDate + "'");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("You entered wrong value of amount of new book. Enter the number");
            enteringAmountOfBooksStage();
        }
    }

    private boolean doesBookAlreadyExist(String title, String author, String releaseDate) throws SQLException {

        ResultSet resultSet = statement.executeQuery("SELECT books.title FROM books WHERE books.title = '" + title + "' AND books.author = '" + author + "' AND release_date = '" + releaseDate + "'");
        int countExistBook = 0;
        while (resultSet.next()) {
            countExistBook++;
        }
        return countExistBook > 0;
    }

    private boolean dateIsValide(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

}
