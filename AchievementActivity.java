package com.example.peerhub;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AchievementActivity extends AppCompatActivity {

    private EditText organizationEditText;
    private EditText achievementEditText;
    private EditText dateEditText;
    private EditText urlEditText;
    private DatePicker datePicker;
    private Button saveButton;

    private DatabaseReference databaseReference;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievements);

        organizationEditText = findViewById(R.id.organization);
        achievementEditText = findViewById(R.id.achievement);
        dateEditText = findViewById(R.id.date_picker);
        urlEditText = findViewById(R.id.cgpa_percentage);
        saveButton = findViewById(R.id.achievementibtn);
        datePicker = findViewById(R.id.datePicker);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("achievements");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveAchievement();
            }
        });
    }

    private void showDatePicker() {
        final Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateEditText.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
    private void saveAchievement() {
        String organization = organizationEditText.getText().toString().trim();
        String achievement = achievementEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String url = urlEditText.getText().toString().trim();

        // Check if any field is blank
        if (organization.isEmpty() || achievement.isEmpty() || date.isEmpty() || url.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current user's ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // User not logged in
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current user's ID or username (replace "userId" with the actual user ID or username)
        String userId = currentUser.getUid(); // Replace "userId" with the actual user ID or username

        // Get a reference to the achievements node for the current user
        DatabaseReference userAchievementsRef = FirebaseDatabase.getInstance().getReference("user_achievements").child(userId);

        // Get a unique key for the new achievement under the user's node
        String achievementId = databaseReference.child(userId).push().getKey();

        // Create a new Achievement object
        Achievement newAchievement = new Achievement(organization, achievement, date, url);

        // Save the achievement under the user's node
        if (achievementId != null) {
            databaseReference.child(userId).child(achievementId).child("organization").setValue(organization);
            databaseReference.child(userId).child(achievementId).child("achievement").setValue(achievement);
            databaseReference.child(userId).child(achievementId).child("date").setValue(date);
            databaseReference.child(userId).child(achievementId).child("url").setValue(url);

            Toast.makeText(this, "Achievement saved successfully", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);

    }
}