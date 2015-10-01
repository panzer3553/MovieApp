package com.example.project.movieapp;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();
        String jsonMovie = intent.getStringExtra("movie");
        FragmentTransaction  ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_detail_container, MovieDetailFragment.newInstance(jsonMovie));
        ft.commit();
    }
}
