package com.example.android.muviz;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.muviz.data.Movies;
import com.example.android.muviz.data.PreferenceConfig;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class GridActivity extends AppCompatActivity {
    static final String TAG = "GridActivity";
    RecyclerView mGridView;
    URL mUrl;
    String sort;
    String order;
    MovieAsyncTask movieAsyncTask;
    ArrayList<Movies> moviesArrayList;
    MovieAdapter mAdapter;
    ProgressBar progressSpinner;
    View noConnView;
    ConnectivityManager manager;
    Button mRetry;
    ShortcutManager mShortcut;

    PreferenceConfig config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        config = PreferenceConfig.getInstance(this);
        sort = config.getString(PreferenceConfig.SORT_BASIS, "popularity");
        order = config.getString(PreferenceConfig.SORT_ORDER, ".desc");


        Intent widgetIntent = new Intent(this, DetailActivity.class);
        progressSpinner = findViewById(R.id.progress_bar);
        mGridView = findViewById(R.id.movies_grid);
        moviesArrayList = new ArrayList<>();
        mGridView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new MovieAdapter(this, moviesArrayList);
        mGridView.setAdapter(mAdapter);
        movieAsyncTask = new MovieAsyncTask();

        noConnView = findViewById(R.id.no_con_view);
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mRetry = findViewById(R.id.retry_button);
        checkInternetConnection();
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noConnView.setVisibility(View.INVISIBLE);
                checkInternetConnection();
            }
        });
        widgetIntent.setAction("Open_details");
        if (Build.VERSION.SDK_INT >= 25) {
            Icon i = Icon.createWithResource(getApplicationContext(), R.drawable.ic_group_black_24dp);
            mShortcut = getSystemService(ShortcutManager.class);
            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(this, "shortcut")
                    .setShortLabel("Most Popular")
                    .setIcon(i)
                    .setLongLabel("The most popular movie right now.")
                    .setIntent(widgetIntent).build();
            mShortcut.setDynamicShortcuts(Collections.singletonList(shortcutInfo));
        }
    }

    public void checkInternetConnection() {
        if (manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            fillLayout(sort + order);
        else noConnView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    void fillLayout(String query) {
        mUrl = NetworkUtils.buildURL(query);
        if (mUrl == null)
            Toast.makeText(this, "Check that API key in NetworkUtils", Toast.LENGTH_SHORT).show();
        else
            movieAsyncTask.execute(mUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.settings:
                Intent goToSettings = new Intent(GridActivity.this, SettingActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                goToSettings.putExtra("sort", sort + "");
                goToSettings.putExtra("order", order + "");
                startActivity(goToSettings);
                break;
        }
        return true;
    }

    public class MovieAsyncTask extends AsyncTask<URL, Void, ArrayList<Movies>> {
        @Override
        protected void onPreExecute() {
            progressSpinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movies> doInBackground(URL... urls) {
            URL url = urls[0];
            String jsonResponse;
            try {
                jsonResponse = NetworkUtils.makeHttpRequest(url);
                moviesArrayList = NetworkUtils.extractMovieList(jsonResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return moviesArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> s) {
            progressSpinner.setVisibility(View.GONE);
            moviesArrayList = s;
            mAdapter.setMovies(moviesArrayList);
            mAdapter.notifyDataSetChanged();
        }
    }

}
