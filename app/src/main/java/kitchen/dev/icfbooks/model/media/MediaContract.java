package kitchen.dev.icfbooks.model.media;

import android.provider.BaseColumns;

/**
 * Created by noc on 19.02.16.
 */
public final class MediaContract {

    private MediaContract(){}

    private static final String TEXT_TYPE = " TEXT";
    public static final String SQL_CREATE_MEDIA_TABLE =
            "CREATE TABLE " + MediaEntry.TABLE_NAME + " (" +
                    MediaEntry.COLUMN_NAME_ID + TEXT_TYPE + " PRIMARY KEY," +
                    MediaEntry.COLUMN_NAME_TITLE + TEXT_TYPE + "," +
                    MediaEntry.COLUMN_NAME_TEASER + TEXT_TYPE + "," +
                    MediaEntry.COLUMN_NAME_TYPE + TEXT_TYPE + "," +
                    MediaEntry.COLUMN_NAME_THUMB_URL + TEXT_TYPE+"," +
                    MediaEntry.COLUMN_NAME_UPDATED_AT + TEXT_TYPE+"," +
                    MediaEntry.COLUMN_NAME_DATA + TEXT_TYPE + ")";
                    //TODO: add book and chapter reference

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MediaEntry.TABLE_NAME;

    public static abstract class MediaEntry implements BaseColumns {
        public static final String TABLE_NAME = "media";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TEASER = "teaser";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_THUMB_URL = "thumbUrl";
        public static final String COLUMN_NAME_DATA = "data";
        public static final String COLUMN_NAME_UPDATED_AT = "updated_at";
    }

    public static void upgradeSchema(int targetVersion) {

    }
}
