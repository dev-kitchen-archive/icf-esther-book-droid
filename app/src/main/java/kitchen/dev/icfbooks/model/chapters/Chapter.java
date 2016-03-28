package kitchen.dev.icfbooks.model.chapters;

/**
 * Created by samue on 03/21/16.
 */
public class Chapter {
    private int id;
    private String title;
    private int book_id;
    private int number;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
