package net.baragon.MyFitnessBuddy.util;

import java.util.ArrayList;


public class Meal {
    public int id;
    public String name;
    public ArrayList<FoodEntry> foodEntries;

    public Meal(int id, String name) {
        this.id = id;
        this.name = name;
        foodEntries = new ArrayList<FoodEntry>();
    }

    public Meal(int id, String name, ArrayList<FoodEntry> foodEntries) {
        this.id = id;
        this.name = name;
        this.foodEntries = foodEntries;
    }

    public Macros getTotalMacros() {
        double cals = 0;
        double protein = 0;
        double carbs = 0;
        double fats = 0;
        for (FoodEntry foodEntry : foodEntries) {
            double grams = foodEntry.amount * foodEntry.servingSize * 0.01;
            cals += grams * foodEntry.foodInfo.cals;
            protein += grams * foodEntry.foodInfo.protein;
            carbs += grams * foodEntry.foodInfo.carbs;
            fats += grams * foodEntry.foodInfo.fats;
        }
        return new Macros(cals, protein, carbs, fats);
    }

}
