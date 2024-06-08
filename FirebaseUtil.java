package com.example.peerhub.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn(){
        return currentUserId() != null;
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("Users").document(currentUserId());
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("Users");
    }

    public static DocumentReference getChatroomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatroomMessageReference(String chatroomId){
        return getChatroomReference(chatroomId).collection("chats");
    }

    public static String getChatroomId(String userId1, String userId2){
        if (userId1 != null && userId2 != null) {
            if (userId1.hashCode() < userId2.hashCode()) {
                return userId1 + "_" + userId2;
            } else {
                return userId2 + "_" + userId1;
            }
        } else {
            // Handle the case where userId1 or userId2 is null
            return null;
        }
    }

    public static CollectionReference allChatroomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherUserFromChatroom(List<String> userIds){
        if(userIds != null && userIds.size() >= 2) {
            String otherUserId = userIds.get(0).equals(currentUserId()) ? userIds.get(1) : userIds.get(0);
            return allUserCollectionReference().document(otherUserId);
        } else {
            return null;
        }
    }

    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference getCurrentProfilePicStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(currentUserId());
    }

    public static StorageReference getOtherProfilePicStorageRef(String otherUserId){
        if (otherUserId != null && !otherUserId.isEmpty()) {
            return FirebaseStorage.getInstance().getReference().child("profile_pic")
                    .child(otherUserId);
        } else {
            // Handle the case where otherUserId is null or empty
            return null;
        }
    }
}
