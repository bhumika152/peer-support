package com.example.peerhub;

public class UserManager {
    private String username;
    private String name;

    private static final UserManager instance = new UserManager();

    private UserManager() {
        // Private constructor to prevent instantiation
    }

    public static UserManager getInstance() {
        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

