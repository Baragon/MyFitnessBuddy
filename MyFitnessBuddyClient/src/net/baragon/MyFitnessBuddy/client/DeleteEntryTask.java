package net.baragon.MyFitnessBuddy.client;

import android.widget.ProgressBar;
import net.baragon.MyFitnessBuddy.DiaryActivity;
import net.baragon.MyFitnessBuddy.R;


public class DeleteEntryTask extends ALoadingTask<Integer, Void, Void> {
    private DiaryActivity activity;
    private int id;

    public DeleteEntryTask(DiaryActivity activity, LoadingTaskExecutor executor, OnTaskFinishListener callback) {
        super((ProgressBar) activity.findViewById(R.id.mealsList_progressBar), executor, callback);
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        id = params[0];
        executor.execDeleteEntry(id);
        return null;
    }

    @Override
    protected void onPostLoad(Void result) {
        activity.onDeleteEntry(id);
    }
}
