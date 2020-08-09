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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.techtown.daychallenge.ui.Category.CategoryFragment;
import org.techtown.daychallenge.ui.Challenge.ChallengeFragment;
import org.techtown.daychallenge.ui.Feed.FeedFragment;
import org.techtown.daychallenge.ui.Post.PostFragment;
import org.techtown.daychallenge.ui.Writing.WritingFragment;
import org.techtown.daychallenge.ui.none.NoneFragment;

import java.util.Stack;

public class MainActivity extends dbAction implements OnTabItemSelectedListener {

    CategoryFragment categoryFragment;
    ChallengeFragment challengeFragment;
    NoneFragment noneFragment;
    WritingFragment writingFragment;
    PostFragment postFragment;
    FeedFragment feedFragment;

    BottomNavigationView navView;

    public SharedPreferences prefs;

    public static Stack<Fragment> fragmentStack; //B 뒤로가기 누를 때 현재 프래그먼트 정보가 쌓일 스택
    public static FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkSelfPermission();

        // 2020.08.03 송고은
        // 작업 내용 - 앱을 처음 실행 했을 경우에만 DB, TABLE 생성함
        prefs = getSharedPreferences("Pref", MODE_PRIVATE); // 생성하기
        checkFirstRun(); // 앱을 처음 실행했는지 확인하는 함수
        // delTable(); //Data가 많아져서 지저분 해졌을 경우에 table 자체를 삭제하고 생성함

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

}
