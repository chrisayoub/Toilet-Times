package com.toilet.toilet_times.network;

import com.toilet.toilet_times.data.Building;
import com.toilet.toilet_times.data.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 10/28/2017.
 */

/* Note: DO ALL OF THESE METHODS WHERE WE DO NOT BLOCK THE UI THREAD */
public class DataTransport {

    private static final String BASE_URL = "http://18.216.67.28";
    private static final SimpleDateFormat ISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /* API Calls */

    public static int createNewUser() {
        String url = BASE_URL + "/user/new";
        JSONObject result = doPost(url, "");
        try {
            return result.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static List<Post> getUserPosts(int userId) {
        String url = BASE_URL + "/user/new";
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(userId));
        JSONObject result = doGet(url, map);
        return parseJsonToPosts(result);
    }

    public static void createNewPost(int userId, int rating, String comment, String building, int floor) {
        String url = BASE_URL + "/post/new";
        JSONObject toSend = new JSONObject();
        try {
            toSend.put("userId", userId);
            toSend.put("rating", rating);
            toSend.put("comment", comment);
            toSend.put("building", building);
            toSend.put("floor", floor);

            System.out.println(toSend.toString());

            doPost(url, toSend.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<Post> getTrendingPosts(int userId, int page) {
        String url = BASE_URL + "/posts/trending";
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("page", String.valueOf(page));
        JSONObject result = doGet(url, map);
        return parseJsonToPosts(result);
    }

    public static List<Post> getRecentPosts(int userId, int page) {
        String url = BASE_URL + "/posts/recent";
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("page", String.valueOf(page));
        JSONObject result = doGet(url, map);
        return parseJsonToPosts(result);
    }

    public static List<Post> getAllTimePosts(int userId, int page) {
        String url = BASE_URL + "/posts/all";
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("page", String.valueOf(page));
        JSONObject result = doGet(url, map);
        return parseJsonToPosts(result);
    }

    public static void flagPost(int postId) {
        String url = BASE_URL + "/post/flag";
        JSONObject toSend = new JSONObject();
        try {
            toSend.put("id", postId);
            doPost(url, toSend.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void votePost(int userId, int postId, int vote) {
        String url = BASE_URL + "/post/vote";
        JSONObject toSend = new JSONObject();
        try {
            toSend.put("postId", postId);
            toSend.put("userId", userId);
            toSend.put("value", vote);
            doPost(url, toSend.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void removePost(int postId) {
        String url = BASE_URL + "/post/delete";
        JSONObject toSend = new JSONObject();
        try {
            toSend.put("id", postId);
            doPost(url, toSend.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* Helper methods */

    private static List<Post> parseJsonToPosts(JSONObject postObj) {
        List<Post> postList = new ArrayList<>();
        try {
            JSONArray posts = postObj.getJSONArray("posts");
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.getJSONObject(i);
                Post postToAdd = new Post();
                /* Field extraction */
                postToAdd.id = post.getInt("id");
                postToAdd.userId = post.getInt("userId");
                postToAdd.comment = post.getString("comment");
                postToAdd.flagCount = post.getInt("flagCount");
                postToAdd.floor = post.getInt("floor");
                postToAdd.building = Building.valueOf(post.getString("building"));
                postToAdd.voteTotal = post.getInt("voteTotal");
                postToAdd.rating = post.getInt("rating");
                postToAdd.time = ISO.parse(post.getString("time"));
                postToAdd.userVote = post.getInt("userVote");
                /* Add to list */
                postList.add(postToAdd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postList;
    }

    /* HTTP methods */

    private static JSONObject doPost(String url, String data) {
        String result = "";
        try {
            //Connect
            HttpURLConnection httpcon = (HttpURLConnection) ((new URL (url).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setDoInput(true);
            httpcon.setRequestMethod("POST");
            httpcon.connect();

            //Write
            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.close();
            os.close();

            //Read
            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            result = sb.toString();
            if (result.equals("\"OK\"")) {
                return new JSONObject();
            }
            return new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject doGet(String url, Map<String, String> params) {
        String result = "";
        try {
            // Build URL
            StringBuilder builder = new StringBuilder();
            builder.append(url);
            builder.append("?");
            for (String key : params.keySet()) {
                builder.append(key);
                builder.append("=");
                builder.append(params.get(key));
                builder.append("&");
            }
            builder.setLength(builder.length() - 1);
            url = builder.toString();

            //Connect
            HttpURLConnection httpcon = (HttpURLConnection) ((new URL (url).openConnection()));
            //httpcon.setDoOutput(true);
            //httpcon.setRequestProperty("Content-Type", "application/json");
            //httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod("GET");
            httpcon.connect();

            //Read
            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            result = sb.toString();
            return new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
