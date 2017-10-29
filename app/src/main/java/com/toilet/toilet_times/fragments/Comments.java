package com.toilet.toilet_times.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.toilet.toilet_times.CreateNewPost;
import com.toilet.toilet_times.R;

/**
 * Created by Chris on 10/28/2017.
 */

public class Comments extends Fragment {

    public EditText text;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_comments, container, false);
        /* Post button */
        rootView.findViewById(R.id.post_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Make network request */
                CreateNewPost c = (CreateNewPost) getActivity();
                c.makePost();
                /* Close */
                getActivity().finish();
            }
        });

        EditText t = rootView.findViewById(R.id.comment_text);
        text = t;
        t.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    InputMethodManager inputMethodManager =(InputMethodManager) container.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });


//        Left arrow
        rootView.findViewById(R.id.left_arrow_comments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPager pager = (ViewPager) container;
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        });
        return rootView;
    }

}