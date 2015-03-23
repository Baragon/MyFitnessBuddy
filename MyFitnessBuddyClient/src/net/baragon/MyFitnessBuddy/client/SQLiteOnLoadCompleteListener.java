package net.baragon.MyFitnessBuddy.client;

import android.database.sqlite.SQLiteDatabase;


public interface SQLiteOnLoadCompleteListener {
    public void OnLoadComplete(SQLiteDatabase database);
}
