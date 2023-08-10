package my.edu.utar.assignment;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayResultsActivity extends AppCompatActivity {

    private TextView textViewStoredResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        textViewStoredResults = findViewById(R.id.textViewStoredResults);

        Intent intent = getIntent();
        if (intent != null) {
            String storedResults = intent.getStringExtra("storedResults");
            if (storedResults != null) {
                textViewStoredResults.setText(storedResults);
            }
        }
    }
}