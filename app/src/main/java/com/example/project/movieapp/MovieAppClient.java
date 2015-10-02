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

    public String run(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().string();
    }
}
