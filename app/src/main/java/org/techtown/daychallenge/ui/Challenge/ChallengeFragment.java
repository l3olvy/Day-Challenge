package org.techtown.daychallenge.ui.Challenge;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.daychallenge.Challenge;
import org.techtown.daychallenge.ChallengeAdapter;
import org.techtown.daychallenge.MainActivity;
import org.techtown.daychallenge.OnChallengeItemClickListener;
import org.techtown.daychallenge.OnTabItemSelectedListener;
import org.techtown.daychallenge.R;
import org.techtown.daychallenge.dbAction;

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
                activity.onFragmentChanged(2);
                // 챌린지 내용 불러오면서 idx도 설정해줌
                activity.idx = 2;

                activity.onFragmentChanged(3); //B Writing 화면으로 전환
            }
        });

        initUI(rootView);

        // 데이터 로딩
        loadChListData(cate);

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
    public void loadChListData(String sel_category) {
        dbAction db = new dbAction(getContext());

        adapter.setItems(db.ChallengeSel(sel_category)); //B adapter에 데이터 추가
        adapter.notifyDataSetChanged(); //B adatper에 추가가 완료되었다고 알려주는 함수 호출

        //return recordCount;
    }



}