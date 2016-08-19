package kitchen.dev.icfbooks.esther;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import kitchen.dev.icfbooks.esther.dal.ApiClient;
import kitchen.dev.icfbooks.esther.dal.ApiResultHandler;
import kitchen.dev.icfbooks.esther.model.media.Media;
import kitchen.dev.icfbooks.esther.model.media.MediaFactory;
import kitchen.dev.icfbooks.esther.model.media.MediaTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MediaListActivity extends AppCompatActivity {

    static final int SCANNER_REQUEST = 1;

    public final static String BUNDLE_BOOK_ID = "BookID";
    public final static String SHARED_PREF_SETUP_FINISHED = "SetupFinished";

    private List<Media> itemList;
    private ApiClient api;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE);

        api = ApiClient.getInstance(getBaseContext());

        if (!pref.getBoolean(SHARED_PREF_SETUP_FINISHED, false)) {
            Intent intent = new Intent(this, SplashScreen.class);
            startActivity(intent);
            return;
        }

        itemList = (ArrayList<Media>) MediaFactory.getInstance(getApplicationContext()).getAllItems();
        setContentView(R.layout.activity_item_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start barcode scanner on click
                Intent intent = new Intent(MediaListActivity.this, BarcodeScannerActivity.class);
                startActivityForResult(intent, SCANNER_REQUEST);
            }
        });

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    // get url from barcode and add new item
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SCANNER_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                final Toast loadingToast = Toast.makeText(getBaseContext(),R.string.loading,Toast.LENGTH_LONG);
                Uri url = Uri.parse(data.getStringExtra("url"));
                api.getMedia(url.getLastPathSegment(), new ApiResultHandler<Media>() {
                    @Override
                    public void onResult(Media result) {
                        loadingToast.cancel();
                        MediaFactory media = MediaFactory.getInstance(getBaseContext());

                        if (media.getItem(result.getId()) == null) {
                            media.saveItem(result);
                        }

                        Intent intent = new Intent(getBaseContext(), PlaybackActivity.class);
                        intent.putExtra(PlaybackActivity.ARG_URL, ((Media<MediaTypes.Movie>)result).getData().getFile_url());
                        getBaseContext().startActivity(intent);

                        //Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                        //intent.putExtra(DetailActivity.ARG_DETAIL_ID, result.getId().toString());

                        //getBaseContext().startActivity(intent);
                    }

                    @Override
                    public void onError(Object error) {
                        loadingToast.cancel();
                        Toast.makeText(MediaListActivity.this, getString(R.string.invalid_qr_error), Toast.LENGTH_LONG).show();
                    }
                });
                //TODO: act on recieved url (extract UUID, start intent for detail, detail getItem from factory)
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().toString().equals(this.getString(R.string.menu_about))) {
            Intent intent = new Intent(getBaseContext(), AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(itemList));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Media> mValues;

        public SimpleItemRecyclerViewAdapter(List<Media> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);

            Bitmap bmp = BitmapFactory.decodeFile(getBaseContext().getFileStreamPath(mValues.get(position).getId().toString()).getAbsolutePath());
            holder.mImage.setImageBitmap(bmp);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), PlaybackActivity.class);
                    intent.putExtra(PlaybackActivity.ARG_URL, ((Media<MediaTypes.Movie>)holder.mItem).getData().getFile_url());
                    getBaseContext().startActivity(intent);
                    //Context context = v.getContext();
                    //Intent intent = new Intent(context, DetailActivity.class);
                    //intent.putExtra(DetailActivity.ARG_DETAIL_ID, holder.mItem.getId().toString());
                    //context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImage;
            public Media mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImage = (ImageView) view.findViewById(R.id.list_thumbnail);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mItem.getTitle() + "'";
            }
        }
    }
}
