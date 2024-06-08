package com.example.peerhub;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    TextView usernameTextView;
    TextView commentTextView;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        usernameTextView = itemView.findViewById(R.id.txt_username);
        commentTextView = itemView.findViewById(R.id.txt_comment);
    }

    public void bind(Comment comment) {
        usernameTextView.setText(comment.getUsername());
        commentTextView.setText(comment.getContent());
    }
}
