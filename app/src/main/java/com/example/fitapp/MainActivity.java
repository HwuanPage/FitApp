//시작일03-02-2020
//최종수정자:황성철
//최종수정일:03-07-2020

package com.example.fitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static final int NOTIFICATION_ID= 1;                 //알람추가를 위한 상수

    int stepCount = 0;                                              //걸음수
    int hour = 0, min = 0;


    Calendar mCalendar = Calendar.getInstance();
    TimePickerDialog timePickerDialog;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    NotificationManager notificationManager;

    private SensorManager sensorManager;
    private Sensor stepCountSensor;

    Button btn_set_push;                                        //알람시간 설정 버튼
    TextView txtTime, txtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_set_push = (Button) findViewById(R.id.btnsetpush);
        txtCount = (TextView) findViewById(R.id.txtstepcount);
        txtTime = (TextView) findViewById(R.id.txttime);

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel();
        setStep();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsetpush:           //시간설정
                timeSet();
                break;
            case R.id.btnalarmset:          //푸시알람 테스트
                alarmSet();
                break;
        }
    }

    //=======================================Notification============================================//
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)                    //Oreo 이상
        {
            NotificationChannel notificationChannel = new NotificationChannel("notich_id", "FitApp", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
            Log.i("mainact","createNotiChannel");
        }
    }
    public void sndPush(View v,String title,String text) {
        Log.i("mainact","sndPush");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notich_id");         //푸시알림 생성
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)                                                            //아이콘,글씨 설정
                .setContentTitle(title)
                .setContentText(text);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    //=======================================TimeSetting==========================================//
    private void timeSet() {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.i("mainact","timeSet");
                txtTime.setText("Time   " + hourOfDay + ":" + minute);
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                mCalendar.set(Calendar.SECOND, 0);
                hour = hourOfDay;
                min = minute;
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }
    public void alarmSet()
    {

        Toast.makeText(this,hour+"시"+min+"분으로 알람이 설정되었습니다.",Toast.LENGTH_SHORT).show();
    }
    //=======================================StepCount==============================================//
    public void setStep() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepCountSensor == null) {
            Toast.makeText(this, "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0f) {
                stepCount++;
                txtCount.setText("Step Detect : " + String.valueOf(stepCount));
            }
        }
    }
        @Override
        public void onAccuracyChanged (Sensor sensor,int accuracy){

        }
        @Override
        protected void onResume () {
            super.onResume();
            sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
