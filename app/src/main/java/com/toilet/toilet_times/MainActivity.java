package com.toilet.toilet_times;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.toilet.toilet_times.data.Post;
import com.toilet.toilet_times.network.DataTransport;

import java.text.SimpleDateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Chris's work
    private LinearLayout cardHolder;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private SwipeRefreshLayout refresh;

    private static SimpleDateFormat df = new SimpleDateFormat("MM/dd");

    private int currentPage = 0;

    private int userId;

    private int currentMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true); // show or hide the default home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        registerReceiver(new MyReceiver(),
                new IntentFilter("reload"));

        userId = getSharedPreferences("prefs", MODE_PRIVATE).getInt("userId", -1);

        cardHolder = findViewById(R.id.cardHolder);
        drawerLayout = findViewById(R.id.drawer_layout);
        currentMenu = R.id.trending;
        refresh = findViewById(R.id.swipeRefreshLayout);

        final ScrollView scrollView = findViewById(R.id.scroll_view);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                if (currentMenu == R.id.trending) {
                    new Thread() {
                        public void run() {
                            final List<Post> posts = DataTransport.getTrendingPosts(userId, currentPage);
                            MainActivity.this.runOnUiThread(new Runnable(){
                                @Override
                                public void run() {
                                    cardHolder.removeAllViews();
                                    addCards(posts);
                                    refresh.setRefreshing(false);
                                }});
                            super.run();
                        }
                    }.start();
                } else if (currentMenu == R.id.recent) {
                    new Thread() {
                        public void run() {
                            final List<Post> posts = DataTransport.getRecentPosts(userId, currentPage);
                            MainActivity.this.runOnUiThread(new Runnable(){
                                @Override
                                public void run() {
                                    cardHolder.removeAllViews();
                                    addCards(posts);
                                    refresh.setRefreshing(false);
                                }});
                            super.run();
                        }
                    }.start();
                } else if (currentMenu == R.id.all_time) {
                    new Thread() {
                        public void run() {
                            final List<Post> posts = DataTransport.getAllTimePosts(userId, currentPage);
                            MainActivity.this.runOnUiThread(new Runnable(){
                                @Override
                                public void run() {
                                    cardHolder.removeAllViews();
                                    addCards(posts);
                                    refresh.setRefreshing(false);
                                }});
                            super.run();
                        }
                    }.start();
                } else {
                    refresh.setRefreshing(false);
                }
            }
        });

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                boolean result = doReload(item.getItemId());
                scrollView.smoothScrollTo(0,0);
                return result;
            }
        });
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateNewPost.class);
                startActivity(intent);
            }
        });

        new Thread() {
            public void run() {
                final List<Post> posts = DataTransport.getTrendingPosts(userId, currentPage);
                MainActivity.this.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        addCards(posts);
                    }});
                super.run();
            }
        }.start();

    }

    private void addCards(List<Post> posts) {
        LayoutInflater in = LayoutInflater.from(getApplicationContext());
        for (Post p : posts) {
            CardView view = (CardView) in.inflate(R.layout.card_template, cardHolder, false);

            final int postId = p.id;

            ImageView image = view.findViewById(R.id.buildingPic);
            image.setImageResource(getResources().getIdentifier(p.building.name().toLowerCase(),
                    "drawable", "com.toilet.toilet_times"));

            TextView timePosted = view.findViewById(R.id.timePosted);
            TextView buildingName = view.findViewById(R.id.buildingName);
            TextView floorNum = view.findViewById(R.id.floorNum);
            TextView comment = view.findViewById(R.id.description);
            final Button fav = view.findViewById(R.id.favorite);
            final Button flush = view.findViewById(R.id.flush);
            final TextView votes = view.findViewById(R.id.upvotes);
            RatingBar bar = view.findViewById(R.id.starRating);

            buildingName.setText(p.building.toString() + " ·");
            floorNum.setText("F" + p.floor);
            if (p.comment.equals("")) {
                comment.setText("No comment specified.");
            } else {
                comment.setText(p.comment);
            }
            votes.setText(p.voteTotal + "");
            bar.setRating(p.rating);
            timePosted.setText(df.format(p.time) + " ·");

            final int blue = Color.parseColor("#17D4D3");
            final int grey = Color.parseColor("#CCCCCC");

            if (p.userVote == 1) {
                fav.setTypeface(null, Typeface.BOLD);
                fav.setTextColor(blue);
            } else {
                fav.setTypeface(null, Typeface.NORMAL);
                fav.setTextColor(grey);
            }
            if (p.userVote == -1) {
                flush.setTypeface(null, Typeface.BOLD);
                flush.setTextColor(blue);
            } else {
                flush.setTypeface(null, Typeface.NORMAL);
                flush.setTextColor(grey);
            }

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int score = Integer.parseInt(votes.getText().toString());
                    if (fav.getTypeface() != null && fav.getTypeface().isBold()) {
                        /* Subtract from score */
                        score -= 1;
                        votes.setText("" + score);
                        /* Send downvote to server */
                        new Thread() {
                            public void run() {
                                DataTransport.votePost(userId, postId, 0);
                            }
                        }.start();
                        /* Unbold */
                        fav.setTypeface(null, Typeface.NORMAL);
                        fav.setTextColor(grey);
                    } else {
                        if (flush.getTypeface() != null && flush.getTypeface().isBold()) {
                            /* Add to score */
                            score += 2;
                            votes.setText("" + score);
                            /* Send upvote to server */
                            new Thread() {
                                public void run() {
                                    DataTransport.votePost(userId, postId, 0);
                                    DataTransport.votePost(userId, postId, 1);
                                }
                            }.start();
                            /* Bold */
                            fav.setTypeface(null, Typeface.BOLD);
                            fav.setTextColor(blue);
                            flush.setTypeface(null, Typeface.NORMAL);
                            flush.setTextColor(grey);
                        } else {
                            /* Add to score */
                            score += 1;
                            votes.setText("" + score);
                            /* Send upvote to server */
                            new Thread() {
                                public void run() {
                                    DataTransport.votePost(userId, postId, 1);
                                }
                            }.start();
                            /* Bold*/
                            fav.setTypeface(null, Typeface.BOLD);
                            fav.setTextColor(blue);
                        }
                    }

                }
            });

            flush.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int score = Integer.parseInt(votes.getText().toString());
                    if (flush.getTypeface() != null && flush.getTypeface().isBold()) {
                        /* Add to score */
                        score += 1;
                        votes.setText("" + score);
                        /* Send downvote to server */
                        new Thread() {
                            public void run() {
                                DataTransport.votePost(userId, postId, 0);
                            }
                        }.start();
                        /* Unbold */
                        flush.setTypeface(null, Typeface.NORMAL);
                        flush.setTextColor(grey);
                    } else {
                        if (fav.getTypeface() != null && fav.getTypeface().isBold()) {
                            /* Add to score */
                            score -= 2;
                            votes.setText("" + score);
                            /* Send upvote to server */
                            new Thread() {
                                public void run() {
                                    DataTransport.votePost(userId, postId, 0);
                                    DataTransport.votePost(userId, postId, -1);
                                }
                            }.start();
                            /* Bold */
                            flush.setTypeface(null, Typeface.BOLD);
                            flush.setTextColor(blue);
                            fav.setTypeface(null, Typeface.NORMAL);
                            fav.setTextColor(grey);
                        } else {
                            /* Add to score */
                            score -= 1;
                            votes.setText("" + score);
                            /* Send upvote to server */
                            new Thread() {
                                public void run() {
                                    DataTransport.votePost(userId, postId, -1);
                                }
                            }.start();
                            /* Bold*/
                            flush.setTypeface(null, Typeface.BOLD);
                            flush.setTextColor(blue);
                        }
                    }
                }
            });

            cardHolder.addView(view);
        }
        /* Load more button */
        final CardView loadView = (CardView) in.inflate(R.layout.load_more, cardHolder, false);
        loadView.findViewById(R.id.load_more_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage++;
                if (currentMenu == R.id.trending) {
                    new Thread() {
                        public void run() {
                            final List<Post> posts = DataTransport.getTrendingPosts(userId, currentPage);
                            if (posts.size() > 0) {
                                MainActivity.this.runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        cardHolder.removeView(loadView);
                                        addCards(posts);
                                    }});
                                super.run();
                            } else {
                                MainActivity.this.runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        cardHolder.removeView(loadView);
                                        Toast.makeText(loadView.getContext(), "No more posts are available.", Toast.LENGTH_SHORT).show();
                                    }});
                                super.run();
                            }
                        }
                    }.start();
                } else if (currentMenu == R.id.recent) {
                    new Thread() {
                        public void run() {
                            final List<Post> posts = DataTransport.getRecentPosts(userId, currentPage);
                            if (posts.size() > 0) {
                                MainActivity.this.runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        cardHolder.removeView(loadView);
                                        addCards(posts);
                                    }});
                                super.run();
                            } else {
                                MainActivity.this.runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        cardHolder.removeView(loadView);
                                        Toast.makeText(loadView.getContext(), "No more posts are available.", Toast.LENGTH_SHORT).show();
                                    }});
                                super.run();
                            }
                        }
                    }.start();
                } else if (currentMenu == R.id.all_time) {
                    new Thread() {
                        public void run() {
                            final List<Post> posts = DataTransport.getAllTimePosts(userId, currentPage);
                            if (posts.size() > 0) {
                                MainActivity.this.runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        cardHolder.removeView(loadView);
                                        addCards(posts);
                                    }});
                                super.run();
                            } else {
                                MainActivity.this.runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        cardHolder.removeView(loadView);
                                        Toast.makeText(loadView.getContext(), "No more posts are available.", Toast.LENGTH_SHORT).show();
                                    }});
                                super.run();
                            }
                        }
                    }.start();
                } else /* My posts */ {

                }
            }
        });
        cardHolder.addView(loadView);
    }


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            doReload(currentMenu);
        }

    }

    private boolean doReload(int item) {
        currentPage = 0;
        if (item == R.id.trending) {
            currentMenu = R.id.trending;
            new Thread() {
                public void run() {
                    final List<Post> posts = DataTransport.getTrendingPosts(userId, currentPage);
                    MainActivity.this.runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            cardHolder.removeAllViews();
                            addCards(posts);
                        }});
                    super.run();
                }
            }.start();
        } else if (item == R.id.recent) {
            currentMenu = R.id.recent;
            new Thread() {
                public void run() {
                    final List<Post> posts = DataTransport.getRecentPosts(userId, currentPage);
                    MainActivity.this.runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            cardHolder.removeAllViews();
                            addCards(posts);
                        }});
                    super.run();
                }
            }.start();
        } else if (item == R.id.all_time) {
            currentMenu = R.id.all_time;
            new Thread() {
                public void run() {
                    final List<Post> posts = DataTransport.getAllTimePosts(userId, currentPage);
                    MainActivity.this.runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            cardHolder.removeAllViews();
                            addCards(posts);
                        }});
                    super.run();
                }
            }.start();
        } else /* My posts */ {

        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.START);
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}
