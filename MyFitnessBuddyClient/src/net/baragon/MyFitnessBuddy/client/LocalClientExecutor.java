package net.baragon.MyFitnessBuddy.client;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ProgressBar;
import net.baragon.MyFitnessBuddy.DiaryActivity;
import net.baragon.MyFitnessBuddy.R;
import net.baragon.MyFitnessBuddy.util.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class LocalClientExecutor implements SQLiteOnLoadCompleteListener, LoadingTaskExecutor {
    private static final double FDA_RECOMMENDED_CALORIES = 2000;
    private static final double FDA_RECOMMENDED_PROTEIN = 50;
    private static final double FDA_RECOMMENDED_CARBS = 300;
    private static final double FDA_RECOMMENDED_FAT = 65;
    private DatabaseHelper dbHelper;
    protected SQLiteDatabase db;
    private DiaryActivity activity;
    private DateTimeFormatter dateFormatter;

    public LocalClientExecutor(DiaryActivity activity) {
        this.activity = activity;
        dbHelper = new DatabaseHelper(activity);
        dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    }

    public void Init() {
        SQLiteLoadingTask task = new SQLiteLoadingTask((ProgressBar) activity.findViewById(R.id.mealsList_progressBar));
        task.addListener(this);
        task.execute(this.dbHelper);
    }

    private FoodInfo ReadInfo(Cursor cursor) {
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        double cals = cursor.getFloat(2);
        double protein = cursor.getFloat(3);
        double carbs = cursor.getFloat(4);
        double fat = cursor.getFloat(5);
        return new FoodInfo(id, name, cals, protein, carbs, fat);
    }

    @Override
    public void OnLoadComplete(SQLiteDatabase database) {
        db = database;
        activity.onClientLoadComplete();
    }

    @Override
    public Integer execLogFood(FoodEntry foodEntry, int meal, boolean newServing) {
        int servingID = -1;
        if (newServing) {
            ContentValues values = new ContentValues();
            values.put("name", foodEntry.servingName);
            values.put("food", foodEntry.foodInfo.id);
            values.put("grams", foodEntry.servingSize);
            servingID = (int) db.insert("serving", null, values);
            foodEntry.foodInfo.servings.add(new Serving(servingID, foodEntry.servingName, foodEntry.servingSize));
        }
        ContentValues intakeValues = new ContentValues();
        if (servingID == -1) {
            servingID = foodEntry.foodInfo.getServingByName(foodEntry.servingName).id;
        }
        intakeValues.put("amount", foodEntry.amount);
        intakeValues.put("serving", servingID);
        intakeValues.put("meal", meal);
        foodEntry.id = (int) db.insert("intake", null, intakeValues);
        return foodEntry.id;
    }

    @Override
    public DiaryEntry execGetMeals(DateTime date) {
        ArrayList<Meal> meals = new ArrayList<Meal>();
        String query = "SELECT id,name FROM meals WHERE date=?";
        Cursor mealsCursor = db.rawQuery(query, new String[]{dateFormatter.print(date)});
        mealsCursor.moveToFirst();
        while (!mealsCursor.isAfterLast()) {
            int mealID = mealsCursor.getInt(mealsCursor.getColumnIndex("id"));
            String name = mealsCursor.getString(mealsCursor.getColumnIndex("name"));
            String foodInMealQuerry = "SELECT amount,name,food,grams,a.id as entryid FROM intake a JOIN serving b ON a.serving=b.id WHERE meal=?";
            Cursor foodsCursor = db.rawQuery(foodInMealQuerry, new String[]{String.valueOf(mealID)});
            foodsCursor.moveToFirst();
            Meal meal = new Meal(mealID, name);
            while (!foodsCursor.isAfterLast()) {
                float amount = foodsCursor.getFloat(foodsCursor.getColumnIndex("amount"));
                float grams = foodsCursor.getFloat(foodsCursor.getColumnIndex("grams"));
                String servingName = foodsCursor.getString(foodsCursor.getColumnIndex("name"));
                int foodID = foodsCursor.getInt(foodsCursor.getColumnIndex("food"));
                int entryid = foodsCursor.getInt(foodsCursor.getColumnIndex("entryid"));
                String foodInfoQuerry = "SELECT id,name,cal,protein,carbs,fat FROM foods WHERE id=?";
                Cursor foodInfoCursor = db.rawQuery(foodInfoQuerry, new String[]{String.valueOf(foodID)});
                foodInfoCursor.moveToFirst();
                FoodInfo foodInfo = ReadInfo(foodInfoCursor);
                meal.foodEntries.add(new FoodEntry(entryid, amount, grams, servingName, foodInfo));
                foodsCursor.moveToNext();
            }
            meals.add(meal);
            mealsCursor.moveToNext();
        }
        return new DiaryEntry(meals, getGoals(date));
    }

    @Override
    public void execDeleteEntry(int id) {
        db.delete("intake", "id=" + id, null);
    }

    private ArrayList<Serving> getServings(int foodID) {
        ArrayList<Serving> servings = new ArrayList<>();
        Cursor servingsCursor = db.query("serving", new String[]{"id", "name", "grams"}, "food=" + foodID, null, null, null, null);
        servingsCursor.moveToFirst();
        while (!servingsCursor.isAfterLast()) {
            int servingID = servingsCursor.getInt(0);
            String servingName = servingsCursor.getString(1);
            double grams = servingsCursor.getFloat(2);
            Serving serving = new Serving(servingID, servingName, grams);
            servings.add(serving);
            servingsCursor.moveToNext();
        }
        return servings;
    }

    @Override
    public ArrayList<FoodInfo> execFindFood(String name) {
        ArrayList<FoodInfo> foodInfos = new ArrayList<>();
        Cursor foodsCursor = db.query("foods", new String[]{"id", "name", "cal", "protein", "carbs", "fat"}, "name LIKE ?", new String[]{name + "%"}, null, null, null, "10");
        foodsCursor.moveToFirst();
        while (!foodsCursor.isAfterLast()) {
            int foodID = foodsCursor.getInt(0);
            String foodName = foodsCursor.getString(1);
            double cal = foodsCursor.getFloat(2);
            double protein = foodsCursor.getFloat(3);
            double carbs = foodsCursor.getFloat(4);
            double fat = foodsCursor.getFloat(5);
            FoodInfo foodInfo = new FoodInfo(foodID, foodName, cal, protein, carbs, fat);
            foodInfo.servings = getServings(foodID);
            foodInfos.add(foodInfo);
            foodsCursor.moveToNext();
        }
        return foodInfos;
    }

    @Override
    public FoodInfo execNewFood(FoodInfo foodInfo, String name, double cal, double protein, double carbs, double fat) {
        ContentValues foodValues = new ContentValues();
        foodValues.put("name", name);
        foodValues.put("cal", cal);
        foodValues.put("protein", protein);
        foodValues.put("carbs", carbs);
        foodValues.put("fat", fat);
        int foodid = (int) db.insert("foods", null, foodValues);
        ContentValues servingValues = new ContentValues();
        servingValues.put("name", "100g");
        servingValues.put("food", foodid);
        servingValues.put("grams", 100f);
        int servingID = (int) db.insert("serving", null, servingValues);
        foodInfo.servings.add(new Serving(servingID, "100g", 100));
        foodInfo.id = foodid;
        return foodInfo;
    }

    @Override
    public Integer execNewMeal(String name, String date) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("date", date);
        return (int) db.insert("meals", null, values);
    }

    @Override
    public ArrayList<FoodInfo> execRecentFood() {
        ArrayList<FoodInfo> recentFoods = new ArrayList<>();
        String query = "SELECT DISTINCT id,name,cal,protein,carbs,fat FROM foods t JOIN (\n" +
                "SELECT food FROM serving x JOIN (\n" +
                "SELECT serving,a.date FROM meals a LEFT JOIN intake b on a.id=b.meal where (b.meal is not null)  ORDER BY date DESC\n" +
                ") y ON x.id=y.serving\n" +
                ") g on g.food=t.id LIMIT ?";
        Cursor recentCursor = db.rawQuery(query, new String[]{"10"});
        recentCursor.moveToFirst();
        while (!recentCursor.isAfterLast()) {
            int foodID = recentCursor.getInt(0);
            String foodName = recentCursor.getString(1);
            double cal = recentCursor.getFloat(2);
            double protein = recentCursor.getFloat(3);
            double carbs = recentCursor.getFloat(4);
            double fat = recentCursor.getFloat(5);
            FoodInfo foodInfo = new FoodInfo(foodID, foodName, cal, protein, carbs, fat);
            foodInfo.servings = getServings(foodID);
            recentFoods.add(foodInfo);
            recentCursor.moveToNext();
        }
        return recentFoods;
    }


    private Macros getGoals(DateTime date) {
        String dateString = dateFormatter.print(date);
        Cursor goalsCursor = db.query("goals", new String[]{"cal", "protein", "carbs", "fat"}, "date <= ?", new String[]{dateString}, null, null, "date ASC", "1");
        if (goalsCursor.moveToFirst()) {
            double cal = goalsCursor.getFloat(0);
            double protein = goalsCursor.getFloat(1);
            double carbs = goalsCursor.getFloat(2);
            double fat = goalsCursor.getFloat(3);
            return new Macros(cal, protein, carbs, fat);
        }
        return new Macros(FDA_RECOMMENDED_CALORIES, FDA_RECOMMENDED_PROTEIN, FDA_RECOMMENDED_CARBS, FDA_RECOMMENDED_FAT);
    }

    @Override
    public void setGoals(Macros macros, DateTime date) {
        String dateString = dateFormatter.print(date);
        db.delete("goals", "date=?", new String[]{dateString});

        ContentValues goalsValues = new ContentValues();
        goalsValues.put("cal", macros.getCal());
        goalsValues.put("protein", macros.getProtein());
        goalsValues.put("carbs", macros.getCarbs());
        goalsValues.put("fat", macros.getFat());
        goalsValues.put("date", dateString);

        db.insert("goals", null, goalsValues);
    }

    @Override
    public void DeleteLocalData() {
        dbHelper.onUpgrade(db, DatabaseHelper.DATABASE_VERSION, DatabaseHelper.DATABASE_VERSION);
    }
}
