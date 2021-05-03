package com.example.archproject;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weight_entry")
public class WeightEntry {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "weight")
    public double weight;
    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "associated_user")
    public String associatedUser;
    public boolean goal;
}
