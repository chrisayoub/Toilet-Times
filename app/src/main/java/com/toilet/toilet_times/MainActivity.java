package com.toilet.toilet_times;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Chris's work
    private LinearLayout cardHolder;
    private DrawerLayout drawerLayout;


    //I'm confused
    private String[] filters; //what your navigation bar displays?
    private DrawerLayout mDrawerLayout; //the layout that stores it???
//    private ListView mDrawerList;
    private ListView mDrawerList; //??????????

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Chris made this
        cardHolder = findViewById(R.id.cardHolder);
        for (int i = 0; i < 5; i++) {
            addOneCard();
        }

        //what
//        drawerLayout = (DrawerLayout) findViewById(R.id.nav_view);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateNewPost.class);
                startActivity(intent);
            }
        });



    }

    private void addOneCard() {
        LayoutInflater in = LayoutInflater.from(getApplicationContext());
        CardView view = (CardView)in.inflate(R.layout.card_template, cardHolder, false);
//        TextView text = findViewById(R.id.cardText);
//        text.setText("This is different!");
        cardHolder.addView(view);
    }




}
