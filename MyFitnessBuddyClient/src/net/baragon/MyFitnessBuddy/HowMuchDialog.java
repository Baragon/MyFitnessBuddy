package net.baragon.MyFitnessBuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import net.baragon.MyFitnessBuddy.util.FoodInfo;


public class HowMuchDialog implements AdapterView.OnItemSelectedListener,DialogInterface.OnClickListener,TextWatcher {
    private AlertDialog dialog;
    private View dialogView;
    private AddFoodActivity activity;
    private FoodInfo selectedFood;
    private ServingSpinnerAdapter adapter;
    private Button positiveButton;
    private EditText inputServingName;
    private EditText inputGrams;
    private boolean newServing;
    private double servingSize;
    private String servingName;
    public HowMuchDialog(AddFoodActivity activity,FoodInfo selectedFood) {
        this.activity=activity;
        this.selectedFood=selectedFood;
        newServing=false;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.how_much_dialog_title));
        dialogView = LayoutInflater.from(activity).inflate(R.layout.add_food_dialog,null);
        builder.setView(dialogView);
        Spinner spinner = (Spinner)dialogView.findViewById(R.id.addFoodDialog_servingSpinner);
        spinner.setOnItemSelectedListener(this);
        adapter = new ServingSpinnerAdapter(activity,selectedFood.servings);
        spinner.setAdapter(adapter);
        builder.setPositiveButton("OK", this);
        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
            }
        });
        inputServingName = ((EditText)dialogView.findViewById(R.id.addFoodDialog_servingName));
        inputServingName.addTextChangedListener(this);
        inputGrams = ((EditText)dialogView.findViewById(R.id.addFoodDialog_servinggrams));
        inputGrams.addTextChangedListener(this);
        positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
    }

    private boolean isValidServingDescription() {
        try {
            return (inputServingName.getText().length() > 0) && (Double.valueOf(inputGrams.getText().toString()) > 0);
        }
        catch (NumberFormatException e)
        {
            Log.e(activity.getString(R.string.app_name),e.getMessage());
            return false;
        }
    }

    public void show() {
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        EditText inputAmount = (EditText) dialogView.findViewById(R.id.addFoodDialog_amount);
        double amount = Double.valueOf(inputAmount.getText().toString());
        if(newServing){
            servingName = inputServingName.getText().toString();
            servingSize = Double.valueOf(inputGrams.getText().toString());
        }
        activity.onDialogOK(selectedFood,amount,servingSize,servingName,newServing);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position<adapter.getData().size()) {
            dialogView.findViewById(R.id.addFoodDialog_extra).setVisibility(View.INVISIBLE);
            newServing=false;
            servingName = selectedFood.servings.get(position).name;
            servingSize = selectedFood.servings.get(position).grams;
            positiveButton.setEnabled(true);
        }else
        {
            dialogView.findViewById(R.id.addFoodDialog_extra).setVisibility(View.VISIBLE);
            newServing=true;
            positiveButton.setEnabled(isValidServingDescription());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        positiveButton.setEnabled(isValidServingDescription());
    }
}
