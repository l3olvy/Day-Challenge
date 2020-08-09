package org.techtown.daychallenge.ui.Feed;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.daychallenge.Feed;
import org.techtown.daychallenge.FeedAdapter;
import org.techtown.daychallenge.MainActivity;
import org.techtown.daychallenge.OnFeedItemClickListener;
import org.techtown.daychallenge.OnTabItemSelectedListener;
import org.techtown.daychallenge.R;
import org.techtown.daychallenge.dbAction;

public class FeedFragment extends Fragment {

    RecyclerView recyclerView;
    FeedAdapter adapter;
    Feed item;
    Context context;
    OnTabItemSelectedListener listener;


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

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_feed, container, false);

        initUI(rootView);

        // 데이터 로딩
        loadFeedListData();

        return rootView;
    }

    private void initUI(ViewGroup rootView) {

        recyclerView = rootView.findViewById(R.id.feed_recyclerview);

        //B 한 행에 아이템 세 개씩 격자로 위치
        int numberOfCol = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfCol);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new FeedAdapter();

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnFeedItemClickListener() {
            @Override
            public void onItemClick(FeedAdapter.ViewHolder holder, View view, int position) {

                Fragment currentFragment = MainActivity.manager.findFragmentById(R.id.nav_host_fragment);
                //B 이동버튼 클릭할 때 stack에 push
                MainActivity.fragmentStack.push(currentFragment);

                //B Feed 리싸이클러뷰에 있는 아이템 클릭 시 Adapter 이용해서 Item 정보 갖고오고,
                //listener 이용해서 MainActivity에 정보 전달 (프래그먼트끼리 데이터 주고받으려면 엑티비티 거쳐야하기때문!)
                item = adapter.getItem(position);

                if (listener != null) {
                    listener.showPostFragment(item);
                }

            }
        });

    }


    /**
     * 리스트 데이터 로딩
     */
    public void loadFeedListData() {
        dbAction db = new dbAction(getContext());
        adapter.setItems(db.FeedSel()); //B adapter에 데이터 추가
        adapter.notifyDataSetChanged(); //B adatper에 추가가 완료되었다고 알려주는 함수 호출

       // return recordCount;
    }

}