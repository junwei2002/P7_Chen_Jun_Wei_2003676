package my.edu.utar.doit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText editTextTotalBill;
    private EditText editTextNumberOfPerson;
    private TextView textViewResult;
    private Button buttonSplit;
    private Button buttonSplitPercentage;
    private TextView textViewSplitResult;
    private EditText editTextSplitAmount;
    private Button buttonSplitAmount;


    private EditText editTextPercentage;

    public MainActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTotalBill = findViewById(R.id.editTextNumberDecimal);
        editTextNumberOfPerson = findViewById(R.id.editTextNumber);
        textViewResult = findViewById(R.id.editTextNumberDecimal2);
        buttonSplit = findViewById(R.id.button);
        buttonSplitPercentage = findViewById(R.id.buttonSplitPercentage);
        textViewSplitResult = findViewById(R.id.textViewSplitResult);
        editTextTotalBill = findViewById(R.id.editTextNumberDecimal);
        buttonSplitPercentage = findViewById(R.id.buttonSplitPercentage);
        textViewSplitResult = findViewById(R.id.textViewSplitResult);
        editTextPercentage = findViewById(R.id.editTextNumber2);
        buttonSplitAmount = findViewById(R.id.button2);
        editTextSplitAmount = findViewById(R.id.editTextNumberDecimal4);




        buttonSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateSplitAmount();
            }
        });
        buttonSplitPercentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateSplitByPercentage(); // Call the function to perform the calculation
            }
        });
        buttonSplitAmount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                calculateSplitByAmount();
            }
        });
        Button buttonStoreResults = findViewById(R.id.buttonStoreResults);
        buttonStoreResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double billAmount = Double.parseDouble(editTextTotalBill.getText().toString());
                saveBillResult(billAmount);
                Toast.makeText(MainActivity.this, "Result stored successfully", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void calculateSplitAmount() {

        String billAmountStr = editTextTotalBill.getText().toString();
        String numberOfPersonStr = editTextNumberOfPerson.getText().toString();

        if (billAmountStr.isEmpty() || numberOfPersonStr.isEmpty()) {
            textViewResult.setText("Enter both bill amount and number of persons.");
            return;
        }

        double billAmount = Double.parseDouble(billAmountStr);
        int numberOfPerson = Integer.parseInt(numberOfPersonStr);

        if (numberOfPerson <= 0) {
            textViewResult.setText("Number of persons must be greater than zero.");
            return;
        }

        double splitAmount = billAmount / numberOfPerson;
        textViewResult.setText("Each person should pay: $" + splitAmount);
    }
    private void calculateSplitByPercentage() {
        String percentageStr = editTextPercentage.getText().toString();
        String totalBillStr = editTextTotalBill.getText().toString();

        if (TextUtils.isEmpty(percentageStr) || TextUtils.isEmpty(totalBillStr)) {
            textViewSplitResult.setText("Enter both total bill and percentage.");
            return;
        }

        double percentage = Double.parseDouble(percentageStr);
        double totalBill = Double.parseDouble(totalBillStr);

        if (percentage < 0 || percentage > 100) {
            textViewSplitResult.setText("Percentage must be between 0 and 100.");
            return;
        }

        double personAmount = (percentage / 100.0) * totalBill;
        textViewSplitResult.setText(String.format("Amount to pay: $%.2f", personAmount));

    }
    private void calculateSplitByAmount() {
        String splitAmountStr = editTextSplitAmount.getText().toString();
        String numberOfPersonStr = editTextNumberOfPerson.getText().toString();

        if (TextUtils.isEmpty(splitAmountStr) || TextUtils.isEmpty(numberOfPersonStr)) {
            textViewSplitResult.setText("Enter split amount and number of persons.");
            return;
        }

        double splitAmount = Double.parseDouble(splitAmountStr);
        int numberOfPerson = Integer.parseInt(numberOfPersonStr);
        if (numberOfPerson <= 0) {
            textViewSplitResult.setText("Number of persons must be greater than zero.");
            return;
        }

        double personAmount = splitAmount / numberOfPerson;
        textViewSplitResult.setText(String.format("Amount to pay per person: $%.2f", personAmount));
    }
    private void saveBillResult(double amount) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int counter = sharedPreferences.getInt("counter", 0);

        editor.putFloat("amount_" + counter, (float) amount);

        // Store the current timestamp in milliseconds
        long timestamp = System.currentTimeMillis();
        editor.putLong("timestamp_" + counter, timestamp);

        editor.putInt("counter", counter + 1);

        editor.apply();
    }


    private void loadBillResults() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        int counter = sharedPreferences.getInt("counter", 0);

        StringBuilder resultText = new StringBuilder();
        for (int i = 0; i < counter; i++) {
            float amount = sharedPreferences.getFloat("amount_" + i, 0);
            resultText.append("Bill ").append(i + 1).append(": $").append(amount).append("\n");
        }

        // Display the results in a TextView or another UI element
        textViewResult.setText(resultText.toString());
    }


}

















