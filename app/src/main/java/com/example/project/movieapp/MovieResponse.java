package com.example.project.movieapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoang on 01/10/2015.
 */
public class MovieResponse {
    @SerializedName("results")
    List<Movie> movieList;

    public List<Movie> getMovies(){
        return movieList;
    }
}
