package common;

import server.Server;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Tweet implements Serializable,Comparable<Tweet>{
    private String text;
    private String picLink;
    private String userId;
    private int likes;
    private int retweet;
    private int comment;
    private String date;
    private int isFavStar;
    private String likesIds;
    private String hashtags;
    private String retweetIds;
    private String commentText;

    public Tweet(String text, String picLink, String userId) { // client new tweet
        this.text = text;
        this.picLink = picLink;
        this.userId = userId;
        this.date = new Date().toString();
        this.isFavStar=0;
        this.likes=0;
        this.retweet=0;
        this.comment=0;
        this.likesIds = "-";
        this.hashtags = "-";
        this.commentText="-";
        this.retweetIds="-";
    }

    public Tweet(String text, String picLink, String userId, int likes, int retweet, int comment, String date, int isFavStar,String commentText,String retweetIds) { // server get tweet
        this.text = text;
        this.picLink = picLink;
        this.userId = userId;
        this.likes = likes;
        this.retweet = retweet;
        this.comment = comment;
        this.date = date;
        this.isFavStar = isFavStar;
        this.retweetIds=retweetIds;
        this.commentText=commentText;
    }

    public String getHashtags() {
        return hashtags;
    }

    public String getLikesIds() throws SQLException {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = connection.createStatement();
        return likesIds;
    }

    public int getIsFavStar() {
        return isFavStar;
    }

    public void setIsFavStar(int isFavStar) {
        this.isFavStar = isFavStar;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPicLink() {
        return picLink;
    }

    public void setPicLink(String picLink) {
        this.picLink = picLink;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getRetweet() {
        return retweet;
    }

    public void setRetweet(int retweet) {
        this.retweet = retweet;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }
    public String getRetweetIds(){return retweetIds;}

    public String getCommentText() {
        return commentText;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(Tweet o) {
        return this.date.compareTo(o.getDate());
    }
}
