package net.baragon.MyFitnessBuddy.client;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;


public abstract class ALoadingTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    protected ProgressBar progressBar;
    protected LoadingTaskExecutor executor;
    protected OnTaskFinishListener onFinishCallback;

    public ALoadingTask(ProgressBar progressBar, LoadingTaskExecutor executor, OnTaskFinishListener callback) {
        super();
        this.progressBar = progressBar;
        this.executor = executor;
        onFinishCallback = callback;
    }

    @Override
    public void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Result result) {
        progressBar.setVisibility(View.GONE);
        onPostLoad(result);
        onFinishCallback.onTaskFinish(this);
    }

    abstract protected void onPostLoad(Result result);
}
