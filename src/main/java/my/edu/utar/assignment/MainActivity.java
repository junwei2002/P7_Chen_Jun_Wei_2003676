package my.edu.utar.assignment;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTotalAmount;
    private EditText editTextNumberOfPeople;
    private EditText editTextValues;
    private Button buttonCalculate;
    private Button buttonClear;
    private TextView textViewResult;
    private Button buttonShareResults;
    private Button buttonStoreResults;
    private Button buttonEnterDetails;
    private Button buttonDisplayStoredResults;
    private Spinner dropdown;
    private SharedPreferences sharedPreferences;
    private TextView textViewTotalAmount;
    private TextView textViewNumberOfPeople;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextTotalAmount = findViewById(R.id.editTextTotalAmount);
        editTextNumberOfPeople = findViewById(R.id.editTextNumberOfPeople);
        editTextValues = findViewById(R.id.editTextValues);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonClear = findViewById(R.id.buttonClear);
        textViewResult = findViewById(R.id.textViewResult);
        buttonShareResults = findViewById(R.id.buttonShareResults);
        buttonStoreResults = findViewById(R.id.buttonStoreResults);
        buttonDisplayStoredResults = findViewById(R.id.buttonDisplayStoredResults);
        buttonEnterDetails = findViewById(R.id.buttonEnterDetails);
        dropdown = findViewById(R.id.dropdown);
        textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
        textViewNumberOfPeople = findViewById(R.id.textViewNumberOfPeople);



        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.breakdown_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        dropdown.setAdapter(adapter);


        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = dropdown.getSelectedItem().toString();
                String hint = "";

                if (selectedOption.equals("Percentage") || selectedOption.equals("Ratio")) {
                    hint = "Enter whole number(comma-separated)";
                } else if (selectedOption.equals("Amount")) {
                    hint = "Enter whole number or decimals (comma-separated) ";
                } else if (selectedOption.equals("Combination")) {
                    hint = "Enter whole number for percentage,decimals for individual amount (comma-separated) ";
                }

                editTextValues.setHint(hint);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("BillResults", MODE_PRIVATE);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBillBreakdown();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear all input fields
                editTextTotalAmount.getText().clear();
                editTextNumberOfPeople.getText().clear();
                editTextValues.getText().clear();
            }
        });

        buttonShareResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareResults();
            }
        });

        buttonStoreResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeResults();
            }
        });

        buttonDisplayStoredResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayStoredResults();
            }
        });

        buttonEnterDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StudentDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void calculateBillBreakdown() {
        // Get the total amount and number of people from the EditText fields
        double totalAmount = Double.parseDouble(editTextTotalAmount.getText().toString());
        int numberOfPeople = Integer.parseInt(editTextNumberOfPeople.getText().toString());

        textViewTotalAmount.setText(String.format(Locale.getDefault(), "Total Amount: %.2f", totalAmount));
        textViewNumberOfPeople.setText(String.format(Locale.getDefault(), "Number of People: %d", numberOfPeople));


        String selectedOption = dropdown.getSelectedItem().toString();

        // Calculate the equal amount to be paid by each person
        double equalAmount = totalAmount / numberOfPeople;

        if (selectedOption.equals("Percentage")) {
            // Get the percentages entered by the user
            String percentages = editTextValues.getText().toString();
            if (!percentages.isEmpty()) {
                String[] percentageArray = percentages.split(",");
                if (percentageArray.length != numberOfPeople) {
                    Toast.makeText(this, "Number of percentages should match the number of people",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                double[] amounts = new double[numberOfPeople];
                double totalPercentage = 0;

                // Convert the percentages to double and calculate the total percentage
                for (int i = 0; i < numberOfPeople; i++) {
                    amounts[i] = (Double.parseDouble(percentageArray[i]) / 100) * totalAmount;
                    totalPercentage += Double.parseDouble(percentageArray[i]);
                }

                // Allow for a small tolerance due to floating-point precision
                if (Math.abs(totalPercentage - 100) > 0.01) {
                    Toast.makeText(this, "Total percentage should be 100%", Toast.LENGTH_SHORT).show();
                    return;
                }

                displayResult(amounts);
                return;
            }
        }

        else if (selectedOption.equals("Ratio")) {
            // Get the ratios entered by the user
            String ratios = editTextValues.getText().toString();
            if (!ratios.isEmpty()) {
                String[] ratioArray = ratios.split(",");
                if (ratioArray.length != numberOfPeople) {
                    Toast.makeText(this, "Number of ratios should match the number of people",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                double[] amounts = new double[numberOfPeople];
                int totalRatio = 0;

                // Convert the ratios to double and calculate the total ratio
                for (int i = 0; i < numberOfPeople; i++) {
                    totalRatio += Integer.parseInt(ratioArray[i]);
                }

                // Calculate the individual amounts based on the ratios
                for (int i = 0; i < numberOfPeople; i++) {
                    amounts[i] = (Double.parseDouble(ratioArray[i]) / totalRatio) * totalAmount;
                }

                displayResult(amounts);
                return;
            }
        }

        else if (selectedOption.equals("Amount")) {
            double[] amounts = new double[numberOfPeople];
            double sum = 0;
            String Amount = editTextValues.getText().toString();
            if (!Amount.isEmpty()) {
                String[] AmountArray = Amount.split(",");
                if (AmountArray.length != numberOfPeople) {
                    Toast.makeText(this, "Number of inputs should match the number of people",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i=0 ;i < numberOfPeople;i++){
                    amounts[i] = Double.parseDouble(AmountArray[i]);
                    sum += amounts[i];
                }

                if (Math.abs(sum - totalAmount) > 0.01) {
                    Toast.makeText(this, "The sum of individual amounts does not match the total bill amount",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                displayResult(amounts);
                return;
            }
        }

        else if (selectedOption.equals("Combination")) {
            double[] amounts = new double[numberOfPeople];
            double sum = 0;

            String combinationInput = editTextValues.getText().toString();
            if (!combinationInput.isEmpty()) {
                String[] combinationArray = combinationInput.split(",");
                if (combinationArray.length != numberOfPeople) {
                    Toast.makeText(this, "Number of inputs should match the number of people",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < numberOfPeople; i++) {
                    String input = combinationArray[i];
                    if (input.contains(".")) {
                        // Amount breakdown
                        amounts[i] = Double.parseDouble(input);
                    } else {
                        // Percentage breakdown
                        double percentage = Double.parseDouble(input);
                        amounts[i] = (percentage / 100) * totalAmount;
                    }
                    sum += amounts[i];
                }
                if (Math.abs(sum - totalAmount) > 0.01) {
                    Toast.makeText(this, "The sum of individual amounts does not match the total bill amount",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                displayResult(amounts);
                return;
            }
        }

        // Calculate the equal amount to be paid by each person when no options are chosen
        double[] amounts = new double[numberOfPeople];
        Arrays.fill(amounts, equalAmount);
        displayResult(amounts);
    }


    private void displayResult(double[] amounts) {
        StringBuilder result = new StringBuilder();
        String selectedOption = dropdown.getSelectedItem().toString();

        if (selectedOption.equals("Percentage") || selectedOption.equals("Ratio")) {
            for (int i = 0; i < amounts.length; i++) {
                String formattedAmount = String.format("%.2f", amounts[i]);
                result.append(" Each person pay ").append(": RM ").append(formattedAmount).append("\n");
            }
        } else if (selectedOption.equals("Amount") || selectedOption.equals("Combination")) {
            for (int i = 0; i < amounts.length; i++) {
                String formattedAmount = String.format("%.2f", amounts[i]);
                result.append(" Person ").append(i + 1).append(": RM ").append(formattedAmount).append("\n");
            }
        }

        textViewResult.setText(result.toString());
    }

    private void storeResults() {
        String newResult = textViewResult.getText().toString();

        if (newResult.isEmpty()) {
            Toast.makeText(this, "No results to store", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve the stored results from SharedPreferences
        String storedResults = sharedPreferences.getString("results", "");

        // Append the new result
        StringBuilder updatedResults = new StringBuilder();
        if (!storedResults.isEmpty()) {
            updatedResults.append(storedResults).append("\n");
        }
        updatedResults.append(newResult);

        // Store the updated results in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("results", updatedResults.toString());
        editor.apply();

        Toast.makeText(this, "Results stored successfully", Toast.LENGTH_SHORT).show();
    }

    private void displayStoredResults() {
        String storedResults = sharedPreferences.getString("results", "");
        if (!storedResults.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, DisplayResultsActivity.class);
            intent.putExtra("storedResults", storedResults);
            startActivity(intent);
        } else {
            Toast.makeText(this, "No stored results found", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareResults() {
        String results = textViewResult.getText().toString();
        if (!results.isEmpty()) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, results);
            startActivity(Intent.createChooser(shareIntent, "Share Results"));
        } else {
            Toast.makeText(this, "There are no results to share.", Toast.LENGTH_SHORT).show();
        }
    }
}






