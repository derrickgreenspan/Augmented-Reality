package com.example.dgrayson.maps_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by jorge_000 on 11/24/2015.
 */
public class OptionsScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.optionslayout);
    }


    public void Getmain(View view) {
        setResult(RESULT_OK);
        finish();
    }

    public void Getdifficulty(View view) {
        Intent GetDifficultyScreen = new Intent(this, DifficultyScreen.class);
        startActivity(GetDifficultyScreen);
    }

   /* public void Gettime(View view) {
        Intent GetTimeScreen = new Intent(this, TimeScreen.class);
        startActivity(GetTimeScreen);
    }*/
}
