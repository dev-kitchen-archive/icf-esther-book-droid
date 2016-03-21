package kitchen.dev.icfbooks.model.items;

import android.provider.BaseColumns;

/**
 * Created by noc on 19.02.16.
 */
public final class ItemContract {

    private ItemContract(){}

    private static final String TEXT_TYPE = " TEXT";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
                    ItemEntry.COLUMN_NAME_ID + TEXT_TYPE + " PRIMARY KEY," +
                    ItemEntry.COLUMN_NAME_TITLE + TEXT_TYPE + "," +
                    ItemEntry.COLUMN_NAME_TEASER + TEXT_TYPE + "," +
                    ItemEntry.COLUMN_NAME_TYPE + TEXT_TYPE + "," +
                    ItemEntry.COLUMN_NAME_THUMB_URL + TEXT_TYPE+"," +
                    ItemEntry.COLUMN_NAME_ASSET_URL + TEXT_TYPE + ")";
                    //TODO: add book and chapter reference

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME;

    public static abstract class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TEASER = "teaser";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_THUMB_URL = "thumbUrl";
        public static final String COLUMN_NAME_ASSET_URL = "assetUrl";
    }
}
