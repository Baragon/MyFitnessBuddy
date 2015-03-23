package net.baragon.MyFitnessBuddy.util;

import java.io.Serializable;

public class FoodEntry implements Serializable {
    public int id;
    public double amount;
    public double servingSize;
    public String servingName;
    public FoodInfo foodInfo;

    public FoodEntry(int id, double amount, double servingSize, String servingName, FoodInfo foodInfo) {
        this.id = id;
        this.amount = amount;
        this.servingSize = servingSize;
        this.servingName = servingName;
        this.foodInfo = foodInfo;
    }

    public Macros getTotalMacros() {
        double serving = servingSize * amount * 0.01;
        return new Macros(foodInfo.cals * serving, foodInfo.protein * serving, foodInfo.carbs * serving, foodInfo.fats * serving);
    }

}
