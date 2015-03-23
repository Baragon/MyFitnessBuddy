package net.baragon.MyFitnessBuddy.util;

import java.io.Serializable;


public class Serving implements Serializable {
    public double grams;
    public String name;
    public int id;

    public Serving(int id, String name, double grams) {
        this.name = name;
        this.id = id;
        this.grams = grams;
    }
}
