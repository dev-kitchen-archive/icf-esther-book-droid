package kitchen.dev.icfbooks.model.books;

/**
 * Created by samue on 03/21/16.
 */
public class Book {
    private int id;
    private String title;
    private String author;
    private String description;
    private String image_url;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public int getId() {
        return id;
    }
}
