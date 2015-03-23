package net.baragon.MyFitnessBuddy.util;

import java.util.ArrayList;


public class DiaryEntry {
    public ArrayList<Meal> meals;
    public Macros goals;

    public DiaryEntry(ArrayList<Meal> meals, Macros goals) {
        this.meals = meals;
        this.goals = goals;
    }
}
