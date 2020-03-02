//시작일03-02-2020
//최종수정자:황성철
//최종수정일:03-02-2020

package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final int NOTIFICATION_ID =1;                 //알람추가를 위한 상수

    TimePickerDialog timePickerDialog;

    Button btn_set_push;                                        //알람시간 설정 버튼
    TextView txtTime,txtCount;
    int stepCount=0;                                              //걸음수
    int hour=0,min=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_set_push=(Button)findViewById(R.id.btnsetpush);
        txtCount=(TextView)findViewById(R.id.txtstepcount);
        txtTime=(TextView)findViewById(R.id.txttime);
    }
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.btnsetpush:
                final Calendar c = Calendar.getInstance();
                int mHour =c.get(Calendar.HOUR_OF_DAY);
                int mMinute =c.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        txtTime.setText("Time   "+hourOfDay+":"+minute);
                        hour=hourOfDay;
                        min=minute;
                    }
                },mHour,mMinute,false);
                timePickerDialog.show();
                break;
            case R.id.btnnotitest:
                NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
                builder.setSmallIcon(R.drawable.ic_launcher_foreground);
                builder.setContentTitle("StepCount:"+stepCount);

                NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID,builder.build());
                break;
        }
    }
}
