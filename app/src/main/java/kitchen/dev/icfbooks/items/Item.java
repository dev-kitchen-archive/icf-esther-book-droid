package kitchen.dev.icfbooks.items;

import java.util.UUID;

/**
 * Created by noc on 19.02.16.
 */
public class Item {
    private UUID id;
    private String type;
    private String title;
    private String teaser;
    private String thumpUrl;
    private String assetUrl;

    Item(UUID id){
        this.id = id;
    }

    public void destroy(){

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

    public String getThumpUrl() {
        return thumpUrl;
    }

    public void setThumpUrl(String thumpUrl) {
        this.thumpUrl = thumpUrl;
    }

    public String getAssetUrl() {
        return assetUrl;
    }

    public void setAssetUrl(String assetUrl) {
        this.assetUrl = assetUrl;
    }

}