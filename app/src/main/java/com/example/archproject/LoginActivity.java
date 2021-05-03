package com.example.archproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity implements LifecycleOwner {
    private EditText username;
    private EditText password;
    private Button register;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        LifecycleOwner obj = this;


        SessionSingleton.getInstance(getApplicationContext());
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDAO users = SessionSingleton.getInstance(null).getDb().userDAO();
                User user = new User();
                user.username = username.getText().toString();
                user.password = password.getText().toString();
                users.getUser(username.getText().toString()).observe(obj,(User userDB)->{

                    if (userDB == null && (user.password.length() !=0 || user.username.length() != 0)) {
                        users.insertUser(user).subscribeOn(Schedulers.computation()).subscribe();
                        SessionSingleton.setUser(user);
                        successfulLogin();
                    } else {
                        username.setError("Invalid");
                        password.setError("Invalid");
                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDAO users = SessionSingleton.getInstance(null).getDb().userDAO();
                users.getUser(username.getText().toString()).observe(obj, (User user) -> {
                    if(user != null && password.getText().toString().equals(user.password)){
                        SessionSingleton.setUser(user);
                        successfulLogin();
                    } else {
                        username.setError("Invalid");
                        password.setError("Invalid");
                    }
                });
            }
        });
    }
    public void successfulLogin() {
        Intent table = new Intent(this, TableActivity.class);
        startActivity(table);
    }


}