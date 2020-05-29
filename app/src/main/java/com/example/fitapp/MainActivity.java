package com.example.fitapp;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int NOTIFICATION_ID= 1;
    private NotificationManager notificationManager;
    private long backKeyPressedTime = 0;
    Toast toast;

    private background stepService;
    boolean isService = false;
    private TextView textCount, statusService;
    private Button startBtn, endBtn,notiBtn,WorkoutBtn,DietBtn,WeightBtn;
    private Intent intent;
    private float weight;

    private StepCallback stepCallback = new StepCallback() {        //백그라운드
        @Override
        public void onStepCallback(int step) {
            textCount.setText("" + step);
        }

        @Override
        public void onUnbindService() {                             //센서해제
            isService = false;
            statusService.setText("해제됨");
            Toast.makeText(MainActivity.this, "디스바인딩", Toast.LENGTH_SHORT).show();
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(MainActivity.this, "예스바인딩", Toast.LENGTH_SHORT).show();
            background.MyBinder mb = (background.MyBinder) service;
            stepService = mb.getService();
            stepService.setCallback(stepCallback);
            isService = true;
            statusService.setText("연결됨");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {


            isService = false;
            statusService.setText("해제됨");
            Toast.makeText(MainActivity.this, "디스바인딩", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        stepService = new background();
        startBtn = findViewById(R.id.startBtn);
        endBtn = findViewById(R.id.endBtn);
        notiBtn = findViewById(R.id.notiBtn);
        textCount = findViewById(R.id.textCount);
        statusService = findViewById(R.id.textStatusService);
        WeightBtn=findViewById(R.id.WeightBtn);
        WorkoutBtn=findViewById(R.id.WorkBtn);
        DietBtn=findViewById(R.id.DietBtn);


        setListener();
        createNotificationChannel();

    }
    public void setListener() {
        startBtn.setOnClickListener(this);
        endBtn.setOnClickListener(this);
        WeightBtn.setOnClickListener(this);
        WorkoutBtn.setOnClickListener(this);
        DietBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.startBtn:
                intent = new Intent(this, background.class);
                startService(intent);
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.endBtn:
                try {
                    stopService(intent);
                    unbindService(serviceConnection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.notiBtn:
                sndPush("FitApp","Count:");
                break;
            case R.id.WorkBtn:
                intent=new Intent(this,WorkoutActivity.class);
                startActivity(intent);
                break;
            case R.id.DietBtn:
                intent=new Intent(this,DietActivity.class);
                startActivity(intent);
                break;
            case R.id.WeightBtn:
                intent=new Intent(this,WeightActivity.class);
                startActivity(intent);
                break;
        }
    }
    /*@Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }*/
    //=======================================Notification============================================//
    public void createNotificationChannel() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)                    //Oreo 이상
        {
            NotificationChannel notificationChannel = new NotificationChannel("Fit", "FitApp", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
            Log.i("mainact","createNotiChannel");
        }
    }
    public void sndPush(String title,String text) {
        Log.i("mainact","sndPush");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Fit");         //푸시알림 생성
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)                                                            //아이콘,글씨 설정
                .setContentTitle(title)
                .setContentText(text);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 3000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast=Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 3000) {
            finish();
            toast.cancel();
        }
    }
}