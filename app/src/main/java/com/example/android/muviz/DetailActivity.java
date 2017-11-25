package com.example.android.muviz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {
    ImageView detailImage;
    TextView detailTitle;
    URL mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle(getIntent().getStringExtra("movie_title"));
        detailImage=findViewById(R.id.detail_image);
        detailTitle=findViewById(R.id.detail_name);
        mUrl=NetworkUtils.buildDetailURL(getIntent().getStringExtra("movie_id"));
        Log.e("DetailActivity: ", "onCreate: "+mUrl);
    }
}
