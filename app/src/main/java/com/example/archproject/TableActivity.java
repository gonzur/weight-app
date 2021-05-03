package com.example.archproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.hardware.camera2.params.MandatoryStreamCombination;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TableActivity extends AppCompatActivity implements LifecycleOwner {
    private Button todaysWeightButton;
    private Button targetWeightButton;
    private TextView todaysWeightText;
    private TextView targetWeightText;
    private RecyclerView weightList;
    private ArrayList<WeightEntry> weightListData = new ArrayList<>();
    private int notificationId = 0;
    private NotificationManager manager;
    private LifecycleOwner owner = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        todaysWeightButton = findViewById(R.id.todaysWeightBtn);
        targetWeightButton = findViewById(R.id.targetWeightBtn);
        todaysWeightText = findViewById(R.id.todaysWeightText);
        targetWeightText = findViewById(R.id.targetWeightText);
        weightList = findViewById(R.id.weightList);
        weightList.setAdapter(new MyWeightRecyclerViewAdapter(weightListData));

        requestPermissions(new String[] {Manifest.permission.SEND_SMS},0);

        SessionSingleton.getDb().weightEntryDAO().getGoal(SessionSingleton.getUser().username).observe(owner, (WeightEntry weightEntry) -> {
            try {
                targetWeightText.setText(String.format("%.2f", weightEntry.weight));
            } catch (Exception e) {
                return;
            }
        });

        SessionSingleton.getDb().weightEntryDAO().getAll(SessionSingleton.getUser().username).observe(owner,(List<WeightEntry> weightEntries) -> {
            weightListData.addAll(weightEntries);
            weightList.getAdapter().notifyItemRangeChanged(0, weightListData.size());
        });

        todaysWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(todaysWeightText.getText().length() > 0) {
                       double newWeight = Double.parseDouble(todaysWeightText.getText().toString());
                       User user = SessionSingleton.getUser();
                       WeightEntry todaysEntry = new WeightEntry();
                       todaysEntry.associatedUser = user.username;
                       todaysEntry.weight = newWeight;
                       todaysEntry.date = Calendar.getInstance().getTime().toString();

                       weightListData.add(0,todaysEntry);
                       weightList.getAdapter().notifyItemChanged(0);

                       SessionSingleton.getDb().weightEntryDAO().insertAll(todaysEntry).subscribeOn(Schedulers.computation()).subscribe();

                       SessionSingleton.getDb().weightEntryDAO().getGoal(user.username).observe(owner, (WeightEntry goal) -> {
                           if (goal != null && goal.weight >= todaysEntry.weight) {
                               Log.e("targetWeightButtonError", "current "+todaysEntry.weight+" goal "+goal.weight+" "+todaysWeightText.getText());
                               sendNotification();
                           }
                       });
                    }

                } catch (Exception e ) {
                    Log.e("expceptiontodaysweight", e.getMessage());
                    return;
                }
            }
        });

        targetWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (targetWeightText.getText().length() > 0) {
                        double newWeight = Double.parseDouble(targetWeightText.getText().toString());
                        User user = SessionSingleton.getUser();
                        WeightEntry targetEntry = new WeightEntry();
                        targetEntry.associatedUser = user.username;
                        targetEntry.weight = newWeight;
                        targetEntry.goal = true;
                        targetWeightText.setText(String.format("%.2f",targetEntry.weight));

                        SessionSingleton.getDb().weightEntryDAO().insertAll(targetEntry).subscribeOn(Schedulers.computation()).subscribe();
                        SessionSingleton.getDb().weightEntryDAO().getLatestEntry(user.username).observe(owner, (WeightEntry weightEntry) -> {
                            if(weightEntry != null && weightEntry.weight <= targetEntry.weight) {
                                sendNotification();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
//

    }

    private void sendNotification() {
        boolean sendSMS = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
        if (false && sendSMS) {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("sms_body", "Congratulations you have reached your goal of " + this.targetWeightText.getText().toString() + " lbs");
            sendIntent.setType("vnd.android-dir/mms-sms");
            startActivity(sendIntent);
        } else {
            if (manager == null)
                manager = createNotificationChannel();
            NotificationCompat.Builder builder = this.BuildNotification();
            manager.notify(this.notificationId, builder.build());
        }
    }

    private NotificationManager createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "backup";
        String description = "if user refuses sms";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("backup", name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        return notificationManager;
    }

    private NotificationCompat.Builder BuildNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "backup")
                .setContentTitle("Weight Goal Achieved")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Congratulations you have reached your goal of " + this.targetWeightText.getText().toString() + " lbs"))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentText("Congratulations you have reached your goal of " + this.targetWeightText.getText().toString() + " lbs");

        return builder;

    }


}