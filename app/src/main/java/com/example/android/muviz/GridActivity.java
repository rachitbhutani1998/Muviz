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

import java.net.URL;

public class GridActivity extends AppCompatActivity {
    static final String TAG = "GridActivity";
    TextView mGridView;
    URL mUrl;
    String sort = "popularity",order = ".desc";
    MovieAsyncTask movieAsyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        Intent intent=getIntent();
        if (getIntent()!=null){
            sort=intent.getStringExtra("sort");
            order=intent.getStringExtra("order");
            Toast.makeText(this, "Sorted By"+sort +" Ordered By "+order, Toast.LENGTH_SHORT).show();
        }
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
        movieAsyncTask.execute(mUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.settings:
                Intent goToSettings = new Intent(GridActivity.this, SettingActivity.class);
                goToSettings.putExtra("sort",sort+"");
                goToSettings.putExtra("order",order+"");
                startActivity(goToSettings);
                break;
        }
        return true;
    }

    public class MovieAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String jsonResponse = NetworkUtils.getJSONfromURL(url);
            Log.e(TAG, "onOptionsItemSelected() returned: " + mUrl);
            Log.e(TAG, "onOptionsItemSelected() returned: " + jsonResponse);
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            mGridView.setText(mUrl.toString());
            mGridView.append("\n\n" + s);
        }
    }
}
