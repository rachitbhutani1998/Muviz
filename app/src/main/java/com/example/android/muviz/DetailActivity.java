package com.example.android.muviz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {
    ImageView detailImage;
    RatingBar mRating;
    URL mUrl;
    Movies movie;
    DetailAsyncTask mTask;
    Toolbar mToolbar;
    TextView tRating, mTitle, mReleaseDate, mPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mTitle = findViewById(R.id.movie_title);
        mReleaseDate = findViewById(R.id.movie_date);
        mPlot = findViewById(R.id.movie_plot);

        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("movie_title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHideOnContentScrollEnabled(false);

        tRating = findViewById(R.id.rating_text);
        tRating.setVisibility(View.INVISIBLE);
        detailImage = findViewById(R.id.detail_image);
        detailImage.getLayoutParams().height = displayMetrics.heightPixels / 3;

        mRating = findViewById(R.id.rating_bar);
        mRating.setVisibility(View.INVISIBLE);
        mRating.setNumStars(5);

        mUrl = NetworkUtils.buildDetailURL(getIntent().getStringExtra("movie_id"));
        mTask = new DetailAsyncTask();
        mTask.execute(mUrl);
    }

    class DetailAsyncTask extends AsyncTask<URL, Void, Movies> {

        @Override
        protected Movies doInBackground(URL... urls) {
            String jsonResponse;
            try {
                jsonResponse = NetworkUtils.makeHttpRequest(urls[0]);
                movie = NetworkUtils.extractMovie(jsonResponse);
                Log.e("DetailActivity", "doInBackground: " + jsonResponse);
                return movie;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movies movies) {
            Glide.with(getApplicationContext()).load(Movies.POSTER_BASE_URL + movies.getBackdrop()).into(detailImage);
            Float rating = Float.valueOf(movies.getRating()) / 2;
            mRating.setVisibility(View.VISIBLE);
            tRating.setVisibility(View.VISIBLE);
            tRating.setText(String.valueOf(rating).substring(0, 3));
            mRating.setRating(rating);
            mTitle.setText(movies.getMovieTitle());
            mPlot.setText(movies.getPlot());
            mReleaseDate.setText(movies.getReleaseDate());
            Log.e("DetailActivity", "onPostExecute: " + Movies.POSTER_BASE_URL + movies.getBackdrop());
        }
    }
}