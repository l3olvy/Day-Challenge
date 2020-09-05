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


import org.techtown.daychallenge.MainActivity;
import org.techtown.daychallenge.R;
import org.techtown.daychallenge.dbAction;
import org.techtown.daychallenge.ui.Challenge.ChallengeFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class WritingFragment extends Fragment {
    private Context context;
    ImageView picture_img;
    dbAction dayDB;
    Uri uri;
    static final int REQUEST_CODE=1;
    String ch;
    EditText con;
    boolean check;
    String nContent;

    @Override
    public void onResume() { // setText
        super.onResume();

        if(MainActivity.idx != 0) {
            con.setText(nContent);
        } else if(check == false) con.setText("");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        uri = null;
        context = container.getContext();
        dayDB = new dbAction(context);
        check = false;

        // 닫기버튼 누르면 Challenge로 넘어가도록 - 2020.07.30 송고은
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_writing, container, false);
        con = rootView.findViewById(R.id.contentsInput);
        picture_img = rootView.findViewById(R.id.pictureImageView);

        // 수정에서 넘어 온 경우 해당 내용 뿌려짐
        if(MainActivity.idx != 0) {
            updatePost(dayDB);
        }

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
                saveBtn(dayDB);
          }
        });

        Button delete_btn = rootView.findViewById(R.id.deleteBtn);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 삭제 버튼 클릭시
                picture_img.setImageDrawable(getResources().getDrawable(R.drawable.imagetoset));
                uri = null;
            }
        });

        picture_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //B 카메라 사용하기 위한 코드(권한?을 위해 데이터 주고받는 느낌)
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);
                check = true;
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
        nContent = con.getText().toString();
        Log.d("content", nContent);

        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(context, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == REQUEST_CODE) {
            uri = data.getData();
        }
        setImage(uri);
    }

    private void setImage(Uri uri) {
        try {
            InputStream in = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            picture_img.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e){ e.printStackTrace(); }
    }

    //B poat 테이블에 Challenge 넣어주기위해 전달받음
    public void setItem(String ch) {
        this.ch = ch;
    }

    public void updatePost(dbAction dayDB) {
        ArrayList data = dayDB.upSel(MainActivity.idx);
        nContent = (String) data.get(1);

        String nuri = (String) data.get(0);
        if(nuri.length() > 0) {
            Uri oUri = Uri.parse((String)data.get(0));
            uri = oUri;
            setImage(oUri);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveBtn(dbAction dayDB) {;
        MainActivity activity = (MainActivity) getActivity();

        String contents = con.getText().toString();
        String imageUri;
        if(uri != null) {
            imageUri = uri.toString();
        } else {
            imageUri = "";
        }

        if(contents.equals("") && imageUri.length() <= 0) {
            Toast.makeText(context,"글 또는 사진을 채워 주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(MainActivity.idx == 0) { // 삽입하는 것
            dayDB.insertRecord("post", ChallengeFragment.cate, ch, contents, imageUri);
            dayDB.enable(ch);
        } else { // 0이 아니라는 것은 수정하기에서 온 것
            // 공백으로 저장하고 싶은 경우 어떡하지? -> 공백으로 들어가는데
            if(contents == "" || contents == null) contents = " ";
            dayDB.updateRecord(contents, imageUri, MainActivity.idx);
        }

        activity.showPostFragment3(imageUri, ch, contents);
        uri = null;
        check = false;
        activity.onFragmentChanged(4); //B Post로 전환

    }

}