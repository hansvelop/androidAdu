package com.example.pjt_student;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    EditText nameView;
    EditText emailView;
    EditText phoneView;
    EditText memoView;
    Button addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        nameView=findViewById(R.id.add_name);
        emailView=findViewById(R.id.add_email);
        phoneView=findViewById(R.id.add_phone);
        memoView=findViewById(R.id.add_memo);
        addBtn=findViewById(R.id.add_btn);

        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name=nameView.getText().toString();
        String email=emailView.getText().toString();
        String phone=phoneView.getText().toString();
        String memo=memoView.getText().toString();

        if(name == null || name.equals("")){
            Toast toast=Toast.makeText(this, R.string.add_name_null,
                    Toast.LENGTH_SHORT);
            toast.show();
        }else {
            DBHelper helper=new DBHelper(this);
            SQLiteDatabase db=helper.getWritableDatabase();
            db.execSQL("insert into tb_student (name, email, phone, memo) " +
                    "values (?,?,?,?)",
                    new String[]{name, email, phone, memo});
            db.close();

            //화면은????
            //MainActivity로 자동 화면 전환..
            //Intent를 사용하지 않고.. 이 activity를 종료시켜서.. 자동으로 main으로
            finish();

        }
    }
}
