package com.ucf.dgrayson.dragon_radar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static int RC_SIGN_IN = 9001;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInflow = true;
    private boolean mSignInClicked = false;

    private GoogleApiClient mGoogleApiClient;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp = MediaPlayer.create(this, R.raw.dragonball_radar);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        //findViewById(R.id.demobutton).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startGame(View v){

        if(mp.isPlaying())
            mp.stop();

        mp.start();
        Intent intent = new Intent(this, MapsActivity.class);
        this.startActivity(intent);

    }

    public void launchDifficutlyScreen(View v){
        mp.start();
        Intent i = new Intent(this, DifficultyScreen.class);
        startActivity(i);
    }

    public void Exit(View view) {
        mp.start();
        mGoogleApiClient.disconnect();
        finish();
    }

    // Code used to connect to Gooogle Play Services

    @Override
    public void onConnected(Bundle bundle) {
        // Unlock Dragonball Hunter achivement
        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.dragonball_hunter_achivement_id));
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {

        mp.start();

        if(v.getId() == R.id.sign_in_button ){
            mSignInClicked = true;
            mGoogleApiClient.connect();
        }
        /*else if(v.getId() == R.id.demobutton){
            DataHolder.setData("demo");
        }*/
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(mResolvingConnectionFailure){
            return;
        }

        if(mSignInClicked || mAutoStartSignInflow){
            mAutoStartSignInflow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = false;

            if(!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getResources().getString(R.string.signin_other_error))){
                mResolvingConnectionFailure = false;
            }
        }
    }
}

