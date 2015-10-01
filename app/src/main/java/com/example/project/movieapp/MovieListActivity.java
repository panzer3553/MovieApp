package com.example.project.movieapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

public class MovieListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MovieFetchTask().execute();

    }

    class MovieFetchTask extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params) {
            String jsonStr;
            try{
               jsonStr = new MovieAppClient().run();
            }catch (Exception e){
                Log.d("error", e.toString());
                return null;
            }
            Log.d("json", jsonStr);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null) {
                Gson gson = new Gson();
                MovieResponse movieResponse = gson.fromJson(s, MovieResponse.class);
                Log.d("Size", "Size: " + movieResponse.getMovies().size());
            }
        }
    }
}
