import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegBookInDb {;
    Statement statement;
    BufferedReader br;
    private Book book;

    public RegBookInDb() throws SQLException {
        book = new Book();
        br = new BufferedReader(new InputStreamReader(System.in));
        statement = new DBWarker().getConnection().createStatement();
    }

    public void addNewBook() {

        try {
            System.out.println("Enter book's title");
            book.setTitle(br.readLine());

            System.out.println("Enter book's author");
            book.setAuthor(br.readLine());

            enteringDate();
            enteringStock();

            if (!isBookExist()){
                new LibWorker().addNewBookToDbBooks(book);
            } else {
                addBookToExisting();
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void addBookToExisting() throws SQLException {
        statement.executeUpdate(String.format("UPDATE mylibrary.books SET available = available + %d, stock = stock + %d " +
                        "WHERE books.title = '%s' AND books.author = '%s' and release_date = '%s'",
                        book.getStock(), book.getStock(), book.getTitle(), book.getAuthor(), book.getReleaseDate()));
    }

    private void enteringDate() throws IOException {
        System.out.println("Enter book's release year in format YYYY");
        String releaseDate = br.readLine();

        while (releaseDate.length() != 4 || !releaseDate.chars().allMatch(Character::isDigit)) {
            System.out.println("Value invalid. Try again");
            releaseDate = br.readLine();
        }
        book.setReleaseDate(releaseDate);
    }

    public void enteringStock() {
        try {
            System.out.println("How many books do you want to register? Enter the number");
            String stock = br.readLine();
            while (!stock.chars().allMatch(Character::isDigit)){
                System.out.println("Value invalid. Try again");
                br.readLine();
            }
            book.setStock(Integer.parseInt(stock));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isBookExist() throws SQLException {
        ResultSet rs = statement.executeQuery(String.format("SELECT books.title FROM books WHERE books.title = '%s' AND books.author = '%s' " +
                "AND release_date = '%s'", book.getTitle(), book.getAuthor(), book.getReleaseDate()));
        if (rs.next()){
            return true;
        }
        return false;
    }

}
