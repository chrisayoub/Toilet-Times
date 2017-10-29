package com.toilet.toilet_times.fragments;

import android.media.Rating;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.toilet.toilet_times.R;

/**
 * Created by Chris on 10/28/2017.
 */

public class Ranking extends Fragment {

    public RatingBar ratingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rating, container, false);
        //        Right arrow
        final ImageButton b = rootView.findViewById(R.id.right_arrow_rating);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPager pager = (ViewPager) container;
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        });
        b.setVisibility(View.INVISIBLE);

        RatingBar bar = rootView.findViewById(R.id.ratingBar2);
        ratingBar = bar;
        bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean bo) {
                if ((int) v > 0) {
                    b.setVisibility(View.VISIBLE);
                } else {
                    b.setVisibility(View.INVISIBLE);
                }
            }
        });

//        Left arrow
        rootView.findViewById(R.id.left_arrow_rating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPager pager = (ViewPager) container;
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        });
        return rootView;
    }

}