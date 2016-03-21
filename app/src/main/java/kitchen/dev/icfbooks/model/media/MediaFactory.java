package kitchen.dev.icfbooks.model.media;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import kitchen.dev.icfbooks.dal.ApiHelper;
import kitchen.dev.icfbooks.dal.SqlHelper;

/**
 * Created by noc on 19.02.16.
 */
public class MediaFactory {

    private static MediaFactory itemFactory;
    private SqlHelper dbHelper;
    private static Context context;
    private ApiHelper apiHelper;

    private MediaFactory(){
        this.dbHelper = new SqlHelper(context);
        this.apiHelper = new ApiHelper(context);
    }

    public static MediaFactory getInstance(Context context){
        //TODO check this code
        MediaFactory.context = context;
        if(itemFactory == null){
            itemFactory = new MediaFactory();
        }
        return itemFactory;
    }

    public List<Media> getAllItems(){
        List<Media> itemList = new ArrayList<Media>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();


        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MediaContract.ItemEntry.COLUMN_NAME_ID,
                MediaContract.ItemEntry.COLUMN_NAME_TITLE,
                MediaContract.ItemEntry.COLUMN_NAME_TEASER,
                MediaContract.ItemEntry.COLUMN_NAME_TYPE,
                MediaContract.ItemEntry.COLUMN_NAME_THUMB_URL,
                MediaContract.ItemEntry.COLUMN_NAME_ASSET_URL
        };

        Cursor c = db.query(MediaContract.ItemEntry.TABLE_NAME, projection, null, null, null, null, null);

        c.moveToFirst();

        while(c.moveToNext()) {
            UUID uuid = (UUID.fromString(c.getString(c.getColumnIndexOrThrow(MediaContract.ItemEntry.COLUMN_NAME_ID))));
            String title = c.getString(c.getColumnIndexOrThrow(MediaContract.ItemEntry.COLUMN_NAME_TITLE));
            String teaser = c.getString(c.getColumnIndexOrThrow(MediaContract.ItemEntry.COLUMN_NAME_TEASER));
            String type = c.getString(c.getColumnIndexOrThrow(MediaContract.ItemEntry.COLUMN_NAME_TYPE));
            String thumbUrl = c.getString(c.getColumnIndexOrThrow(MediaContract.ItemEntry.COLUMN_NAME_THUMB_URL));
            String assetUrl = c.getString(c.getColumnIndexOrThrow(MediaContract.ItemEntry.COLUMN_NAME_ASSET_URL));

            itemList.add(createItem(uuid, title, type, teaser, thumbUrl, assetUrl));
        }

        return itemList;
    }

    public Media getItem(UUID id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MediaContract.ItemEntry.COLUMN_NAME_ID,
                MediaContract.ItemEntry.COLUMN_NAME_TITLE,
                MediaContract.ItemEntry.COLUMN_NAME_TEASER,
                MediaContract.ItemEntry.COLUMN_NAME_TYPE,
                MediaContract.ItemEntry.COLUMN_NAME_THUMB_URL,
                MediaContract.ItemEntry.COLUMN_NAME_ASSET_URL
        };

        // Define 'where' part of query.
        String selection = MediaContract.ItemEntry.COLUMN_NAME_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { id.toString() };

        // How you want the results sorted in the resulting Cursor
        // TODO: add order possibility
        /*String sortOrder =
                FeedEntry.COLUMN_NAME_UPDATED + " DESC";*/

        Cursor c = db.query(
                MediaContract.ItemEntry.TABLE_NAME,      // The table to query
                projection,                             // The columns to return
                selection,                              // The columns for the WHERE clause
                selectionArgs,                          // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );

        //TODO: check if entry is up to date

        if(c.getCount() == 0){
            return apiHelper.getItem(id.toString());
            //TODO: save thumbnails to db
        }else {
            c.moveToFirst();
            UUID uuid = (UUID.fromString(
                            c.getString(
                                    c.getColumnIndexOrThrow(MediaContract.ItemEntry.COLUMN_NAME_ID)
                            )
                    )
            );
            String title = c.getString(c.getColumnIndexOrThrow(MediaContract.ItemEntry.COLUMN_NAME_TITLE));
            String teaser = c.getString(c.getColumnIndexOrThrow(MediaContract.ItemEntry.COLUMN_NAME_TEASER));
            String type = c.getString(c.getColumnIndexOrThrow(MediaContract.ItemEntry.COLUMN_NAME_TYPE));
            String thumbUrl = c.getString(c.getColumnIndexOrThrow(MediaContract.ItemEntry.COLUMN_NAME_THUMB_URL));
            String assetUrl = c.getString(c.getColumnIndexOrThrow(MediaContract.ItemEntry.COLUMN_NAME_ASSET_URL));
            return createItem(uuid, title, type, teaser, thumbUrl, assetUrl);

        }
    }

    public Media addItem(UUID id, String title, String type, String teaser, String thumbUrl, String assetUrl){
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MediaContract.ItemEntry.COLUMN_NAME_ID, id.toString());
        values.put(MediaContract.ItemEntry.COLUMN_NAME_TITLE, title);
        values.put(MediaContract.ItemEntry.COLUMN_NAME_TEASER, teaser);
        values.put(MediaContract.ItemEntry.COLUMN_NAME_TYPE, type);
        values.put(MediaContract.ItemEntry.COLUMN_NAME_THUMB_URL, thumbUrl);
        values.put(MediaContract.ItemEntry.COLUMN_NAME_ASSET_URL, assetUrl);

        // Insert the new row, returning the primary key value of the new row
        db.insert(MediaContract.ItemEntry.TABLE_NAME, null, values);

        return createItem(id, type, title, teaser, thumbUrl, assetUrl);
    }

    public void deleteItem(UUID id){
        //TODO: implement deleteItem
    }

    public Media createItem(UUID id, String title, String type, String teaser, String thumbUrl, String assetUrl){
        Media item = new Media(id);
        item.setAssetUrl(assetUrl);
        item.setThumpUrl(thumbUrl);
        item.setType(type);
        item.setTeaser(teaser);
        item.setTitle(title);

        return item;
    }


}
