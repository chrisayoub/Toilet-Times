package com.toilet.toilet_times.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListAdapter;

import com.toilet.toilet_times.R;
import com.toilet.toilet_times.data.Building;

/**
 * Created by Chris on 10/28/2017.
 */

public class WhereAreYou extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_where_are_you, container, false);
        final ImageButton b = rootView.findViewById(R.id.right_arrow_where_are_you);
        b.setVisibility(View.INVISIBLE);
//        Right arrow
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPager pager = (ViewPager) container;
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        });

        int index = 0;
        String[] vals = new String[Building.values().length];
        for (Building build : Building.values()) {
            vals[index++] = build.toString();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, vals);
        final AutoCompleteTextView textView = rootView.findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int i = s.length(); i > 0; i--) {
                    if (s.subSequence(i - 1, i).toString().equals("\n")) {
                        s.replace(i - 1, i, "");
                        break;
                    }
                }

                ListAdapter listAdapter = textView.getAdapter();
                for (int i = 0; i < listAdapter.getCount(); i++) {
                    String temp = listAdapter.getItem(i).toString();
                    if (s.toString().equals(temp)) {
                        b.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                b.setVisibility(View.INVISIBLE);
            }
        });

        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    // on focus off
                    String str = textView.getText().toString();
                    ListAdapter listAdapter = textView.getAdapter();
                    for (int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if (str.equals(temp)) {
                            return;
                        }
                    }
                    textView.setText("");

                    InputMethodManager inputMethodManager =(InputMethodManager) container.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager inputMethodManager =(InputMethodManager) container.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                ViewPager pager = (ViewPager) container;
                pager.setCurrentItem(pager.getCurrentItem() + 1);

                b.setVisibility(View.VISIBLE);
            }
        });

        return rootView;
    }
}
