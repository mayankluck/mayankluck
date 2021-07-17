package com.example.useradvent;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Useradvent extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
