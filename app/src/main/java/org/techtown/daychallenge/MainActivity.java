package org.techtown.daychallenge;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.techtown.daychallenge.ui.Category.CategoryFragment;
import org.techtown.daychallenge.ui.Challenge.ChallengeFragment;
import org.techtown.daychallenge.ui.Feed.FeedFragment;
import org.techtown.daychallenge.ui.Post.PostFragment;
import org.techtown.daychallenge.ui.Writing.WritingFragment;
import org.techtown.daychallenge.ui.none.NoneFragment;

public class MainActivity extends dbAction {

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

        // 2020.08.03 송고은
        // 작업 내용 - 앱을 처음 실행 했을 경우에만 DB, TABLE 생성함
        prefs = getSharedPreferences("Pref", MODE_PRIVATE); // 생성하기
        checkFirstRun(); // 앱을 처음 실행했는지 확인하는 함수
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

    // 2020.08.04 송고은
    public void checkFirstRun() {
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);
        // 앱 처음 실행했는지 확인하는 조건문
        if (isFirstRun) { // ture인 경우 처음 실행하는 것
            createDatabase();
            createTable();
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
