package com.ucf.dgrayson.dragon_radar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;

import com.google.android.gms.maps.model.Circle;

import java.util.ArrayList;

/**
 * Created by dgrayson on 11/20/2015.
 */
public class Player {

    private double latitude;
    private double longitude;
    public int itemsCollected = 0;
    public int score = 600000;

    public AlertDialog.Builder alert;

    // Create player object
    public Player(double lat, double lon, MapsActivity a){
        this.latitude = lat;
        this.longitude = lon;

        alert = new AlertDialog.Builder(a);

        // Change initial score depending on difficulty
        if(DataHolder.getData() == "easy"){
            this.score = 1800000;
        }
        else if (DataHolder.getData() == "medium") {
            this.score = 1200000;
        }
        else if (DataHolder.getData() == "hard") {
            this.score = 600000;
        }
    }


    // getters and setter s to return longitude adn latitidue
    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public void updateLocation(double lat, double lon){
        this.latitude = lat;
        this.longitude = lon;
    }


    // Functino used to check teh current location and determine if a dragonball is within range
    public void checkLocation(ArrayList<Dragonball> list, Circle circ){

        // Variable used to store distance
        float[] distance = new float[2];

        for(int i = 0; i < list.size(); i++){

            // Check the distance between two points
            Location.distanceBetween(list.get(i).latitude, list.get(i).longitude, circ.getCenter().latitude,
                    circ.getCenter().longitude, distance);

            // If the item is within the circle display message and remove item list and marker from the map
            if(distance[0] < circ.getRadius()) {

                // Display message on screen
                alert.setMessage("You picked up the " + list.get(i).star + " star Dragonball!" );
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();

                // Remove marker and increment itemes collected
                list.get(i).removeMarker();
                list.remove(i);
                this.itemsCollected++;
            }
        }
    }

}
