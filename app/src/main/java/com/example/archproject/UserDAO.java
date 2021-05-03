package com.example.archproject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.reactivestreams.Publisher;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;


@Dao
public interface UserDAO {
    @Query("SELECT * FROM user WHERE username LIKE :user LIMIT 1")
    public LiveData<User> getUser(String user);
    @Insert
    public Completable insertUser(User user);

}
