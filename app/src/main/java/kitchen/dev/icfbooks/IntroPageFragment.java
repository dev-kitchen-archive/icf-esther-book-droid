package kitchen.dev.icfbooks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

/**
 * Created by noc on 14.02.16.
 */
public class IntroPageFragment extends Fragment{

    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int pageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static IntroPageFragment create(int pageNumber) {
        IntroPageFragment fragment = new IntroPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public IntroPageFragment() {
    }

    //on create to get arguments from factory method
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_intro_page, container, false);

        // Set the title view to show the page number.
        TextView introText = (TextView) rootView.findViewById(R.id.intro_text);

        FloatingActionButton introButton = (FloatingActionButton) rootView.findViewById(R.id.intro_button);

        introButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("introDone",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("INTRO_DONE", true);
                editor.commit();

                Intent intent = new Intent(getActivity(), ItemListActivity.class);
                startActivity(intent);
            }
        });


        switch (pageNumber){
            case 0: introText.setText(getString(R.string.intro_text_start));
                introButton.setVisibility(View.INVISIBLE);
                break;
            case 1: introText.setText(getString(R.string.intro_text_howto));
                introButton.setVisibility(View.INVISIBLE);
                break;
            case 2: introText.setText(getString(R.string.intro_text_go)); break;
        }

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return pageNumber;
    }
}
