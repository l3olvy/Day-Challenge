package org.techtown.daychallenge;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.daychallenge.ui.Challenge.Challenge;
import org.techtown.daychallenge.ui.Feed.Feed;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class dbAction extends AppCompatActivity {
    public String cate;
    SQLiteDatabase db;
    ContentValues row;
    Context ctx;
    public TextView txt;
    DatabaseHelper mHelper;
    public ImageView post_img;
    String ch_content;

    public dbAction(Context ctx) {
        this.ctx = ctx;
        mHelper = new DatabaseHelper(ctx);
    }

    public void selectData() {
        db = mHelper.getReadableDatabase();
        Cursor cursor;

        cursor = db.rawQuery("select * from 'challenge' where category='DRAWING' ", null);

        while(cursor.moveToNext()) {
            String a = cursor.getString(0);
            String b = cursor.getString(1);
            String c = cursor.getString(2);

            //txt.append(a+" "+b+" "+c+" \n");
            //Log.d("data ----------", a+" "+b+" "+c+" \n");
        }

        cursor.close();
        mHelper.close();
    }

    public String executeQuery(String sel_category) {
        db = mHelper.getReadableDatabase();

        String sql = "select * from post where category = "
                + "'"+sel_category+"'";
        Cursor cursor = db.rawQuery(sql, null);
        int recordCount = cursor.getCount();
        //println("레코드 개수 : " + recordCount);
        String uris = null;
        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();

            int id = cursor.getInt(0);
            String category = cursor.getString(1);
            String ch_content = cursor.getString(2);
            String content = cursor.getString(3);
            String photo = cursor.getString(4);
            String rdate = cursor.getString(5);
            uris = photo;
            //println("레코드 #" + i + " : " + id + ", " + category + ", " + ch_content + ", " + content+ ", " + photo + ", " + rdate+"\n");
            println(content);
        }
        cursor.close();
        mHelper.close();
        return uris;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertRecord(String tableName, String cate, String ch_con, String cons, String photo) {
        db = mHelper.getWritableDatabase();

        long now = System.currentTimeMillis();
        LocalDate mDate = LocalDate.now();
        Date dates = new Date(now);
        SimpleDateFormat dateNow = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String strnow = dateNow.format(dates);

        String sql = "insert into '" +tableName+"'"
                + "(category, ch_content, content, photo, rdate) "
                + " values "
                + "('"+cate+"','"+ch_con+"','"+cons+"','"+photo+"','"+strnow+"')";

        db.execSQL(sql);
        mHelper.close();
    }


    public void delTable() { // 테이블 초기화 하는거임
//        database.execSQL("DROP TABLE IF EXISTS post");
//
//        String sql = "create table if not exists post ("
//                + " _id integer PRIMARY KEY autoincrement, "
//                + " category text, "
//                + " ch_content text, "
//                + " content text, "
//                + " photo text, "
//                + " rdate text "
//                + " )";
//
//        database.execSQL(sql);
    }

    public String reCon(int idx) {
        db = mHelper.getReadableDatabase();

        String sql = "select * from challenge where _id = "
                + idx;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        String recon = cursor.getString(2);
        cursor.close();
        mHelper.close();
        return recon;
    }

    public void println(String data) {
        txt.append(data);
    }


    public ArrayList chSelData(String sel_category) {
        db = mHelper.getReadableDatabase();
        ArrayList datas = new ArrayList();
        ArrayList<Challenge> items = new ArrayList<Challenge>();
        int recordCount = -1;
        String sql = "select * from post where category = " + "'"+sel_category+"'" + "order by _id desc";
        Cursor cursor = db.rawQuery(sql, null);
        recordCount = cursor.getCount();

        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();

            int id = cursor.getInt(0);
            String ch_content = cursor.getString(2);
            String content = cursor.getString(3);
            String photo = cursor.getString(4);
            String rdate = cursor.getString(5);

            items.add(new Challenge(id, ch_content, content, photo, rdate));
        }
        cursor.close();

        datas.add(items);
        datas.add(recordCount);

        return datas;
    }

    public ArrayList feSelData() {
        db = mHelper.getReadableDatabase();

        ArrayList datas = new ArrayList();
        int recordCount = -1;
        ArrayList<Feed> items = new ArrayList<Feed>();

        //B post 테이블에서 id 역순으로 데이터 갖고 오도록
        String sql = "select _id, ch_content, content, photo from post order by _id desc";
        Cursor cursor = db.rawQuery(sql, null);
        recordCount = cursor.getCount();

        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();

            int id = cursor.getInt(0);
            String ch_content = cursor.getString(1);
            String content = cursor.getString(2);
            String photo = cursor.getString(3);

            items.add(new Feed(id, ch_content, content, photo));
        }
        cursor.close();
        datas.add(items);
        datas.add(recordCount);

        return datas;
    }

    public ArrayList getChallenge(String sel_category) {
        db = mHelper.getReadableDatabase();

        ArrayList<ChContent> items = new ArrayList<ChContent>();

        String sql = "select * from challenge where category = " + "'" + sel_category + "'" + " and enable is null order by random() limit 1";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String category = cursor.getString(1);
            ch_content = cursor.getString(2);
            int enable = cursor.getInt(3);
            items.add(new ChContent(id, category, ch_content, enable));
            Log.d("test-----------", category);
            Log.d("sql---------------",sql);

            cursor.close();
            mHelper.close();
            return items;

        }else{
            return null;
        }
    }
    public void enable(Integer id){
        db = mHelper.getWritableDatabase();

            String sql = "update challenge set " +
                    "   enable = 1" +
                    " where " +
                    "   _id = " + id;

        db.execSQL(sql);

    }

}
