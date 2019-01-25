package com.example.pjt_student1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

//두번째 tab 화면을 여는순간 html  로딩 하려고..
public class DetailActivity extends AppCompatActivity implements TabHost.OnTabChangeListener{

    ImageView studentImageView;
    TextView nameView;
    TextView phoneView;
    TextView emailView;
    TabHost host;

    int studentId=1;

    TextView addScoreView;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0,
            btnBack, btnAdd;

    MyView scoreView;

    ListView listView;
    //SimpleAdapter에게 넘기는 데이터 타입
    ArrayList<HashMap<String, String>> scoreList;//항목 구성 집합 데이터
    SimpleAdapter sa;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        studentId = intent.getIntExtra("id", 1);

        initData();
        initTab();
        initAddScore();
        initSpannable();
        initList();

        initWebView();
    }
    private void initData(){
        studentImageView=findViewById(R.id.detail_student_image);
        nameView=findViewById(R.id.detail_name);
        phoneView=findViewById(R.id.detail_phone);
        emailView=findViewById(R.id.detail_email);
        scoreView=findViewById(R.id.detail_score);

        DBHelper helper=new DBHelper(this);
        SQLiteDatabase db=helper.getReadableDatabase();
        //Cursor : select된 row의 집합...
        //Cursor를 움직여서 row 를 선택하고 선택된 row의 column data 추출..
        Cursor cursor=db.rawQuery("select * from tb_student " +
                        "where _id=?",
                new String[]{String.valueOf(studentId)});

        String photo=null;
        if(cursor.moveToFirst()){
            nameView.setText(cursor.getString(1));
            emailView.setText(cursor.getString(2));
            phoneView.setText(cursor.getString(3));
            photo=cursor.getString(4);

            Cursor scoreCursor=db.rawQuery("select score from tb_score " +
                            "where student_id=? order by date desc limit 1",
                    new String[]{String.valueOf(studentId)});
            int score=0;
            if(scoreCursor.moveToFirst()){
                score=scoreCursor.getInt(0);
            }
            scoreView.setScore(score);
        }
        db.close();

    }
    private void initTab(){
        host=findViewById(R.id.host);
        //너네 하위에 FrameLayout과 TabWidget이 선언되어 있을거다.. 선언된데로
        //초기화 하라..
        host.setup();

        //TabSpec 하나에 tab button 과 그 button 클릭시 나올 content가 결합되어
        //추가되는 개념..
        //매개변수 문자열은 개발자가 tab 을 식별하기 위해 주는 임의 문자열..
        TabHost.TabSpec spec=host.newTabSpec("tab1");
        spec.setIndicator("score");//button
        spec.setContent(R.id.detail_score_list);//content
        host.addTab(spec);

        spec=host.newTabSpec("tab2");
        spec.setIndicator("chart");//button
        spec.setContent(R.id.detail_score_chart);//content
        host.addTab(spec);

        spec=host.newTabSpec("tab3");
        spec.setIndicator("add");//button
        spec.setContent(R.id.detail_score_add);//content
        host.addTab(spec);

        spec=host.newTabSpec("tab4");
        spec.setIndicator("memo");//button
        spec.setContent(R.id.detail_score_memo);//content
        host.addTab(spec);

        host.setOnTabChangedListener(this);
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

    View.OnClickListener addScoreListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v==btnAdd){
                long date=System.currentTimeMillis();
                String score=addScoreView.getText().toString();

                DBHelper helper=new DBHelper(DetailActivity.this);
                SQLiteDatabase db=helper.getWritableDatabase();
                db.execSQL("insert into tb_score (student_id, date, score) "+
                                "values (?,?,?)",
                        new String[]{String.valueOf(studentId),
                                String.valueOf(date), score});
                db.close();
                //화면은?????
                //코드적으로 첫번째 tab 화면으로 이동...
                host.setCurrentTab(0);
                addScoreView.setText("0");

                scoreView.setScore(Integer.parseInt(score));

                HashMap<String, String> map=new HashMap<>();
                map.put("score", score);
                Date d=new Date(date);
                SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
                map.put("date", sd.format(d));
                scoreList.add(map);

                //동적 항목 추가/제거는 데이터 추가 /제거후 반영 명령만..
                sa.notifyDataSetChanged();

            }else if(v==btnBack){
                String score=addScoreView.getText().toString();
                if(score.length()==1){
                    addScoreView.setText("0");
                }else {
                    String newScore=score.substring(0,
                            score.length()-1);
                    addScoreView.setText(newScore);
                }
            }else {
                Button btn=(Button)v;
                String txt=btn.getText().toString();

                String score=addScoreView.getText().toString();
                if(score.equals("0")){
                    addScoreView.setText(txt);
                }else {
                    String newScore=score+txt;
                    int intScore=Integer.parseInt(newScore);
                    if(intScore>100){
                        Toast toast=Toast.makeText(DetailActivity.this,
                                R.string.read_add_score_over_score,
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }else {
                        addScoreView.setText(newScore);
                    }
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initSpannable(){
        TextView spanView=findViewById(R.id.spanView);
        TextView htmlView=findViewById(R.id.htmlView);

        //ForegroundColorSpan 등은 이 span이 적용되어 그 문자열이 그 UI로
        //나오면 끝....
        //URLSpan 은 이 span 이 적용되어 링크 모양으로 나온다가 끝이 아니라..
        //유저가 링크 클릭시의 이벤트는???????개발자 로직이다..
        //URLSpan 상속받아 이벤트 로직을 가지는 클래스로 적용..
        URLSpan urlSpan=new URLSpan(""){
            @Override
            public void onClick(View widget) {
                Toast toast=Toast.makeText(DetailActivity.this,
                        "more click", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        String data=spanView.getText().toString();
        Spannable spannable=(Spannable)spanView.getText();

        int pos=data.indexOf("EXID");
        while(pos > -1){
            spannable.setSpan(new ForegroundColorSpan(Color.RED),
                    pos, pos+4,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            pos=data.indexOf("EXID", pos+1);
        }

        pos=data.indexOf("more");
        spannable.setSpan(urlSpan, pos, pos+4,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //아래 코드 없으면 이벤트 발생 안된다..
        spanView.setMovementMethod(LinkMovementMethod.getInstance());

        String html="<font color='blue'>HANI</font><br/>" +
                "<img src='myImage'/>";
        htmlView.setText(
                //formHtml 함수에서 태그에 해당되는 Span 을 적용..
                //이미지 태그만 없다면 매개변수 하나짜리 사용..
                Html.fromHtml(
                        html,
                        //브라우저 아니다. <img> 태그에 나올 이미지 획득은
                        //개발자가 직접..
                        new MyImageGetter(),
                        //특정 클래스를 개발자가 직접 customize 하려면...
                        null));
    }

    class MyImageGetter implements Html.ImageGetter{
        //fromHtml 함수에서 이미지 획득 목적으로 호출..
        //<img> 태그 하나당 한번식...
        //매개변수는 <img> 태그의 src 값....
        //최종 리턴되는 Drawable을 <img> 영역에 적용...
        @Override
        public Drawable getDrawable(String source) {
            if(source.equals("myImage")){
                Drawable drawable=ResourcesCompat.getDrawable(
                        getResources(), R.drawable.hani_1, null);
                //아래의 정보 설정 안되면 이미지 화면에 안보인다..
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight());
                return drawable;
            }
            //null 리턴하면 에러나지 않고.. 이미지 없다는 이야기다..
            return null;
        }
    }

    private void initList(){
        listView=findViewById(R.id.detail_score_list);
        scoreList=new ArrayList<>();

        DBHelper helper=new DBHelper(this);
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select score, date from tb_score " +
                        "where student_id=? order by date desc",
                new String[]{String.valueOf(studentId)});
        while (cursor.moveToNext()){
            HashMap<String, String> map=new HashMap<>();
            map.put("score", cursor.getString(0));
            Date d=new Date(Long.parseLong(cursor.getString(1)));
            SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
            map.put("date", sd.format(d));
            scoreList.add(map);
        }
        db.close();

        sa=new SimpleAdapter(
                this,
                //항목 집합 데이터
                scoreList,
                //항목 layout xml
                R.layout.read_list_item,
                //항목 데이터가 Map이다.. 데이터 추출을 위한 key
                new String[]{"score","date"},
                //항목 layout에 데이터 출력 view 여러개다.. 어떤 view에 찍어야
                //하는지 view의 id 값..
                new int[]{R.id.read_list_score, R.id.read_list_date});

        listView.setAdapter(sa);
    }

    private void initWebView(){
        webView=findViewById(R.id.detail_score_chart);
        //WebView의 js engine이 기본으로 꺼져있다..
        WebSettings settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);

        //js에서 chart 데이터 출력 목적으로 java 함수 호출..
        //java에서 객체를 공개해야 하고 공개된 객체의 함수만 호출 가능..
        //개발자 임의 단어.. 공개한 객체의 js에서의 이름..
        webView.addJavascriptInterface(new JavascriptTest(), "android");

    }

    public class JavascriptTest {
        //아래의 annotation 이 추가된 함수만 호출 가능하다..
        @JavascriptInterface
        public String getWebData(){
            StringBuffer buffer=new StringBuffer();
            buffer.append("[");
            if(scoreList.size()<=10){
                int j=1;
                for(int i=scoreList.size(); i>0; i--){
                    buffer.append("["+j+",");
                    buffer.append(scoreList.get(i-1).get("score"));
                    buffer.append("]");
                    if(i>1) buffer.append(",");
                    j++;

                }
            }else {
                int j=1;
                for(int i=10; i>0; i--){
                    buffer.append("["+j+",");
                    buffer.append(scoreList.get(i-1).get("score"));
                    buffer.append("]");
                    if(i>1) buffer.append(",");
                    j++;

                }
            }
            buffer.append("]");
            Log.d("chart data", buffer.toString());
            return buffer.toString();
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        if(tabId.equals("tab2")){
            webView.loadUrl("file:///android_asset/test.html");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String sendData = scoreList.get(0).get("score")+" - "+ scoreList.get(0).get("date");
        int id = item.getItemId();
        if(id == R.id.menu_detail_sms){
            String phoneNumber = phoneView.getText().toString();
            if(phoneNumber != null && !phoneNumber.equals("")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + phoneNumber));
                intent.putExtra("sms_body", sendData);
                startActivity(intent);
            }
        }else if(id == R.id.menu_detail_email){
            String email = emailView.getText().toString();
            if(email != null && !email.equals("")){
                String mailto = "mailto:"+email+"?subject="+Uri.encode("score")+"&body="+Uri.encode(sendData);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(mailto));

                try{
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText( this, "no email app", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}













