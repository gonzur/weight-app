package com.example.archproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, WeightEntry.class}, version = 100, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDAO();
    public abstract WeightEntryDAO weightEntryDAO();
}
