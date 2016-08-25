package kitchen.dev.icfbooks.esther;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import kitchen.dev.icfbooks.esther.dal.ApiClient;
import kitchen.dev.icfbooks.esther.dal.SqlHelper;

public class IntroActivity extends AppCompatActivity {
    public final static String SHARED_PREF_SETUP_FINISHED = "SetupFinished";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences pref = getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        final Activity activity = this;
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                if (!pref.getBoolean(SHARED_PREF_SETUP_FINISHED, false)) {
                    ApiClient api = ApiClient.getInstance(getBaseContext());
                    getBaseContext().deleteDatabase(SqlHelper.DATABASE_NAME);
                    final SqlHelper sqlHelper = new SqlHelper(getBaseContext());
                    final SQLiteDatabase sql = sqlHelper.getWritableDatabase();

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(SHARED_PREF_SETUP_FINISHED, true);
                    editor.commit();
                }

                        Intent intent = new Intent(activity, MediaListActivity.class);
                        startActivity(intent);


                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
            }
        }.execute();
    }

}
