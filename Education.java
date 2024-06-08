package com.example.peerhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Education extends Fragment {

    private TextView universityTextView;
    private TextView degreeTextView;
    private TextView fromDateTextView;
    private TextView toDateTextView;
    private TextView cgpaTextView;
    private ImageView initialImageView;
    private TextView initialTextView;
    private DatabaseReference userEducationRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_education, container, false);

        // Initialize views
        universityTextView = view.findViewById(R.id.universityTextView);
        degreeTextView = view.findViewById(R.id.degreeTextView);
        fromDateTextView = view.findViewById(R.id.fromDateTextView);
        toDateTextView = view.findViewById(R.id.toDateTextView);
        cgpaTextView = view.findViewById(R.id.cgpaTextView);
        initialImageView = view.findViewById(R.id.initialImageView);
        initialTextView = view.findViewById(R.id.initialTextView);


        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Retrieve user ID
            String userId = currentUser.getUid();

            // Construct the reference to the user's education data in Firebase
            userEducationRef = FirebaseDatabase.getInstance().getReference("user_education").child(userId);
            // Add a ValueEventListener to retrieve the education data
            userEducationRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Check if data exists for the user
                    if (dataSnapshot.exists()) {
                        // Data exists, retrieve it
                        String university = dataSnapshot.child("university").getValue(String.class);
                        String degree = dataSnapshot.child("degree").getValue(String.class);
                        String fromDate = dataSnapshot.child("fromDate").getValue(String.class);
                        String toDate = dataSnapshot.child("toDate").getValue(String.class);
                        String cgpa = dataSnapshot.child("cgpa").getValue(String.class);

                        // Update UI with retrieved data
                        universityTextView.setText(university);
                        degreeTextView.setText(degree);
                        fromDateTextView.setText(fromDate);
                        toDateTextView.setText(toDate);
                        cgpaTextView.setText(cgpa);

                        // Hide the initial image and text views
                        initialImageView.setVisibility(View.GONE);
                        initialTextView.setVisibility(View.GONE);
                    } else {
                        initialImageView.setVisibility(View.VISIBLE);
                        initialTextView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        return view;
    }
}