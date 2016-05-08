package kitchen.dev.icfbooks.model.chapters;

import java.util.Date;

/**
 * Created by samue on 03/21/16.
 */
public class Chapter {
    private int id;
    private String title;
    private int number;
    private Date updated_at;

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

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
