import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LibWorker {

    DBWorker dbWorker;
    Statement statement;
    ResultSet resultSet;

    public LibWorker() throws SQLException {
        dbWorker = new DBWorker();
        statement = dbWorker.getConnection().createStatement();
    }

    public void showAllFreeBooksFromDb() throws SQLException {                                                            /// shows all free books from Db
        resultSet = statement.executeQuery("SELECT DISTINCT title, author, release_date FROM books ");

        while (resultSet.next()) {
            System.out.println(String.format("%s,   %s,   %s", resultSet.getString("title"), resultSet.getString("author"), resultSet.getString("release_date")));
        }
    }

    public void addNewUserToDbUsers(User libUser) throws SQLException {
        String addNewUser = String.format("INSERT INTO  mylibrary.`users` (login, first_name, last_name, date_of_birth, phone_number) VALUES ('%s','%s', '%s', '%s', '%s')", libUser.getLogin(), libUser.getFirst_name(), libUser.getLast_name(), libUser.getDate_of_birth(), libUser.getTelephone());
        statement.execute(addNewUser);
    }

    public void addNewBookToDbBooks(Book book) throws SQLException {
        String addNewBook = String.format("INSERT INTO  mylibrary.`books` (title, author, release_date, stock) VALUES ('%s','%s', '%s', %d)", book.getTitle(), book.getAuthor(), book.getReleaseDate(), book.getStock());
        statement.execute(addNewBook);
    }

    public int getIdUserbyLogin(String userlogin) throws SQLException {
        resultSet = statement.executeQuery(String.format("SELECT user_id FROM users WHERE login = '%s'", userlogin));
        int userId = -1;
        if (resultSet.next()) {
            userId = resultSet.getInt("user_id");
        }
        return userId;
    }

    public int getIdBookByName(String bookName, String booksYearRelese) throws SQLException {
        resultSet = statement.executeQuery(String.format("SELECT book_id FROM books WHERE title = '%s' AND books.release_date ='%s'", bookName, booksYearRelese));
        int bookId = -1;
        if (resultSet.next()) {
            bookId = resultSet.getInt("book_id");
        }
        return bookId;
    }


    public void giveNewBookToUser(int userId, String bookName, String booksYearRelese) throws SQLException {
        int bookId = getIdBookByName(bookName, booksYearRelese);
        statement.execute(String.format("INSERT INTO  mylibrary.`borrowings` (user_id, book_id, borrowing_date) VALUES ('%d','%d', '%s')", userId, bookId, new CurentTime().getCurrentTime()));
        System.out.println("This book is yours! Nice reading");
    }


    public boolean bookIsFree(String bookName, String booksYearRelese) throws SQLException {
        int bookId = getIdBookByName(bookName, booksYearRelese);
        resultSet = statement.executeQuery(String.format("SELECT (SELECT stock\n" +
                "        FROM books\n" +
                "        WHERE book_id = '%d') - (SELECT count(book_id)\n" +
                "                                 FROM borrowings\n" +
                "                                 WHERE book_id = '%d' AND returning_date IS NULL) AS free_books", bookId, bookId));
        resultSet.next();
        int freeBooks = resultSet.getInt("free_books");
        return freeBooks > 0;
    }


    public void returnUserBook(int idUser, String bookName, String booksYearRelese) throws SQLException {
        int idBook = getIdBookByName(bookName, booksYearRelese);
        statement.execute(String.format("UPDATE mylibrary.borrowings\n" +
                "SET returning_date = '%s'\n" +
                "WHERE user_id = '%d' AND book_id = '%d'", new CurentTime().getCurrentTime(), idUser, idBook));
    }


    public int doesUserBorrowBooks(int userId) throws SQLException {
        int count = 0;
        String query = String.format("SELECT\n" +
                "  title,\n" +
                "  author,\n" +
                "  release_date\n" +
                "FROM ((borrowings\n" +
                "  INNER JOIN books ON borrowings.book_id =books.book_id)\n" +
                "  INNER JOIN users ON borrowings.user_id = users.user_id) WHERE ( users.user_id= '%d' ) AND returning_date is NULL", userId);
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            count++;
            System.out.println(String.format("%s,  %s,  %s", resultSet.getString("title"), resultSet.getString("author"), resultSet.getString("release_date")));
        }
        return count;
    }

    public boolean userBorrowThisBook(int userId, String bookName, String booksYearRelese) throws SQLException {
        String query = String.format("SELECT borrowing_date\n" +
                "FROM borrowings\n" +
                "  INNER JOIN books ON borrowings.book_id = books.book_id\n" +
                "WHERE borrowings.user_id = '%d' AND title = '%s' AND release_date = '%s' AND returning_date IS NULL", userId, bookName, booksYearRelese);
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public void deleteUserWithId(int userId) throws SQLException {
        statement.execute(String.format("DELETE FROM users WHERE user_id = '%d'", userId));
    }
}