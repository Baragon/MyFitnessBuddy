package net.baragon.MyFitnessBuddy.client;

import android.widget.ProgressBar;
import net.baragon.MyFitnessBuddy.DiaryActivity;
import net.baragon.MyFitnessBuddy.R;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class NewMealTask extends ALoadingTask<Object, Void, Integer> {
    DiaryActivity activity;
    String name;

    public NewMealTask(DiaryActivity activity, LoadingTaskExecutor executor, OnTaskFinishListener callback) {
        super((ProgressBar) activity.findViewById(R.id.mealsList_progressBar), executor, callback);
        this.activity = activity;
    }

    @Override
    protected Integer doInBackground(Object... params) {
        name = (String) params[0];
        DateTime dateTime = (DateTime) params[1];
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        String date = fmt.print(dateTime);
        return executor.execNewMeal(name, date);
    }

    @Override
    protected void onPostLoad(Integer result) {
        activity.addNewMeal(result, name);
    }

}
