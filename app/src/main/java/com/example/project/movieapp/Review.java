package com.example.project.movieapp;

/**
 * Created by hoang on 02/10/2015.
 */
public class Review {
    String author;
    String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Review(String author, String content) {

        this.author = author;
        this.content = content;
    }
}
