package com.example.dgrayson.maps_test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.util.ArrayList;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;

    private LatLng UCF = new LatLng(28.601975, -81.200553);

    // Map Boundary Variables
    private LatLng wUCF = new LatLng(28.602399, -81.203974);
    private LatLng eUCF = new LatLng(28.602265, -81.196429);
    private LatLng nUCF = new LatLng(28.605118, -81.200141);
    private LatLng sUCF = new LatLng(28.599044, -81.200218);

    // Home boundary vairables for debugging
    private LatLng nHome = new LatLng(28.710680, -81.113907);
    private LatLng sHome = new LatLng(28.709947, -81.113851);
    private LatLng wHome = new LatLng(28.710325, -81.114635);
    private LatLng eHome = new LatLng(28.710321, -81.113052);

    public Player p;

    public LocationListener locListener;
    public LocationManager locManager;

    Circle circle = null;

    public String diff;
    public long timer;

    public boolean leaderboardShown = false;

    // Alert dialog used for debugging
    public AlertDialog.Builder alert;

    private ArrayList<Dragonball> list = new ArrayList<Dragonball>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        alert = new AlertDialog.Builder(this);

        if(mMap != null) {
            mMap.setMyLocationEnabled(true);
        }

        p = new Player(0, 0, this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        getDifficulty();

        new CountDownTimer(timer, 1000){

            public void onTick(long millisUntilFinished){
                p.score--;
            }

            public void onFinish(){
                alert.setMessage("Time UP");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.show();

                if(leaderboardShown == false) {

                    if(DataHolder.getData() == "easy"){
                        Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.EASY_LEADERBOARD_ID), p.score);
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkI2buDlbEcEAIQAA"), 1);
                    }
                    else if(DataHolder.getData() == "medium"){
                        Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.MEDIUM_LEADERBOARD_ID), p.score);
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkI2buDlbEcEAIQBw"), 1);
                    }
                    else if(DataHolder.getData() == "hard"){
                        Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.HARD_LEADERBOARD_ID), p.score);
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkI2buDlbEcEAIQCA"), 1);
                    }
                    //launchMainMenu();
                    leaderboardShown = true;
                }
            }
        }.start();


    }

    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        // Set map to move to and zoom in on UCF
        CameraUpdate center = CameraUpdateFactory.newLatLng(UCF);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        // Change the google map type
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //mMap.addTileOverlay(new TileOverlayOptions().tileProvider(new CustomTileMapProvider(getResources().getAssets())));


        // Add center marker
        mMap.addMarker(new MarkerOptions().position(UCF).title("This is UCF"));

        // Add boundary markers
        /*mMap.addMarker(new MarkerOptions().position(nUCF).title("This is North UCF"));
        mMap.addMarker(new MarkerOptions().position(sUCF).title("This is South UCF"));
        mMap.addMarker(new MarkerOptions().position(eUCF).title("This is East UCF"));
        mMap.addMarker(new MarkerOptions().position(wUCF).title("This is West UCF"));*/

        // Generate and place markers in random locations within the boundaries
        for(int i = 0; i < 7; i++) {

            LatLng pos = generateCoordinates();

            switch (i) {
                case 0:
                    list.add(new Dragonball(1, pos, mMap));
                    //mMap.addMarker(new MarkerOptions().position(pos).title("This is the " + (i + 1) + " star Dragonball!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ball1)));
                    break;
                case 1:
                    list.add(new Dragonball(2, pos, mMap));
                    //mMap.addMarker(new MarkerOptions().position(pos).title("This is the " + (i + 1) + " star Dragonball!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ball2)));
                    break;
                case 2:
                    list.add(new Dragonball(3, pos, mMap));
                    //mMap.addMarker(new MarkerOptions().position(pos).title("This is the " + (i + 1) + " star Dragonball!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ball3)));
                    break;
                case 3:
                    list.add(new Dragonball(4, pos, mMap));
                    //mMap.addMarker(new MarkerOptions().position(pos).title("This is the " + (i + 1) + " star Dragonball!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ball4)));
                    break;
                case 4:
                    list.add(new Dragonball(5, pos, mMap));
                    //mMap.addMarker(new MarkerOptions().position(pos).title("This is the " + (i + 1) + " star Dragonball!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ball5)));
                    break;
                case 5:
                    list.add(new Dragonball(6, pos, mMap));
                    //mMap.addMarker(new MarkerOptions().position(pos).title("This is the " + (i + 1) + " star Dragonball!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ball6)));
                    break;
                case 6:
                    list.add(new Dragonball(7, pos, mMap));
                    //mMap.addMarker(new MarkerOptions().position(pos).title("This is the " + (i + 1) + " star Dragonball!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ball7)));
                    break;
            }
        }

        // Create Location Manager and Location Listener
        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locListener = new LocationListener() {

            // Update player coordinates when location has changed
            public void onLocationChanged(Location loc){

                if(circle != null) {
                    circle.remove();
                }

                p.updateLocation(loc.getLatitude(), loc.getLongitude());


                circle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(p.getLatitude(), p.getLongitude()))
                        .radius(10)
                        .fillColor(Color.TRANSPARENT)
                        .strokeColor(Color.BLACK ));

                p.checkLocation(list, circle);

                if(p.itemsCollected > 0){
                    if(p.itemsCollected == 1){
                        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.dragonball_finder_achivement_id));
                    }
                    else if(p.itemsCollected == 6){
                        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.dragonball_pursuer_achivement_id));
                    }
                }

                if(list.size() == 0){
                    if(leaderboardShown == false) {
                        alert.setMessage("You have collected all of the dragonballs!");
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.show();

                        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.dragonball_knight_achivement_id));

                        if(DataHolder.getData() == "easy"){
                            Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.EASY_LEADERBOARD_ID), p.score);
                            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkI2buDlbEcEAIQAA"), 1);
                        }
                        else if(DataHolder.getData() == "medium"){
                            Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.MEDIUM_LEADERBOARD_ID), p.score);
                            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkI2buDlbEcEAIQBw"), 1);
                        }
                        else if(DataHolder.getData() == "hard"){
                            Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.HARD_LEADERBOARD_ID), p.score);
                            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkI2buDlbEcEAIQCA"), 1);
                        }

                        leaderboardShown = true;
                    }

                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras){}

            @Override
            public void onProviderEnabled(String provider){}

            @Override
            public void onProviderDisabled(String provider){}
        };

        // Start listening for location changes
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locListener);
    }

    // Generate the longitude and latitude for the markers
    public LatLng generateCoordinates(){

        double tempLat = 0;
        double tempLng = 0;

        Random r = new Random();

        boolean running = true;

        while(running) {
            tempLat = sUCF.latitude + (nUCF.latitude - sUCF.latitude) * r.nextDouble();
            tempLng = wUCF.longitude + (eUCF.longitude - wUCF.longitude) * r.nextDouble();

            if(!inFountain(tempLat, tempLng) && !inStudentUnion(tempLat, tempLng) && !inParkingLot(tempLat, tempLng) && !inArboretum(tempLat, tempLng))
                running = false;
        }

        /*double tempLat = sHome.latitude + (nHome.latitude - sHome.latitude) * r.nextDouble();
        double tempLng = wHome.longitude + (eHome.longitude - wHome.longitude) * r.nextDouble();*/

        return new LatLng(tempLat, tempLng);
    }

    public void launchMainMenu(){
        System.exit(0);
    }

    public void getDifficulty(){
        diff = DataHolder.getData();

        switch(diff){
            case "easy":
                timer = 18000000;
                alert.setMessage("Difficulty set to Easy mode. You have 30 minutes to find all of the Dragonballs.");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
                break;
            case "medium":
                timer = 12000000;
                alert.setMessage("Difficulty set to Medium mode. You have 20 minutes to find all of the Dragonballs.");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
                break;
            case "hard":
                timer = 600000;
                alert.setMessage("Difficulty set to Hard mode. You have 10 minutes to find all of the Dragonballs.");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
                break;
        }


    }

    public Boolean inStudentUnion(double lat, double lng){
        LatLng wUnion = new LatLng(28.602372, -81.201327);
        LatLng eUnion = new LatLng(28.602232, -81.199142);
        LatLng nUnion = new LatLng(28.603271, -81.200015);
        LatLng sUnion = new LatLng(28.601738, -81.200189);

        if(lat < nUnion.latitude && lat > sUnion.latitude && lng < eUnion.longitude && lng > wUnion.longitude){
            return true;
        }
        else
            return false;
    }

    public Boolean inFountain(double lat, double lng){
        LatLng wFountain = new LatLng(28.599621, -81.202267);
        LatLng eFountain = new LatLng(28.599581, -81.201699);
        LatLng nFountain = new LatLng(28.599812, -81.201942);
        LatLng sFountain = new LatLng(28.599394, -81.201910);

        if(lat < nFountain.latitude && lat > sFountain.latitude && lng < eFountain.longitude && lng > wFountain.longitude){
            return true;
        }
        else
            return false;
    }

    public Boolean inParkingLot(double lat, double lng){
        LatLng wLot1 = new LatLng(28.602087, -81.203815);
        LatLng eLot1 = new LatLng(28.602087, -81.202742);
        LatLng nLot1 = new LatLng(28.602494, -81.203219);
        LatLng sLot1 = new LatLng(28.602494, -81.203219);

        LatLng wLot2 = new LatLng(28.602339, -81.197652);
        LatLng eLot2 = new LatLng(28.602307, -81.196597);
        LatLng nLot2 = new LatLng(28.602984, -81.197097);
        LatLng sLot2 = new LatLng(28.601674, -81.197077);

        LatLng wLot3 = new LatLng(28.604921, -81.201832);
        LatLng eLot3 = new LatLng(28.604997, -81.200453);
        LatLng nLot3 = new LatLng(28.605511, -81.201143);
        LatLng sLot3 = new LatLng(28.604521, -81.201077);

        LatLng wLot4 = new LatLng(28.604102, -81.198533);
        LatLng eLot4 = new LatLng(28.602341, -81.195490);
        LatLng nLot4 = new LatLng(28.605545, -81.197027);
        LatLng sLot4 = new LatLng(28.603146, -81.197160);

        if((lat < nLot1.latitude && lat > sLot1.latitude && lng < eLot1.longitude && lng > wLot1.longitude) ||
                (lat < nLot2.latitude && lat > sLot2.latitude && lng < eLot2.longitude && lng > wLot2.longitude) ||
                (lat < nLot3.latitude && lat > sLot3.latitude && lng < eLot3.longitude && lng > wLot3.longitude) ||
                (lat < nLot4.latitude && lat > sLot4.latitude && lng < eLot4.longitude && lng > wLot4.longitude)){
            return true;
        }
        else
            return false;
    }

    public Boolean inArboretum(double lat, double lng){
        LatLng wArbor = new LatLng(28.599961, -81.197449);
        LatLng eArbor = new LatLng(28.599908, -81.194758);
        LatLng nArbor = new LatLng(28.601534, -81.195864);
        LatLng sArbor = new LatLng(28.598898, -81.195877);

        if(lat < nArbor.latitude && lat > sArbor.latitude && lng < eArbor.longitude && lng > wArbor.longitude){
            return true;
        }
        else
            return false;
    }

    @Override
    public void onConnected(Bundle bundle) {
        // unlock dragonball seeker achivement
        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.dragonball_seeker_achivement_id));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
