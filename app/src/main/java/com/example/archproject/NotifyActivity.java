package com.example.archproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NotifyActivity extends AppCompatActivity {
    private Button updateWeights;
    private Button smsButton;
    private TextView smsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        updateWeights = findViewById(R.id.updateWeightsBtn);
        smsButton = findViewById(R.id.smsToggleBtn);
        smsText = findViewById(R.id.SMStext);
        updateWeights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateWeights();
            }
        });

        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (smsText.getText().toString().equals("SMS Notification: off")) {
                    smsText.setText("SMS Notification: on");
                } else {
                    smsText.setText("SMS Notification: off");
                }
            }
        });
    }
    public void updateWeights() {
        Intent table = new Intent(this, TableActivity.class);
        startActivity(table);
    }
}