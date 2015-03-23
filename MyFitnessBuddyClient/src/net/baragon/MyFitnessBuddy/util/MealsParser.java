package net.baragon.MyFitnessBuddy.util;

import android.util.JsonReader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;


public class MealsParser {
    public static ArrayList<Meal> parse(String json) throws IOException {
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        return ReadMeals(jsonReader);
    }

    public static ArrayList<Meal> parse(Reader data) throws IOException {
        JsonReader jsonReader = new JsonReader(data);
        return ReadMeals(jsonReader);
    }

    private static ArrayList<Meal> ReadMeals(JsonReader jsonReader) throws IOException {
        ArrayList<Meal> meals = new ArrayList<Meal>();
        jsonReader.beginObject();
        if (!jsonReader.nextName().equalsIgnoreCase("meals")) throw new IOException("Wrong json format");
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            meals.add(ReadMeal(jsonReader));
        }
        jsonReader.endArray();
        jsonReader.endObject();
        jsonReader.close();
        return meals;
    }

    private static Meal ReadMeal(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        Meal meal;
        int id = -1;
        String name = null;
        ArrayList<FoodEntry> foodEntries = null;
        while (jsonReader.hasNext()) {
            String dataName = jsonReader.nextName();
            if (dataName.equalsIgnoreCase("mealid")) id = jsonReader.nextInt();
            else if (dataName.equalsIgnoreCase("name")) name = jsonReader.nextString();
            else if (dataName.equalsIgnoreCase("foods")) foodEntries = ReadFoodEntries(jsonReader);
        }
        jsonReader.endObject();
        if ((id == -1) || (name == null) || (foodEntries == null)) throw new IOException("Not enough properties");
        meal = new Meal(id, name, foodEntries);
        return meal;
    }

    public static ArrayList<FoodEntry> ReadFoodEntries(JsonReader jsonReader) throws IOException {
        ArrayList<FoodEntry> foodEntries = new ArrayList<FoodEntry>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            int id = -1;
            double amount = -1;
            double servingSize = -1;
            String servingName = null;
            FoodInfo info = null;
            while (jsonReader.hasNext()) {
                String dataName = jsonReader.nextName();
                if (dataName.equalsIgnoreCase("amount")) amount = jsonReader.nextDouble();
                else if (dataName.equalsIgnoreCase("servingsize")) servingSize = jsonReader.nextDouble();
                else if (dataName.equalsIgnoreCase("name")) servingName = jsonReader.nextString();
                else if (dataName.equalsIgnoreCase("id")) id = jsonReader.nextInt();
                else if (dataName.equalsIgnoreCase("info")) info = ReadFoodEntryInfo(jsonReader);
            }
            jsonReader.endObject();
            if ((amount == -1) || (servingSize == -1) || (servingName == null) || (info == null) || (id == -1))
                throw new IOException("Not enough properties");
            foodEntries.add(new FoodEntry(id, (float) amount, (float) servingSize, servingName, info));
        }
        jsonReader.endArray();
        return foodEntries;
    }

    public static FoodInfo ReadInfo(JsonReader jsonReader) throws IOException {
        String name = null;
        int id = -1;
        double protein, cal, carbs, fat;
        protein = -1;
        cal = -1;
        carbs = -1;
        fat = -1;
        ArrayList<Serving> servings = null;
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String dataName = jsonReader.nextName();
            if (dataName.equalsIgnoreCase("name")) name = jsonReader.nextString();
            else if (dataName.equalsIgnoreCase("protein")) protein = jsonReader.nextDouble();
            else if (dataName.equalsIgnoreCase("cal")) cal = jsonReader.nextDouble();
            else if (dataName.equalsIgnoreCase("carbs")) carbs = jsonReader.nextDouble();
            else if (dataName.equalsIgnoreCase("fat")) fat = jsonReader.nextDouble();
            else if (dataName.equalsIgnoreCase("id")) id = jsonReader.nextInt();
            else if (dataName.equalsIgnoreCase("servings")) servings = ReadServings(jsonReader);
        }
        jsonReader.endObject();
        if ((name == null) || (protein == -1) || (cal == -1) || (carbs == -1) || (fat == -1) || (id == -1) || (servings == null))
            throw new IOException("Not enough properties");
        return new FoodInfo(id, name, cal, protein, carbs, fat, servings);
    }

    public static ArrayList<Serving> ReadServings(JsonReader jsonReader) throws IOException {
        ArrayList<Serving> servings = new ArrayList<Serving>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            String name = null;
            double grams = -1;
            int id = -1;
            while (jsonReader.hasNext()) {
                String dataName = jsonReader.nextName();
                if (dataName.equalsIgnoreCase("name")) name = jsonReader.nextString();
                else if (dataName.equalsIgnoreCase("gramms")) grams = jsonReader.nextDouble();
                else if (dataName.equalsIgnoreCase("id")) id = jsonReader.nextInt();
            }
            jsonReader.endObject();
            if ((name == null) || (grams == -1) || (id == -1)) throw new IOException("Not enough properties");
            servings.add(new Serving(id, name, grams));
        }
        jsonReader.endArray();
        return servings;
    }


    public static FoodInfo ReadFoodEntryInfo(JsonReader jsonReader) throws IOException {
        String name = null;
        int id = -1;
        double protein, cal, carbs, fat;
        protein = -1;
        cal = -1;
        carbs = -1;
        fat = -1;
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String dataName = jsonReader.nextName();
            if (dataName.equalsIgnoreCase("name")) name = jsonReader.nextString();
            else if (dataName.equalsIgnoreCase("protein")) protein = jsonReader.nextDouble();
            else if (dataName.equalsIgnoreCase("cal")) cal = jsonReader.nextDouble();
            else if (dataName.equalsIgnoreCase("carbs")) carbs = jsonReader.nextDouble();
            else if (dataName.equalsIgnoreCase("fat")) fat = jsonReader.nextDouble();
            else if (dataName.equalsIgnoreCase("id")) id = jsonReader.nextInt();
        }
        jsonReader.endObject();
        if ((name == null) || (protein == -1) || (cal == -1) || (carbs == -1) || (fat == -1) || (id == -1))
            throw new IOException("Not enough properties");
        return new FoodInfo(id, name, cal, protein, carbs, fat);
    }
}
