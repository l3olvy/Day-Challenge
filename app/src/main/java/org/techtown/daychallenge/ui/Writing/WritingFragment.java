package org.techtown.daychallenge.ui.Writing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.techtown.daychallenge.ChContent;
import org.techtown.daychallenge.MainActivity;
import org.techtown.daychallenge.R;
import org.techtown.daychallenge.dbAction;
import org.techtown.daychallenge.ui.Challenge.ChallengeFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class WritingFragment extends Fragment {
    private Context context;
    ImageView picture_img;
    dbAction dayDB;
    Uri uri;
    static final int REQUEST_CODE=1;
    ChContent item;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();

        // 닫기버튼 누르면 Challenge로 넘어가도록 - 2020.07.30 송고은
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_writing, container, false);
        Button closeBtn = rootView.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(1); //B Challenge로 전환
            }
        });

        // 2020.08.04 송고은
        Button save_btn = rootView.findViewById(R.id.saveBtn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) { // saveBtn 클릭시

                MainActivity activity = (MainActivity) getActivity();

                EditText con = rootView.findViewById(R.id.contentsInput);
                String contents = con.getText().toString();

                dayDB = new dbAction(context);
                String imageUri;
                if(uri != null){
                    imageUri = uri.toString();
                    setImage(uri);
                }else{
                    imageUri = "";
                }


                dayDB.insertRecord("post", ChallengeFragment.cate, item.getCh_content(), contents, imageUri);
                activity.showPostFragment3(imageUri, item.getCh_content(), contents);
                dayDB.enable(item.getId());
                con.setText("");
                activity.onFragmentChanged(4); //B Post로 전환

            }

        });

        Button delete_btn = rootView.findViewById(R.id.deleteBtn);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(1); //B Challenge로 전환
            }
        });

        picture_img = rootView.findViewById(R.id.pictureImageView);
        picture_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //B 카메라 사용하기 위한 코드(권한?을 위해 데이터 주고받는 느낌)
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        return rootView;
    }

    // 권한에 대한 응답이 있을 때 작동하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //권한을 허용 했을 경우
        if(requestCode == 1){
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                    Log.d("MainActivity","권한 허용 : " + permissions[i]);
                }
            }
        }
    }

    // 2020.08.04 카메라 저장 코드
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(context, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == REQUEST_CODE) { uri = data.getData(); }
        setImage(uri);

    }

    private void setImage(Uri uri) {
        try{
            InputStream in = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            picture_img.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e){ e.printStackTrace(); }
    }

    public void setItem(ChContent item) {
        this.item = item;
    }

}