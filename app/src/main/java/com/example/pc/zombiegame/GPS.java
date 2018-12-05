package com.example.pc.zombiegame;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.text.DecimalFormat;

public class GPS {

    private LocationManager locationManager;

    private GameClientActivity clientParent;
    private Player playerParent;

    public GPS(GameClientActivity parent){
        clientParent = parent;

    }


    public GPS(Player parent){
        playerParent = parent;
    }

    public String getLocationService(){
        return Context.LOCATION_SERVICE;
    }



}

