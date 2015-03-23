package net.baragon.MyFitnessBuddy.client;

import android.widget.ProgressBar;
import net.baragon.MyFitnessBuddy.AddFoodActivity;
import net.baragon.MyFitnessBuddy.R;
import net.baragon.MyFitnessBuddy.util.FoodInfo;


public class NewFoodTask extends ALoadingTask<FoodInfo, Void, FoodInfo> {
    AddFoodActivity activity;

    public NewFoodTask(AddFoodActivity activity, LoadingTaskExecutor executor, OnTaskFinishListener callback) {
        super((ProgressBar) activity.findViewById(R.id.addFood_progressBar), executor, callback);
        this.activity = activity;
    }

    @Override
    protected FoodInfo doInBackground(FoodInfo... params) {
        FoodInfo food = params[0];
        String name = food.name;
        double cal = food.cals;
        double protein = food.protein;
        double carbs = food.carbs;
        double fat = food.fats;
        return executor.execNewFood(food, name, cal, protein, carbs, fat);
    }

    @Override
    protected void onPostLoad(FoodInfo foodInfo) {
        activity.onNewFoodAdded(foodInfo);
    }
}
