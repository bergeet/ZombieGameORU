package com.example.pc.zombiegame;

import java.net.PasswordAuthentication;

public class Player {


    private int number = ++LoginActivity.number_of_players;
    private String username;
    private String password; //TODO: Byt kryptering för lösenord

    private enum ZombieState{HUMAN, ZOMBIE, UNKNOWN};
    private ZombieState zombie_state = ZombieState.UNKNOWN;


    public Player (String username, String password) {
        this.username = username;
        this.password = password;
    }


    public void toHuman() {
        zombie_state = ZombieState.HUMAN;
    }

    public void toZombie() {
        zombie_state = ZombieState.ZOMBIE;
    }

    public boolean is_zombie() {
        return zombie_state == ZombieState.ZOMBIE;
    }


    public void setZombie(boolean is_zombie) {
		if (is_zombie)
			toZombie();
		else
			toHuman();
	}

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getAltitude() {
        return altitude;
    }

    private GPS gps;

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    private double latitude;
    private double longitude;
    private double accuracy;
    private double altitude;





}
