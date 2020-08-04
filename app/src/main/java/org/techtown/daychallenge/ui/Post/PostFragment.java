package org.techtown.daychallenge.ui.Post;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.techtown.daychallenge.R;
import org.techtown.daychallenge.dbAction;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class PostFragment extends Fragment {
    dbAction dayDB;
    ImageView post_img;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post, container, false);

        dayDB = new dbAction();
        dayDB.textView = rootView.findViewById(R.id.textView3);
        post_img = rootView.findViewById(R.id.post_img);
        Uri uris = Uri.parse(dayDB.executeQuery(dayDB.cate));
        setImage(uris);

        return rootView;
    }

    private void setImage(Uri uri) {
        try{
            InputStream in = getContext().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            post_img.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e){ e.printStackTrace(); }
    }
}