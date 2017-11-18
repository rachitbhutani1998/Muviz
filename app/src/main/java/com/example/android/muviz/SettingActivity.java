package com.example.android.muviz;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    String sort="popularity",order=".desc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        RadioGroup sortGroup=findViewById(R.id.radio_sort);
        if (sortGroup.getCheckedRadioButtonId()==R.id.sort_average)
            sort="vote_average";
        else sort="popularity";

        RadioGroup orderGroup=findViewById(R.id.radio_order);
        if (orderGroup.getCheckedRadioButtonId()==R.id.order_asc)
            order=".asc";
        else order=".desc";

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_setting,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.save){
            saveSettings();
        }
        return true;
    }

    private void saveSettings() {
        Intent goToMain=new Intent(SettingActivity.this,GridActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        goToMain.putExtra("sort",sort+"");
        goToMain.putExtra("order",order+"");
        startActivity(goToMain);
        finish();
    }
}
