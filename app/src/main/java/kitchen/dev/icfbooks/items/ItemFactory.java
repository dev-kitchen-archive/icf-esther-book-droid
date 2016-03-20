package kitchen.dev.icfbooks.items;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import kitchen.dev.icfbooks.api.ApiHelper;

/**
 * Created by noc on 19.02.16.
 */
public class ItemFactory {

    private static ItemFactory itemFactory;
    private ItemSqlHelper dbHelper;
    private static Context context;
    private ApiHelper apiHelper;

    private ItemFactory(){
        this.dbHelper = new ItemSqlHelper(context);
        this.apiHelper = new ApiHelper(context);
    }

    public static ItemFactory getInstance(Context context){
        if(itemFactory == null){
            itemFactory = new ItemFactory();
        }
        ItemFactory.context = context;
        return itemFactory;
    }

    public List<Item> getAllItems(){
        List<Item> itemList = new ArrayList<Item>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ItemContract.ItemEntry.COLUMN_NAME_ID,
                ItemContract.ItemEntry.COLUMN_NAME_TITLE,
                ItemContract.ItemEntry.COLUMN_NAME_TEASER,
                ItemContract.ItemEntry.COLUMN_NAME_TYPE,
                ItemContract.ItemEntry.COLUMN_NAME_THUMB_URL,
                ItemContract.ItemEntry.COLUMN_NAME_ASSET_URL
        };

        Cursor c = db.query(ItemContract.ItemEntry.TABLE_NAME, projection, null, null, null, null, null);

        c.moveToFirst();

        while(c.moveToNext()) {
            UUID uuid = (UUID.fromString(c.getString(c.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME_ID))));
            String title = c.getString(c.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME_TITLE));
            String teaser = c.getString(c.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME_TEASER));
            String type = c.getString(c.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME_TYPE));
            String thumbUrl = c.getString(c.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME_THUMB_URL));
            String assetUrl = c.getString(c.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME_ASSET_URL));

            itemList.add(createItem(uuid, title, type, teaser, thumbUrl, assetUrl));
        }

        return itemList;
    }

    public Item getItem(UUID id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ItemContract.ItemEntry.COLUMN_NAME_ID,
                ItemContract.ItemEntry.COLUMN_NAME_TITLE,
                ItemContract.ItemEntry.COLUMN_NAME_TEASER,
                ItemContract.ItemEntry.COLUMN_NAME_TYPE,
                ItemContract.ItemEntry.COLUMN_NAME_THUMB_URL,
                ItemContract.ItemEntry.COLUMN_NAME_ASSET_URL
        };

        // Define 'where' part of query.
        String selection = ItemContract.ItemEntry.COLUMN_NAME_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { id.toString() };

        // How you want the results sorted in the resulting Cursor
        // TODO: add order possibility
        /*String sortOrder =
                FeedEntry.COLUMN_NAME_UPDATED + " DESC";*/

        Cursor c = db.query(
                ItemContract.ItemEntry.TABLE_NAME,      // The table to query
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
                                    c.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME_ID)
                            )
                    )
            );
            String title = c.getString(c.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME_TITLE));
            String teaser = c.getString(c.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME_TEASER));
            String type = c.getString(c.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME_TYPE));
            String thumbUrl = c.getString(c.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME_THUMB_URL));
            String assetUrl = c.getString(c.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME_ASSET_URL));
            return createItem(uuid, title, type, teaser, thumbUrl, assetUrl);

        }
    }

    public Item addItem(UUID id, String title, String type, String teaser, String thumbUrl, String assetUrl){
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_NAME_ID, id.toString());
        values.put(ItemContract.ItemEntry.COLUMN_NAME_TITLE, title);
        values.put(ItemContract.ItemEntry.COLUMN_NAME_TEASER, teaser);
        values.put(ItemContract.ItemEntry.COLUMN_NAME_TYPE, type);
        values.put(ItemContract.ItemEntry.COLUMN_NAME_THUMB_URL, thumbUrl);
        values.put(ItemContract.ItemEntry.COLUMN_NAME_ASSET_URL, assetUrl);

        // Insert the new row, returning the primary key value of the new row
        db.insert(ItemContract.ItemEntry.TABLE_NAME, null, values);

        return createItem(id, type, title, teaser, thumbUrl, assetUrl);
    }

    public void deleteItem(UUID id){
        //TODO: implement deleteItem
    }

    public Item createItem(UUID id, String title, String type, String teaser, String thumbUrl, String assetUrl){
        Item item = new Item(id);
        item.setAssetUrl(assetUrl);
        item.setThumpUrl(thumbUrl);
        item.setType(type);
        item.setTeaser(teaser);
        item.setTitle(title);

        return item;
    }


}
