package net.baragon.MyFitnessBuddy.client;

import android.widget.ProgressBar;
import net.baragon.MyFitnessBuddy.AddFoodActivity;
import net.baragon.MyFitnessBuddy.R;
import net.baragon.MyFitnessBuddy.util.FoodInfo;

import java.util.ArrayList;


public class RecentFoodTask extends ALoadingTask<Object, Void, ArrayList<FoodInfo>> {
    AddFoodActivity activity;

    public RecentFoodTask(AddFoodActivity activity, LoadingTaskExecutor executor, OnTaskFinishListener callback) {
        super((ProgressBar) activity.findViewById(R.id.addFood_progressBar), executor, callback);
        this.activity = activity;
    }

    @Override
    protected ArrayList<FoodInfo> doInBackground(Object... params) {
        return executor.execRecentFood();
    }

    @Override
    protected void onPostLoad(ArrayList<FoodInfo> foodInfos) {
        activity.onLoadRecentFoods(foodInfos);
    }
}
