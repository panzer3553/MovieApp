package com.example.project.movieapp;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by hoang on 01/10/2015.
 */
public class MovieAppClient {
    private  OkHttpClient client;

    public MovieAppClient(){
        client = new OkHttpClient();
    }

    public String run() throws Exception {
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=9d3ea725df2618aba8f2324d5015a4ea")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().string();
    }
}
