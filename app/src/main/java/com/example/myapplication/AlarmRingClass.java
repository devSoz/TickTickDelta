package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmRingClass extends BroadcastReceiver
{

    public static int notifyID = 100;

    public static String CHANNEL_ID = "Tick-Tick";// The id of the channel.
    public static String name = "Alarms", type, desc,uid;
    Ringtone ringtone;

    @Override
    public void onReceive(Context context, Intent intent)
    {
       // type = intent.getStringExtra("type");
        desc = intent.getStringExtra("desc");
       // uid =intent.getStringExtra("uid");

        Log.i("alarm", "notifi");
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notifyID);

        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context)
                    .setContentTitle("AlarmApp")
                    .setContentText("Reminder for " + desc +" !!!")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setChannelId(CHANNEL_ID)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .build();
        } else {
            notification = new Notification.Builder(context)
                    .setContentTitle("AlarmApp")
                    .setContentText("" + desc +" !!!")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setAutoCancel(true)
                    .build();
        }

        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1023, notification);


            Log.d("Ok alarm", "inside alarmring");
            Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
            vibrator.vibrate(4000);

            Toast.makeText(context, desc + "!!!!!", Toast.LENGTH_LONG).show();
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }


            ringtone = RingtoneManager.getRingtone(context, alarmUri);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ringtone.stop();
                }
            }, 1000 * 5);

            ringtone.play();
        }
}
