package my.edu.utar.assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

public class StudentDetailsActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhoneNumber;
    private Button buttonStoreDetails;
    private Button buttonDisplayStoredDetails;
    private Button buttonClearDetails;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonStoreDetails = findViewById(R.id.buttonStoreDetails);
        buttonDisplayStoredDetails = findViewById(R.id.buttonDisplayStoredDetails);
        buttonClearDetails = findViewById(R.id.buttonClearDetails);


        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("StudentDetails", MODE_PRIVATE);

        ImageView imageViewLogo = findViewById(R.id.imageViewLogo);
        Picasso.get().load(R.drawable.logo).into(imageViewLogo);

        buttonStoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeStudentDetails();
            }
        });

        buttonDisplayStoredDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayStoredStudentDetails();
            }
        });

        buttonClearDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear student details when the Clear button is clicked
                clearStudentDetails();
            }
        });
    }

    private void storeStudentDetails() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter all the information",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate phone number format
        if (!isValidPhoneNumber(phoneNumber)) {
            Toast.makeText(this, "Please enter a valid phone number (e.g., +60123456789)",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve the current number of students from SharedPreferences
        int numberOfStudents = sharedPreferences.getInt("numberOfStudents", 0);

        // Increment the number of students and save it back to SharedPreferences
        numberOfStudents++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("numberOfStudents", numberOfStudents);

        // Store the student details using a unique identifier (e.g., "name_1", "email_1", "phoneNumber_1")
        String identifier = "_" + numberOfStudents;
        editor.putString("name" + identifier, name);
        editor.putString("email" + identifier, email);
        editor.putString("phoneNumber" + identifier, phoneNumber);

        editor.apply();

        Toast.makeText(this, "Student details stored successfully", Toast.LENGTH_SHORT).show();
    }

    private void displayStoredStudentDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("StudentDetails", MODE_PRIVATE);

        int count = sharedPreferences.getInt("count", 0); // Get the current count of stored details

        if (count == 0) {
            Toast.makeText(this, "No stored student details found", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(StudentDetailsActivity.this, DisplayStudentDetailsActivity.class);

        for (int i = 0; i < count; i++) {
            String name = sharedPreferences.getString("name" + i, "");
            String email = sharedPreferences.getString("email" + i, "");
            String phoneNumber = sharedPreferences.getString("phoneNumber" + i, "");

            // Pass each set of details to the intent
            intent.putExtra("name" + i, name);
            intent.putExtra("email" + i, email);
            intent.putExtra("phoneNumber" + i, phoneNumber);
        }
        startActivity(intent);
    }

    private boolean isValidEmail(String email) {
        // Use a simple email validation regex
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Use a regex to validate phone number format (Malaysia format: +6012xxxxxxx)
        return phoneNumber.matches("^\\+601\\d{8}$");
    }

    private void clearStudentDetails() {
        // Clear the text in the EditText fields for student details
        editTextName.setText("");
        editTextEmail.setText("");
        editTextPhoneNumber.setText("");
    }
}
