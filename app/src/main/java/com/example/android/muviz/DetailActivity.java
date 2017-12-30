package com.example.android.muviz;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    ProgressBar mProgress;
    View scrollView;
    ConnectivityManager manager;
    View noConnView;
    Button retryButton;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mTitle = findViewById(R.id.movie_title);
        mReleaseDate = findViewById(R.id.movie_date);
        mPlot = findViewById(R.id.movie_plot);
        mProgress = findViewById(R.id.progress_bar_detail);
        scrollView = findViewById(R.id.scrollView_detail);
        noConnView = findViewById(R.id.no_con_detail_view);
        retryButton = findViewById(R.id.retry_button_detail);
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
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
        if (getIntent().getExtras() == null) {
            mUrl = NetworkUtils.buildURL("popularity.desc");
            flag = 1;
        } else {
            mUrl = NetworkUtils.buildDetailURL(getIntent().getStringExtra("movie_id"));
            flag = 0;
        }
        mTask = new DetailAsyncTask();
        if (internetAvailable()) {
            mTask.execute(mUrl);
            noConnView.setVisibility(View.INVISIBLE);
        } else {
            noConnView.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
        }
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (internetAvailable()) {
                    mTask.execute(mUrl);
                    noConnView.setVisibility(View.INVISIBLE);
                } else {
                    noConnView.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public boolean internetAvailable() {
        return manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    class DetailAsyncTask extends AsyncTask<URL, Void, Movies> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Movies doInBackground(URL... urls) {
            String jsonResponse;
            try {
                jsonResponse = NetworkUtils.makeHttpRequest(urls[0]);
                if (flag == 1) {
                    movie = NetworkUtils.mostPopular(jsonResponse);
                } else {
                    movie = NetworkUtils.extractMovie(jsonResponse);
                }
                Log.i("DetailActivity", "doInBackground: " + jsonResponse);
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
            mProgress.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            tRating.setText(String.valueOf(rating).substring(0, 3));
            mRating.setRating(rating);
            mTitle.setText(movies.getMovieTitle());
            mPlot.setText(movies.getPlot());
            mReleaseDate.setText(movies.getReleaseDate());
        }
    }
}