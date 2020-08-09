package org.techtown.daychallenge.ui.Challenge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.techtown.daychallenge.MainActivity;
import org.techtown.daychallenge.R;


public class ChallengeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_challenge, container, false);

        Button write_btn = rootView.findViewById(R.id.write);
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(2);
                // 챌린지 내용 불러오면서 idx도 설정해줌
                activity.idx = 2;
            }
        });
        return rootView;
    }
}