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
import android.widget.Button;
import android.widget.TextView;

import org.techtown.daychallenge.MainActivity;
import org.techtown.daychallenge.R;


public class WritingFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
<<<<<<< Updated upstream
        writingViewModel =
                ViewModelProviders.of(this).get(WritingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_writing, container, false);
        final TextView textView = root.findViewById(R.id.text_writing);
        writingViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
=======
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_writing, container, false);

        Button close_btn = rootView.findViewById(R.id.closeButton);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(0);
>>>>>>> Stashed changes
            }
        });

        Button save_btn = rootView.findViewById(R.id.saveButton);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(3);
            }
        });

        Button delete_btn = rootView.findViewById(R.id.deleteButton);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(0);
            }
        });

        Button x_btn = rootView.findViewById(R.id.locationTextView);
        x_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(0);
            }
        });
        return rootView;
    }
}