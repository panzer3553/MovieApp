package com.example.project.movieapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by hoang on 01/10/2015.
 */
public class MovieListFragment extends Fragment {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    ArrayList<Movie> movieList;
    public static MovieListFragment newInstance() {

        Bundle args = new Bundle();

        MovieListFragment fragment = new MovieListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_list_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listView);
        movieList = new ArrayList<>();
        // Create adapter passing in the sample user data

        // The number of Columns
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MovieAdapter(getActivity(), movieList);
        // Attach the adapter to the recyclerview to populate items
        mRecyclerView.setAdapter(mAdapter);
        // Set layout manager to position the items
        new MovieFetchTask().execute();
        return view;
    }

    class MovieFetchTask extends AsyncTask<Void, Void, String> {
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
                movieList.clear();
                for(Movie movie : movieResponse.getMovies()){
                    movieList.add(movie);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
