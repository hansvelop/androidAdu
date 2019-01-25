package com.example.pjt_student1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

//    Button testBtn;
    ImageView addBtn;
    ListView listView;
    ArrayList<StudentVO> datas;

    double initTime;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        testBtn = findViewById(R.id.main_test_btn);
        addBtn = findViewById(R.id.main_btn);
        listView = findViewById(R.id.main_list);

//        testBtn.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        addBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == addBtn){
            Intent intent= new Intent(this, AddActivity.class);
            startActivity(intent);
//        }else if(v==testBtn){
        }else if(v==listView){
            Intent intent= new Intent(this, DetailActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent= new Intent(this, DetailActivity.class);
        intent.putExtra("id",datas.get(position).id);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis() - initTime > 3000){
                Toast toast = Toast.makeText(this,R.string.main_back_end, Toast.LENGTH_SHORT);
                toast.show();
            }else{
                finish();
            }
            initTime = System.currentTimeMillis();
            return true;
        }
        return super.onKeyDown(keyCode, event);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_main_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setQueryHint(getResources().getString(R.string.main_search_hint));
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(queryListener);

        return super.onCreateOptionsMenu(menu);
    }

    SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            searchView.setQuery("", false);
            searchView.setIconified(true);
            Log.d("hansvelop", s);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        datas = new ArrayList<>();
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_student order by name", null);

        while(cursor.moveToNext()){
            StudentVO vo = new StudentVO();
            vo.id = cursor.getInt(0);
            vo.name = cursor.getString(1);
            vo.email = cursor.getString(2);
            vo.phone = cursor.getString(3);
            vo.photo = cursor.getString(4);
            vo.memo = cursor.getString(5);
            datas.add(vo);
        }
        db.close();
        MainListAdapter adapter = new MainListAdapter(this, R.layout.main_list_item, datas);
        listView.setAdapter(adapter);
    }
}
