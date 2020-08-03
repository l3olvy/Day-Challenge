package org.techtown.daychallenge.ui.Writing;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
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

import java.io.InputStream;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class WritingFragment extends Fragment {
    private Context context;
    ImageView picture_img;
    dbAction dayDB;
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
                activity.onFragmentChanged(0);
            }
        });


        // 2020.08.04 송고은
        Button save_btn = rootView.findViewById(R.id.saveBtn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) { // saveBtn 클릭시
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(3);

                // 이후에 해야할 작업 db 챌린지들 불러와서 여기에 넣어야함, 이미지 파일 작업
                EditText con = rootView.findViewById(R.id.contentsInput);
                String contents = con.getText().toString();
                dayDB = new dbAction();
                dayDB.insertRecord("post", dayDB.cate, "음악 들어", contents, "photo.img");
                con.setText("");
            }
        });

        Button delete_btn = rootView.findViewById(R.id.deleteBtn);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(0);
            }
        });

        picture_img = rootView.findViewById(R.id.pictureImageView);
        picture_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, 101);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            try {
                InputStream is = getActivity().getContentResolver().openInputStream(data.getData());
                Bitmap bm = BitmapFactory.decodeStream(is);
                is.close();
                picture_img.setImageBitmap(bm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(requestCode == 101 && resultCode == RESULT_CANCELED){
                Toast.makeText(context,"취소", Toast.LENGTH_SHORT).show();
        }
    }

}