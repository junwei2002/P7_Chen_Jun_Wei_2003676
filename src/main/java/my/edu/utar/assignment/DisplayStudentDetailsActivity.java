package my.edu.utar.assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayStudentDetailsActivity extends AppCompatActivity {

    private TextView textViewStudentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_student_details);

        textViewStudentDetails = findViewById(R.id.textViewStudentDetails);

        Intent intent = getIntent();
        if (intent != null) {
            // Retrieve the number of students from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("StudentDetails", MODE_PRIVATE);
            int numberOfStudents = sharedPreferences.getInt("numberOfStudents", 0);

            StringBuilder studentDetailsBuilder = new StringBuilder();

            // Loop through each student and append their details to the StringBuilder
            for (int i = 1; i <= numberOfStudents; i++) {
                String name = sharedPreferences.getString("name_" + i, "");
                String email = sharedPreferences.getString("email_" + i, "");
                String phoneNumber = sharedPreferences.getString("phoneNumber_" + i, "");

                // Append the student details to the StringBuilder with a new line
                studentDetailsBuilder.append("Student ").append(i).append(":\n");
                studentDetailsBuilder.append("Name: ").append(name).append("\n");
                studentDetailsBuilder.append("Email: ").append(email).append("\n");
                studentDetailsBuilder.append("Phone Number: ").append(phoneNumber).append("\n\n");
            }

            // Set the text in the TextView to display all student details
            textViewStudentDetails.setText(studentDetailsBuilder.toString());
        }
    }
}
