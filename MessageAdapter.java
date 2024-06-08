// MessageAdapter.java
package com.example.peerhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private static final int MESSAGE_LAYOUT = 0;
    String fellowProfileImageUrl;
    String myUID;
    Context mContext;
    List<Message> messages;


    public MessageAdapter(Context mContext) {
        this.mContext = mContext;
        this.messages = new ArrayList<>();
        myUID = FirebaseAuth.getInstance().getUid();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.adapter_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (myUID.equals(message.getSenderId())) {
            holder.container.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            holder.profileImage.setVisibility(View.GONE);
        } else {
            holder.container.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            holder.profileImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(fellowProfileImageUrl).into(holder.profileImage);
        }
        holder.message.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void clearMessages() {
        messages.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        ImageView profileImage;
        View container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.txt_message);
            profileImage = itemView.findViewById(R.id.img_profile);
            container = itemView.findViewById(R.id.container);
        }
    }
}
