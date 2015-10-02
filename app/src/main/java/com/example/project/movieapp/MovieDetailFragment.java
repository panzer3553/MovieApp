package com.example.project.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoang on 02/10/2015.
 */
public class MovieDetailFragment extends Fragment {

    TextView titleView, releaseYearView, scoreView, overviewView;
    ImageView imagePoster;
    ArrayList<Trailer> trailerVideo;
    ViewGroup insertPoint;

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
        //Bind view with id
        View view = inflater.inflate(R.layout.detail_movie_fragment, container, false);
        titleView = (TextView) view.findViewById(R.id.title);
        releaseYearView = (TextView) view.findViewById(R.id.release_year);
        scoreView = (TextView) view.findViewById(R.id.score);
        overviewView = (TextView) view.findViewById(R.id.overview);
        imagePoster = (ImageView) view.findViewById(R.id.image);
        insertPoint = (ViewGroup) view.findViewById(R.id.insert_point);
        trailerVideo = new ArrayList<>();
        //Parse json movie from intent extra
        Gson gson = new Gson();
        String jsonMovie = getArguments().getString("movie");
        Movie movie = gson.fromJson(jsonMovie, Movie.class);
        //Set text and source picture.
        titleView.setText(movie.getTitle());
        releaseYearView.setText(movie.getRelease_date());
        scoreView.setText(movie.getscore() + "/10");
        String url = "http://image.tmdb.org/t/p/w185/" + movie.getimage_path();
        Picasso.with(getActivity()).load(url).into(imagePoster);
        overviewView.setText(movie.getOverview());
        //Start new thread
        FetchTrailerTask task = new FetchTrailerTask();
        task.execute(movie.getId());
        return view;
    }

    class FetchTrailerTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String jsonStr;
            try {
                String url = "https://api.themoviedb.org/3/movie/" + params[0]
                        + "?api_key=9d3ea725df2618aba8f2324d5015a4ea&append_to_response=trailers";

                jsonStr = new MovieAppClient().run(url);
            } catch (Exception e) {
                Log.d("Error 1", e.toString());
                return null;
            }
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String s) {
            parseJsonTrailer(s);
            addTrailerView();

        }

        public void parseJsonTrailer(String jsonStr) {
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject trailers = jsonObject.getJSONObject("trailers");
                    JSONArray youtubeVideos = trailers.getJSONArray("youtube");
                    for (int i = 0; i < youtubeVideos.length(); i++) {
                        JSONObject movieJsonObject = youtubeVideos.getJSONObject(i);
                        String name = movieJsonObject.getString("name");
                        String source = movieJsonObject.getString("source");
                        trailerVideo.add(new Trailer(name, source));
                    }
                } catch (JSONException e) {
                    Log.d("error", e.toString());
                }
                Log.d("trailer", "size: " + trailerVideo.size());
            }
        }

        public void addTrailerView(){
            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < trailerVideo.size(); i++) {
                View trailerView = vi.inflate(R.layout.trailer_view, null);
                final TextView trailerTitleView = (TextView) trailerView.findViewById(R.id.trailer_title);
                trailerTitleView.setText(trailerVideo.get(i).getName());
                ImageButton imageButton = (ImageButton) trailerView.findViewById(R.id.trailer_play_button);
                final String source = trailerVideo.get(i).getSource();
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" +
                                source));
                        startActivity(intent);

                    }
                });
                insertPoint.addView(trailerView, 0, new ViewGroup.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
    }
}