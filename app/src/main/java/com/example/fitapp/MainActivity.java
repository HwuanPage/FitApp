//시작일03-02-2020
//최종수정자:황성철
//최종수정일:03-05-2020

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

public class MainActivity extends AppCompatActivity {

    public static final int NOTIFICATION_ID = 1;                 //알람추가를 위한 상수
    int stepCount = 0;                                              //걸음수
    int hour = 0, min = 0;
    Calendar mCalendar=Calendar.getInstance();
    public final String Alarm = hour + ":" + min;
    TimePickerDialog timePickerDialog;
    BroadcastReceiver br;

    Button btn_set_push;                                        //알람시간 설정 버튼
    TextView txtTime, txtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);


        btn_set_push = (Button) findViewById(R.id.btnsetpush);
        txtCount = (TextView) findViewById(R.id.txtstepcount);
        txtTime = (TextView) findViewById(R.id.txttime);

        createNotificationChannel();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsetpush:           //시간설정
                timeSet();
                break;
            case R.id.btnnotitest:          //푸시알람 테스트
                sndNoti(v);
                break;
        }
    }
    //=======================================Notification============================================//
    public void sndNoti(View v) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"@string/notich_id");         //푸시알림 생성
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)                                                            //아이콘,글씨 설정
                .setContentTitle("StepCount:" + stepCount + " / time set: " + hour + " : " + min)
                .setContentText("시험 푸시입니다.");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)                    //Oreo 이상
        {
            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("notich_id","@string/notich_id",NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    private void timeSet()
    {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                txtTime.setText("Time   " + hourOfDay + ":" + minute);
                mCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                mCalendar.set(Calendar.MINUTE,minute);
                mCalendar.set(Calendar.SECOND,0);
                Log.i("timepicker", "time " + hour + " : " + min);
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}
