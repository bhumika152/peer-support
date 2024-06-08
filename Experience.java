package com.example.peerhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class Experience extends Fragment {

    public Experience () {

    }

    public Experience(String designation, String company, String location, String fromDate, String toDate) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_experience, container, false);
    }
    private String designation;
    private String company;
    private String location;
    private String fromDate;
    private String toDate;
    private boolean studyingCurrently;
    private String username;
        public Experience(String designation, String company, String location, String fromDate, String toDate, boolean studyingCurrently, String username) {
        this.designation = designation;
        this.company = company;
        this.location = location;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.studyingCurrently = studyingCurrently;
        this.username = username;
    }

    // Getters and setters
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public boolean isStudyingCurrently() {
        return studyingCurrently;
    }

    public void setStudyingCurrently(boolean studyingCurrently) {
        this.studyingCurrently = studyingCurrently;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
