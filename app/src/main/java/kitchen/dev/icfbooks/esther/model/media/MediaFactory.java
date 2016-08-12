package kitchen.dev.icfbooks.esther.model.media;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.annotations.JsonAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import kitchen.dev.icfbooks.esther.dal.ApiClient;
import kitchen.dev.icfbooks.esther.dal.SqlHelper;

/**
 * Created by noc on 19.02.16.
 */
public class MediaFactory {

    private static MediaFactory itemFactory;
    private SqlHelper dbHelper;
    private static Context context;
    private ApiClient api;

    private MediaFactory() {
        this.dbHelper = new SqlHelper(context);
        this.api = ApiClient.getInstance(context);
    }

    public static MediaFactory getInstance(Context context) {
        //TODO check this code
        MediaFactory.context = context;
        if (itemFactory == null) {
            itemFactory = new MediaFactory();
        }
        return itemFactory;
    }

    public List<Media> getAllItems() {
        List<Media> itemList = new ArrayList<Media>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();


        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MediaContract.MediaEntry.COLUMN_NAME_ID,
                MediaContract.MediaEntry.COLUMN_NAME_TITLE,
                MediaContract.MediaEntry.COLUMN_NAME_TEASER,
                MediaContract.MediaEntry.COLUMN_NAME_TYPE,
                MediaContract.MediaEntry.COLUMN_NAME_THUMB_URL,
                MediaContract.MediaEntry.COLUMN_NAME_DATA
        };

        Cursor c = db.query(MediaContract.MediaEntry.TABLE_NAME, projection, null, null, null, null, null);

        c.moveToFirst();

        while (c.moveToNext()) {
            itemList.add(getMedia(c));
        }

        //Media m = new Media(UUID.fromString("4f7d85a6-3313-412b-850b-a3272f1981e6"));
        //m.setTitle("Test 1");
        //itemList.add(m);
//
        //m = new Media(UUID.fromString("4f7d85a6-3313-412b-850b-a3272f1981e6"));
        //m.setTitle("Test 2");
        //itemList.add(m);

        return itemList;
    }

    public Media getItem(UUID id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MediaContract.MediaEntry.COLUMN_NAME_ID,
                MediaContract.MediaEntry.COLUMN_NAME_TITLE,
                MediaContract.MediaEntry.COLUMN_NAME_TEASER,
                MediaContract.MediaEntry.COLUMN_NAME_TYPE,
                MediaContract.MediaEntry.COLUMN_NAME_THUMB_URL,
                MediaContract.MediaEntry.COLUMN_NAME_DATA,
                MediaContract.MediaEntry.COLUMN_NAME_UPDATED_AT
        };

        // Define 'where' part of query.
        String selection = MediaContract.MediaEntry.COLUMN_NAME_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {id.toString()};

        // How you want the results sorted in the resulting Cursor
        // TODO: add order possibility
        /*String sortOrder =
                FeedEntry.COLUMN_NAME_UPDATED + " DESC";*/

        Cursor c = db.query(
                MediaContract.MediaEntry.TABLE_NAME,      // The table to query
                projection,                             // The columns to return
                selection,                              // The columns for the WHERE clause
                selectionArgs,                          // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );

        //TODO: check if entry is up to date

        c.moveToFirst();
        return getMedia(c);

    }

    private Media getMedia(Cursor cursor) {
        if(cursor.getCount() == 0) {
            return null;
        }

        UUID uuid = (UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(MediaContract.MediaEntry.COLUMN_NAME_ID))));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaContract.MediaEntry.COLUMN_NAME_TITLE));
        String teaser = cursor.getString(cursor.getColumnIndexOrThrow(MediaContract.MediaEntry.COLUMN_NAME_TEASER));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(MediaContract.MediaEntry.COLUMN_NAME_TYPE));
        String thumbUrl = cursor.getString(cursor.getColumnIndexOrThrow(MediaContract.MediaEntry.COLUMN_NAME_THUMB_URL));
        String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaContract.MediaEntry.COLUMN_NAME_DATA));
        Date date = new Date();
        try {
            date = dbHelper.convertToDateTime(cursor.getString(cursor.getColumnIndexOrThrow(MediaContract.MediaEntry.COLUMN_NAME_UPDATED_AT)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createItem(uuid, type, title, teaser, thumbUrl, date, data);
    }

    public void saveItem(Media media) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MediaContract.MediaEntry.COLUMN_NAME_ID, media.getId().toString());
        values.put(MediaContract.MediaEntry.COLUMN_NAME_TITLE, media.getTitle());
        values.put(MediaContract.MediaEntry.COLUMN_NAME_TEASER, media.getTeaser());
        values.put(MediaContract.MediaEntry.COLUMN_NAME_TYPE, media.getType());
        values.put(MediaContract.MediaEntry.COLUMN_NAME_THUMB_URL, media.getThumbnail_url());
        values.put(MediaContract.MediaEntry.COLUMN_NAME_DATA, new Gson().toJson(media.getData()));
        values.put(MediaContract.MediaEntry.COLUMN_NAME_UPDATED_AT, dbHelper.convertFromDateTime(media.getUpdated_at()));

        // Insert the new row, returning the primary key value of the new row
        db.insert(MediaContract.MediaEntry.TABLE_NAME, null, values);
    }

    public Media createItem(UUID id, String type, String title, String teaser, String thumbnail_url, Date updated_at, String data) {
        Media media = null;
        Gson gson = new GsonBuilder().create();
        switch (type) {
            case "movie":
                media = new Media<MediaTypes.Movie>();
                media.setData(gson.fromJson(data, MediaTypes.Movie.class));
                break;
            case "two_movies_and_text":
                media = new Media<MediaTypes.TwoMoviesAndText>();
                media.setData(gson.fromJson(data, MediaTypes.TwoMoviesAndText.class));
                break;
        }

        media.setId(id);
        media.setType(type);
        media.setTitle(title);
        media.setTeaser(teaser);
        media.setThumbnail_url(thumbnail_url);
        media.setUpdated_at(updated_at);

        return media;
    }

    public void deleteItem(UUID id) {
        //TODO: implement deleteItem
    }

}
