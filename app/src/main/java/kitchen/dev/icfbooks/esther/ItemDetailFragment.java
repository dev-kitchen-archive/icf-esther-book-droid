package kitchen.dev.icfbooks.esther;

import android.app.Activity;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.net.URL;
import java.util.UUID;

import kitchen.dev.icfbooks.esther.dal.ApiClient;
import kitchen.dev.icfbooks.esther.dal.SqlHelper;
import kitchen.dev.icfbooks.esther.model.media.Media;
import kitchen.dev.icfbooks.esther.model.media.MediaFactory;
import kitchen.dev.icfbooks.esther.model.media.MediaTypes;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link MediaListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private Media media;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            media = MediaFactory.getInstance(getContext()).getItem(UUID.fromString(getArguments().getString(ARG_ITEM_ID)));
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            //if (appBarLayout != null) {
            //    appBarLayout.setTitle(mItem.content);
            //}
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layout = 0;
        if (media.getData() instanceof MediaTypes.Movie)
            layout = R.layout.detail_movie;
        else if(media.getData() instanceof MediaTypes.TwoMoviesAndText)
            layout = R.layout.item_detail;

        LinearLayout rootView = (LinearLayout) inflater.inflate(layout, container, false);
        getActivity().setTitle(media.getTitle());

        if(media.getData() instanceof MediaTypes.Movie) {
            Uri uri = Uri.parse(ApiClient.BASE_URL + ((MediaTypes.Movie)media.getData()).getFile_url());
            ((VideoView)rootView.findViewById(R.id.videoView)).setVideoURI(uri);
            ((TextView)rootView.findViewById(R.id.teaser)).setText(media.getTeaser());
        }

        return rootView;
    }
}
