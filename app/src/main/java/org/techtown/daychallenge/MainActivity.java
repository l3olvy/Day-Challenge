package org.techtown.daychallenge;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.techtown.daychallenge.ui.Category.CategoryFragment;
import org.techtown.daychallenge.ui.Challenge.ChallengeFragment;
import org.techtown.daychallenge.ui.Feed.FeedFragment;
import org.techtown.daychallenge.ui.Post.PostFragment;
import org.techtown.daychallenge.ui.Writing.WritingFragment;
import org.techtown.daychallenge.ui.none.NoneFragment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public int idx;
    dbAction pdb;
    CategoryFragment categoryFragment;
    ChallengeFragment challengeFragment;
    NoneFragment noneFragment;
    WritingFragment writingFragment;
    PostFragment postFragment;
    FeedFragment feedFragment;
    BottomNavigationView navView;

    public SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkSelfPermission();

        // 2020.08.09 DB 복사
        Context mContext = getApplicationContext();
        pdb = new dbAction(mContext);
        try {
            boolean bResult = isCheckDB(mContext);
            if(!bResult) {
                copyDB(mContext);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        //delTable(); //Data가 많아져서 지저분 해졌을 경우에 table 자체를 삭제하고 생성함

        categoryFragment = new CategoryFragment();
        challengeFragment = new ChallengeFragment();
        noneFragment = new NoneFragment();
        writingFragment = new WritingFragment();
        postFragment = new PostFragment();
        feedFragment = new FeedFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, categoryFragment).commit();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_category:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, categoryFragment).commit();
                        return true;
                    case R.id.navigation_none:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, noneFragment).commit();
                        return true;
                    case R.id.navigation_feed:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, feedFragment).commit();
                        return true;
                }
                return false;
            }
        });
       /*BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        */
    }

    public boolean isCheckDB(Context mContext) {
        String filePath = "/data/data/org.techtown.daychallenge/databases/dayChallenge.db";
        File file = new File(filePath);
        if(file.exists()) return true;
        return false;
    }

    public void copyDB(Context mContext) {
        Log.d("MINP", "COPYDB");
        AssetManager manager = mContext.getAssets();
        String folderPath = "/data/data/org.techtown.daychallenge/databases";
        String filePath = "/data/data/org.techtown.daychallenge/databases/dayChallenge.db";
        File folder = new File(folderPath);
        File file = new File(filePath);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            InputStream is = manager.open("dbInit.db");
            BufferedInputStream bis = new BufferedInputStream(is);

            if(folder.exists()) {

            } else {
                folder.mkdirs();
            }

            if(file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read = -1;
            byte[] buffer = new byte[1024];
            while((read = bis.read(buffer,0,1024)) != -1) {
                bos.write(buffer,0,read);
            }

            bos.flush();
            bos.close();
            fos.close();
            bis.close();
            is.close();
        }  catch(Exception e) {
            Log.d("Error--------------", e.toString());
        }
    }


    public void checkSelfPermission() {
        String temp = "";

        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }else {
            // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }

    public void onFragmentChanged(int index){
        if (index == 0){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, challengeFragment).commit();
        }
        else if (index == 1){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, categoryFragment).commit();
        }
        else if (index == 2){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, writingFragment).commit();
        }
        else if (index == 3){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, postFragment).commit();
        }
    }

}