package com.example.pjt_student1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button testBtn;
    ImageView addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testBtn = findViewById(R.id.main_test_btn);
        addBtn = findViewById(R.id.main_btn);

        testBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == addBtn){
            Intent intent= new Intent(this, AddActivity.class);
            startActivity(intent);
        }else if(v==testBtn){
            Intent intent= new Intent(this, DetailActivity.class);
            startActivity(intent);

        }
    }
}
