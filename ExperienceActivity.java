package com.example.peerhub;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExperienceActivity extends AppCompatActivity {

    private EditText editDesignation;
    private TextView textView;
    private EditText editCompany;
    private EditText editLocation;
    private EditText fromDateLabel;
    private EditText fromDateLabel2;
    private CheckBox currentlyStudying;
    private Button saveButton;
    private DatabaseReference userRef;
    private DatePicker datePicker2;
    private DatePicker datePicker3;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experience);

        // Initialize Firebase
        userRef = FirebaseDatabase.getInstance().getReference().child("Experience");

        // Initialize EditText fields
        editDesignation = findViewById(R.id.editdesignation);
        editCompany = findViewById(R.id.editcompany);
        editLocation = findViewById(R.id.editlocation);
        fromDateLabel = findViewById(R.id.from_date_label);
        fromDateLabel2 = findViewById(R.id.from_date_label2);
        currentlyStudying = findViewById(R.id.currently_studying);
        datePicker2 = findViewById(R.id.datePicker2);
        datePicker3 = findViewById(R.id.datePicker3);
        textView = findViewById(R.id.to);

        // Initialize the Save button
        saveButton = findViewById(R.id.expbtn);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        fromDateLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        fromDateLabel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker2();
            }
        });

        // Add OnCheckedChangeListener to the CheckBox
        currentlyStudying.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the checkbox is checked, hide the "to" label and its corresponding EditText view
                    textView.setVisibility(View.GONE);
                    fromDateLabel2.setVisibility(View.GONE);
                    // Adjust the layout as necessary
                    // For example, you might want to hide other views related to the end date
                } else {
                    // If the checkbox is unchecked, show the "to" label and its corresponding EditText view
                    textView.setVisibility(View.VISIBLE);
                    fromDateLabel2.setVisibility(View.VISIBLE);
                }
            }
        });

        // Set onClick listener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveExperience();
            }
        });
    }

    private void saveExperience() {
        // Retrieve data from EditText fields
        String designation = editDesignation.getText().toString().trim();
        String company = editCompany.getText().toString().trim();
        String location = editLocation.getText().toString().trim();
        String fromDate = fromDateLabel.getText().toString().trim();
        String toDate = currentlyStudying.isChecked() ? "Currently Working" : fromDateLabel2.getText().toString().trim();
        boolean studyingCurrently = currentlyStudying.isChecked();

        // Check if any field is blank
        if (designation.isEmpty() || company.isEmpty() || location.isEmpty() || fromDate.isEmpty()) {
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
        // Get the current user's ID or username
        String userId = currentUser.getUid(); // Replace "userId" with the actual user ID or username

        // Get a reference to the experience node for the current user
        DatabaseReference userExperienceRef = FirebaseDatabase.getInstance().getReference("user_experience").child(userId);

        // Get a unique key for the new experience under the user's node
        String experienceId = userExperienceRef.push().getKey();

        // Create a new Experience object
        Experience newExperience = new Experience(designation, company, location, fromDate, toDate);

        // Save the experience under the user's node
        if (experienceId != null) {
            userExperienceRef.child(userId).child(experienceId).child("designation").setValue(designation);
            userExperienceRef.child(userId).child(experienceId).child("company").setValue(company);
            userExperienceRef.child(userId).child(experienceId).child("location").setValue(location);
            userExperienceRef.child(userId).child(experienceId).child("fromDate").setValue(fromDate);
            userExperienceRef.child(userId).child(experienceId).child("toDate").setValue(toDate);
            Toast.makeText(this, "Experience saved successfully", Toast.LENGTH_SHORT).show();
        }

        Intent[] intents = new Intent[]{new Intent(this, Profile.class)};
        startActivities(intents);
    }
    private void showDatePicker() {
        final Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateLabel.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
    // Add the showDatePicker2 method
    private void showDatePicker2() {
        final Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateLabel2.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
