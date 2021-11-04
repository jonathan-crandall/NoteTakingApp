package com.crandallj.notetaking.models;

public class Note {
    private String title;
    private long timestamp;
    private String body;
    private String userID;

    public Note(){}

    public Note (String title, long timestamp, String body, String userID){
        this.title = title;
        this.timestamp = timestamp;
        this.body = body;
        this.userID = userID;
    }


    public String getTitle() {
        return title;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public String getBody() {
        return body;
    }
    public String getUserID() {
        return userID;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
