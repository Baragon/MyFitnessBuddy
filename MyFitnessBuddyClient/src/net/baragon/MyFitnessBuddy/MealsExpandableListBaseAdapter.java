package net.baragon.MyFitnessBuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import net.baragon.MyFitnessBuddy.util.FoodEntry;
import net.baragon.MyFitnessBuddy.util.Macros;
import net.baragon.MyFitnessBuddy.util.Meal;

import java.util.ArrayList;


public class MealsExpandableListBaseAdapter extends BaseExpandableListAdapter {
    private static final int GROUP_TYPE_COUNT = 2;
    private static final int CHILD_TYPE_COUNT = 2;
    public static final int ID_MEAL = 0;
    public static final int ID_NEWMEAL = 1;
    public static final int ID_FOOD = 0;
    public static final int ID_NEWFOOD = 1;
    private Context context;
    private ArrayList<Meal> data;

    public MealsExpandableListBaseAdapter(Context context, ArrayList<Meal> meals) {
        data = meals;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return data.size() + 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (groupPosition == data.size()) ? 1 : data.get(groupPosition).foodEntries.size() + 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return (groupPosition < data.size()) ? groupPosition : -1;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (childPosition < data.get(groupPosition).foodEntries.size()) ? childPosition : -1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (getGroupType(groupPosition) == ID_MEAL) return getMealView(groupPosition, isExpanded, convertView, parent);
        if (getGroupType(groupPosition) == ID_NEWMEAL)
            return getNewMealView(groupPosition, isExpanded, convertView, parent);
        return null;
    }

    private View getMealView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.meals_group_view, null);
        }
        TextView mealNameTextView = (TextView) convertView.findViewById(R.id.mealNameTextView);
        mealNameTextView.setText(data.get(groupPosition).name);
        TextView calMeal = (TextView) convertView.findViewById(R.id.calMeal);
        TextView proteinMeal = (TextView) convertView.findViewById(R.id.proteinMeal);
        TextView carbsMeal = (TextView) convertView.findViewById(R.id.carbsMeal);
        TextView fatMeal = (TextView) convertView.findViewById(R.id.fatMeal);

        Macros totalMeal = data.get(groupPosition).getTotalMacros();
        calMeal.setText(String.valueOf((int) totalMeal.getCal()));
        proteinMeal.setText(String.valueOf((int) totalMeal.getProtein()));
        carbsMeal.setText(String.valueOf((int) totalMeal.getCarbs()));
        fatMeal.setText(String.valueOf((int) totalMeal.getFat()));
        return convertView;
    }

    private View getNewMealView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.new_meal_view, null);
            ((ExpandableListView)parent).setGroupIndicator(null);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (getChildType(groupPosition, childPosition) == ID_FOOD)
            return getFoodView(groupPosition, childPosition, isLastChild, convertView, parent);
        if (getChildType(groupPosition, childPosition) == ID_NEWFOOD)
            return getNewFoodView(groupPosition, childPosition, isLastChild, convertView, parent);
        return null;
    }

    public View getFoodView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.food_view, null);
        }
        TextView foodTextView = (TextView) convertView.findViewById(R.id.foodNameTextView);
        TextView servingTextView = (TextView) convertView.findViewById(R.id.servingTextView);
        FoodEntry foodEntry = data.get(groupPosition).foodEntries.get(childPosition);
        servingTextView.setText(foodEntry.servingName + " x " + String.format("%.02f", foodEntry.amount));
        foodTextView.setText(foodEntry.foodInfo.name);
        TextView calTextView = (TextView) convertView.findViewById(R.id.calTextView);
        TextView proteinTextView = (TextView) convertView.findViewById(R.id.proteinTextView);
        TextView carbsTextView = (TextView) convertView.findViewById(R.id.carbsTextView);
        TextView fatTextView = (TextView) convertView.findViewById(R.id.fatTextView);
        Macros foodEntryMacros = foodEntry.getTotalMacros();
        calTextView.setText(String.valueOf((int) foodEntryMacros.getCal()));
        proteinTextView.setText(String.valueOf((int) foodEntryMacros.getProtein()));
        carbsTextView.setText(String.valueOf((int) foodEntryMacros.getCarbs()));
        fatTextView.setText(String.valueOf((int) foodEntryMacros.getFat()));
        return convertView;
    }

    public View getNewFoodView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.new_food_view, null);
        }
        convertView.findViewById(R.id.addFoodButton).setTag(data.get(groupPosition).id);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return childPosition < data.get(groupPosition).foodEntries.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getGroupType(int groupPosition) {
        return (groupPosition < data.size()) ? ID_MEAL : ID_NEWMEAL;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return (childPosition < data.get(groupPosition).foodEntries.size()) ? ID_FOOD : ID_NEWFOOD;
    }

    @Override
    public int getGroupTypeCount() {
        return GROUP_TYPE_COUNT;
    }

    @Override
    public int getChildTypeCount() {
        return CHILD_TYPE_COUNT;
    }

    public void setData(ArrayList<Meal> newData) {
        data = newData;
    }

    public ArrayList<Meal> getData() {
        return data;
    }

}
