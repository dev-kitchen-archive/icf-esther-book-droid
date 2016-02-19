package kitchen.dev.books_proto.api;

import android.content.Context;

import kitchen.dev.books_proto.items.Item;
import kitchen.dev.books_proto.items.ItemFactory;

/**
 * Created by noc on 19.02.16.
 */
public class ApiHelper {

    private Context context;

    private static final String MEDIA_URL = "https://rhino.dev.kitchen/media/";

    public ApiHelper(Context context){
        this.context = context;
    }

    public Item getItem(String id){
        String url = MEDIA_URL+id+".json";
        //TODO: create url and get data



        return ItemFactory.getInstance(this.context).addItem(uuid, title, type, teaser, thumbUrl, assetUrl);
    }

    //TODO: getVideo
    //TODO: getPic
}
