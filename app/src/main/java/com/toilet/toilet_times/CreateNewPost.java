package com.toilet.toilet_times;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.toilet.toilet_times.data.Post;
import com.toilet.toilet_times.fragments.Comments;
import com.toilet.toilet_times.fragments.Ranking;
import com.toilet.toilet_times.fragments.WhereAreYou;
import com.toilet.toilet_times.fragments.WhichFloor;
import com.toilet.toilet_times.network.DataTransport;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class CreateNewPost extends AppCompatActivity {

    public WhereAreYou building;
    public WhichFloor floor;
    public Ranking rank;
    public Comments comment;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);

        /* Close button */
        findViewById(R.id.close_new_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewPost.this.finish();
            }
        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        mSectionsPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                building = new WhereAreYou();
                return building;
            } else if (position == 1){
                floor = new WhichFloor();
                return floor;
            } else if (position == 2) {
                rank = new Ranking();
                return rank;
            } else {
                comment = new Comments();
                return comment;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public void makePost() {
        new Thread() {
            public void run() {
                String symbol = building.build;
                int floorVal = Integer.parseInt(floor.text.getText().toString());
                int rating = (int) rank.ratingBar.getRating();
                String commentVal = comment.text.getText().toString();
                int userId = getSharedPreferences("prefs", MODE_PRIVATE).getInt("userId", -1);
                DataTransport.createNewPost(userId, rating, commentVal, symbol, floorVal);

                CreateNewPost.this.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        Intent i = new Intent("reload");
                        sendBroadcast(i);
                        finish();
                    }});
                super.run();
            }
        }.start();

    }
}
