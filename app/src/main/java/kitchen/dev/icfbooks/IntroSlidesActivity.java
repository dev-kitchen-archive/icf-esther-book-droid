package kitchen.dev.icfbooks;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class IntroSlidesActivity extends AppCompatActivity {

    //Number of pages
    private static final int NUM_PAGES=3; // Intro, how to (fab)
    private ViewPager IntroPager;
    private PagerAdapter IntroPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slides);

        //Initialisation of ViewPager and its Adapter
        IntroPager = (ViewPager) findViewById(R.id.pager);
        IntroPagerAdapter = new IntroPageAdapter(getSupportFragmentManager());
        IntroPager.setAdapter(IntroPagerAdapter);
    }

    @Override
    public void onBackPressed(){
        //If first slide is active go back on global back stack
        if(IntroPager.getCurrentItem() == 0){
            super.onBackPressed();
        }else{
            //Return to last slide
            IntroPager.setCurrentItem(IntroPager.getCurrentItem()-1);
        }
    }

    private class IntroPageAdapter extends FragmentStatePagerAdapter {
        public IntroPageAdapter(FragmentManager fragMgmt){
            super(fragMgmt);
        }

        @Override
        public Fragment getItem(int position){
            return IntroPageFragment.create(position);
        }

        @Override
        public int getCount(){
            return NUM_PAGES;
        }
    }
}
