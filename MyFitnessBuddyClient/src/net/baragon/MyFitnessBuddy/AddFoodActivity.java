package net.baragon.MyFitnessBuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import net.baragon.MyFitnessBuddy.client.ClientLocator;
import net.baragon.MyFitnessBuddy.util.FoodEntry;
import net.baragon.MyFitnessBuddy.util.FoodInfo;

import java.util.ArrayList;


public class AddFoodActivity extends Activity implements TextView.OnEditorActionListener {
    private static final int NEW_FOOD_REQUEST = 1;
    private EditText inputSearch;
    private int meal;
    private ListView list;
    public FoodListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_activity);
        meal = getIntent().getIntExtra("meal", 0);

        inputSearch = (EditText) findViewById(R.id.add_food_input_search);
        inputSearch.setOnEditorActionListener(this);
        adapter = new FoodListAdapter(this, new ArrayList<FoodInfo>());
        list = (ListView) findViewById(R.id.recentFoodListView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HowMuchDialog dialog = new HowMuchDialog(AddFoodActivity.this, adapter.getData().get(position));
                dialog.show();
            }
        });
        ClientLocator.getClient().LoadRecentFoods(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_food_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_food_menu_new:
                Intent intent = new Intent(this, NewFoodActivity.class);
                startActivityForResult(intent, NEW_FOOD_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onDialogOK(FoodInfo foodInfo, double amount, double servingSize, String servingName, boolean newServing) {
        Intent result = new Intent();
        result.putExtra("meal", meal);
        result.putExtra("newServing", newServing);
        result.putExtra("foodEntry", new FoodEntry(-1, amount, servingSize, servingName, foodInfo));
        setResult(RESULT_OK, result);
        finish();
    }

    public void onLoadRecentFoods(ArrayList<FoodInfo> result) {
        adapter.setData(result);
        adapter.notifyDataSetChanged();
    }

    public void onLoadSearchResult(ArrayList<FoodInfo> result) {
        adapter.setData(result);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_FOOD_REQUEST) {
            if (resultCode == RESULT_OK) {
                FoodInfo food = (FoodInfo) data.getSerializableExtra("food");
                ClientLocator.getClient().NewFood(this, food);
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onSearchClick(null);
            return true;
        }
        return false;
    }

    public void onSearchClick(View v) {
        if (inputSearch.length() > 0)
            FindFood(inputSearch.getText().toString());
    }

    public void FindFood(String foodName) {
        ClientLocator.getClient().FindFood(this, foodName);
    }

    public void onNewFoodAdded(FoodInfo foodInfo) {
        adapter.getData().add(0, foodInfo);
        adapter.notifyDataSetChanged();
    }
}
