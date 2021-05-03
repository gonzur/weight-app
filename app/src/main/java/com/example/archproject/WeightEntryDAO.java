package com.example.archproject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface WeightEntryDAO {
    @Insert
    Completable insertAll(WeightEntry... weightEntry);

    @Query("SELECT * FROM weight_entry WHERE associated_user LIKE :associated_user AND goal = 0 ORDER BY ID DESC")
    LiveData<List<WeightEntry>> getAll(String associated_user);

    @Query("SELECT * FROM weight_entry WHERE associated_user LIKE :associated_user AND goal = 1 ORDER BY id DESC LIMIT 1")
    LiveData<WeightEntry> getGoal(String associated_user);

    @Query("SELECT * FROM weight_entry WHERE associated_user LIKE :associated_user AND goal = 0 ORDER BY id DESC LIMIT 1")
    LiveData<WeightEntry> getLatestEntry(String associated_user);
}
