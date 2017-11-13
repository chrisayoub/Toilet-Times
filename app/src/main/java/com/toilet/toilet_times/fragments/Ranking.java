package com.toilet.toilet_times.fragments;

import android.media.Rating;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.toilet.toilet_times.R;

/**
 * Created by Chris on 10/28/2017.
 */

public class Ranking extends Fragment {

    private static final int MAX_SHEETS = 5;

    public int currentRating;
    private ImageView[] sheets = new ImageView[MAX_SHEETS];

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

        sheets[0] = rootView.findViewById(R.id.squareOne);
        sheets[1] = rootView.findViewById(R.id.squareTwo);
        sheets[2] = rootView.findViewById(R.id.squareThree);
        sheets[3] = rootView.findViewById(R.id.squareFour);
        sheets[4] = rootView.findViewById(R.id.squareFive);

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                for (int sheet = 0; sheet < MAX_SHEETS; sheet++) {
                    if (isEventOnTopOfView(motionEvent, sheets[sheet])) {
                        for (int i = 0; i < MAX_SHEETS; i++) {
                            if (i >= sheet + 1) {
                                sheets[i].setActivated(false);
                            } else {
                                sheets[i].setActivated(true);
                            }
                        }
                        b.setVisibility(View.VISIBLE);
                        currentRating = sheet + 1;
                        break;
                    }
                }
                return true;
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

    private boolean isEventOnTopOfView(MotionEvent event, View view) {
        return isViewContains(view, (int) event.getRawX(), (int) event.getRawY());
    }

    private boolean isViewContains(View view, int rx, int ry) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        int w = view.getWidth();
        int h = view.getHeight();

        if (rx < x || rx > x + w || ry < y || ry > y + h) {
            return false;
        }
        return true;
    }

}