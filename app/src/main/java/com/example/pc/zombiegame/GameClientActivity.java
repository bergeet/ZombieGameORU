package com.example.pc.zombiegame;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameClientActivity extends AppCompatActivity {

    private GPS gps;
    private Player player = null;
    private LocationManager locationManager;
    private ArrayList<Player> playerArray;
    private aLocationListener locationListener;

    private DecimalFormat decimalFormat = new DecimalFormat("0.0000");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private TextView textView = null;
    private Button mapBtn = null;
    private Button connectBtn = null;


    private ServerHandlerThread serverHandlerThread = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (player == null) {
            player = new Player("Elliot", "H");

        }
        setContentView(R.layout.activity_game_client);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new aLocationListener(player);

        textView = (TextView) findViewById(R.id.textView1);

        serverHandlerThread = new ServerHandlerThread(this);

        mapBtn = (Button) findViewById(R.id.mapBtn);
        mapBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String heej = "Hej";

                final Sender sender = new Sender();
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
                    sender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, heej);
                else
                    sender.execute();
            }
        });

        connectBtn = (Button) findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                serverHandlerThread.execute();
            }
        });

    }




    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            print("Detta är en nyare Android (API " + android.os.Build.VERSION.SDK_INT + "), så appen måste begära GPS-rättigheter av användaren.");
            int hasPermission = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasPermission == android.content.pm.PackageManager.PERMISSION_GRANTED)
                print("Har redan GPS-rättigheter.");
            else
                this.requestPermissions((new String[]{Manifest.permission.ACCESS_FINE_LOCATION}), 4711);
        }
        else {
            print("Detta är en äldre Android (API " + android.os.Build.VERSION.SDK_INT + "), så GPS-rättigheterna i AndroidManifest.xml räcker.");
        }

        try {
            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException e) {
            print("requestLocationUpdates failed because missing permissions: " + e.getMessage());
        }
    }

    public void print(String input){
        Date now = new Date();
        String nowString = dateFormat.format(now);
        String line = nowString + ": " + input;
        textView.setText(textView.getText() + line + "\n");
        Log.d("**************", line);
        textView.invalidate();
        textView.postInvalidate();
    }

    private class aLocationListener implements LocationListener {

        private Player player;


        public aLocationListener(Player player){
            this.player = player;
        }


        @Override
        public void onLocationChanged(Location location) {

            player.setLongitude(location.getLongitude());
            player.setLatitude(location.getLatitude());
            player.setAccuracy(location.getAccuracy());
            player.setAccuracy(location.getAltitude());
//
//            print("Received a GPS location: Long " + decimalFormat.format(player.getLongitude()) +
//                    ", lat " + decimalFormat.format(player.getLatitude()) +
//                    ", accuracy " + decimalFormat.format(player.getAccuracy()) +
//                    ", alt " + decimalFormat.format(player.getAccuracy()));

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            print("GPS provider has changed" + status) ;
        }

        @Override
        public void onProviderEnabled(String provider) {
            print("GPS provider enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            print("GPS provider disabled");
        }


    }

    // Find the latest known location
    private Location getLatestKnownLocation() {
        try {
            Location locationFromGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationFromNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            long GPSLocationTime = 0;
            if (locationFromGPS == null)
                return locationFromNetwork;
            else if (locationFromNetwork == null)
                return locationFromGPS;
            else if (locationFromNetwork.getTime() > locationFromGPS.getTime())
                return locationFromNetwork;
            else
                return locationFromGPS;
        } catch (SecurityException e) {
            print("getLastKnownLocation failed because missing permissions: " + e.getMessage());
            return null;
        }
    } // getLatestKnownLocation

    private class Sender extends AsyncTask<String, Void, Void> {

        public void send_to_server(String input) {
            if (serverHandlerThread == null) {
                //TODO: ADD NOT CONNECTED
            }
            input = ++LoginActivity.seq_number + " " + input;
            //TODO: LOG WINDOW??
            serverHandlerThread.send_message(input);
        }



        @Override
        protected Void doInBackground(String... strings) {

            int count = strings.length;
            for(int i = 0; i < count; i++){
                try {

                    send_to_server(strings[i]);
                    Log.d("Sender run", strings[i]);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Run", e.toString());
                }
            }
            return null;
        }
    }

}
