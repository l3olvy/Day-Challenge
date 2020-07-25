package org.techtown.daychallenge.ui.Writing;

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


public class WritingFragment extends Fragment {
    private WritingViewModel writingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        writingViewModel =
                ViewModelProviders.of(this).get(WritingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_writing, container, false);
        final TextView textView = root.findViewById(R.id.text_writing);
        writingViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}