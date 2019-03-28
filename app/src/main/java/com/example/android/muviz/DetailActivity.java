package com.example.android.muviz;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.muviz.data.DBHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    ImageView detailImage;
    RatingBar mRating;
    URL mUrl, mVideoUrl, mReviewUrl;
    Movies movie;
    DetailAsyncTask mTask;
    Toolbar mToolbar;
    TextView tRating, mTitle, mReleaseDate, mPlot, mReviews;
    ProgressBar mProgress;
    View scrollView;
    ConnectivityManager manager;
    View noConnView;
    Button retryButton;
    FloatingActionButton mTrailerFab;
    String mTrailerKey;
    boolean isDataFetched;
    HashMap<String, String> reviewsMap;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        reviewsMap = new HashMap<>();

        mTitle = findViewById(R.id.movie_title);
        mReleaseDate = findViewById(R.id.movie_date);
        mPlot = findViewById(R.id.movie_plot);
        mProgress = findViewById(R.id.progress_bar_detail);
        scrollView = findViewById(R.id.scrollView_detail);
        noConnView = findViewById(R.id.no_con_detail_view);
        retryButton = findViewById(R.id.retry_button_detail);
        mReviews = findViewById(R.id.reviews_tv);
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mTrailerFab = findViewById(R.id.fab_trailer);
        isDataFetched = false;

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
            mVideoUrl = NetworkUtils.buildExtraUrl(getIntent().getStringExtra("movie_id"), "videos");
            mReviewUrl = NetworkUtils.buildExtraUrl(getIntent().getStringExtra("movie_id"), "reviews");
            flag = 0;
        }
        mTask = new DetailAsyncTask();
        if (internetAvailable()) {
            mTask.execute(mUrl, mVideoUrl, mReviewUrl);
            noConnView.setVisibility(View.INVISIBLE);
        } else {
            noConnView.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
        }
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (internetAvailable()) {
                    mTask.execute(mUrl, mVideoUrl);
                    noConnView.setVisibility(View.INVISIBLE);
                } else {
                    noConnView.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.INVISIBLE);
                }
            }
        });
        mTrailerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDataFetched) {
                    Intent viewTrailer = new Intent(Intent.ACTION_VIEW);
                    viewTrailer.setData(Uri.parse("https://www.youtube.com/watch?v=" + mTrailerKey));
                    startActivity(viewTrailer);
                } else
                    Toast.makeText(DetailActivity.this, "Trailer not available", Toast.LENGTH_SHORT).show();
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
            String jsonResponse, jsonVideoResponse, jsonReviewResponse;
            try {
                jsonResponse = NetworkUtils.makeHttpRequest(urls[0]);
                if (flag == 1) {
                    movie = NetworkUtils.mostPopular(jsonResponse);
                    jsonVideoResponse = NetworkUtils.makeHttpRequest(NetworkUtils.buildExtraUrl(movie.getMovieId(), "videos"));
                    jsonReviewResponse = NetworkUtils.makeHttpRequest(NetworkUtils.buildExtraUrl(movie.getMovieId(), "reviews"));
                } else {
                    movie = NetworkUtils.extractMovie(jsonResponse);
                    jsonVideoResponse = NetworkUtils.makeHttpRequest(urls[1]);
                    jsonReviewResponse = NetworkUtils.makeHttpRequest(urls[2]);
                }
                mTrailerKey = NetworkUtils.getTrailer(jsonVideoResponse);
                reviewsMap = NetworkUtils.getReviews(jsonReviewResponse);
                if (mTrailerKey != null)
                    isDataFetched = true;
                Log.i("DetailActivity", "doInBackground: " + urls[0]);
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
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(movie.getMovieTitle());
            mRating.setVisibility(View.VISIBLE);
            tRating.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            tRating.setText(String.valueOf(rating).substring(0, 3));
            mRating.setRating(rating);
            mTitle.setText(movies.getMovieTitle());
            mPlot.setText(movies.getPlot());
            mReleaseDate.setText(movies.getReleaseDate());
            if (reviewsMap.isEmpty()) {
                mReviews.setText("No reviews yet");
            } else {
                for (Object o : reviewsMap.entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    mReviews.append(Html.fromHtml("<b>" + pair.getKey() + "</b>"));
                    mReviews.append("\n--------\n" + pair.getValue() + "\n\n");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_fav) {
            if (isFavourite())
                item.setIcon(android.R.drawable.btn_star_big_off);
            else {
                item.setIcon(android.R.drawable.btn_star_big_on);
            }
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    private boolean isFavourite() {
        ContentValues values = new ContentValues();
//        values.put(DBHelper.COL_ID, movie.getMovieId());
//        values.put(DBHelper.COL_TITLE, movie.getMovieTitle());

        Uri uri = getContentResolver().insert(Uri.parse("content://com.example.android.muviz/favourites"), values);
        return uri != null;
    }
}