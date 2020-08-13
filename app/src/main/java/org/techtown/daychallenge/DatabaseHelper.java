package org.techtown.daychallenge;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context mContext;
    public DatabaseHelper(Context context) {
        // super(context, NAME, null, VERSION);
        super(context, "dayChallenge.db", null, 1);
        mContext = context;
    }

    public void onCreate(SQLiteDatabase db) { }

    public void onOpen(SQLiteDatabase db) { }

    public void onClose(SQLiteDatabase db) { db.close(); }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void println(String data) {
        Log.d("DatabaseHelper", data);
    }
}
