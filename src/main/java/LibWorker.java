import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LibWorker {

    static int idCount = 0;
    DBWarker dbWarker = new DBWarker();
    Statement statement = dbWarker.getConnection().createStatement();
    ResultSet resultSet;
    CurentTime time = new CurentTime();

    public LibWorker() throws SQLException {
    }

    public void showAllFreeBooksFromDb() throws SQLException {    /// shows all books from Db
        resultSet = statement.executeQuery("SELECT DISTINCT title, author FROM books WHERE available > 0");

        while (resultSet.next()) {
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");
//            String releaseDate = resultSet.getString("release_date");
            System.out.println(title + " / " + author);
        }
    }

    public int getDatafromAllFreeBooksByNameFromDb(String booksTitle) throws SQLException {    /// shows all books from Db
        resultSet = statement.executeQuery("SELECT title, author, release_date FROM books WHERE available > 0 AND title = '" + booksTitle + "'");
        int count =0;
        while (resultSet.next()) {
            count++;
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");
            String releaseDate = resultSet.getString("release_date");
            System.out.println(title + " / " + author + " / " + releaseDate);
        }
        return count;
    }

    public String getDateOfTheBook (String booksTitle) throws SQLException {
        resultSet = statement.executeQuery("SELECT release_date FROM books WHERE available > 0 AND title = '" + booksTitle + "'");
        String booksDate = null;
        while (resultSet.next()) {
            booksDate = resultSet.getString("release_date");
        }
        return booksDate;
    }

    public void addNewUserToDbUsers(User libUser) throws SQLException {
        String addNewUser = String.format("INSERT INTO  mylibrary.`users` (login, first_name, last_name, date_of_birth, phone_number) VALUES ('%s','%s', '%s', '%s', '%s')", libUser.getLogin(), libUser.getFirst_name(), libUser.getLast_name(), libUser.getDate_of_birth(), libUser.getTelephone());
        statement.execute(addNewUser);
    }

    public void addNewBookToDbBooks(Book book) throws SQLException {
        String addNewBook = String.format("INSERT INTO  mylibrary.`books` (title, author, release_date, stock) VALUES ('%s','%s', '%s', %d)", book.getTitle(), book.getAuthor(), book.getReleaseDate(), book.getStock());
        statement.execute(addNewBook);
        statement.execute("UPDATE mylibrary.books SET available = books.available + stock WHERE books.title = '" + book.getTitle() + "' AND books.author = '" + book.getAuthor() + "' AND books.release_date = '" + book.getReleaseDate() + "'");
    }

    public int getIdUserbyLogin(String userlogin) throws SQLException {
        resultSet = statement.executeQuery("SELECT user_id FROM users WHERE login = '" + userlogin + "'");
        int userId = -1;
        if (resultSet.next()) {
            userId = resultSet.getInt("user_id");
        }
        return userId;
    }

    public int getIdBookByName(String bookName, String booksYearRelese) throws SQLException {
        resultSet = statement.executeQuery("SELECT book_id FROM books WHERE title = '" + bookName + "' AND books.release_date ='" + booksYearRelese + "'");
        int bookId = -1;
        if (resultSet.next()) {
            bookId = resultSet.getInt("book_id");
        }
        return bookId;
    }


    public void giveNewBookToUser(int userId, String bookName, String booksYearRelese) throws SQLException {
        int bookId = getIdBookByName(bookName, booksYearRelese);
        addUsersBookToDbBorrowings(userId, bookId, booksYearRelese);
        System.out.println("This book is yours! Nice reading");
    }

    public void addUsersBookToDbBorrowings(int idUser, int idBook, String booksYearRelese) throws SQLException {
        statement.execute(String.format("INSERT INTO  mylibrary.`borrowings` (user_id, book_id, borrowing_date) VALUES ('%d','%d', '%s')", idUser, idBook, time.getCurrentTime()));
        statement.execute(String.format("UPDATE mylibrary.books SET available = available - 1 WHERE book_id='" + idBook + "' AND release_date = '" + booksYearRelese + "'"));
    }

    public boolean bookIsFree(String bookName, String booksYearRelese) throws SQLException {
        resultSet = statement.executeQuery("SELECT books.title FROM books WHERE books.title = '" + bookName + "' and books.available > 0 AND books.release_date ='" + booksYearRelese + "'");
        int countFreeBooks = 0;
        while (resultSet.next()) {
            countFreeBooks++;
        }
        return countFreeBooks > 0;
    }

    public boolean bookIsInLibrary(String bookName) throws SQLException {
        resultSet = statement.executeQuery("SELECT books.title FROM books WHERE books.title = '" + bookName + "'");
        int countBooks = 0;
        while (resultSet.next()) {
            countBooks++;
        }
        return countBooks > 0;
    }

    public void returnUserBook(int idUser, String bookName, String booksYearRelese) throws SQLException {
        int idBook = getIdBookByName(bookName, booksYearRelese);
        statement.execute(String.format("UPDATE mylibrary.borrowings SET returning_date='" + time.getCurrentTime() + "' WHERE user_id='" + idUser + "' and book_id='" + idBook + "'"));
        statement.execute(String.format("UPDATE mylibrary.books SET available = available + 1 WHERE book_id='" + idBook + "'"));
    }

    public int borrowedUsersBooks(int userId) throws SQLException {
        int count = 0;
        String query = "SELECT\n" +
                "  title,\n" +
                "  author,\n" +
                "  release_date\n" +
                "FROM ((borrowings\n" +
                "  INNER JOIN books ON borrowings.book_id =books.book_id)\n" +
                "  INNER JOIN users ON borrowings.user_id = users.user_id) WHERE (users.user_id=" + userId + ") AND returning_date is NULL";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            count++;
            String bookTitle = resultSet.getString("title");
            String bookAuthor = resultSet.getString("author");
            String releaseDate = resultSet.getString("release_date");
            System.out.println(bookTitle + " / " + bookAuthor + " / " + releaseDate);
        }
        return count;
    }

    public boolean doesUserBorrowTheBook(int userId, String bookName, String booksYearRelese) throws SQLException {
        int count = 0;
        String query = "select borrowing_date FROM borrowings INNER JOIN books on borrowings.book_id = books.book_id WHERE borrowings.user_id = '" + userId + "' AND title = '" + bookName + "' AND release_date = '" + booksYearRelese + "' AND returning_date is NULL";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            count++;
        }
        return count > 0;
    }

    public void deleteUserWithId(Integer id) throws SQLException {
        statement.execute("DELETE FROM users WHERE `user_id`=" + "'" + id + "'");
    }
}