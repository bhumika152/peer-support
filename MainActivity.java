package com.example.peerhub;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.peerhub.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            openHomeActivity();
        } else {
            // User is not signed up, open SignupActivity
            Intent intent = new Intent(MainActivity.this, Signup.class);
            startActivity(intent);
            finish(); // Finish MainActivity to prevent going back to it when pressing back button from SignupActivity
        }
    }

    private void openHomeActivity() {
        NetworkManager networkManager;
        networkManager = new NetworkManager(this);
        View backgroundImage = findViewById(R.id.background_image);
        if (networkManager.isNetworkConnected()) {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            loadFragment(new HomeFragment());
            binding.navigationBar.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    loadFragment(new HomeFragment());
                } else if (itemId == R.id.nav_map) {
                    loadFragment(new map());
                } else if (itemId == R.id.nav_create) {
                    loadFragment(new Create());
                } else if (itemId == R.id.nav_profile) {
                    loadFragment(new Profile());
                }
                return true;
            });

        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            backgroundImage.setVisibility(View.VISIBLE);
        }
        NetworkBroadcastReceiver networkBroadcastReceiver = new NetworkBroadcastReceiver();
        registerReceiver(networkBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Create.REQUEST_CODE_CREATE_POST && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("post")) {
                getSupportFragmentManager().popBackStack();
            }
        }
    }
}
