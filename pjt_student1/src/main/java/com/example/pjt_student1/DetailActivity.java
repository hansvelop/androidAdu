package com.example.pjt_student1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    ImageView studentImageView;
    TextView nameView;
    TextView phoneView;
    TextView emailView;
    TabHost host;

    int studentId = 2;

    TextView addScoreView;
    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn0,btnBack,btnAdd;

    MyView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initData();
        initTab();
        initAddScore();
        initSpannable();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initData(){
        studentImageView = findViewById(R.id.detail_student_image);
        nameView = findViewById(R.id.detail_name);
        phoneView = findViewById(R.id.detail_phone);
        emailView = findViewById(R.id.detail_email);
        scoreView = findViewById(R.id.detail_score);

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from tb_student order by _id desc",new String[]{});
        Cursor cursor = db.rawQuery("select * from tb_student where _id = ? order by _id desc", new String[]{String.valueOf(studentId)});

        String photo = null;
        if(cursor.moveToFirst()){
            nameView.setText(cursor.getString(1));
            emailView.setText(cursor.getString(2));
            phoneView.setText(cursor.getString(3));
            photo = cursor.getString(4);

            Cursor scoreCursor = db.rawQuery("select score from tb_score " +
                    "where student_id = ? order by date desc", new String[]{String.valueOf(studentId)});
            int score = 0;
            if(scoreCursor.moveToFirst()){
                score = scoreCursor.getInt(0);
            }
            scoreView.setScore(score);
        }
        db.close();

    }

    private void initTab(){
        host=findViewById(R.id.host);

        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("tab1");
        spec.setIndicator("score");
        spec.setContent(R.id.detail_score_list);
        host.addTab(spec);

        spec = host.newTabSpec("tab2");
        spec.setIndicator("chart");
        spec.setContent(R.id.detail_score_chart);
        host.addTab(spec);

        spec = host.newTabSpec("tab3");
        spec.setIndicator("add");
        spec.setContent(R.id.detail_score_add);
        host.addTab(spec);

        spec = host.newTabSpec("tab4");
        spec.setIndicator("memo");
        spec.setContent(R.id.detail_score_memo);
        host.addTab(spec);
    }

    private void initAddScore() {
        btn0 = (Button) findViewById(R.id.key_0);
        btn1 = (Button) findViewById(R.id.key_1);
        btn2 = (Button) findViewById(R.id.key_2);
        btn3 = (Button) findViewById(R.id.key_3);
        btn4 = (Button) findViewById(R.id.key_4);
        btn5 = (Button) findViewById(R.id.key_5);
        btn6 = (Button) findViewById(R.id.key_6);
        btn7 = (Button) findViewById(R.id.key_7);
        btn8 = (Button) findViewById(R.id.key_8);
        btn9 = (Button) findViewById(R.id.key_9);
        btnBack = (Button) findViewById(R.id.key_back);
        btnAdd = (Button) findViewById(R.id.key_add);

        addScoreView = (TextView) findViewById(R.id.key_edit);

        btn0.setOnClickListener(addScoreListener);
        btn1.setOnClickListener(addScoreListener);
        btn2.setOnClickListener(addScoreListener);
        btn3.setOnClickListener(addScoreListener);
        btn4.setOnClickListener(addScoreListener);
        btn5.setOnClickListener(addScoreListener);
        btn6.setOnClickListener(addScoreListener);
        btn7.setOnClickListener(addScoreListener);
        btn8.setOnClickListener(addScoreListener);
        btn9.setOnClickListener(addScoreListener);
        btnBack.setOnClickListener(addScoreListener);
        btnAdd.setOnClickListener(addScoreListener);

    }

    View.OnClickListener addScoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == btnAdd){
                long date = System.currentTimeMillis();
                String score = addScoreView.getText().toString();

                DBHelper helper = new DBHelper(DetailActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("insert into tb_score (student_id, date, score) " +
                        "values (?,?,?)", new String[]{String.valueOf(studentId), String.valueOf(date), score});

                db.close();

                host.setCurrentTab(0);
                addScoreView.setText("0");
                scoreView.setScore(Integer.parseInt(score));
            }else if(v == btnBack){
                String score = addScoreView.getText().toString();
                if(score.length() == 1){
                    addScoreView.setText("0");
                }else{
                    addScoreView.setText(score.substring(0, score.length()-1));
                }
            }else{
                Button btn = (Button)v;
                String txt = btn.getText().toString();
                String score = addScoreView.getText().toString();
                if(score.equals("0")){
                    addScoreView.setText(txt);
                }else{
                    String newScore = score+txt;
                    int intScore = Integer.parseInt(newScore);
                    if(intScore > 100){
                        Toast toast = Toast.makeText(DetailActivity.this, R.string.read_add_score_over_score, Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                        addScoreView.setText(newScore);
                    }
                }
            }
        }
    };

    private void initSpannable(){
        TextView spanView = findViewById(R.id.spanView);
        TextView htmlView = findViewById(R.id.htmlView);

        URLSpan urlSpan = new URLSpan(""){
            @Override
            public void onClick(View widget) {
                Toast toast = Toast.makeText(DetailActivity.this, "more click", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        String data = spanView.getText().toString();
        Spannable spannable = (Spannable)spanView.getText();

        int pos = data.indexOf("EXID");
        while(pos > -1){
            spannable.setSpan(new ForegroundColorSpan(Color.DKGRAY), pos, pos+4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            pos = data.indexOf("EXID", pos+1);
        }
        pos = data.indexOf("more");
        spannable.setSpan(urlSpan, pos, pos+4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanView.setMovementMethod(LinkMovementMethod.getInstance());

        String html ="<font color='blue'> HANI</fond><br /><img src='myImage'/>";

        htmlView.setText(Html.fromHtml(html, new MyImageGetter(), null));
    }

    class MyImageGetter implements Html.ImageGetter{
        @Override
        public Drawable getDrawable(String source) {
            if(source.equals("myImage")){
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.hani_1, null);
                drawable.setBounds(0,0,drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            }
            return null;
        }
    }
}
