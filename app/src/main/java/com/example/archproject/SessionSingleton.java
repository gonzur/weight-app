package com.example.archproject;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

public  class SessionSingleton {
    private static SessionSingleton sessionSingleton;
    private static AppDatabase db;
    private static User user;

    private SessionSingleton() {}

    public static SessionSingleton getInstance(Context context) {
        if( sessionSingleton == null) {
            sessionSingleton = new SessionSingleton();
            db = Room.databaseBuilder(context,AppDatabase.class, "backup").build();
        }
        return sessionSingleton;
    }
    public static void setUser(User newUser) {
        user = newUser;
    }
    public static User getUser() {
        return user;
    }
    public static AppDatabase getDb() {
        return db;
    }
}
