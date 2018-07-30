package com.livenoproblem.travelsketch.Model;

public class Wind {
    private double spead;
    private double deg;

    public Wind(double spead, double deg) {
        this.spead = spead;
        this.deg = deg;

    }

    public double getSpead() {
        return spead;
    }

    public void setSpead(double spead) {
        this.spead = spead;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }
}
