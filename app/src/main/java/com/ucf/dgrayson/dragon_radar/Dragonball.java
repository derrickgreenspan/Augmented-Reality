package com.ucf.dgrayson.dragon_radar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by dgrayson on 11/23/2015.
 */
public class Dragonball {

    public int star;
    public double latitude, longitude;
    private Marker marker;

    public Dragonball(int star, LatLng pos, GoogleMap map){
        this.star = star;
        this.latitude = pos.latitude;
        this.longitude = pos.longitude;

        switch(star) {

            case 1:
                this.marker = map.addMarker(new MarkerOptions().position(pos).title("This is the " + star + " star Dragonball!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ball1)));
                break;
            case 2:
                this.marker = map.addMarker(new MarkerOptions().position(pos).title("This is the " + star + " star Dragonball!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ball2)));
                break;
            case 3:
                this.marker = map.addMarker(new MarkerOptions().position(pos).title("This is the " + star + " star Dragonball!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ball3)));
                break;
            case 4:
                this.marker = map.addMarker(new MarkerOptions().position(pos).title("This is the " + star + " star Dragonball!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ball4)));
                break;
            case 5:
                this.marker = map.addMarker(new MarkerOptions().position(pos).title("This is the " + star + " star Dragonball!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ball5)));
                break;
            case 6:
                this.marker = map.addMarker(new MarkerOptions().position(pos).title("This is the " + star + " star Dragonball!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ball6)));
                break;
            case 7:
                this.marker = map.addMarker(new MarkerOptions().position(pos).title("This is the " + star + " star Dragonball!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ball7)));
                break;
        }
    }

    public void removeMarker(){
        marker.remove();
    }
}
