package net.baragon.MyFitnessBuddy.client;

import android.widget.ProgressBar;
import net.baragon.MyFitnessBuddy.DiaryActivity;
import net.baragon.MyFitnessBuddy.R;
import net.baragon.MyFitnessBuddy.util.Macros;
import org.joda.time.DateTime;


public class SetGoalsTask extends ALoadingTask<Object, Void, Void> {
    private final DiaryActivity activity;

    public SetGoalsTask(DiaryActivity activity, LoadingTaskExecutor executor, OnTaskFinishListener callback) {
        super((ProgressBar) activity.findViewById(R.id.mealsList_progressBar), executor, callback);
        this.activity = activity;
    }

    @Override
    protected void onPostLoad(Void aVoid) {
        activity.onGoalsSet();
    }

    @Override
    protected Void doInBackground(Object... params) {
        Macros goals = (Macros) params[0];
        DateTime date = (DateTime) params[1];
        executor.setGoals(goals, date);
        return null;
    }
}
