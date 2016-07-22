package kitchen.dev.icfbooks.esther.model.chapters;

import android.provider.BaseColumns;

/**
 * Created by samue on 03/21/16.
 */
public class ChapterContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    public static final String SQL_CREATE_CHAPTER_TABLE =
            "CREATE TABLE " + ChapterEntry.TABLE_NAME + " (" +
                    ChapterEntry.COLUMN_NAME_ID + TEXT_TYPE + " PRIMARY KEY," +
                    ChapterEntry.COLUMN_NAME_TITLE + TEXT_TYPE + "," +
                    ChapterEntry.COLUMN_NAME_NUMBER + INTEGER_TYPE  + "," +
                    ChapterEntry.COLUMN_NAME_UPDATED_AT + TEXT_TYPE  + "," +
                    ChapterEntry.COLUMN_NAME_BOOK_ID + TEXT_TYPE + ")";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ChapterEntry.TABLE_NAME;

    public static abstract class ChapterEntry implements BaseColumns {
        public static final String TABLE_NAME = "chapter";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_NUMBER = "number";
        public static final String COLUMN_NAME_BOOK_ID = "book_id";
        public static final String COLUMN_NAME_UPDATED_AT = "updated_at";
    }

    public static String upgradeSchema(int targetVersion) {
        switch (targetVersion) {
            case 5:
                return "ALTER TABLE " + ChapterEntry.TABLE_NAME + "\n";
            default:
                throw new UnsupportedOperationException();
        }
    }
}
