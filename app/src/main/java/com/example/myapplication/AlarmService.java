package com.example.myapplication;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.net.Inet4Address;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import static com.example.myapplication.AlarmRingClass.notifyID;
import static com.example.myapplication.MainActivity.CHANNEL_ID;

public class AlarmService extends Service
{

    public static final String ANDROID_CHANNEL_NAME = "Background Service";
    public static final String ANDROID_CHANNEL_ID = "Tick-Tick";
    private Notification.Builder nBuiler;
    private CountDownTimer countDownTimer;
    NotificationManager notificationManager;
    Notification notification;
    String controlt;
    long count = 0;
    AlarmManager alarmManager;

    private final static String TAG = "AlarmService";

    public static final String COUNTDOWN_BR = "com.example.myapplication.countdown_br";
    public static final String COUNTDOWN_C = "com.example.myapplication.Cancel";
    Intent bi = new Intent(COUNTDOWN_BR);
    Intent iCancel = new Intent(COUNTDOWN_C);

    CountDownTimer cdt = null;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //startForeground();
        long time = intent.getLongExtra("time",0);
        controlt = intent.getStringExtra("control");
        startChannel();
        startnew(time);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notifyID);

        count = time;
        //count = System.currentTimeMillis();
        sendNotification(count);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        cdt.cancel();
        notificationManager.cancel(1023);

        super.onDestroy();
        Log.i(TAG, "Timer cancelled");
    }

    private void startChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                    ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            androidChannel.enableLights(false);
            androidChannel.enableVibration(false);
            androidChannel.setLightColor(Color.BLUE);
            //androidChannel.setImportance(NotificationManager.IMPORTANCE_NONE);
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(androidChannel);


        }
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void sendNotification(long count) {

       // iCancel.putExtra("action", "cancel");
        PendingIntent pendingIntentTemp = PendingIntent.getBroadcast(this, 10, iCancel, PendingIntent.FLAG_UPDATE_CURRENT);
        nBuiler = new Notification.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = nBuiler
                    .setContentTitle("Tick-Tick")
                    //.setWhen(count*1000) //- count)
                   // .setOngoing(true)
                    //.setTicker()
                    //.setUsesChronometer(true)
                    .addAction(R.drawable.cancel, "Cancel", pendingIntentTemp)
                    .setOnlyAlertOnce(true)
                    //.setChronometerCountDown(true)
                    .setContentText("")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setChannelId(CHANNEL_ID)
                    //.setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .build();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                notification = nBuiler
                        .setContentTitle("Tick-Tick")
                        //.setWhen(count*1000) //- count)
                        //.setOngoing(true)
                        //.setUsesChronometer(true)
                        .setOnlyAlertOnce(true)
                        //.setChronometerCountDown(true)
                        .setContentText(" !!!")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setAutoCancel(true)
                        .build();
            }
        }
        notificationManager.notify(1023, notification);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Starting timer...");


    }
    void startnew(long time)
    {
        cdt = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                bi.putExtra("countdown", millisUntilFinished);
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                nBuiler.setContentText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));//String.valueOf(count));
                notificationManager.notify(1023, nBuiler.build());
                count-=1000;

                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                    notificationManager.cancel(1023);
                Log.i(TAG, "Timer finished");
            }
        };

        cdt.start();
    }
}
