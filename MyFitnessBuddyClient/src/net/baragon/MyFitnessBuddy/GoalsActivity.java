package net.baragon.MyFitnessBuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import net.baragon.MyFitnessBuddy.util.Macros;


public class GoalsActivity extends Activity implements TextWatcher, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
    private static final double KG_TO_LBS = 2.20462262;
    private static final double PROTEIN_PER_LBS = 0.8;
    private static final double FAT_PER_LBS = 0.4;
    private EditText inputHeight;
    private EditText inputWeight;
    private EditText inputCal;
    private EditText inputProtein;
    private EditText inputCarbs;
    private EditText inputFat;
    private EditText inputAge;
    private Button continueButton;
    private Spinner exerciseSpinner;
    private Spinner goalsSpinner;
    private Macros goals;
    private CheckBox checkBox;
    private Switch sexSwitch;
    private boolean isFemale;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goals_activity);
        goals = new Macros();
        InitViews();
    }

    private void InitViews() {
        inputHeight = (EditText) findViewById(R.id.goals_height);
        inputWeight = (EditText) findViewById(R.id.goals_weight);
        inputCal = (EditText) findViewById(R.id.goals_cal);
        inputProtein = (EditText) findViewById(R.id.goals_protein);
        inputCarbs = (EditText) findViewById(R.id.goals_carbs);
        inputFat = (EditText) findViewById(R.id.goals_fat);
        inputAge = (EditText) findViewById(R.id.goals_age);
        inputHeight.addTextChangedListener(this);
        inputWeight.addTextChangedListener(this);
        inputCal.addTextChangedListener(this);
        inputProtein.addTextChangedListener(this);
        inputCarbs.addTextChangedListener(this);
        inputFat.addTextChangedListener(this);
        inputAge.addTextChangedListener(this);
        continueButton = (Button) findViewById(R.id.goals_continue_button);
        continueButton.setEnabled(false);
        checkBox = (CheckBox) findViewById(R.id.goals_custom_macros_checkbox);
        sexSwitch = (Switch) findViewById(R.id.goals_sex_switch);
        sexSwitch.setOnCheckedChangeListener(this);
        exerciseSpinner = (Spinner) findViewById(R.id.goals_exercise);
        goalsSpinner = (Spinner) findViewById(R.id.goals_goal);
        ArrayAdapter exerciseAdapter = ArrayAdapter.createFromResource(this, R.array.exercise_level_array, android.R.layout.simple_spinner_item);
        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(exerciseAdapter);
        ArrayAdapter goalsAdapter = ArrayAdapter.createFromResource(this, R.array.goals_array, android.R.layout.simple_spinner_item);
        goalsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsSpinner.setAdapter(goalsAdapter);
        exerciseSpinner.setSelection(2);
        goalsSpinner.setSelection(3);
        exerciseSpinner.setOnItemSelectedListener(this);
        goalsSpinner.setOnItemSelectedListener(this);
        inputCal.setEnabled(checkBox.isChecked());
        inputProtein.setEnabled(checkBox.isChecked());
        inputCarbs.setEnabled(checkBox.isChecked());
        inputFat.setEnabled(checkBox.isChecked());
    }

    public void onContinueClick(View v) {
        double cal = Double.valueOf(inputCal.getText().toString());
        double protein = Double.valueOf(inputProtein.getText().toString());
        double carbs = Double.valueOf(inputCarbs.getText().toString());
        double fat = Double.valueOf(inputFat.getText().toString());
        goals = new Macros(cal, protein, carbs, fat);

        Intent data = new Intent();
        data.putExtra("goals", goals);
        setResult(RESULT_OK, data);
        finish();
    }

    public void onCheckboxClick(View v) {
        boolean checkBoxState = checkBox.isChecked();
        inputCal.setEnabled(checkBoxState);
        inputProtein.setEnabled(checkBoxState);
        inputCarbs.setEnabled(checkBoxState);
        inputFat.setEnabled(checkBoxState);
        onDataChanged();
    }

    private boolean isValidData() {
        if (!checkBox.isChecked())
            return ((inputHeight.getText().length() > 0) && (inputWeight.getText().length() > 0) && (inputAge.getText().length() > 0));
        else
            return ((inputCal.getText().length() > 0) && (inputProtein.getText().length() > 0) && (inputCarbs.getText().length() > 0) && (inputFat.getText().length() > 0));
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!checkBox.isChecked() &&
                ((inputCal.getEditableText() == s) || (inputCarbs.getEditableText() == s) || (inputProtein.getEditableText() == s) || (inputFat.getEditableText() == s)))
            return;
        continueButton.setEnabled(isValidData());
        onDataChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        onDataChanged();
    }

    private void onDataChanged() {
        if (isValidData() && (!checkBox.isChecked())) {
            try {
                String[] exerciseStrings = getResources().getStringArray(R.array.exercise_level_array);
                String[] goalsStrings = getResources().getStringArray(R.array.goals_array);
                String selectedExerciseString = (String) exerciseSpinner.getSelectedItem();
                String selectedGoalString = (String) goalsSpinner.getSelectedItem();
                int age = Integer.valueOf(inputAge.getText().toString());
                int height = Integer.valueOf(inputHeight.getText().toString());
                int weight = Integer.valueOf(inputWeight.getText().toString());
                int exerciseSelection = 0;
                while ((!exerciseStrings[exerciseSelection].equalsIgnoreCase(selectedExerciseString)) && (exerciseSelection < exerciseStrings.length)) {
                    exerciseSelection++;
                }
                int goalsSelection = 0;
                while ((!goalsStrings[goalsSelection].equalsIgnoreCase(selectedGoalString)) && (goalsSelection < goalsStrings.length)) {
                    goalsSelection++;
                }
                double exerciseMultiplier = 1;
                double goalsMultiplier = 1;
                switch (exerciseSelection) {
                    case 0:
                        exerciseMultiplier = 1;
                        break;
                    case 1:
                        exerciseMultiplier = 1.2;
                        break;
                    case 2:
                        exerciseMultiplier = 1.375;
                        break;
                    case 3:
                        exerciseMultiplier = 1.419;
                        break;
                    case 4:
                        exerciseMultiplier = 1.463;
                        break;
                    case 5:
                        exerciseMultiplier = 1.506;
                        break;
                    case 6:
                        exerciseMultiplier = 1.638;
                        break;
                    case 7:
                        exerciseMultiplier = 1.900;
                        break;
                }
                switch (goalsSelection) {
                    case 0:
                        goalsMultiplier = 0.75;
                        break;
                    case 1:
                        goalsMultiplier = 0.8;
                        break;
                    case 2:
                        goalsMultiplier = 0.85;
                        break;
                    case 3:
                        goalsMultiplier = 1;
                        break;
                    case 4:
                        goalsMultiplier = 1.05;
                        break;
                    case 5:
                        goalsMultiplier = 1.1;
                        break;
                    case 6:
                        goalsMultiplier = 1.15;
                        break;
                }
                int sexModifier = isFemale ? -161 : 5;
                double bmr = 10 * weight + 6.25 * height - 5 * age + sexModifier;
                double cals = bmr * exerciseMultiplier * goalsMultiplier;
                double protein = weight * KG_TO_LBS * PROTEIN_PER_LBS;
                double fat = weight * KG_TO_LBS * FAT_PER_LBS;
                double carbs = (cals - 9 * fat - 4 * protein) / 4;

                inputCal.setText(String.valueOf((int) cals));
                inputProtein.setText(String.valueOf((int) protein));
                inputCarbs.setText(String.valueOf((int) carbs));
                inputFat.setText(String.valueOf((int) fat));

            } catch (NumberFormatException e) {
                Log.e(getString(R.string.app_name), e.getMessage());
                inputCal.setText("");
                inputProtein.setText("");
                inputCarbs.setText("");
                inputFat.setText("");
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        onDataChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isFemale = isChecked;
        continueButton.setEnabled(isValidData());
        onDataChanged();
    }
}
