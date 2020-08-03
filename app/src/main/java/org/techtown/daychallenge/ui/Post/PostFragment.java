package org.techtown.daychallenge.ui.Post;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.techtown.daychallenge.R;
import org.techtown.daychallenge.dbAction;


public class PostFragment extends Fragment {
    dbAction dayDB;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post, container, false);

        dayDB = new dbAction();
        dayDB.textView = rootView.findViewById(R.id.textView3);
        dayDB.executeQuery(dayDB.cate);
        return rootView;
    }
}