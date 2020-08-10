package org.techtown.daychallenge.ui.Category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.techtown.daychallenge.MainActivity;
import org.techtown.daychallenge.R;
import org.techtown.daychallenge.dbAction;
import org.techtown.daychallenge.ui.Challenge.ChallengeFragment;

public class CategoryFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_category, container, false);

        final dbAction dayDB = new dbAction(getContext());
        // 각 카테고리마다 db category로 바뀌도록

        Button music_btn = rootView.findViewById(R.id.btn_music);
        music_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = MainActivity.manager.findFragmentById(R.id.nav_host_fragment);
                //B 이동버튼 클릭할 때 stack에 push
                MainActivity.fragmentStack.push(currentFragment);
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(1);
                ChallengeFragment.cate = "MUSIC"; //B Challenge 프래그먼트의 loadNoteListData(cate) 인자로 전달
            }
        });

        Button drawing_btn = rootView.findViewById(R.id.btn_drawing);
        drawing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = MainActivity.manager.findFragmentById(R.id.nav_host_fragment);
                //B 이동버튼 클릭할 때 stack에 push
                MainActivity.fragmentStack.push(currentFragment);
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(1);
                ChallengeFragment.cate = "DRAWING";
            }
        });

        Button happiness_btn = rootView.findViewById(R.id.btn_happiness);
        happiness_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = MainActivity.manager.findFragmentById(R.id.nav_host_fragment);
                //B 이동버튼 클릭할 때 stack에 push
                MainActivity.fragmentStack.push(currentFragment);
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(1);
                ChallengeFragment.cate = "HAPPINESS";
            }
        });

        return rootView;
    }
}