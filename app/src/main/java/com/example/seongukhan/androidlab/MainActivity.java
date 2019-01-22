package com.example.seongukhan.androidlab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        btn=findViewById(R.id.btn);
        editText=findViewById(R.id.edit);

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String data=editText.getText().toString();
        Log.d("editText",data);
    }
}
