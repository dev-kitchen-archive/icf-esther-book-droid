package kitchen.dev.icfbooks.esther;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.UUID;

import kitchen.dev.icfbooks.esther.model.media.Media;
import kitchen.dev.icfbooks.esther.model.media.MediaFactory;
import kitchen.dev.icfbooks.esther.model.media.MediaTypes;

public class DetailActivity extends AppCompatActivity {

    public static final String ARG_DETAIL_ID = "ARG_DETAIL_ID";
    private Media<MediaTypes.Movie> media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            UUID id = UUID.fromString(getIntent().getStringExtra(DetailActivity.ARG_DETAIL_ID));
            media = MediaFactory.getInstance(getBaseContext()).getItem(id);
            loadMedia();
        }
    }

    private void loadMedia(){
        this.setTitle(media.getTitle());
        ImageView img = (ImageView) findViewById(R.id.detail_thumbnail);
        Bitmap bmp = BitmapFactory.decodeFile(getBaseContext().getFileStreamPath(media.getId().toString()).getAbsolutePath());
        img.setImageBitmap(bmp);
    }

    public void onThumbnailClick(View view) {
        Intent intent = new Intent(getBaseContext(), PlaybackActivity.class);
        intent.putExtra(PlaybackActivity.ARG_URL, media.getData().getFile_url());
        getBaseContext().startActivity(intent);
    }
}