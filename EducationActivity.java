package com.example.peerhub;

import android.app.DatePickerDialog;
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

public class EducationActivity extends AppCompatActivity {

    private TextView textView;
    private EditText universityCollegeEditText;
    private EditText degreeSpecializationEditText;
    private EditText fromDateEditText;
    private EditText toDateEditText;
    private CheckBox currentlyStudyingCheckBox;
    private EditText cgpaPercentageEditText;
    private Button saveButton;
    private DatabaseReference userRef;
    private DatePicker datePicker4;
    private DatePicker datePicker5;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.education);

        // Initialize Firebase
        userRef = FirebaseDatabase.getInstance().getReference().child("Education");

        // Initialize views
        universityCollegeEditText = findViewById(R.id.university_college);
        degreeSpecializationEditText = findViewById(R.id.degree_specialization);
        fromDateEditText = findViewById(R.id.from_date_label);
        toDateEditText = findViewById(R.id.from_date_label2);
        currentlyStudyingCheckBox = findViewById(R.id.currently_studying);
        cgpaPercentageEditText = findViewById(R.id.cgpa_percentage);
        saveButton = findViewById(R.id.save_button);
        datePicker4 = findViewById(R.id.datePicker4);
        datePicker5 = findViewById(R.id.datePicker5);
        textView = findViewById(R.id.to2);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        fromDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        toDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker2();
            }
        });

        // Add OnCheckedChangeListener to the CheckBox
        currentlyStudyingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the checkbox is checked, hide the "to" label and its corresponding EditText view
                    textView.setVisibility(View.GONE);
                    fromDateEditText.setVisibility(View.GONE);
                    // Adjust the layout as necessary
                    // For example, you might want to hide other views related to the end date
                } else {
                    // If the checkbox is unchecked, show the "to" label and its corresponding EditText view
                    textView.setVisibility(View.VISIBLE);
                    toDateEditText.setVisibility(View.VISIBLE);
                }
            }
        });

        // Set onClick listener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEducation();
            }
        });
    }

    private void saveEducation() {
        // Retrieve data from EditText fields
        String universityCollege = universityCollegeEditText.getText().toString();
        String degreeSpecialization = degreeSpecializationEditText.getText().toString();
        String cgpaPercentage = cgpaPercentageEditText.getText().toString();
        String fromDate = fromDateEditText.getText().toString();
        String toDate = currentlyStudyingCheckBox.isChecked() ? "Currently Studying" : toDateEditText.getText().toString().trim();
        boolean currentlyStudying = currentlyStudyingCheckBox.isChecked();

        // Check if any field is blank
        if (universityCollege.isEmpty() || degreeSpecialization.isEmpty() || cgpaPercentage.isEmpty() || fromDate.isEmpty()) {
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
        DatabaseReference userEducationRef = FirebaseDatabase.getInstance().getReference("user_education").child(userId);

        // Get a unique key for the new experience under the user's node
        String experienceId = userEducationRef.push().getKey();

        // Create a new Experience object
        Experience newExperience = new Experience(universityCollege, degreeSpecialization, cgpaPercentage, fromDate, toDate);

        // Save the experience under the user's node
        if (experienceId != null) {
            userEducationRef.child(userId).child(experienceId).child("university").setValue(universityCollege);
            userEducationRef.child(userId).child(experienceId).child("degree").setValue(degreeSpecialization);
            userEducationRef.child(userId).child(experienceId).child("cgpa").setValue(cgpaPercentage);
            userEducationRef.child(userId).child(experienceId).child("fromDate").setValue(fromDate);
            userEducationRef.child(userId).child(experienceId).child("toDate").setValue(toDate);
            Toast.makeText(this, "Education saved successfully", Toast.LENGTH_SHORT).show();
        }

    }
    private void showDatePicker() {
        final Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEditText.setText(dateFormatter.format(newDate.getTime()));
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
                toDateEditText.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

}