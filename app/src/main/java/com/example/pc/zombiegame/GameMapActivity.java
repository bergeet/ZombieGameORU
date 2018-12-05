package com.example.pc.zombiegame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;

public class GameMapActivity extends AppCompatActivity {

    PointsGraphSeries<DataPoint> xySeries;

    GraphView mScatterPlot;

    private ArrayList<Player> playerArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_map);


    }
}
