package com.toilet.toilet_times.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.toilet.toilet_times.R;

/**
 * Created by Chris on 10/28/2017.
 */

public class WhichFloor extends Fragment {

    public int MAX_FLOOR = 0;

    public TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_which_floor, container, false);
        text = rootView.findViewById(R.id.floorNumber);
        text.setHint("Floor (0 - " + MAX_FLOOR + ")");
//        Right arrow
        final ImageButton b = rootView.findViewById(R.id.right_arrow_which_floor);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager =(InputMethodManager) container.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                ViewPager pager = (ViewPager) container;
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        });
        b.setVisibility(View.INVISIBLE);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    b.setVisibility(View.INVISIBLE);
                } else {
                    b.setVisibility(View.VISIBLE);
                }
                if (editable.toString().equals("")) {
                    return;
                }
                int val = Integer.parseInt(editable.toString());
                if (val > MAX_FLOOR) {
                    int length = editable.length();
                    editable.replace(length - 1, length, "");
                }
            }
        });

        text.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    if (text.getText().toString().equals("")) {
                        return false;
                    }

                    InputMethodManager inputMethodManager =(InputMethodManager) container.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    b.setVisibility(View.VISIBLE);
                    ViewPager pager = (ViewPager) container;
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                    return true;
                }
                return false;
            }
        });

//        Left arrow
        rootView.findViewById(R.id.left_arrow_which_floor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPager pager = (ViewPager) container;
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        });
        return rootView;
    }

    public void setMaxFloor(int max) {
        System.out.println(max);
        MAX_FLOOR = max;
        text.setHint("Floor (0 - " + MAX_FLOOR + ")");
    }
}