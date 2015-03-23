package net.baragon.MyFitnessBuddy.client;

import android.widget.ProgressBar;
import net.baragon.MyFitnessBuddy.DiaryActivity;
import net.baragon.MyFitnessBuddy.R;
import net.baragon.MyFitnessBuddy.util.FoodEntry;


public class LogFoodTask extends ALoadingTask<Object, Void, Integer> {
    DiaryActivity activity;
    int meal;
    FoodEntry foodEntry;

    public LogFoodTask(DiaryActivity activity, LoadingTaskExecutor executor, OnTaskFinishListener callback) {
        super((ProgressBar) activity.findViewById(R.id.mealsList_progressBar), executor, callback);
        this.activity = activity;
    }

    @Override
    protected Integer doInBackground(Object... params) {
        foodEntry = (FoodEntry) params[0];
        meal = (Integer) params[1];
        boolean newServing = (Boolean) params[2];
        return executor.execLogFood(foodEntry, meal, newServing);
    }

    @Override
    protected void onPostLoad(Integer integer) {
        activity.onEntryLogged(meal, foodEntry);
    }
}
