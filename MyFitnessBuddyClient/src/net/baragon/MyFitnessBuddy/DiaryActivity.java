package net.baragon.MyFitnessBuddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.*;
import net.baragon.MyFitnessBuddy.client.*;
import net.baragon.MyFitnessBuddy.util.DiaryEntry;
import net.baragon.MyFitnessBuddy.util.FoodEntry;
import net.baragon.MyFitnessBuddy.util.Macros;
import net.baragon.MyFitnessBuddy.util.Meal;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;


public class DiaryActivity extends Activity implements View.OnCreateContextMenuListener, SharedPreferences.OnSharedPreferenceChangeListener, DatePickerDialog.OnDateSetListener {
    private static final int ADD_FOOD_REQUEST = 0;
    private static final int CHANGE_GOALS_REQUEST = 1;
    private static final int DATE_DIALOG_ID = 1;
    private DateTime dateTime;
    private DatePickerDialog datePickerDialog;
    private Macros goals;
    private ExpandableListView expandableListView;
    public MealsExpandableListBaseAdapter adapter;
    private ActionMode mActionMode;
    private int selectedFoodID;
    private TextView totalCalsView;
    private TextView totalProteinView;
    private TextView totalCarbsView;
    private TextView totalFatView;
    private TextView goalCalsView;
    private TextView goalProteinView;
    private TextView goalCarbsView;
    private TextView goalFatView;
    private TextView remainingCalsView;
    private TextView remainingProteinView;
    private TextView remainingCarbsView;
    private TextView remainingFatView;
    private TextView dateTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LoadPreferences();
        InitViews();
        InitMeals();

        dateTime = new DateTime();
        onDateChanged();

        ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.food_entry_menu, menu);
                return true;

            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.food_entry_menu_delete:
                        ClientLocator.getClient().DeleteFood(DiaryActivity.this, selectedFoodID);
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mActionMode = null;
            }
        };
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);
                    if (adapter.getChildType(groupPosition, childPosition) == adapter.ID_FOOD) {
                        if (mActionMode != null) {
                            return false;
                        }
                        selectedFoodID = adapter.getData().get(groupPosition).foodEntries.get(childPosition).id;
                        mActionMode = DiaryActivity.this.startActionMode(mActionModeCallback);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void InitViews() {
        dateTextView = (TextView) findViewById(R.id.main_header_date_textview);
        totalCalsView = (TextView) findViewById(R.id.main_totalCal);
        totalProteinView = (TextView) findViewById(R.id.main_totalProtein);
        totalCarbsView = (TextView) findViewById(R.id.main_totalCarbs);
        totalFatView = (TextView) findViewById(R.id.main_totalFat);
        goalCalsView = (TextView) findViewById(R.id.main_goalCal);
        goalProteinView = (TextView) findViewById(R.id.main_goalProtein);
        goalCarbsView = (TextView) findViewById(R.id.main_goalCarbs);
        goalFatView = (TextView) findViewById(R.id.main_goalFat);
        remainingCalsView = (TextView) findViewById(R.id.main_remainCal);
        remainingProteinView = (TextView) findViewById(R.id.main_remainProtein);
        remainingCarbsView = (TextView) findViewById(R.id.main_remainCarbs);
        remainingFatView = (TextView) findViewById(R.id.main_remainFat);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
    }

    private void LoadPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
        setClient(preferences.getBoolean("pref_online", false), preferences.getString("server_address", null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void InitMeals() {
        adapter = new MealsExpandableListBaseAdapter(this, new ArrayList<Meal>());
        expandableListView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_options:
                Intent intent = new Intent(this, MFBPreference.class);
                startActivity(intent);
                return true;
            case R.id.main_menu_refresh:
                Refresh();
                return true;
            case R.id.main_menu_change_goals:
                onChangeGoalsClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onChangeGoalsClick() {
        Intent intent = new Intent(this, GoalsActivity.class);
        startActivityForResult(intent, CHANGE_GOALS_REQUEST);
    }


    public void onNewMealClick(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.new_meal_dialog_title));
        alert.setMessage(getString(R.string.new_meal_dialog_message));
        EditText edit = new EditText(this);
        alert.setView(edit);

        alert.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClientLocator.getClient().NewMeal(DiaryActivity.this, edit.getText().toString(), dateTime);
            }
        });
        alert.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    public void onMealsLoaded(DiaryEntry result) {
        adapter.setData(result.meals);
        adapter.notifyDataSetChanged();
        goals = result.goals;
        onDataUpdated();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_FOOD_REQUEST) {
            if (resultCode == RESULT_OK) {
                int meal = data.getIntExtra("meal", -1);
                if (meal != -1) {
                    FoodEntry foodEntry = (FoodEntry) data.getSerializableExtra("foodEntry");
                    ClientLocator.getClient().LogFood(this, foodEntry, meal, data.getBooleanExtra("newServing", false));
                }
            }
        }
        if (requestCode == CHANGE_GOALS_REQUEST) {
            if (resultCode == RESULT_OK) {
                goals = (Macros) data.getSerializableExtra("goals");
                ClientLocator.getClient().SetGoals(this, goals, dateTime);
            }
        }
    }

    private Meal findMealByID(ArrayList<Meal> meals, int id) {
        for (Meal meal : meals)
            if (meal.id == id) return meal;
        return null;
    }

    public void onAddFoodClick(View view) {
        int meal = (Integer) view.getTag();
        Intent addFoodIntent = new Intent(this, AddFoodActivity.class);
        addFoodIntent.putExtra("meal", meal);
        startActivityForResult(addFoodIntent, ADD_FOOD_REQUEST);
    }

    public void addNewMeal(Integer mealID, String name) {
        adapter.getData().add(new Meal(mealID, name));
        adapter.notifyDataSetChanged();
    }

    public void onDataUpdated() {
        adapter.notifyDataSetChanged();
        ArrayList<Meal> meals = adapter.getData();
        Macros totalMacros = new Macros();
        for (Meal m : meals) {
            totalMacros = totalMacros.plus(m.getTotalMacros());
        }
        totalCalsView.setText(String.valueOf((int) totalMacros.getCal()));
        totalProteinView.setText(String.valueOf((int) totalMacros.getProtein()));
        totalCarbsView.setText(String.valueOf((int) totalMacros.getCarbs()));
        totalFatView.setText(String.valueOf((int) totalMacros.getFat()));
        Macros remaining = goals.minus(totalMacros);
        goalCalsView.setText(String.valueOf((int) goals.getCal()));
        goalProteinView.setText(String.valueOf((int) goals.getProtein()));
        goalCarbsView.setText(String.valueOf((int) goals.getCarbs()));
        goalFatView.setText(String.valueOf((int) goals.getFat()));
        remainingCalsView.setText(String.valueOf((int) remaining.getCal()));
        remainingCalsView.setTextColor(getRemainingMacroColor(remaining.getCal(), goals.getCal()));
        remainingProteinView.setText(String.valueOf((int) remaining.getProtein()));
        remainingProteinView.setTextColor(getRemainingMacroColor(remaining.getProtein(), goals.getProtein()));
        remainingCarbsView.setText(String.valueOf((int) remaining.getCarbs()));
        remainingCarbsView.setTextColor(getRemainingMacroColor(remaining.getCarbs(), goals.getCarbs()));
        remainingFatView.setText(String.valueOf((int) remaining.getFat()));
        remainingFatView.setTextColor(getRemainingMacroColor(remaining.getFat(), goals.getFat()));
    }

    private int getRemainingMacroColor(double remaining, double goal) {
        if (remaining < 0) return getResources().getColor(R.color.remaining_bad);
        if (remaining / goal < 0.1) return getResources().getColor(R.color.remaining_okay);
        return getResources().getColor(R.color.remaining_good);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ((key.equalsIgnoreCase("pref_server")) || (key.equalsIgnoreCase("pref_online"))) {
            setClient(sharedPreferences.getBoolean("pref_online", false), sharedPreferences.getString("pref_server", null));
        }

    }

    public void setClient(boolean online, String ip) {
        LoadingTaskExecutor executor;
        if ((online) && (ip != null))
            executor = new WebSearchExecutor(this, ip);
        else
            executor = new LocalClientExecutor(this);
        Client client = new Client(executor);
        client.Init();
        ClientLocator.setClient(client);
    }

    public void onClientLoadComplete() {
        Refresh();
    }

    public void onLeftArrowClick(View w) {
        dateTime = dateTime.minusDays(1);
        onDateChanged();
        Refresh();
    }

    public void onRightArrowClick(View w) {
        dateTime = dateTime.plusDays(1);
        onDateChanged();
        Refresh();
    }

    public void onDateChanged() {
        DateTime today = new DateTime();
        DateTime yesterday = today.minusDays(1);
        if ((dateTime.year().equals(today.year())) &&
                (dateTime.dayOfYear().equals(today.dayOfYear()))) {
            dateTextView.setText(R.string.today);
        } else if ((dateTime.year().equals(yesterday.year())) &&
                (dateTime.dayOfYear().equals(yesterday.dayOfYear()))) {
            dateTextView.setText(R.string.yesterday);
        } else {
            DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd MMM");
            dateTextView.setText(dateFormat.print(dateTime));
        }

    }

    public void Refresh() {
        ClientLocator.getClient().LoadMeals(this, dateTime);
    }

    public void onDateClick(View v) {
        showDialog(DATE_DIALOG_ID);
        datePickerDialog.getDatePicker().init(dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth(), null);
    }

    public Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG_ID) {
            datePickerDialog = new DatePickerDialog(this, this, dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth());
            return datePickerDialog;
        }
        return super.onCreateDialog(id);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        dateTime = new DateTime(year, monthOfYear + 1, dayOfMonth, dateTime.getHourOfDay(), dateTime.getMinuteOfHour());
        onDateChanged();
        Refresh();
    }

    public void onGoalsSet() {
        onDataUpdated();
    }

    public void onDeleteEntry(int id) {
        Meal mealToDeleteFrom = null;
        int foodEntryToDeleteIndex = -1;
        search:
        for (Meal m : adapter.getData()) {
            for (int i = 0; i < m.foodEntries.size(); i++)
                if (id == m.foodEntries.get(i).id) {
                    mealToDeleteFrom = m;
                    foodEntryToDeleteIndex = i;
                    break search;
                }
        }
        if ((mealToDeleteFrom == null) || (foodEntryToDeleteIndex == -1)) {
            Log.e(getString(R.string.app_name), "Cannot delete food entry with ID=" + id + "; No such entry found");
            return;
        }
        mealToDeleteFrom.foodEntries.remove(foodEntryToDeleteIndex);
        adapter.notifyDataSetChanged();
        onDataUpdated();
    }

    public void onEntryLogged(int meal, FoodEntry foodEntry) {
        findMealByID(adapter.getData(), meal).foodEntries.add(foodEntry);
        onDataUpdated();
    }
}
