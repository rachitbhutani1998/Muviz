package com.example.android.muviz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class GridActivity extends AppCompatActivity {
    static final String TAG = "GridActivity";
    TextView mGridView;
    URL mUrl;
    String sort = "popularity", order = ".desc";
    MovieAsyncTask movieAsyncTask;
    ArrayList<Movies> moviesArrayList=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        Intent intent = getIntent();
        sort = intent.getStringExtra("sort");
        order = intent.getStringExtra("order");
        if (sort==null||order==null){
            sort = "popularity";
            order = ".desc";
        }
        Toast.makeText(this, "Sorted By" + sort + " Ordered By " + order, Toast.LENGTH_SHORT).show();
        mGridView = findViewById(R.id.movies_grid);
        movieAsyncTask = new MovieAsyncTask();
        fillLayout(sort + order);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    void fillLayout(String query) {
        mUrl = NetworkUtils.buildURL(query);
        mGridView.setText(mUrl.toString());
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
        protected ArrayList<Movies> doInBackground(URL... urls) {
            URL url = urls[0];
            String jsonResponse = null;
            try {
                jsonResponse = NetworkUtils.makeHttpRequest(url);
                moviesArrayList=NetworkUtils.extractMovieList(jsonResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "onOptionsItemSelected() returned: " + mUrl);
            Log.e(TAG, "onOptionsItemSelected() returned: " + jsonResponse);
            return moviesArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> s) {
            mGridView.setText(mUrl.toString());
            for(int i=0;i<s.size();i++)
                mGridView.append("\n\n" + s.get(i).getMovieId()+"\n"+s.get(i).getPoster());
        }
    }
}
