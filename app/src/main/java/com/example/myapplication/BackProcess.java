package com.example.myapplication;

import android.app.Application;
import android.content.Intent;
import android.os.Build;


public class BackProcess extends Application {
    public static BackProcess instance;
    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
  //          startForegroundService(new Intent(this,AlarmService.class));// startService(new Intent(this,AlarmService.class));
        }
        instance = this;
        super.onCreate();
    }

}
