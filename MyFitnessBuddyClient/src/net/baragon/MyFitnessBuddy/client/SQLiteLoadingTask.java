package net.baragon.MyFitnessBuddy.client;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.util.LinkedList;


public class SQLiteLoadingTask extends AsyncTask<DatabaseHelper, Void, SQLiteDatabase> {
    private ProgressBar progressBar;
    private LinkedList<SQLiteOnLoadCompleteListener> listeners;

    public SQLiteLoadingTask(ProgressBar progressBar) {
        this.progressBar = progressBar;
        listeners = new LinkedList<>();
    }

    public void addListener(SQLiteOnLoadCompleteListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(SQLiteDatabase database) {
        progressBar.setVisibility(View.GONE);
        for (SQLiteOnLoadCompleteListener callback : listeners)
            callback.OnLoadComplete(database);
    }

    @Override
    protected SQLiteDatabase doInBackground(DatabaseHelper... params) {
        DatabaseHelper helper = params[0];
        return helper.getWritableDatabase();
    }
}
