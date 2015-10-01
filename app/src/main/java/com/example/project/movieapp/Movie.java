package com.example.project.movieapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 01/10/2015.
 */
public class Movie {
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("vote_average")
    private double score;
    @SerializedName("poster_path")
    private String image_path;

    public Movie(String title, String overview, String release_date, String image_path, double score) {
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.image_path = image_path;
        this.score = score;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getscore() {
        return score;
    }

    public void setscore(double score) {
        this.score = score;
    }

    public String getimage_path() {
        return image_path;
    }

    public void setimage_path(String image_path) {
        this.image_path = image_path;
    }
}
