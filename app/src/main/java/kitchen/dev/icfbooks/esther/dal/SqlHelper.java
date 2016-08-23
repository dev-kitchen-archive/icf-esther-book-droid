package kitchen.dev.icfbooks.esther.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kitchen.dev.icfbooks.esther.model.media.MediaContract;

/**
 * Created by noc on 19.02.16.
 */
public class SqlHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "data.db";

    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MediaContract.SQL_CREATE_MEDIA_TABLE);
        System.out.println("created tables in database " + DATABASE_NAME + ".");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("upgrade tables in database " + DATABASE_NAME + ".");
        db.execSQL(MediaContract.SQL_DROP_MEDIA_TABLE);
        db.execSQL(MediaContract.SQL_CREATE_MEDIA_TABLE);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String convertFromDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    public Date convertToDateTime(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.parse(date);
    }
}
