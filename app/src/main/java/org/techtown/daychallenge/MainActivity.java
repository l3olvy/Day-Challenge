package org.techtown.daychallenge;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
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

import java.util.Calendar;
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

    SharedPreferences.Editor editor;
    public SharedPreferences prefs;
    SharedPreferences msp;
    SharedPreferences dsp;
    SharedPreferences hsp;
    SharedPreferences psp;

    SharedPreferences m_chg;
    SharedPreferences d_chg;
    SharedPreferences h_chg;
    SharedPreferences p_chg;

    public static Context tContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //B TerminationService에서 MainActivity메소드 접근하기 위해 선언
        tContext = this;
        startService(new Intent(this, TerminationService.class));
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

        //B 최초 실행 시 Challenge 넣어줌
        prefs = getSharedPreferences("Pref", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun",true);
        if(isFirstRun) {
            dbAction db = new dbAction(this);
            ChallengeFragment.m_change = 1;
            ChallengeFragment.d_change = 1;
            ChallengeFragment.h_change = 1;
            ChallengeFragment.p_change = 1;
            ChallengeFragment.m_items = db.getChallenge("MUSIC");
            ChallengeFragment.d_items = db.getChallenge("DRAWING");
            ChallengeFragment.h_items = db.getChallenge("HAPPINESS");
            ChallengeFragment.p_items = db.getChallenge("PHOTO");

            prefs.edit().putBoolean("isFirstRun", false).apply();
        } else {
            //B 종료 전 저장해뒀던 챌린지와 change 변수 가져옴
            getPreferences();
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

        //B 현재 지정된 시간으로 알람 시간 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        //B 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        //B  Preference에 설정한 값 저장
        editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
        editor.putLong("nextNotifyTime", (long)calendar.getTimeInMillis());

        editor.apply();

        //B 챌린지가 남아있다면 알람 울림
        if(((dbAction)dbAction.context).checkAllClear() == false) {
            diaryNotification(calendar);
        }
    }

    // 일반 종료시 불림
    @Override
    protected void onDestroy() {
        super.onDestroy();
        save(ChallengeFragment.music, ChallengeFragment.drawing, ChallengeFragment.happiness, ChallengeFragment.photo);
        // photo 추가해야함
        change(ChallengeFragment.m_change, ChallengeFragment.d_change, ChallengeFragment.h_change, ChallengeFragment.p_change);
    }

    //B 챌린지 랜덤
    public void change(Integer m_num, Integer d_num, Integer h_num, Integer p_num) {
        m_chg = getSharedPreferences("m_change", MODE_PRIVATE);
        SharedPreferences.Editor editor5 = m_chg.edit();
        editor5.putInt("m_changeChallenge", m_num);
        editor5.commit();
        d_chg = getSharedPreferences("d_change", MODE_PRIVATE);
        SharedPreferences.Editor editor6 = d_chg.edit();
        editor6.putInt("d_changeChallenge", d_num);
        editor6.commit();
        h_chg = getSharedPreferences("h_change", MODE_PRIVATE);
        SharedPreferences.Editor editor7 = h_chg.edit();
        editor7.putInt("h_changeChallenge", h_num);
        editor7.commit();
        p_chg = getSharedPreferences("p_change", MODE_PRIVATE);
        SharedPreferences.Editor editor8 = p_chg.edit();
        editor8.putInt("p_changeChallenge", p_num);
        editor8.commit();
    }

    //B 종료 전 Challenge 내용 SharedPreferences에 저장.
    public void save(String m, String d, String h, String p) {
        msp = getSharedPreferences("msp", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = msp.edit();
        editor1.putString("saveMusic", m);
        editor1.commit();
        dsp = getSharedPreferences("dsp", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = dsp.edit();
        editor2.putString("saveDrawing", d);
        editor2.commit();
        hsp = getSharedPreferences("hsp", MODE_PRIVATE);
        SharedPreferences.Editor editor3 = hsp.edit();
        editor3.putString("saveHappiness", h);
        editor3.commit();
        psp = getSharedPreferences("psp", MODE_PRIVATE);
        SharedPreferences.Editor editor4 = psp.edit();
        editor4.putString("savePhoto", p);
        editor4.commit();
    }

    //B 종료시 저장해뒀던 Challenge 다시 넣어줌
    public void getPreferences(){
        msp = getSharedPreferences("msp", MODE_PRIVATE);
        ChallengeFragment.music = msp.getString("saveMusic","");
        dsp = getSharedPreferences("dsp", MODE_PRIVATE);
        ChallengeFragment.drawing = dsp.getString("saveDrawing", "");
        hsp = getSharedPreferences("hsp", MODE_PRIVATE);
        ChallengeFragment.happiness = hsp.getString("saveHappiness", "");
        psp = getSharedPreferences("psp", MODE_PRIVATE);
        ChallengeFragment.photo = psp.getString("savePhoto", "");

        // 강종이 문제네 강종이
        //B 난 왜 이 코드가 안 들어가는데도 실행이 되는질 몰겠어. 이거 넣어도 실행은 되는ㄷㅔ 알람 들어올 때 change횟수가 1로 초기화가 안 됨 개빡ㅊㅣ게!
        //일단 주석처리 해놓긴했는데 지우지말아줘.
        // 주석 처리 한 상태에서 실행하니 일어나는 문제 1. 한번 튕기고 2. 다시 들어가니 랜덤 버튼 안누른 카테고리의 챌린지가 안뜸
        // 3. 랜덤 버튼 누르니 강종 됐다.

        // 종료, 강종 할 때 이전 값을 다시 부르는코드인데 문제는 알람을 넣은 이후에도 그 값이 다시 들어간다.
        // 이 코드를 살려서 실행 햇을 때의 문제
        // 1. 다 되는데 한번 튕김
        // 2. 알람을 넣은 이후에도 이 값이 다시 들어간다.
        m_chg = getSharedPreferences("m_change", MODE_PRIVATE);
        ChallengeFragment.m_change = m_chg.getInt("m_changeChallenge", 10);
        d_chg = getSharedPreferences("d_change", MODE_PRIVATE);
        ChallengeFragment.d_change = d_chg.getInt("d_changeChallenge", 10);
        h_chg = getSharedPreferences("h_change", MODE_PRIVATE);
        ChallengeFragment.h_change = h_chg.getInt("h_changeChallenge", 10);
        p_chg = getSharedPreferences("p_change", MODE_PRIVATE);
        ChallengeFragment.p_change = p_chg.getInt("p_changeChallenge", 10);

    }

    void diaryNotification(Calendar calendar)
    {
        Boolean dailyNotify = true; // 무조건 알람을 사용

        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {
            if (alarmManager != null) {

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }

            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
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

    //B Challenge, 삽입한 image, contents를 PostFragment로 전달
    public void showPostFragment3(String picture, String ch_content, String content){
        postFragment = new PostFragment();
        postFragment.setItem3(picture, ch_content, content);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, postFragment).commit();
    }

    //B Challenge를 WritingFragment으로 전달
    public void challenge(String ch){
        writingFragment.setItem(ch);
    }


}