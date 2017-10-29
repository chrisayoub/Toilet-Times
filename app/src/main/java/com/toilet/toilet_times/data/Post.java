package com.toilet.toilet_times.data;

import java.util.Date;

/**
 * Created by clarezhang on 10/28/17.
 */

public class Post {
    /* IDs */
    public int id;
    public int userId;

    /* Post content */
    public int rating;
    public Building building;
    public int floor;
    public String comment;
    public Date time;

    /* Attributes */
    public int voteTotal;
    public int flagCount;
    public int userVote;

}
