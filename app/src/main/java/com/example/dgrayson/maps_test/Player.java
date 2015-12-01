package com.example.dgrayson.maps_test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import com.google.android.gms.maps.model.Circle;

import java.util.ArrayList;

/**
 * Created by dgrayson on 11/20/2015.
 */
public class Player {

    private double latitude;
    private double longitude;
    private int itemsCollected = 0;
    public int score = 600000;

    public AlertDialog.Builder alert;

    public Player(double lat, double lon, MapsActivity a){
        this.latitude = lat;
        this.longitude = lon;

        alert = new AlertDialog.Builder(a);
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public void setLatitude(double lat){
        this.latitude = lat;
    }

    public void setLongitude(double lon){
        this.longitude = lon;
    }

    public void updateLocation(double lat, double lon){
        this.latitude = lat;
        this.longitude = lon;
    }

    public void checkLocation(ArrayList<Dragonball> list, Circle circ){
        float[] distance = new float[2];

        for(int i = 0; i < list.size(); i++){

            Location.distanceBetween(list.get(i).latitude, list.get(i).longitude, circ.getCenter().latitude,
                    circ.getCenter().longitude, distance);

            if(distance[0] < circ.getRadius()) {
                alert.setMessage("You picked up the " + list.get(i).star + " star Dragonball!" );
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
                list.get(i).removeMarker();
                list.remove(i);
                this.itemsCollected++;
            }
        }
    }

}
