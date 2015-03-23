package net.baragon.MyFitnessBuddy.client;

import android.widget.ProgressBar;
import net.baragon.MyFitnessBuddy.DiaryActivity;
import net.baragon.MyFitnessBuddy.R;
import net.baragon.MyFitnessBuddy.util.DiaryEntry;
import org.joda.time.DateTime;


public class GetMealsTask extends ALoadingTask<DateTime, Void, DiaryEntry> {
    private DiaryActivity activity;

    public GetMealsTask(DiaryActivity activity, LoadingTaskExecutor executor, OnTaskFinishListener callback) {
        super((ProgressBar) activity.findViewById(R.id.mealsList_progressBar), executor, callback);
        this.activity = activity;
    }

    @Override
    protected DiaryEntry doInBackground(DateTime... params) {
        DateTime date = params[0];
        return executor.execGetMeals(date);
    }

    @Override
    protected void onPostLoad(DiaryEntry diaryEntry) {
        activity.onMealsLoaded(diaryEntry);
    }
}
