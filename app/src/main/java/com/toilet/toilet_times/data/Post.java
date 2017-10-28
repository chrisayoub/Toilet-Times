package com.toilet.toilet_times.data;

import java.sql.Timestamp;

/**
 * Created by clarezhang on 10/28/17.
 */

public class Post {
    private String content;
    private Location location;
    private Timestamp ts;
    private User user;
    private long postID;
    private int rating;
    private int numFlags;
    private int currentRanking;

}
