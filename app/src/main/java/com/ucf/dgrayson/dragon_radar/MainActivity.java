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

    // Variables used to check connectoin status
    private static int RC_SIGN_IN = 9001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInflow = true;
    private boolean mSignInClicked = false;

    // API client used to connect to google play services
    private GoogleApiClient mGoogleApiClient;

    // Mediaplayer to play sounds
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create new media player with dragon radar button sound effect
        mp = MediaPlayer.create(this, R.raw.dragonball_radar);

        // Create client to connect to google play services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();


        // Set the onclick listener for the sign in button
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


    //Start the game
    public void startGame(View v){

        // If the button sound is still playing stop it
        if(mp.isPlaying())
            mp.stop();


        // play button sound
        mp.start();

        // Launch new activity
        Intent intent = new Intent(this, MapsActivity.class);
        this.startActivity(intent);

    }


    // Launch the difficulty screen
    public void launchDifficutlyScreen(View v){
        mp.start();

        Intent i = new Intent(this, DifficultyScreen.class);
        startActivity(i);
    }


    // Exit app
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
        // Reconnect to client if connection is suspended
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {

        // play button sound
        mp.start();

        if(v.getId() == R.id.sign_in_button ){
            mSignInClicked = true;
            mGoogleApiClient.connect();
        }
        /*else if(v.getId() == R.id.demobutton){
            DataHolder.setData("demo");
        }*/
    }


    // Function used to check if the failed connectino is resolveing itself, if not an appropriate
    // error message will be displayed
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

