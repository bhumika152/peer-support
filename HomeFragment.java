package com.example.peerhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerhub.Ancesrtor.PostAdapter;
import com.example.peerhub.Ancesrtor.PostItemData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

        public class HomeFragment extends Fragment implements PostAdapter.OnLikeClickListener {

    private PostAdapter postAdapter;
    private List<PostItemData> postItems = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView postRecyclerView = view.findViewById(R.id.recycler_view);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postAdapter = new PostAdapter(postItems, getContext(), this);

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                  postItems.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String postId = postSnapshot.getKey();
                    String userId = postSnapshot.child("userId").getValue(String.class);
                    String username = postSnapshot.child("username").getValue(String.class);
                    String name = postSnapshot.child("name").getValue(String.class);
                    String text = postSnapshot.child("text").getValue(String.class);
                    String imageUrl = postSnapshot.child("imageUrl").getValue(String.class);
                  String profilePictureUrl = postSnapshot.child("profilePictureUrl").getValue(String.class);
                     Log.d("HomeFragment", "Post ID: " + postId);
                     Log.d("HomeFragment", "User ID: " + userId);
                     Log.d("HomeFragment", "Username: " + username);
                     Log.d("HomeFragment", "Name: " + name);
                     Log.d("HomeFragment", "Text: " + text);
                     Log.d("HomeFragment","Text: "+imageUrl);

                    PostItemData postItem = new PostItemData(postId, userId, profilePictureUrl, username, name, imageUrl, text);
                    postItems.add(postItem);
                }
                postRecyclerView.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged(); // Notify adapter of dataset change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e("HomeFragment", "Database error: " + databaseError.getMessage());
            }
        });
          view.findViewById(R.id.chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ChatActivity
                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onLikeClick(int position) {
        // Handle like click if needed
    }
}
