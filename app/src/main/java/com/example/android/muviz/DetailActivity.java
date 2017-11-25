package com.example.android.muviz;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mToolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("movie_title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detailImage = findViewById(R.id.detail_image);
        mRating=findViewById(R.id.rating_bar);
        mRating.setNumStars(5);
        detailImage.getLayoutParams().height=displayMetrics.heightPixels/3;
        mUrl = NetworkUtils.buildDetailURL(getIntent().getStringExtra("movie_id"));
        Log.e("Detail Activity: ", "onCreate: " + mUrl);
        mTask=new DetailAsyncTask();
        mTask.execute(mUrl);
    }

    class DetailAsyncTask extends AsyncTask<URL, Void, Movies> {

        @Override
        protected Movies doInBackground(URL... urls) {
            String jsonResponse;
            try {
                jsonResponse = NetworkUtils.makeHttpRequest(urls[0]);
                movie=NetworkUtils.extractMovie(jsonResponse);
                Log.e("DetailActivity", "doInBackground: "+jsonResponse );
                return movie;
            } catch (IOException|JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movies movies) {
            Glide.with(getApplicationContext()).load(Movies.POSTER_BASE_URL+movies.getBackdrop()).into(detailImage);
            mRating.setRating(Float.valueOf(movies.getRating()));
            Log.e("DetailActivity", "onPostExecute: "+Movies.POSTER_BASE_URL+movies.getBackdrop() );
        }
    }
}