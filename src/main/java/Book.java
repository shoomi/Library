public class Book {
    private String title, author, releaseDate;
private int stock;
    public Book() {
    }

    public Book(String title, String author, String releaseDate, int stock) {
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int ctock) {
        this.stock = ctock;
    }
}
