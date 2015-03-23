package net.baragon.MyFitnessBuddy.client;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.JsonReader;
import net.baragon.MyFitnessBuddy.DiaryActivity;
import net.baragon.MyFitnessBuddy.util.FoodEntry;
import net.baragon.MyFitnessBuddy.util.FoodInfo;
import net.baragon.MyFitnessBuddy.util.MealsParser;
import net.baragon.MyFitnessBuddy.util.Serving;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class WebSearchExecutor extends LocalClientExecutor {
    private String host;
    private HttpClient client;

    public WebSearchExecutor(DiaryActivity activity, String host) {
        super(activity);
        this.host = host;
    }

    @Override
    public void Init() {
        super.Init();
        client = new DefaultHttpClient();
    }

    @Override
    public FoodInfo execNewFood(FoodInfo foodInfo, String name, double cal, double protein, double carbs, double fat) {
        HttpGet request = new HttpGet();
        try {
            request.setURI(new URI("http://" + host + "/addfood?name=" + name.replaceAll(" ", "%20") + "&cal=" + cal + "&protein=" + protein + "&carbs=" + carbs + "&fat=" + fat));
            HttpResponse response = client.execute(request);
            String responseString = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).readLine();
            String[] foodIDAndDefaultServingID = responseString.split(":");
            int foodID = Integer.valueOf(foodIDAndDefaultServingID[0]);
            int defaultServingID = Integer.valueOf(foodIDAndDefaultServingID[1]);
            foodInfo.id = foodID;
            foodInfo.servings.add(new Serving(defaultServingID, "100g", 100));

            ContentValues foodValues = new ContentValues();
            foodValues.put("id", foodID);
            foodValues.put("name", name);
            foodValues.put("cal", cal);
            foodValues.put("protein", protein);
            foodValues.put("carbs", carbs);
            foodValues.put("fat", fat);
            db.insert("foods", null, foodValues);
            ContentValues servingValues = new ContentValues();
            servingValues.put("id", defaultServingID);
            servingValues.put("name", "100g");
            servingValues.put("food", foodID);
            servingValues.put("grams", 100f);
            db.insert("serving", null, servingValues);
            return foodInfo;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<FoodInfo> execFindFood(String name) {
        ArrayList<FoodInfo> searchResult = new ArrayList<FoodInfo>();
        HttpGet request = new HttpGet();
        try {
            request.setURI(new URI("http://" + host + "/find?name=" + name));
            HttpResponse response = client.execute(request);
            JsonReader jreader = new JsonReader(new InputStreamReader(response.getEntity().getContent()));
            jreader.beginObject();
            jreader.nextName();
            jreader.beginArray();
            while (jreader.hasNext()) {
                searchResult.add(MealsParser.ReadInfo(jreader));
            }
            jreader.endArray();
            jreader.endObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    private void CacheFood(FoodInfo info) {
        Cursor foodCursor = db.query("foods", new String[]{"id"}, "id=?", new String[]{String.valueOf(info.id)}, null, null, null);
        if (foodCursor.getCount() == 0) {
            ContentValues foodValues = new ContentValues();
            foodValues.put("id", info.id);
            foodValues.put("name", info.name);
            foodValues.put("cal", info.cals);
            foodValues.put("protein", info.protein);
            foodValues.put("carbs", info.carbs);
            foodValues.put("fat", info.fats);
            db.insert("foods", null, foodValues);
        }
    }

    @Override
    public Integer execLogFood(FoodEntry foodEntry, int meal, boolean newServing) {
        CacheFood(foodEntry.foodInfo);
        int servingID = -1;
        if (newServing) {
            try {
                HttpGet request = new HttpGet();
                request.setURI(new URI("http://" + host + "/addserving?foodid=" + foodEntry.foodInfo.id + "&name=" + foodEntry.servingName.replaceAll(" ", "%20") + "&g=" + foodEntry.servingSize));
                HttpResponse response = client.execute(request);
                String responseString = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).readLine();
                servingID = Integer.valueOf(responseString);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            ContentValues values = new ContentValues();
            values.put("id", servingID);
            values.put("name", foodEntry.servingName);
            values.put("food", foodEntry.foodInfo.id);
            values.put("grams", foodEntry.servingSize);
            servingID = (int) db.insert("serving", null, values);
            foodEntry.foodInfo.servings.add(new Serving(servingID, foodEntry.servingName, foodEntry.servingSize));
        }
        ContentValues intakeValues = new ContentValues();
        Serving serving;
        if (servingID == -1) {
            serving = foodEntry.foodInfo.getServingByName(foodEntry.servingName);
            servingID = serving.id;
        }
        CacheServing(servingID, foodEntry.servingName, foodEntry.foodInfo.id, foodEntry.servingSize);
        intakeValues.put("amount", foodEntry.amount);
        intakeValues.put("serving", servingID);
        intakeValues.put("meal", meal);
        foodEntry.id = (int) db.insert("intake", null, intakeValues);
        return foodEntry.id;
    }

    private void CacheServing(int servingID, String servingName, int foodID, double grams) {
        Cursor servingCursor = db.query("serving", new String[]{"id"}, "id=?", new String[]{String.valueOf(servingID)}, null, null, null);
        if (servingCursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put("id", servingID);
            values.put("name", servingName);
            values.put("food", foodID);
            values.put("grams", grams);
            db.insert("serving", null, values);
        }
    }
}
