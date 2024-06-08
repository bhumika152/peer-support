package com.example.peerhub;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.peerhub.Ancesrtor.PostItemData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

public class Create extends Fragment {

    public static final int REQUEST_CODE_CREATE_POST = 10;
    private EditText questionEditText;
    private ImageView selectedImageView, profileImageView;
    private ImageButton imageButton;
    private TextView usernameTextView, nameTextView, selectImageButton;
    private Button postButton;

    private static final int REQUEST_IMAGE_PICKER = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create2, container, false);
        questionEditText = view.findViewById(R.id.question);
        selectedImageView = view.findViewById(R.id.selected_image1);
        imageButton = view.findViewById(R.id.image);
        profileImageView = view.findViewById(R.id.profile1);
        usernameTextView = view.findViewById(R.id.username1);
        nameTextView = view.findViewById(R.id.name1);
        postButton = view.findViewById(R.id.post1);
        selectImageButton = view.findViewById(R.id.select_image1);

        imageButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                openImagePicker();
            }
        });

        questionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updatePostButtonState();
            }
        });

        postButton.setOnClickListener(v -> createPost());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    usernameTextView.setText(username);
                    nameTextView.setText(name);
                } else {
                    Toast.makeText(requireContext(), "No user data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        profileImageView.setImageResource(R.drawable.image);
        selectImageButton.setText("Select Photo");

        return view;
    }

    Uri imageUri = null;
    String imageUrl = null;

    private void createPost() {
        postButton.setEnabled(false);

        String question = questionEditText.getText().toString();
        Drawable drawable = selectedImageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null) {
                imageUri = getImageUri(requireContext(), bitmap);
            }
        }

        if (!question.isEmpty() || imageUri != null) {
            String username = usernameTextView.getText().toString();
            String name = nameTextView.getText().toString();

            // Convert profile picture to Base64 string
            String profilePictureBase64 = getImageUrl(profileImageView);

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String userId = currentUser != null ? currentUser.getUid() : "";

            if (imageUri != null) {
                imageUrl = imageUri.toString();
            }

            String postId = postsRef.push().getKey();
            if (postId != null) {
                // Save post data to Firebase Database
                PostItemData postItemData = new PostItemData(postId, userId, profilePictureBase64, username, name, imageUrl, question);
                postsRef.child(postId).setValue(postItemData);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("post_created", true);
                getActivity().setResult(RESULT_OK, resultIntent);
                getActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(requireContext(), "Failed to create post", Toast.LENGTH_SHORT).show();
            }
        }

        postButton.setEnabled(true);
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICKER);
        selectImageButton.setText("Change Photo");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
if (requestCode == REQUEST_IMAGE_PICKER && resultCode == getActivity().RESULT_OK && data != null) {
Uri imageUri = data.getData();
selectedImageView.setImageURI(imageUri);
updatePostButtonState();
}
}private String getImageUrl(ImageView imageView) {
    if (imageView.getId() == R.id.profile1) {
        // Convert profile picture drawable to Base64 directly
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                return Base64.encodeToString(imageBytes, Base64.DEFAULT);
            }
        }
    }
    return "";
}

private void updatePostButtonState() {
    boolean hasText = !questionEditText.getText().toString().isEmpty();
    boolean hasImage = selectedImageView.getDrawable() != null;
    if (hasText || hasImage) {
        postButton.setBackgroundColor(getResources().getColor(R.color.blue));
    } else {
        postButton.setBackgroundColor(getResources().getColor(R.color.grey2));
    }
}
 }