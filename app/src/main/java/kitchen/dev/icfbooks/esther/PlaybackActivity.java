package kitchen.dev.icfbooks.esther;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;

import kitchen.dev.icfbooks.esther.MyMediaPlayer;
import android.widget.MediaController;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PlaybackActivity extends AppCompatActivity implements IVLCVout.Callback {

    public static final String ARG_URL = "ARG_URL";

    private String mVideoUrl;

    // display surface
    private SurfaceView mSurface;
    private SurfaceHolder holder;

    // media player
    private LibVLC libvlc;
    private MyMediaPlayer mMediaPlayer = null;
    private int mVideoWidth;
    private int mVideoHeight;

    //private View mVideoView;
    private MediaController mMediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_playback);

        // Receive path to play from intent
        Intent intent = getIntent();
        mVideoUrl = intent.getExtras().getString(ARG_URL);

        mSurface = (SurfaceView) findViewById(R.id.surface);
        //mVideoView = findViewById(R.id.videoview);

        holder = mSurface.getHolder();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setSize(mVideoWidth, mVideoHeight);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createPlayer(mVideoUrl);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    /*************
     * Surface
     *************/
    private void setSize(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoWidth * mVideoHeight <= 1)
            return;

        if(holder == null || mSurface == null)
            return;

        // get screen size
        int w = getWindow().getDecorView().getWidth();
        int h = getWindow().getDecorView().getHeight();

        // getWindow().getDecorView() doesn't always take orientation into
        // account, we have to correct the values
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (w > h && isPortrait || w < h && !isPortrait) {
            int i = w;
            w = h;
            h = i;
        }

        float videoAR = (float) mVideoWidth / (float) mVideoHeight;
        float screenAR = (float) w / (float) h;

        if (screenAR < videoAR)
            h = (int) (w / videoAR);
        else
            w = (int) (h * videoAR);

        // force surface buffer size
        holder.setFixedSize(mVideoWidth, mVideoHeight);

        // set display size
        LayoutParams lp = mSurface.getLayoutParams();
        lp.width = w;
        lp.height = h;
        mSurface.setLayoutParams(lp);
        mSurface.invalidate();
    }

    /*************
     * Player
     *************/

    private void createPlayer(String media) {
        releasePlayer();
        try {

            ArrayList<String> options = new ArrayList<String>();
            options.add("--aout=opensles");
            options.add("--audio-time-stretch"); // time stretching
            options.add("-vvv"); // verbosity
            libvlc = new LibVLC(options);
            holder.setKeepScreenOn(true);

            // Create media player
            mMediaPlayer = new MyMediaPlayer(libvlc);
            mMediaPlayer.setEventListener(mPlayerListener);

            // Set up video output
            final IVLCVout vout = mMediaPlayer.getVLCVout();
            vout.setVideoView(mSurface);
            vout.addCallback(this);
            vout.attachViews();

            //bind to MediaController
            mMediaController = new MediaController(this);
            mMediaController.setAnchorView(mSurface);
            mMediaController.setMediaPlayer(mMediaPlayer);
            mMediaController.setEnabled(true);
            mMediaController.show();

            Media m = new Media(libvlc, Uri.parse(media));
            mMediaPlayer.setMedia(m);
            mMediaPlayer.play();
        } catch (Exception e) {
            Toast.makeText(this, "Error creating player!", Toast.LENGTH_LONG).show();
        }
    }

    private void releasePlayer() {
        if (libvlc == null)
            return;
        mMediaPlayer.stop();
        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.removeCallback(this);
        vout.detachViews();
        holder = null;
        libvlc.release();
        libvlc = null;

        mVideoWidth = 0;
        mVideoHeight = 0;
    }

    /*************
     * Events
     *************/

    private MyMediaPlayer.EventListener mPlayerListener = (MyMediaPlayer.EventListener) new MyPlayerListener(this);

    @Override
    public void onNewLayout(IVLCVout vout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
        if (width * height == 0)
            return;

        // store video size
        mVideoWidth = width;
        mVideoHeight = height;
        setSize(mVideoWidth, mVideoHeight);
    }

    @Override
    public void onSurfacesCreated(IVLCVout vout) {

    }

    @Override
    public void onSurfacesDestroyed(IVLCVout vout) {

    }

    private static class MyPlayerListener implements MyMediaPlayer.EventListener {
    private WeakReference<PlaybackActivity> mOwner;

    public MyPlayerListener(PlaybackActivity owner) {
        mOwner = new WeakReference<PlaybackActivity>(owner);
    }

    @Override
    public void onEvent(MyMediaPlayer.Event event) {
        PlaybackActivity player = mOwner.get();

        switch(event.type) {
            case MyMediaPlayer.Event.EndReached:
                player.releasePlayer();
                break;
            case MyMediaPlayer.Event.Playing:
            case MyMediaPlayer.Event.Paused:
            case MyMediaPlayer.Event.Stopped:
            default:
                break;
        }
    }
}

    @Override
    public void onHardwareAccelerationError(IVLCVout ivlcVout) {
        // Handle errors with hardware acceleration
        this.releasePlayer();
        Toast.makeText(this, "Error with hardware acceleration", Toast.LENGTH_LONG).show();
    }

}
