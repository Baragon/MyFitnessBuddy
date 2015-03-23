package net.baragon.MyFitnessBuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import net.baragon.MyFitnessBuddy.util.FoodInfo;


public class NewFoodActivity extends Activity implements TextWatcher, TextView.OnEditorActionListener {
    private EditText inputName;
    private EditText inputCal;
    private EditText inputProtein;
    private EditText inputCarbs;
    private EditText inputFat;
    private Button addFoodButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_food_activity);
        InitViews();
        inputFat.setOnEditorActionListener(this);
        addFoodButton = (Button) findViewById(R.id.newFood_addFood);
        inputName.addTextChangedListener(this);
        inputCal.addTextChangedListener(this);
        inputProtein.addTextChangedListener(this);
        inputCarbs.addTextChangedListener(this);
        inputFat.addTextChangedListener(this);
    }

    private void InitViews() {
        inputName = (EditText) findViewById(R.id.newFood_name);
        inputCal = (EditText) findViewById(R.id.newFood_cal);
        inputProtein = (EditText) findViewById(R.id.newFood_protein);
        inputCarbs = (EditText) findViewById(R.id.newFood_carbs);
        inputFat = (EditText) findViewById(R.id.newFood_fat);
    }

    private boolean isValidFoodDescription() {
        return (inputName.getText().length() > 0)
                && (inputCal.getText().length() > 0)
                && (inputProtein.getText().length() > 0)
                && (inputCarbs.getText().length() > 0)
                && (inputFat.getText().length() > 0);
    }

    public void onAddNewFoodFinishClick(View view) {
        Intent result = new Intent();
        String name = inputName.getText().toString();
        double cal = Double.valueOf(inputCal.getText().toString());
        double protein = Double.valueOf(inputProtein.getText().toString());
        double carbs = Double.valueOf(inputCarbs.getText().toString());
        double fat = Double.valueOf(inputFat.getText().toString());
        result.putExtra("food", new FoodInfo(name, cal, protein, carbs, fat));
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        addFoodButton.setEnabled(isValidFoodDescription());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (isValidFoodDescription()) onAddNewFoodFinishClick(null);
            return true;
        }
        return false;
    }
}
