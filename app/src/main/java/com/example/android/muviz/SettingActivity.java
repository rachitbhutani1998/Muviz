package com.example.android.muviz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;

import com.example.android.muviz.data.PreferenceConfig;

public class SettingActivity extends AppCompatActivity {
    String sort = "popularity", order = ".desc";
    RadioButton sortPop, sortAvg, orderAsc, orderDesc;
    PreferenceConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sortPop = findViewById(R.id.sort_popular);
        sortAvg = findViewById(R.id.sort_average);
        orderAsc = findViewById(R.id.order_asc);
        orderDesc = findViewById(R.id.order_desc);

        config = PreferenceConfig.getInstance(this);

        if (config.getString(PreferenceConfig.SORT_BASIS, "popularity").equals("vote_average"))
            sortAvg.toggle();
        else sortPop.toggle();

        if (config.getString(PreferenceConfig.SORT_ORDER, ".desc").equals(".asc"))
            orderAsc.toggle();
        else orderDesc.toggle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            saveSettings();
        }
        return true;
    }

    private void saveSettings() {
        Intent goToMain = new Intent(SettingActivity.this, GridActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (sortPop.isChecked())
            sort = "popularity";
        if (sortAvg.isChecked())
            sort = "vote_average";
        if (orderDesc.isChecked())
            order = ".desc";
        if (orderAsc.isChecked())
            order = ".asc";
        config.saveString(PreferenceConfig.SORT_ORDER, order);
        config.saveString(PreferenceConfig.SORT_BASIS, sort);
        startActivity(goToMain);
        finish();
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }
}
