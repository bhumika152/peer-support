package com.example.peerhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class Achievement extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_achievement, container, false);
    }
    private String organization;
    private String achievement;
    private String date;
    private String url;

    public Achievement() {
        // Default constructor required for calls to DataSnapshot.getValue(Achievement.class)
    }
       public Achievement(String organization, String achievement, String date, String url) {
        this.organization = organization;
        this.achievement = achievement;
        this.date = date;
        this.url = url;
    }

    public String getOrganization () {
        return organization;
    }

    public void setOrganization (String organization) {
        this.organization = organization;
    }

    public String getAchievement () {
        return achievement;
    }

    public void setAchievement (String achievement) {
        this.achievement = achievement;
    }

    public String getDate () {
        return date;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public String getUrl () {
        return url;
    }

    public void setUrl (String url) {
        this.url = url;
    }

}
