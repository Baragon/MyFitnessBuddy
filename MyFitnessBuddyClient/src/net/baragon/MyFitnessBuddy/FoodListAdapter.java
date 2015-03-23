package net.baragon.MyFitnessBuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import net.baragon.MyFitnessBuddy.util.FoodInfo;

import java.util.ArrayList;


public class FoodListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FoodInfo> data;

    public FoodListAdapter(Context context, ArrayList<FoodInfo> list) {
        this.context = context;
        data = list;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.food_list_item, null);
        }
        FoodInfo foodInfo = data.get(position);
        TextView foodNameView = (TextView) convertView.findViewById(R.id.foodListItem_foodName);
        foodNameView.setText(foodInfo.name);
        TextView foodCal = (TextView) convertView.findViewById(R.id.foodListItem_cal);
        TextView foodProtein = (TextView) convertView.findViewById(R.id.foodListItem_protein);
        TextView foodCarbs = (TextView) convertView.findViewById(R.id.foodListItem_carbs);
        TextView foodFat = (TextView) convertView.findViewById(R.id.foodListItem_fat);
        foodCal.setText(String.valueOf((int) foodInfo.cals));
        foodProtein.setText(String.valueOf((int) foodInfo.protein));
        foodCarbs.setText(String.valueOf((int) foodInfo.carbs));
        foodFat.setText(String.valueOf((int) foodInfo.fats));

        return convertView;
    }

    public void setData(ArrayList<FoodInfo> newList) {
        data = newList;
    }

    public ArrayList<FoodInfo> getData() {
        return data;
    }
}
