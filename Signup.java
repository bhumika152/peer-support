package com.example.peerhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity{

    EditText editTextEmail, editTextPassword, editTextRePassword, editTextUsername, editTextName;
    TextView signup, loginnow;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextUsername= findViewById(R.id.signup_username);
        editTextPassword = findViewById(R.id.password);
        editTextRePassword = findViewById(R.id.repassword);
        editTextName = findViewById(R.id.signup_name);
        signup = findViewById(R.id.signuptitle);
        loginnow = findViewById(R.id.loginNow);
        buttonReg = findViewById(R.id.signupbtn);
        progressBar = findViewById(R.id.progressBar);

        loginnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String name, username, email, password, rePassword;
                name = String.valueOf(editTextName.getText());
                username = String.valueOf(editTextUsername.getText());
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                rePassword = String.valueOf(editTextRePassword.getText());

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(Signup.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(username)){
                    Toast.makeText(Signup.this, "Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Signup.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Signup.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(rePassword)){
                    Toast.makeText(Signup.this, "Enter password again", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // User is successfully registered
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userId = user.getUid();

                                // Save name and username to Firebase Firestore or Realtime Database
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                usersRef.child("name").setValue(name);
                                usersRef.child("username").setValue(username);

                                Toast.makeText(Signup.this, "Account Created.",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Signup.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

}