package kitchen.dev.icfbooks.esther;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PlaybackActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link } milliseconds.
     */
    //private static final boolean AUTO_HIDE = true;
    public static final String ARG_URL = "ARG_URL";

    private VideoView mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_playback);

        mContentView = (VideoView) findViewById(R.id.videoView);


        if (savedInstanceState == null) {
            VideoView video = (VideoView)findViewById(R.id.videoView);
            video.setVideoURI(Uri.parse(getIntent().getStringExtra(PlaybackActivity.ARG_URL)));
            video.setMediaController(new MediaController(this));

            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    findViewById(R.id.spinner).setVisibility(View.GONE);
                    VideoView video = (VideoView)findViewById(R.id.videoView);
                    video.setVisibility(View.VISIBLE);
                    video.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    //progDailog.dismiss();
                }
            });

            video.requestFocus();
            video.start();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(new Intent(this,MediaListActivity.class));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
