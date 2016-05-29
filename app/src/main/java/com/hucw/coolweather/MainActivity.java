package com.hucw.coolweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hucw.coolweather.db.CoolWeatherDBHelper;

public class MainActivity extends AppCompatActivity {

    private Button mBtnCrateDatabase;

    private CoolWeatherDBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new CoolWeatherDBHelper(this, "Book", null, 1);

        mBtnCrateDatabase = (Button) findViewById(R.id.create_button);
        mBtnCrateDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDbHelper.getWritableDatabase();
            }
        });
    }
}
