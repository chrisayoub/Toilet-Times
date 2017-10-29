package com.toilet.toilet_times.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toilet.toilet_times.R;

/**
 * Created by Chris on 10/28/2017.
 */

public class WhereAreYou extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_where_are_you, container, false);
        return rootView;
    }
}
