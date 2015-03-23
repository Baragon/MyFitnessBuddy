package net.baragon.MyFitnessBuddy.util;

import java.io.Serializable;


public class Macros implements Serializable {
    private final double protein;
    private final double carbs;
    private final double fats;
    private final double cal;

    public Macros(double cal, double protein, double carbs, double fats) {
        this.cal = cal;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
    }

    public Macros() {
        cal = 0;
        protein = 0;
        carbs = 0;
        fats = 0;
    }

    public Macros plus(Macros macros) {
        return new Macros(cal + macros.cal, protein + macros.protein, carbs + macros.carbs, fats + macros.fats);
    }

    public Macros minus(Macros macros) {
        return new Macros(cal - macros.cal, protein - macros.protein, carbs - macros.carbs, fats - macros.fats);
    }

    public double getProtein() {
        return protein;
    }

    public double getCal() {
        return cal;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFat() {
        return fats;
    }
}
