package net.baragon.MyFitnessBuddy;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import net.baragon.MyFitnessBuddy.util.Serving;

import java.util.ArrayList;


public class ServingSpinnerAdapter implements SpinnerAdapter {

    private ArrayList<Serving> data;
    private Context context;

    public ServingSpinnerAdapter(Context context, ArrayList<Serving> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getSpinnerItemView(position, convertView);
    }

    private View getSpinnerItemView(int position, View convertView) {
        if (convertView == null) {
            convertView = new TextView(context);
        }
        String text = (position == data.size()) ? context.getString(R.string.how_much_dialog_new_serving) : data.get(position).name;
        ((TextView) convertView).setText(text);
        return convertView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return data.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return (position == data.size()) ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getSpinnerItemView(position, convertView);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public ArrayList<Serving> getData() {
        return data;
    }
}
