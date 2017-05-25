package com.example.rohankankapurkar.facebook;

/**
 * Created by rohankankapurkar on 5/25/17.
 */

public class TweetPojo {
    private String name;
    private String time;
    private String tweet;

    public TweetPojo(String name, String time, String tweet) {
        this.name = name;
        this.time = time;
        this.tweet = tweet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }
}
