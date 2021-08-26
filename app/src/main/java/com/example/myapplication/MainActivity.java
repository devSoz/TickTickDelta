package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

public class MainActivity  extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TimePicker timePicker;
    private EditText txtName;
    private TextView txtdate1;
    private DatePicker datePicker;
    private Button btnDate, btnSet, btnChoose;
    private ImageButton btnCancel, btnPause, btnRewind, btnForward;
    private PendingIntent pendingIntent, pendingIntent2;
    public static final String CHANNEL_ID = "Tick-Tick";
    private Integer flagNotif = 0, flagNewTimer = 0, flagFirst = 1, flagPause = 0;
    private AlarmManager alarmManager;
    private long time=0;
    private CountDownTimer countDownTimer;
    private TextView textView;
    private String TAG="MainAlarm";
    private Spinner spinner1, spinner2, spinner3;
    private Integer[] option1=new Integer[61];
    private Integer[] option2=new Integer[61];
    private Integer[] option3=new Integer[61];
    Intent intent1;
    Context context;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //createChannel();
        //  picker=(DatePicker)findViewById(R.id.datePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        context= this;
        textView = findViewById(R.id.txtTimer);
        //setListener();

        for (int i = 0; i <= 60; i++)
        {
            option1[i] = i;
            option2[i] = i;
            option3[i] = i;
        }
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        btnSet = (Button) findViewById(R.id.btnToggle);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnPause = (ImageButton) findViewById(R.id.btnPause);
        btnCancel = (ImageButton) findViewById(R.id.btnCancel);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnRewind = (ImageButton) findViewById(R.id.btnRewind);

        ArrayAdapter<Integer> a1 = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, option1);
        a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(a1);

        ArrayAdapter<Integer> a2 = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, option2);
        a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(a2);

        ArrayAdapter<Integer> a3 = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, option3);
        a3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(a3);

        spinner1.setMinimumHeight(200);
        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        spinner3.setOnItemSelectedListener(this);

        setListener();
        listeners();


}

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id )
    {
        Spinner spin1 = (Spinner)parent;
        Spinner spin2 = (Spinner)parent;

                if(spin1.getId() == R.id.spinner1)
                {
                Toast.makeText(this, "Your choose :" + option1[position],Toast.LENGTH_SHORT).show();
            }
                if(spin2.getId() == R.id.spinner2)
    {
        Toast.makeText(this, "Your choose :" + option1[position],Toast.LENGTH_SHORT).show();
    }}

    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "choose :", Toast.LENGTH_SHORT).show();
    }

    public void setListener()
    {
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                flagNewTimer = 1;
                spinner1.setVisibility(View.VISIBLE);
                spinner2.setVisibility(View.VISIBLE);
                spinner3.setVisibility(View.VISIBLE);
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

               /* if(flagFirst==0)
                {   countDownTimer.cancel();    }*/
                    flagFirst = 0;
                    spinner1.setVisibility(View.INVISIBLE);
                    spinner2.setVisibility(View.INVISIBLE);
                    spinner3.setVisibility(View.INVISIBLE);

                    //setAlarm(spinner1.getSelectedItem(), spinner2.getSelectedItem(), spinner3.getSelectedItem());
                    Integer hours = (Integer) spinner1.getSelectedItem();
                    Integer mins = (Integer) spinner2.getSelectedItem();
                    Integer secs = (Integer) spinner3.getSelectedItem();

                    spinner1.setSelection((Integer) spinner1.getSelectedItem());
                    spinner2.setSelection((Integer) spinner2.getSelectedItem());
                    spinner3.setSelection((Integer) spinner3.getSelectedItem());

                    time = (hours * 60 * 60 + mins * 60 + secs) * 1000;
                    if (time != 0) {

                    btnPause.setImageResource(R.drawable.resume);
                    intent1 = new Intent(MainActivity.this, AlarmService.class);
                    StartService(intent1);

                }
            }
        });
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTimer(intent);
        }
    };

    private BroadcastReceiver br1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            textView.setText("00:00:00");
            StopService(intent1);
        }
    };

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(br, new IntentFilter(AlarmService.COUNTDOWN_BR));
        registerReceiver(br1, new IntentFilter(AlarmService.COUNTDOWN_C));
        Log.i(TAG, " broacast receiver");
    }


    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
        Log.i(TAG, "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {

        }
        super.onStop();
    }
    @Override
    public void onDestroy() {
       // StopService(new Intent(this, AlarmService.class));
        StopService(intent1);
        Log.i(TAG, "Stopped service");

        super.onDestroy();
    }

    private void updateTimer(Intent intent)
    {
        if (intent.getExtras() != null) {
            time = intent.getLongExtra("countdown", 0);
           // int test = (int) (millisUntilFinished );

            NumberFormat f = new DecimalFormat("00");
            long hour = (time / 3600000) % 24;
            long min = (time / 60000) % 60;
            long sec = time/1000 ;
            textView.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));

            Log.i(TAG, "Countdown seconds remaining: " + time / 1000);
        }}


        public void StartService (  Intent intent)
        {

            intent.putExtra("time", time);
            if(flagPause==1)
                intent.putExtra("control", "r");
            else
                intent.putExtra("control", "s");

            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           //     startForegroundService(intent);
            //}
            startService(intent);
        }
        public void StopService (Intent intent)
        {
            if(flagPause==0)
                intent.putExtra("control", "p");
            else
                intent.putExtra("control", "c");
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              //  startForegroundService(intent);
            //}

            stopService(intent);
            Log.i(TAG, "Timer cancelled");
        }

        public void listeners()
        {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    time = 0;
                    textView.setText("00:00:00");
                    StopService(intent1);
                }
            });

            btnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(flagPause==0) {
                        StopService(intent1);
                        flagPause = 1;
                        btnPause.setImageResource(R.drawable.pause);
                    }
                    else
                    {
                        StartService(intent1);
                        flagPause = 0;
                        btnPause.setImageResource(R.drawable.resume);
                    }
                }
            });

            btnRewind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StopService(intent1);
                    time = time-10000;
                    StartService(intent1);
                }
            });

            btnForward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StopService(intent1);
                    time = time+10000;
                    StartService(intent1);
                }
            });
        }
    }

