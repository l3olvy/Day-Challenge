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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertRecord(String tableName, String cate, String ch_con, String cons, String photo) {
        db = mHelper.getWritableDatabase();

        long now = System.currentTimeMillis();
        Date dates = new Date(now);
        SimpleDateFormat dateNow = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String strnow = dateNow.format(dates);

        String sql = "insert into '" +tableName+"'"
                + "(category, ch_content, content, photo, rdate) "
                + " values "
                + "('"+cate+"','"+ch_con+"','"+cons+"','"+photo+"','"+strnow+"')";

        db.execSQL(sql);
        mHelper.close();
        db.close();
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

    public int selPid(String con) { // 수정을 위해 id 값 설정하는 함수
        db = mHelper.getReadableDatabase();
        String sql = "select _id from post where content='"+con+"'";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        int pid = cursor.getInt(0);
        cursor.close();
        mHelper.close();
        db.close();
        return pid;
    }

    public ArrayList upSel(int idx) {
        db = mHelper.getReadableDatabase();
        ArrayList data = new ArrayList();

        String sql = "select photo, content from post where _id="+idx;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();

        data.add(cursor.getString(0));
        data.add(cursor.getString(1));

        mHelper.close();
        db.close();
        return data;
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
        mHelper.close();
        db.close();
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
        mHelper.close();
        db.close();
        datas.add(items);
        datas.add(recordCount);

        return datas;
    }

    public ArrayList getChallenge(String sel_category) {
        db = mHelper.getReadableDatabase();

        ArrayList<ChContent> items = new ArrayList<ChContent>();

        String sql = "select * from challenge where category = " + "'" + sel_category + "'" + "and enable is null order by random() limit 1";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String category = cursor.getString(1);
            ch_content = cursor.getString(2);
            int enable = cursor.getInt(3);
            items.add(new ChContent(id, category, ch_content, enable));

            cursor.close();
            mHelper.close();
            db.close();
            return items;
        } else{
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
        mHelper.close();
        db.close();
    }

    public void updateRecord(String contents, String imageUri, int pid) {
        db = mHelper.getReadableDatabase();

        String sql = "update post set "+
                "content='"+contents+"', "+
                "photo='"+imageUri+"'"+
                " where"+
                " _id ="+pid;
        db.execSQL(sql);
        mHelper.close();
        db.close();
    }
}
