package kitchen.dev.icfbooks.esther.model.books;

import android.provider.BaseColumns;

/**
 * Created by samue on 03/21/16.
 */
public class BookContract {

    private static final String TEXT_TYPE = " TEXT";
    public static final String SQL_CREATE_BOOK_TABLE =
            "CREATE TABLE " + BookEntry.TABLE_NAME + " (" +
                    BookEntry.COLUMN_NAME_ID + TEXT_TYPE + " PRIMARY KEY," +
                    BookEntry.COLUMN_NAME_TITLE + TEXT_TYPE + "," +
                    BookEntry.COLUMN_NAME_AUTHOR + TEXT_TYPE + "," +
                    BookEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + "," +
                    BookEntry.COLUMN_NAME_THUMB_IMAGE_URL + TEXT_TYPE+")";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME;

    public static abstract class BookEntry implements BaseColumns {
        public static final String TABLE_NAME = "book";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_UPDATED_AT = "updated_at";
        public static final String COLUMN_NAME_THUMB_IMAGE_URL = "image_url";
    }

    public static void upgradeSchema(int targetVersion) {

    }
}
