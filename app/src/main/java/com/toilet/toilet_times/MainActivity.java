package com.toilet.toilet_times;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.toilet.toilet_times.data.Post;
import com.toilet.toilet_times.network.DataTransport;

import java.text.SimpleDateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Chris's work
    private LinearLayout cardHolder;
    private DrawerLayout drawerLayout;

    private static SimpleDateFormat df = new SimpleDateFormat("MM/dd");

    private int currentPage = 0;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = getSharedPreferences("prefs", MODE_PRIVATE).getInt("userId", -1);

        cardHolder = findViewById(R.id.cardHolder);
        drawerLayout = findViewById(R.id.drawer_layout);

        final NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.trending) {
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
                } else if (item.getItemId() == R.id.recent) {
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
                } else if (item.getItemId() == R.id.all_time) {
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
            TextView timePosted = view.findViewById(R.id.timePosted);
            TextView buildingName = view.findViewById(R.id.buildingName);
            TextView floorNum = view.findViewById(R.id.floorNum);
            TextView comment = view.findViewById(R.id.description);
            final Button fav = view.findViewById(R.id.favorite);
            final Button flush = view.findViewById(R.id.flush);
            final TextView votes = view.findViewById(R.id.upvotes);
            RatingBar bar = view.findViewById(R.id.starRating);

            buildingName.setText(p.building.toString());
            floorNum.setText("F" + p.floor);
            if (p.comment.equals("")) {
                comment.setText("No comment specified.");
            } else {
                comment.setText(p.comment);
            }
            votes.setText(p.voteTotal + "");
            bar.setRating(p.rating);
            timePosted.setText(df.format(p.time));
            image.setImageResource(R.drawable.circle_selected);

            if (p.userVote == 1) {
                fav.setTypeface(null, Typeface.BOLD);
            } else {
                fav.setTypeface(null, Typeface.NORMAL);
            }
            if (p.userVote == -1) {
                flush.setTypeface(null, Typeface.BOLD);
            } else {
                flush.setTypeface(null, Typeface.NORMAL);
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
                            flush.setTypeface(null, Typeface.NORMAL);
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
                            fav.setTypeface(null, Typeface.NORMAL);
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
                        }
                    }
                }
            });

            cardHolder.addView(view);
        }

    }




}
