package net.baragon.MyFitnessBuddy.client;

import android.widget.ProgressBar;
import net.baragon.MyFitnessBuddy.AddFoodActivity;
import net.baragon.MyFitnessBuddy.R;
import net.baragon.MyFitnessBuddy.util.FoodInfo;

import java.util.ArrayList;


public class FindFoodTask extends ALoadingTask<Object, Void, ArrayList<FoodInfo>> {
    private AddFoodActivity activity;

    public FindFoodTask(AddFoodActivity activity, LoadingTaskExecutor executor, OnTaskFinishListener callback) {
        super((ProgressBar) activity.findViewById(R.id.addFood_progressBar), executor, callback);
        this.activity = activity;
    }

    @Override
    protected ArrayList<FoodInfo> doInBackground(Object... params) {
        String name = (String) params[0];
        return executor.execFindFood(name);
    }

    @Override
    protected void onPostLoad(ArrayList<FoodInfo> foodInfos) {
        activity.onLoadSearchResult(foodInfos);
    }

}
