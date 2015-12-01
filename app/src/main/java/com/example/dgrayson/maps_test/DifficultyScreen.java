package com.example.dgrayson.maps_test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.dgrayson.maps_test.R;

/**
 * Created by jorge_000 on 11/24/2015.
 */
public class DifficultyScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.difficultylayout);
    }

    public void back(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
