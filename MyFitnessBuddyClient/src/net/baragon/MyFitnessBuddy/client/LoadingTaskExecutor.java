package net.baragon.MyFitnessBuddy.client;

import net.baragon.MyFitnessBuddy.util.DiaryEntry;
import net.baragon.MyFitnessBuddy.util.FoodEntry;
import net.baragon.MyFitnessBuddy.util.FoodInfo;
import net.baragon.MyFitnessBuddy.util.Macros;
import org.joda.time.DateTime;

import java.util.ArrayList;


public interface LoadingTaskExecutor {
    public void Init();

    public Integer execLogFood(FoodEntry foodEntry, int meal, boolean newServing);

    public DiaryEntry execGetMeals(DateTime date);

    public void execDeleteEntry(int id);

    public ArrayList<FoodInfo> execFindFood(String name);

    public FoodInfo execNewFood(FoodInfo foodInfo, String name, double cal, double protein, double carbs, double fat);

    public Integer execNewMeal(String name, String date);

    public ArrayList<FoodInfo> execRecentFood();

    public void setGoals(Macros macros, DateTime date);

    public void DeleteLocalData();
}
