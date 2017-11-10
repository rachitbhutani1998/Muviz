package com.example.android.muviz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

public class GridActivity extends AppCompatActivity {
    static final String TAG= "GridActivity";
    TextView mGridView;
    int popular=1,rated=2;
    URL mUrl;
    String order=".desc",sort="popularity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        mGridView=findViewById(R.id.movies_grid);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return true;
    }

    void fillLayout(String query){
        mUrl=NetworkUtils.buildURL(query);
        mGridView.setText(mUrl.toString());
        Log.e(TAG, "onOptionsItemSelected() returned: " + mUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i=item.getItemId();
        switch (i){
            case R.id.sort_popular:
                sort="popularity";
                fillLayout(sort+order);
                break;
            case R.id.sort_rating:
                sort="vote_average";
                fillLayout(sort+order);
                break;
            case R.id.filter_button:
                if (order.equals(".desc"))
                    order=".asc";
                else
                    order=".desc";
                fillLayout(sort+order);
                break;
        }
        return true;
    }

}
