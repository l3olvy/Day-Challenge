package org.techtown.daychallenge.ui.Post;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.techtown.daychallenge.Challenge;
import org.techtown.daychallenge.Feed;
import org.techtown.daychallenge.OnTabItemSelectedListener;
import org.techtown.daychallenge.R;
import org.techtown.daychallenge.dbAction;
import org.techtown.daychallenge.ui.Challenge.ChallengeFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class PostFragment extends Fragment {

    dbAction dayDB = new dbAction();

    Context context;
    OnTabItemSelectedListener listener;
    TextView contentsInput;
    ImageView pictureImageView;

    //B 나중에 데이터 수정 시에 필요할 거 같아서 넣어놓았음
    public static final int MODE_INSERT = 1;
    public static final int MODE_MODIFY = 2;
    int mMode = MODE_INSERT;
    Feed item;
    Challenge item2;

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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post, container, false);


        initUI(rootView);

        applyItem();

        return rootView;
    }

    private void initUI(ViewGroup rootView) {
        contentsInput = rootView.findViewById(R.id.textView3);
        pictureImageView = rootView.findViewById(R.id.post_img);
    }

    public void setContents(String data) {
        contentsInput.setText(data);
    }

    //B Post 프래그먼트 이미지 삽입 함수
    public void setPicture(Uri picturePath) {
        try{
            InputStream in = getContext().getContentResolver().openInputStream(picturePath);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            pictureImageView.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e){ e.printStackTrace(); }
    }

    //B MainActivity에서 Feed와 Challege 각각 item 정보 전달
    public void setItem(Feed item) {
        this.item = item;
    }
    public void setItem2(Challenge item2){this.item2 = item2;}

    public void applyItem() {

        if (item != null) {
            mMode = MODE_MODIFY;

            setContents(item.getContent());

            String picturePath = item.getPicture();
            if (picturePath == null || picturePath.equals("")) {
                pictureImageView.setImageResource(R.drawable.gradation);
            } else {
                Uri uri = Uri.parse(picturePath);
                mMode = MODE_INSERT;
                setPicture(uri);
            }


        } else if (item2 != null) {
            mMode = MODE_MODIFY;

            setContents(item2.getContent());

            String picturePath = item2.getPicture();
            if (picturePath == null || picturePath.equals("")) {
                pictureImageView.setImageResource(R.drawable.gradation);
            } else {
                Uri uri = Uri.parse(picturePath);
                mMode = MODE_INSERT;
                setPicture(uri);
            }

        } else {
            contentsInput.setText("");
            dayDB.textView = contentsInput;
            Uri uris = Uri.parse(dayDB.executeQuery(ChallengeFragment.cate));
            setPicture(uris);

        }

    }

}