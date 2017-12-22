package com.example.android.muviz;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class GridActivity extends AppCompatActivity {
    static final String TAG = "GridActivity";
    GridView mGridView;
    URL mUrl;
    String sort = "popularity", order = ".desc";
    MovieAsyncTask movieAsyncTask;
    ArrayList<Movies> moviesArrayList;
    MovieAdapter mAdapter;
    ProgressBar progressSpinner;
    LinearLayout noConnView;
    ConnectivityManager manager;
    Button mRetry;
    ShortcutManager mShortcut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        Intent widgetIntent = new Intent(this, DetailActivity.class);
        Intent intent = getIntent();
        sort = intent.getStringExtra("sort");
        order = intent.getStringExtra("order");
        if (sort == null || order == null) {
            sort = "popularity";
            order = ".desc";
        }
        progressSpinner = findViewById(R.id.progress_bar);
        mGridView = findViewById(R.id.movies_grid);
        movieAsyncTask = new MovieAsyncTask();
        moviesArrayList = new ArrayList<>();
        mAdapter = new MovieAdapter(this, moviesArrayList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent goToDetail = new Intent(GridActivity.this, DetailActivity.class);
                goToDetail.putExtra("movie_id", moviesArrayList.get(i).getMovieId());
                goToDetail.putExtra("movie_title", moviesArrayList.get(i).getMovieTitle());
                startActivity(goToDetail);
            }
        });
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Snackbar snackbar = Snackbar.make(view, moviesArrayList.get(i).getMovieTitle(), Snackbar.LENGTH_SHORT)
                        .setAction("Open", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent goToDetail = new Intent(GridActivity.this, DetailActivity.class);
                                goToDetail.putExtra("movie_id", moviesArrayList.get(i).getMovieId());
                                goToDetail.putExtra("movie_title", moviesArrayList.get(i).getMovieTitle());
                                startActivity(goToDetail);
                            }
                        });
                snackbar.show();
                return true;
            }
        });
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
        widgetIntent.putExtra("movie_id", "346364");
        widgetIntent.putExtra("movie_title", "It");
        widgetIntent.setAction("Open_details");
        if (Build.VERSION.SDK_INT >= 25) {
            mShortcut = getSystemService(ShortcutManager.class);
            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(this, "shortcut")
                    .setShortLabel("Most Popular")
                    .setLongLabel("The most popular movie yet")
                    .setIntent(widgetIntent).build();
            mShortcut.setDynamicShortcuts(Collections.singletonList(shortcutInfo));
        }
    }

    public void checkInternetConnection() {
        if (manager != null) {
            if (manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                    || manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
                fillLayout(sort + order);
            else noConnView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    void fillLayout(String query) {
        mUrl = NetworkUtils.buildURL(query);
        if (mUrl==null)
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
            Log.e(TAG, "onOptionsItemSelected() returned: " + mUrl);
            return moviesArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> s) {
            progressSpinner.setVisibility(View.GONE);
            mAdapter.clear();
            mAdapter.addAll(s);
        }
    }

}
