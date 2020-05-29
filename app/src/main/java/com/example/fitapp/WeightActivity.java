package com.example.fitapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WeightActivity extends AppCompatActivity {

    Button SetBtn;
    static final int SET_WEIGHT=1;
    TextView Weight,Muscle,Fat,BMR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        SetBtn=findViewById(R.id.SetBtn);
        Muscle=findViewById(R.id.MuscleText);
        Weight=findViewById(R.id.WeightText);
        Fat=findViewById(R.id.FatText);
        BMR=findViewById(R.id.BMRText);

        SetBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),SetWeightActivity.class);
                startActivityForResult(intent,SET_WEIGHT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==SET_WEIGHT){
            if(resultCode==RESULT_OK){

            }
        }
    }
}