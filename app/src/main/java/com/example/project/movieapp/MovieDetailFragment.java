package com.example.project.movieapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

/**
 * Created by hoang on 02/10/2015.
 */
public class MovieDetailFragment extends Fragment {

    TextView titleView, releaseYearView, scoreView, overviewView;
    ImageView imagePoster;
    public static MovieDetailFragment newInstance(String jsonMovie) {
        Bundle args = new Bundle();
        args.putString("movie", jsonMovie);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_movie_fragment, container, false);
        titleView = (TextView) view.findViewById(R.id.title);
        releaseYearView = (TextView) view.findViewById(R.id.release_year);
        scoreView = (TextView) view.findViewById(R.id.score);
        overviewView = (TextView) view.findViewById(R.id.overview);
        imagePoster = (ImageView) view.findViewById(R.id.image);
        Gson gson = new Gson();
        String jsonMovie = getArguments().getString("movie");
        Movie movie = gson.fromJson(jsonMovie, Movie.class);
        titleView.setText(movie.getTitle());
        releaseYearView.setText(movie.getRelease_date());
        scoreView.setText(movie.getscore() + "/10");
        String url = "http://image.tmdb.org/t/p/w185/" + movie.getimage_path();
        Picasso.with(getActivity()).load(url).into(imagePoster);
        overviewView.setText(movie.getOverview());
        return view;
    }
}
