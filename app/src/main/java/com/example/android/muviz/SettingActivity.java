package com.example.android.muviz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;

public class SettingActivity extends AppCompatActivity {
    String sort = "popularity", order = ".desc";
    RadioButton sortPop, sortAvg, orderAsc, orderDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Intent intent = getIntent();
        sortPop = findViewById(R.id.sort_popular);
        sortAvg = findViewById(R.id.sort_average);
        orderAsc = findViewById(R.id.order_asc);
        orderDesc = findViewById(R.id.order_desc);
        if (intent.getStringExtra("sort").equals("vote_average"))
            sortAvg.toggle();
        else sortPop.toggle();

        if (intent.getStringExtra("order").equals(".asc"))
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
        goToMain.putExtra("sort", sort + "");
        goToMain.putExtra("order", order + "");
        startActivity(goToMain);
        finish();
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }
}
