package kitchen.dev.icfbooks.esther.model.media;

import java.util.Date;
import java.util.UUID;

/**
 * Created by noc on 19.02.16.
 */
public class Media<T> {
    private UUID id;
    private String type;
    private String title;
    private String teaser;
    private String thumbnail_url;
    private Date updated_at;
    private Date added_at;
    private T data;


    public Media(){
    }

    public Media(UUID id, String type, String title, String teaser, String thumbnail_url, Date updated_at, Date added_at) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.teaser = teaser;
        this.thumbnail_url = thumbnail_url;
        this.updated_at = updated_at;
        this.added_at = added_at;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getAdded_at() {
        return added_at;
    }

    public void setAdded_at(Date added_at) {
        this.added_at = added_at;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}