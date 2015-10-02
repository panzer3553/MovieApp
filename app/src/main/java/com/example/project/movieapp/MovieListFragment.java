package com.example.project.movieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hoang on 01/10/2015.
 */
public class MovieListFragment extends Fragment {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    MovieAdapter mAdapter;
    View progressContainer;
    ArrayList<Movie> movieList;
    public static MovieListFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        MovieListFragment fragment = new MovieListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_list_fragment, container, false);
        String url = getArguments().getString("url");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listView);
        movieList = new ArrayList<>();
        // Create adapter passing in the sample user data

        // The number of Columns
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MovieAdapter(getActivity(), movieList);
        // Attach the adapter to the recyclerview to populate items
        mRecyclerView.setAdapter(mAdapter);
        progressContainer = view.findViewById(R.id.progressBar_list_container);
        // Set layout manager to position the items
        new MovieFetchTask().execute(url);
        mAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent intent = new Intent(getActivity(), MovieDetail.class);
                Gson gson = new Gson();
                String jsonMovie = gson.toJson(movieList.get(position));
                intent.putExtra("movie", jsonMovie);
                startActivity(intent);
            }
        });
        return view;
    }

    class MovieFetchTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String jsonStr;
            try{
                jsonStr = new MovieAppClient().run(params[0]);
            }catch (Exception e){
                Log.d("error", e.toString());
                return null;
            }
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
                progressContainer.setVisibility(View.GONE);
            }
        }
    }
}
