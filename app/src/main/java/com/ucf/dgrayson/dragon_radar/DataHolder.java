package com.ucf.dgrayson.dragon_radar;

/**
 * Created by dgrayson on 12/2/2015.
 */

// This class is used to store the current difficulty and allow it
// to be accessed from anywhere in the app

public class DataHolder {
    static String difficulty = "medium";

    // Get and set data
    public static String getData(){return difficulty;}
    public static void setData(String diff){difficulty = diff;}
}
