package com.example.peerhub;

public class Comment {
    private String userId;
    private String username;
    private String content;

    public Comment() {
        // Default constructor required for Firebase
    }

    public Comment(String userId, String username, String content) {
        this.userId = userId;
        this.username = username;
        this.content = content;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
