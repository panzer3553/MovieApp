package com.example.project.movieapp;

import android.net.Uri;

/**
 * Created by hoang on 02/10/2015.
 */
public class MovieUrlBuilder {

    final String BASE_URI = "api.themoviedb.org";
    final String DISCOVER_PATH = "discover";
    final String MOVIE_PATH = "movie";
    final String API_KEY_QUERY = "api_key";
    final String API_KEY = "9d3ea725df2618aba8f2324d5015a4ea";
    final String APPEND_PATH_QUERY = "append_to_response";
    final String APPEND_PATH_VALUE = "trailers,reviews";
    final String SORT_BY_QUERY = "sort_by";
    final String SORT_BY_VALUE = "popularity.desc";
    Uri.Builder builder;

    public MovieUrlBuilder(){
        builder = new Uri.Builder();
        builder.scheme("https").authority(BASE_URI).appendPath("3");
    }

    public String buildMoviePath(String id){
        builder.appendPath("movie").appendPath(id).appendQueryParameter(API_KEY_QUERY, API_KEY)
                .appendQueryParameter(APPEND_PATH_QUERY, APPEND_PATH_VALUE);
        return builder.build().toString();
    }

    private void createQuertPath(){
        builder.appendPath(DISCOVER_PATH).appendPath(MOVIE_PATH).
                appendQueryParameter(API_KEY_QUERY, API_KEY);
    }

    public String buildSearchQueryPathBaseOnPopularity(){
        createQuertPath();
        builder.appendQueryParameter(SORT_BY_QUERY, SORT_BY_VALUE);
        return builder.build().toString();
    }


}
