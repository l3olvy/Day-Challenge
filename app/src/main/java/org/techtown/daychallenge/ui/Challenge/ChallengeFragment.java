package org.techtown.daychallenge.ui.Challenge;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.techtown.daychallenge.R;
import org.techtown.daychallenge.ui.Category.CategoryViewModel;


public class ChallengeFragment extends Fragment {
    private ChallengeViewModel challengeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        challengeViewModel =
                ViewModelProviders.of(this).get(ChallengeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_challenge, container, false);
        final TextView textView = root.findViewById(R.id.text_challenge);
        challengeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}