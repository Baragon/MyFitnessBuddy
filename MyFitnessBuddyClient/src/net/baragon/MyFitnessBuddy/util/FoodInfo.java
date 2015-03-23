package net.baragon.MyFitnessBuddy.util;

import java.io.Serializable;
import java.util.ArrayList;


public class FoodInfo implements Serializable {
    public String name;
    public int id;
    public double protein;
    public double carbs;
    public double fats;
    public double cals;
    public ArrayList<Serving> servings;

    public FoodInfo(String name, double cals, double protein, double carbs, double fats) {
        this.name = name;
        this.protein = protein;
        this.cals = cals;
        this.carbs = carbs;
        this.fats = fats;
        servings = new ArrayList<Serving>();
    }

    public FoodInfo(int id, String name, double cals, double protein, double carbs, double fats) {
        this.id = id;
        this.name = name;
        this.protein = protein;
        this.cals = cals;
        this.carbs = carbs;
        this.fats = fats;
        servings = new ArrayList<Serving>();
    }

    public FoodInfo(int id, String name, double cals, double protein, double carbs, double fats, ArrayList<Serving> servings) {
        this.id = id;
        this.name = name;
        this.protein = protein;
        this.cals = cals;
        this.carbs = carbs;
        this.fats = fats;
        this.servings = servings;
    }

    public Serving getServingByName(String name) {
        for (int i = 0; i < servings.size(); i++) {
            if (servings.get(i).name.equalsIgnoreCase(name)) return servings.get(i);
        }
        return null;
    }
}
