package kitchen.dev.icfbooks.esther;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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


import com.google.firebase.analytics.FirebaseAnalytics;

import kitchen.dev.icfbooks.esther.dal.ApiClient;
import kitchen.dev.icfbooks.esther.dal.ApiResultHandler;
import kitchen.dev.icfbooks.esther.model.media.Media;
import kitchen.dev.icfbooks.esther.model.media.MediaFactory;
import kitchen.dev.icfbooks.esther.model.media.MediaTypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MediaListActivity extends AppCompatActivity {

    static final int SCANNER_REQUEST = 1;

    public final static String BUNDLE_BOOK_ID = "BookID";
    public final static String SHARED_PREF_SETUP_FINISHED = "SetupFinished";

    private List<Media> itemList;
    private ApiClient api;

    private FirebaseAnalytics mFirebaseAnalytics;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        api = ApiClient.getInstance(getBaseContext());

        if (!pref.getBoolean(SHARED_PREF_SETUP_FINISHED, false)) {
            Intent intent = new Intent(this, SplashScreen.class);
            startActivity(intent);
            return;
        }

        itemList = (ArrayList<Media>) MediaFactory.getInstance(getApplicationContext()).getAllItems();
        setContentView(R.layout.activity_item_list);

        final Context context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start barcode scanner on click
                Intent intent = new Intent(context, BarcodeScannerActivity.class);
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
                final Context context = this;
                final Toast loadingToast = Toast.makeText(getApplicationContext(),R.string.loading,Toast.LENGTH_LONG);
                Uri url = Uri.parse(data.getStringExtra("url"));
                api.getMedia(url.getLastPathSegment(), new ApiResultHandler<Media>() {
                    @Override
                    public void onResult(Media result) {
                        loadingToast.cancel();
                        MediaFactory media = MediaFactory.getInstance(getApplicationContext());

                        result.setAdded_at(new Date());

                        if (media.getItem(result.getId()) == null) {
                            media.saveItem(result);
                        }

                        Intent intent = new Intent(context, PlaybackActivity.class);
                        intent.putExtra(PlaybackActivity.ARG_URL, ((Media<MediaTypes.Movie>)result).getData().getFile_url());
                        context.startActivity(intent);

                        //Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                        //intent.putExtra(DetailActivity.ARG_DETAIL_ID, result.getId().toString());

                        //getBaseContext().startActivity(intent);
                    }

                    @Override
                    public void onError(Object error) {
                        loadingToast.cancel();
                        Toast.makeText(getApplicationContext(), getString(R.string.invalid_qr_error), Toast.LENGTH_LONG).show();
                    }
                });
                //TODO: act on recieved url (extract UUID, start intent for detail, detail getItem from factory)
            }
        }
    }

    @Override
    protected void onResume() {
        itemList = (ArrayList<Media>) MediaFactory.getInstance(getApplicationContext()).getAllItems();
        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //TODO activate about menu
        //getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().toString().equals(this.getString(R.string.menu_about))) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(itemList,this));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Media> mValues;
        private final Context mContext;

        public SimpleItemRecyclerViewAdapter(List<Media> items, Context context) {
            mValues = items;
            mContext = context;
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
                    Intent intent = new Intent(mContext, PlaybackActivity.class);
                    intent.putExtra(PlaybackActivity.ARG_URL, ((Media<MediaTypes.Movie>)holder.mItem).getData().getFile_url());
                    mContext.startActivity(intent);
                    //Context context = v.getContext();
                    //Intent intent = new Intent(context, DetailActivity.class);
                    //intent.putExtra(DetailActivity.ARG_DETAIL_ID, holder.mItem.getId().toString());
                    //context.startActivity(intent);
                }
            });

            if(BuildConfig.DEBUG) {
                holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        MediaFactory.getInstance(getApplicationContext()).deleteItem(holder.mItem);

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);


                        return true;
                    }
                });
            }
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
