package raymondhernandez.pocketuniv.Model;

/**
 * Created 4/8/2016.
 */
public class Book {
    private String title;
    private String author;
    private String subject;
    private float rating;

    public Book(){    }

    public Book(String title, String author, String subject, float rating) {
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.rating = rating;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
