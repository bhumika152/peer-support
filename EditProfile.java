package com.example.peerhub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class EditProfile extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    private ImageView imageView2;
    private  ImageView imageView3;
    private RadioGroup radioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private RadioButton otherRadioButton;
    private EditText editText1;
    private EditText editText2;
    private TextView notChangedEmail;
    private TextView save;
    FirebaseAuth mAuth;
    DatabaseReference userRef;

    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile2);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        save = findViewById(R.id.save);
        notChangedEmail = findViewById(R.id.notChangedEmail);
        TextView textView1 = findViewById(R.id.experience);
        TextView textView2 = findViewById(R.id.achievements);
        TextView textView3 = findViewById(R.id.education);
        ImageView imageView1 = findViewById(R.id.back_arrow);
        imageView2 = findViewById(R.id.edit);
        imageView3 = findViewById(R.id.profile_image);
        editText1 = findViewById(R.id.EditText1);
        editText2 = findViewById(R.id.editText2);

        radioGroup = findViewById(R.id.radioGroup);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        otherRadioButton = findViewById(R.id.otherRadioButton);

        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Get the email of the current user
            String userEmail = currentUser.getEmail();

            // Set the email to the TextView
            notChangedEmail.setText(userEmail);
        }
        imageView1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       finish();
    }
});
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText1.getText().toString();
                String username = editText2.getText().toString();

                if(name.isEmpty() || username.isEmpty()) {
                    Toast.makeText(EditProfile.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Get the selected gender
                String gender = "";
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton radioButton = findViewById(selectedId);
                    gender = radioButton.getText().toString();
                }

                // Get the current user ID
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    userRef.child("name").setValue(name);
                    userRef.child("username").setValue(username);
                    userRef.child("gender").setValue(gender);
                    Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_LONG).show();
                }
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to open the gallery
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // Check if there's an app to handle the intent
                if (galleryIntent.resolveActivity(getPackageManager()) != null) {
                    // Start the activity for result, allowing the user to pick an image
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
                } else {
                    // If no app can handle the intent, show an error message
                    Toast.makeText(getApplicationContext(), "No app available to handle this action", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Change text appearance
                textView1.setTextColor(Color.WHITE);
                textView1.setTypeface(null, Typeface.BOLD);

                // Reset other TextViews appearance
                textView2.setTextColor(Color.BLACK);
                textView2.setTypeface(null, Typeface.NORMAL);
                textView3.setTextColor(Color.BLACK);
                textView3.setTypeface(null, Typeface.NORMAL);
                Intent intent = new Intent(EditProfile.this, ExperienceActivity.class);
                startActivity(intent);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Change text appearance
                textView2.setTextColor(Color.WHITE);
                textView2.setTypeface(null, Typeface.BOLD);

                // Reset other TextViews appearance
                textView1.setTextColor(Color.BLACK);
                textView1.setTypeface(null, Typeface.NORMAL);
                textView3.setTextColor(Color.BLACK);
                textView3.setTypeface(null, Typeface.NORMAL);
                Intent intent = new Intent(EditProfile.this, AchievementActivity.class);
                startActivity(intent);
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Change text appearance
                textView3.setTextColor(Color.WHITE);
                textView3.setTypeface(null, Typeface.BOLD);

                // Reset other TextViews appearance
                textView1.setTextColor(Color.BLACK);
                textView1.setTypeface(null, Typeface.NORMAL);
                textView2.setTextColor(Color.BLACK);
                textView2.setTypeface(null, Typeface.NORMAL);
                Intent intent = new Intent(EditProfile.this, EducationActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from the gallery picker and if it's successful
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the selected image URI from the data intent
            Uri selectedImageUri = data.getData();

            try {
                // Convert the URI to a Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                // Set the bitmap to imageView3
                imageView3.setImageBitmap(bitmap);

                // Display a toast indicating that the image is selected
                Toast.makeText(getApplicationContext(), "Image selected from gallery", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}