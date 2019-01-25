package com.example.pjt_student;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//app 의 데이터베이스 관리적인 코드 추상화.. table create, scheme 변경..
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context){
        //studentdb : db file 명.. 하나의 file에 여러 table 저장 가능..
        //원한다면 변수로 처리해서 여러 file 핸들링 가능..
        //상위 클래스에 db version 정보 전달...
        super(context, "studentdb", null, 1);
    }
    //app install 후 helper 클래스가 이용되는 최초에 한번...
    @Override
    public void onCreate(SQLiteDatabase db) {
        String studentSql="create table tb_student (" +
                "_id integer primary key autoincrement," +
                "name not null," +
                "email," +
                "phone," +
                "photo," +
                "memo)";

        String scroeSql="create table tb_score (" +
                "_id integer primary key autoincrement," +
                "student_id not null,"+
                "date," +
                "score)";
        db.execSQL(studentSql);
        db.execSQL(scroeSql);
    }
    //상위 클래스에 전달하는 db version 정보가 변경될때마다..
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table tb_student");
        db.execSQL("drop table tb_score");
        onCreate(db);
    }
}
