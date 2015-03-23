package net.baragon.MyFitnessBuddy.client;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myfitnessbuddy.db";
    public static final int DATABASE_VERSION = 14;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        execSQLFromAsset(db, "myfitnessbuddy.sql");
    }

    public void execSQLFromAsset(SQLiteDatabase db, String asset) {
        try {
            InputStream is = context.getAssets().open(asset);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line);
            }
            String[] statements = sql.toString().split(";");
            for (String statement : statements)
                db.execSQL(statement);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS meals");
        db.execSQL("DROP TABLE IF EXISTS foods");
        db.execSQL("DROP TABLE IF EXISTS foods1");
        db.execSQL("DROP TABLE IF EXISTS intake");
        db.execSQL("DROP TABLE IF EXISTS serving");
        db.execSQL("DROP TABLE IF EXISTS goals");
        onCreate(db);
    }
}
