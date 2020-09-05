package org.techtown.daychallenge.ui.Challenge;


import android.app.AlertDialog;
import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.daychallenge.ChContent;
import org.techtown.daychallenge.MainActivity;
import org.techtown.daychallenge.ui.Interface.OnChallengeItemClickListener;
import org.techtown.daychallenge.ui.Interface.OnTabItemSelectedListener;
import org.techtown.daychallenge.R;
import org.techtown.daychallenge.dbAction;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ChallengeFragment extends Fragment {
    RecyclerView recyclerView;
    ChallengeAdapter adapter;
    Challenge item;
    Context context;
    OnTabItemSelectedListener listener;
    public static String cate = null;
    TextView ch_content;

    public static String music;
    public static String drawing;
    public static String happiness;
    public static Integer m_change;
    public static Integer d_change;
    public static Integer h_change;

    Button write_btn;
    Button change_btn;

    public static ArrayList<ChContent> m_items;
    public static ArrayList<ChContent> d_items;
    public static ArrayList<ChContent> h_items;

    LinearLayout ch_box;

    @Override //B 프래그먼트를 Activity에 attach 할 때 호출
    public void onAttach(Context context) {

        super.onAttach(context);

        this.context = context;

        if (context instanceof OnTabItemSelectedListener) {
            listener = (OnTabItemSelectedListener) context;
        }
    }

    @Override //B 프래그먼트와 Activity의 연결고리가 끊길 때 호출
    public void onDetach() {
        super.onDetach();

        if (context != null) {
            context = null;
            listener = null;
        }
    }

    //B 레이아웃을 inflate하는 곳, view 객체를 얻어서 초기화
    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_challenge, container, false);
        ch_content = rootView.findViewById(R.id.challengecontent);

        ch_box = rootView.findViewById(R.id.ch_box);
        ch_content = rootView.findViewById(R.id.challengecontent);

        change_btn = rootView.findViewById(R.id.change);
        //B 랜덤 체인지 버튼 누르면
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //B 현재 날짜 불러오고
                long now = System.currentTimeMillis();
                Date dates = new Date(now);
                SimpleDateFormat dateNow = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                String strnow = dateNow.format(dates);
                dbAction db = new dbAction(getContext());

                //B 각 카테고리에 오늘 작성한 게시물이 있으면 블락
                if(db.checkSave(cate, strnow) == true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("불가");
                    builder.setMessage("오늘의 챌린지를 완료하였기 때문에 변경 불가합니다.");
                    builder.show();
                }else{ //B 없으면 변경횟수 3번 제공
                    if(cate == "MUSIC"){
                        Toast.makeText(context, "m_change = " + m_change, Toast.LENGTH_SHORT).show();
                        if(m_change <= 3){ //B 세 번까진 변경해줌
                            m_change += 1;
                            ChallengeFragment.m_items = db.getChallenge("MUSIC");
                            //B 현재 Fragment 변경사항 새로고침? 해주는 코드
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(ChallengeFragment.this).attach(ChallengeFragment.this).commit();
                        }
                        else{ //B 세 번을 넘어가면 블락
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("불가");
                            builder.setMessage("오늘의 챌린지 변경 횟수를 초과하였습니다.");
                            builder.show();
                        }
                    }
                    else if(cate == "DRAWING"){
                        Toast.makeText(context, "d_change = " + d_change, Toast.LENGTH_SHORT).show();
                        if(d_change <= 3){
                            d_change += 1;
                            ChallengeFragment.d_items = db.getChallenge("DRAWING");
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(ChallengeFragment.this).attach(ChallengeFragment.this).commit();
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("불가");
                            builder.setMessage("오늘의 챌린지 변경 횟수를 초과하였습니다.");
                            builder.show();
                        }
                    }
                    else if(cate == "HAPPINESS"){
                        Toast.makeText(context, "h_change = " + h_change, Toast.LENGTH_SHORT).show();
                        if(h_change <= 3){
                            h_change += 1;
                            ChallengeFragment.h_items = db.getChallenge("HAPPINESS");
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(ChallengeFragment.this).attach(ChallengeFragment.this).commit();
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("불가");
                            builder.setMessage("오늘의 챌린지 변경 횟수를 초과하였습니다.");
                            builder.show();
                        }
                    }
                }

            }
        });
        write_btn = rootView.findViewById(R.id.write);
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = MainActivity.manager.findFragmentById(R.id.nav_host_fragment);
                //B 이동버튼 클릭할 때 stack에 push
                MainActivity.fragmentStack.push(currentFragment);
                MainActivity activity = (MainActivity) getActivity();

                activity.onFragmentChanged(3); //B Writing 화면으로 전환
            }
        });

        initUI(rootView);

        // 데이터 로딩
        loadNoteListData(cate);

        challengeContents(cate);

        return rootView;
    }

    private void initUI(ViewGroup rootView) {

        recyclerView = rootView.findViewById(R.id.challenge_recyclerview);

        //B 아이템을 linearlayout으로 한 줄씩 위치
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChallengeAdapter();

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnChallengeItemClickListener() {
            @Override
            public void onItemClick(ChallengeAdapter.ViewHolder holder, View view, int position) {

                Fragment currentFragment = MainActivity.manager.findFragmentById(R.id.nav_host_fragment);
                //B 이동버튼 클릭할 때 stack에 push
                MainActivity.fragmentStack.push(currentFragment);

                //B Challenge 리싸이클러뷰에 있는 아이템 클릭 시 Adapter 이용해서 Item 정보 갖고오고,
                //listener 이용해서 MainActivity에 정보 전달 (프래그먼트끼리 데이터 주고받으려면 엑티비티 거쳐야하기때문!)
                item = adapter.getItem(position);

                if (listener != null) {
                    listener.showPostFragment2(item);
                }

            }
        });

    }

    /**
     * 리스트 데이터 로딩
     */
    public int loadNoteListData(String sel_category) {
        dbAction db = new dbAction(getContext());
        ArrayList datas = db.chSelData(sel_category);
        ArrayList<Challenge> items = (ArrayList<Challenge>) datas.get(0);

        int recordCount = (int) datas.get(1);
        adapter.setItems(items); //B adapter에 데이터 추가
        adapter.notifyDataSetChanged(); //B adatper에 추가가 완료되었다고 알려주는 함수 호출


        return recordCount;
    }



    //B ChallengeFragment에 category 별로 Challenge와 완료 목록 넣음
    public void challengeContents(final String sel_category) {
        dbAction db = new dbAction(getContext());
        if(db.checkClear(sel_category) == true){ // challenge 테이블에 더이상 지정 카테고리의 챌린지가 없을 때
            ch_content.setText(sel_category + "을(를) 완성하였습니다.");
            write_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ch_box.setBackgroundColor(Color.WHITE);
                    completedDialog(sel_category); // 완료 dialog 띄움
                }
            });
        }else{ // 아직 Challenge가 남아있다면
            if (sel_category == "MUSIC") {
                if (m_items != null) {
                    music = m_items.get(0).getCh_content(); // challenge 넣어주고
                    setChallenge(music); //호출
                }else if(music != null){
                    setChallenge(music);
                }
            } else if (sel_category == "DRAWING") {
                if (d_items != null) {
                    drawing = d_items.get(0).getCh_content();
                    setChallenge(drawing);
                }else if(drawing != null){
                    setChallenge(drawing);
                }
            } else if (sel_category == "HAPPINESS") {
                if (h_items != null) {
                    happiness = h_items.get(0).getCh_content();
                    setChallenge(happiness);
                }else if(happiness != null){
                    setChallenge(happiness);
                }
            }
        }

    }

    public void setChallenge(String ch){
        ch_content.setText(ch); //B ChallengeFragment에 Challenge 넣고
        listener.challenge(ch); //B MainActivity 거쳐서 WritingFragment에 Challenge 전달 (post 테이블에 데이터 삽입하기 위해)
        blockWriteBtn(ch); // 작성 완료시 더이상 못들어가게 Write Button 블락
    }

    public void blockWriteBtn(String challenge){
        dbAction dayDB = new dbAction(context);
        if(dayDB.checkWriting(challenge) == true){ //B post 테이블에 해당 챌린지 작성 내용이 있다면
            ch_box.setBackgroundColor(Color.GRAY); // 박스 색 변화
            ch_content.setText("'" + challenge + "' 완료!"); // 챌린지 완료 문구
            write_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // 하루동안 더이상 작성 못하게 Write Button 블락 & 완료 dialog 띄움
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("완료");
                    builder.setMessage("오늘의 챌린지를 완료하였습니다.");
                    builder.show();
                }
            });
        }else{ //B 챌린지 작성내용이 없다면
            ch_box.setBackgroundColor(Color.WHITE);
            write_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //B Writing 화면 이동 가능하게 블락 풀어둠.
                    Fragment currentFragment = MainActivity.manager.findFragmentById(R.id.nav_host_fragment);
                    //B 이동버튼 클릭할 때 stack에 push
                    MainActivity.fragmentStack.push(currentFragment);
                    MainActivity activity = (MainActivity) getActivity();
                    activity.onFragmentChanged(3); //B Writing 화면으로 전환
                }
            });
        }
    }

    //B 31개 Challenge 완성 dialog 띄움
    public void completedDialog(String sel_category){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("완성");
        builder.setMessage(sel_category + "를 완성하였습니다.");
        builder.show();
    }
}

