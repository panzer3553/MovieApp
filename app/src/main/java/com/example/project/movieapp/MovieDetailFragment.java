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
    ArrayList<Review> reviewsList;
    ViewGroup insertTrailerPoint;
    ViewGroup insertReviewPoint;

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
        insertTrailerPoint = (ViewGroup) view.findViewById(R.id.insert_trailer_point);
        insertReviewPoint = (ViewGroup) view.findViewById(R.id.insert_review_point);
        trailerVideo = new ArrayList<>();
        reviewsList = new ArrayList<>();
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
                String trailerURL = new MovieUrlBuilder().buildMoviePath(params[0]);
                Log.d("url", trailerURL);
                jsonStr = new MovieAppClient().run(trailerURL);
            } catch (Exception e) {
                Log.d("Error 1", e.toString());
                return null;
            }
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String s) {
            parseJsonTrailer(s);
            if(trailerVideo.size() > 0){
                addTrailerView();
            }
            parseJsonReview(s);
            if(reviewsList.size() > 0){
                addReviewView();
            }

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

        public void parseJsonReview(String jsonStr) {
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject reviews = jsonObject.getJSONObject("reviews");
                    JSONArray results = reviews.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject reviewJsonObject = results.getJSONObject(i);
                        String author = reviewJsonObject.getString("author");
                        String content = reviewJsonObject.getString("content");
                        reviewsList.add(new Review(author, content));
                    }
                } catch (JSONException e) {
                    Log.d("error", e.toString());
                }
                Log.d("review", "size: " + reviewsList.size());
            }
        }

        public void addTrailerView(){
            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Log.d("vi", "Null " + Boolean.toString(vi == null));
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
                insertTrailerPoint.addView(trailerView, 0, new ViewGroup.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        public void addReviewView(){
            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < reviewsList.size(); i++) {
                View reviewView = vi.inflate(R.layout.review_view, null);
                TextView authourView = (TextView)reviewView.findViewById(R.id.author_name);
                authourView.setText(reviewsList.get(i).author);
                TextView contentView = (TextView)reviewView.findViewById(R.id.review_content);
                contentView.setText(reviewsList.get(i).content);
                insertReviewPoint.addView(reviewView, 0, new ViewGroup.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
    }
}