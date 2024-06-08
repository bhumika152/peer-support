package com.example.peerhub;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {
    private Button editbtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();

        // Retrieve name and username from Firebase
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);
                    Log.d("Firebase", "Name retrieved from Firebase: " + name);

                    // Set name and username to TextViews
                    TextView nameTextView = view.findViewById(R.id.profile_main_name);
                    TextView usernameTextView = view.findViewById(R.id.profile_main_username);
                    nameTextView.setText(name);
                    usernameTextView.setText(username);

                    // Verify if the TextView is updated
                    String displayedName = nameTextView.getText().toString();
                    Log.d("UI", "Name displayed in TextView: " + displayedName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });

        TextView textView1 = view.findViewById(R.id.experience);
        TextView textView2 = view.findViewById(R.id.achievements);
        TextView textView3 = view.findViewById(R.id.education);
        editbtn = view.findViewById(R.id.editbtn);

        textView1.setTextColor(Color.WHITE);
        textView1.setTypeface(null, Typeface.BOLD);

        replaceFragment(new Experience());

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                startActivity(intent);
            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Experience());

                // Change text appearance
                textView1.setTextColor(Color.WHITE);
                textView1.setTypeface(null, Typeface.BOLD);

                // Reset other TextViews appearance
                textView2.setTextColor(Color.BLACK);
                textView2.setTypeface(null, Typeface.NORMAL);
                textView3.setTextColor(Color.BLACK);
                textView3.setTypeface(null, Typeface.NORMAL);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Achievement());

                // Change text appearance
                textView2.setTextColor(Color.WHITE);
                textView2.setTypeface(null, Typeface.BOLD);

                // Reset other TextViews appearance
                textView1.setTextColor(Color.BLACK);
                textView1.setTypeface(null, Typeface.NORMAL);
                textView3.setTextColor(Color.BLACK);
                textView3.setTypeface(null, Typeface.NORMAL);
            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Education());

                // Change text appearance
                textView3.setTextColor(Color.WHITE);
                textView3.setTypeface(null, Typeface.BOLD);

                // Reset other TextViews appearance
                textView1.setTextColor(Color.BLACK);
                textView1.setTypeface(null, Typeface.NORMAL);
                textView2.setTextColor(Color.BLACK);
                textView2.setTypeface(null, Typeface.NORMAL);
            }
        });

        return view;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, fragment);

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
