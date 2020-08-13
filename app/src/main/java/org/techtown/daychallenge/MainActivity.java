package org.techtown.daychallenge;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.techtown.daychallenge.ui.Category.CategoryFragment;
import org.techtown.daychallenge.ui.Challenge.Challenge;
import org.techtown.daychallenge.ui.Challenge.ChallengeFragment;
import org.techtown.daychallenge.ui.Feed.Feed;
import org.techtown.daychallenge.ui.Feed.FeedFragment;
import org.techtown.daychallenge.ui.Interface.OnTabItemSelectedListener;
import org.techtown.daychallenge.ui.Post.PostFragment;
import org.techtown.daychallenge.ui.Writing.WritingFragment;
import org.techtown.daychallenge.ui.none.NoneFragment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener {
    public static int idx;
    dbAction pdb;
    CategoryFragment categoryFragment;
    ChallengeFragment challengeFragment;
    NoneFragment noneFragment;
    WritingFragment writingFragment;
    PostFragment postFragment;
    FeedFragment feedFragment;

    BottomNavigationView navView;

    public static Stack<Fragment> fragmentStack; //B 뒤로가기 누를 때 현재 프래그먼트 정보가 쌓일 스택
    public static FragmentManager manager;

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

        //B 스택 선언 후 category 프래그먼트 push => 뒤로가기 눌렀을 때 최종적으로 category로 돌아오도록!
        fragmentStack = new Stack<>();
        fragmentStack.push(categoryFragment);
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.nav_host_fragment, categoryFragment).commit();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_category:
                        manager.beginTransaction().replace(R.id.nav_host_fragment, categoryFragment).commit();
                        return true;
                    case R.id.navigation_none:
                        manager.beginTransaction().replace(R.id.nav_host_fragment, noneFragment).commit();
                        return true;
                    case R.id.navigation_feed:
                        manager.beginTransaction().replace(R.id.nav_host_fragment, feedFragment).commit();
                        return true;
                }
                return false;
            }
        });

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
            //Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }

    //B 뒤로가기 구현하려고 네비 바에 있는 것도 추가시켜서 숫자가 좀 바뀜
    public void onFragmentChanged(int position){
        if (position == 0){
            manager.beginTransaction().replace(R.id.nav_host_fragment, categoryFragment).commit();
        }
        else if (position == 1){
            manager.beginTransaction().replace(R.id.nav_host_fragment, challengeFragment).commit();
        }
        else if (position == 2){
            manager.beginTransaction().replace(R.id.nav_host_fragment, feedFragment).commit();
        }
        else if (position == 3){
            manager.beginTransaction().replace(R.id.nav_host_fragment, writingFragment).commit();
        }
        else if (position == 4){
            manager.beginTransaction().replace(R.id.nav_host_fragment, postFragment).commit();
        }
        else if (position == 5){
            manager.beginTransaction().replace(R.id.nav_host_fragment, noneFragment).commit();
        }
    }

    //B 뒤로가기 눌렀을 때 이전 프래그먼트 pop
    @Override
    public void onBackPressed() {
        if(!fragmentStack.isEmpty()){
            Fragment nextFragment = fragmentStack.pop();
            manager.beginTransaction().replace(R.id.nav_host_fragment, nextFragment).commit();
        }else{
            super.onBackPressed();
        }
    }

    //B Feed 프래그먼트에서 아이템 클릭 시 Post 화면으로 이동 및 정보 전달
    public void showPostFragment(Feed item) {

        postFragment = new PostFragment();
        postFragment.setItem(item);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, postFragment).commit();

    }

    //B Challenge 프래그먼트에서 아이템 클릭 시 Post 화면으로 이동 및 정보 전달
    public void showPostFragment2(Challenge item) {

        postFragment = new PostFragment();
        postFragment.setItem2(item);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, postFragment).commit();

    }

    public void showPostFragment3(String picture, String ch_content, String content){
        postFragment = new PostFragment();
        postFragment.setItem3(picture, ch_content, content);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, postFragment).commit();
    }

    public void challenge(ChContent item){
        writingFragment.setItem(item);
    }


}