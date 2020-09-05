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
    public static Context context;

    public dbAction(Context ctx) {
        this.ctx = ctx;
        mHelper = new DatabaseHelper(ctx);
        context = this;
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

    //B Challenge 랜덤으로 하나 가져오는 메소드
    public ArrayList getChallenge(String sel_category) {
        db = mHelper.getReadableDatabase();

        ArrayList<ChContent> items = new ArrayList();

        String sql = "select * from challenge where category = " + "'" + sel_category + "'" + " and enable is null order by random() limit 1";

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
        } else {
            return null;
        }
    }

    //B challenge 테이블에서 작성 완료한 Challenge의 enable을 1로 변경해주는 메소드
    public void enable(String ch){
        db = mHelper.getWritableDatabase();

            String sql = "update challenge set " +
                    "   enable = 1" +
                    " where " +
                    "   ch_content = " + "'" + ch + "'";

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

    //B post 테이블에 작성 유무 알아보는 메소드
    public boolean checkWriting(String ch){
        db = mHelper.getWritableDatabase();

        String sql = "select * from post where ch_content = " + "'" + ch + "'";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null && cursor.moveToFirst()) { //이미 작성한 게 있다면
            cursor.close();
            mHelper.close();
            return true;
        }else{ //없다면
            return false;
        }

    }

    //B 31개 다 작성했나 확인하는 메소드
    public boolean checkClear(String sel_category){
        db = mHelper.getWritableDatabase();

        String sql = "select * from challenge where category="+"'"+sel_category+"'"+ " and enable is null";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null && cursor.moveToFirst()) { // 아직 챌린지가 남아있다면 false 리턴
            cursor.close();
            mHelper.close();
            return false;
        }else{
            return true; // 챌린지 없으면 true 리턴
        }

    }

    //B 모든 챌린지 끝냈나 확인하는 메소드 (다 끝냈으면 푸시알람 안 주기 위해)
    public boolean checkAllClear(){
        db = mHelper.getWritableDatabase();

        String sql = "select * from challenge where enable is null";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null && cursor.moveToFirst()) { // 아직 챌린지가 남아있다면 false 리턴
            cursor.close();
            mHelper.close();
            return false;
        }else{
            return true; // 챌린지 없으면 true 리턴
        }
    }

    //B 해당 카테고리에 당일에 쓴 작성물이 있는지 없는지 체크하는 메소드
    public boolean checkSave(String cate, String date){
        db = mHelper.getWritableDatabase();

        String sql = "select * from post where category = " + "'" + cate + "'" + " and rdate = " + "'" + date + "'";


        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null && cursor.moveToFirst()) { //이미 작성한 게 있다면
            cursor.close();
            mHelper.close();
            return true;
        }else{ //없다면
            return false;
        }
    }
}


