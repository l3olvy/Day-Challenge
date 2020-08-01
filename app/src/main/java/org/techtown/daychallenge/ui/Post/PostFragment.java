package org.techtown.daychallenge.ui.Post;

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


public class PostFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
<<<<<<< Updated upstream
        postViewModel =
                ViewModelProviders.of(this).get(PostViewModel.class);
        View root = inflater.inflate(R.layout.fragment_post, container, false);
        final TextView textView = root.findViewById(R.id.text_post);
        postViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
=======
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post, container, false);



        return rootView;
>>>>>>> Stashed changes
    }
}