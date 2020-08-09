package org.techtown.daychallenge.ui.Challenge;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.techtown.daychallenge.Challenge;
import org.techtown.daychallenge.ChallengeAdapter;

import org.techtown.daychallenge.MainActivity;
import org.techtown.daychallenge.OnChallengeItemClickListener;

import org.techtown.daychallenge.OnTabItemSelectedListener;
import org.techtown.daychallenge.R;
import org.techtown.daychallenge.dbAction;

import java.util.ArrayList;


public class ChallengeFragment extends Fragment {

    RecyclerView recyclerView;
    ChallengeAdapter adapter;
    Challenge item;
    Context context;
    OnTabItemSelectedListener listener;
    public static String cate = null;

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
                             ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_challenge, container, false);

        Button write_btn = rootView.findViewById(R.id.write);
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

        int recordCount = -1;

        ArrayList<Challenge> items = new ArrayList<Challenge>();

        //B post 테이블에서 id 역순으로 카테고리에 따라 데이터 갖고 오도록
        String sql = "select _id, category, ch_content, content, photo, rdate from post where category = " + "'"+sel_category+"'" + "order by _id desc";
        dbAction db = (dbAction)getActivity();
        Cursor cursor = db.database.rawQuery(sql, null);
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

        adapter.setItems(items); //B adapter에 데이터 추가
        adapter.notifyDataSetChanged(); //B adatper에 추가가 완료되었다고 알려주는 함수 호출

        return recordCount;
    }



}