package org.techtown.daychallenge;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;

public class dbAction extends AppCompatActivity {
    static DatabaseHelper dbHelper;
    static SQLiteDatabase database;
    public static String cate = null;
    public TextView textView;
    public ImageView post_img;
    protected void createDatabase() {
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        post_img = findViewById(R.id.post_img);

    }

    protected void createTable() {
        if (database == null) {
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertRecord(String tableName, String cate, String ch_con, String cons, String photo) {
        //database = dbHelper.getWritableDatabase();
        if (database == null) {
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }

        if (tableName == null) {
            println("테이블을 먼저 생성하세요.");
            return;
        }

        long now = System.currentTimeMillis();
        LocalDate mDate = LocalDate.now();
        Date dates = new Date(now);
        SimpleDateFormat dateNow = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String strnow = dateNow.format(dates);

        String sql = "insert into " + tableName
                + "(category, ch_content, content, photo, rdate) "
                + " values "
                + "('"+cate+"','"+ch_con+"','"+cons+"','"+photo+"','"+strnow+"')";

        database.execSQL(sql);
    }

    public String executeQuery(String sel_category) {
        println("executeQuery 호출됨.");

        String sql = "select * from post where category = "
                + "'"+sel_category+"'";
        Cursor cursor = database.rawQuery(sql, null);
        int recordCount = cursor.getCount();
        println("레코드 개수 : " + recordCount);
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
            println("레코드 #" + i + " : " + id + ", " + category + ", " + ch_content + ", " + content+ ", " + photo + ", " + rdate+"\n");
        }
        cursor.close();

        return uris;
    }

    public void delTable() { // 테이블 초기화 하는거임
        database.execSQL("DROP TABLE IF EXISTS post");

        String sql = "create table if not exists post ("
                + " _id integer PRIMARY KEY autoincrement, "
                + " category text, "
                + " ch_content text, "
                + " content text, "
                + " photo text, "
                + " rdate text "
                + " )";

        database.execSQL(sql);
    }

    public void setImage(String path){
        String imagepath = path;
        if (imagepath != null && !imagepath.equals("")) {
            post_img.setImageURI(Uri.parse("file://" + imagepath));
        } else {
            post_img.setImageResource(R.drawable.gradation); // 사진 없으면 약 아이콘으로 설정
        }
    }

    public void println(String data) {
        textView.append(data);
    }

}
