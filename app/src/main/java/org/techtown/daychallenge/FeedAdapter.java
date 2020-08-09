package org.techtown.daychallenge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>
        implements OnFeedItemClickListener{
    ArrayList<Feed> items = new ArrayList<Feed>();

    OnFeedItemClickListener listener;

    @NonNull
    @Override //B 뷰홀더 객체 생성
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.feed_item, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override //B 데이터를 뷰홀더에 바인딩
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) {
        Feed item = items.get(position);
        viewholder.setItem(item);
    }

    @Override //B 전체 아이템 개수 리턴
    public int getItemCount() { return items.size(); }

    public void addItem(Feed item) {
        items.add(item);
    }

    public void setItems(ArrayList<Feed> items) {
        this.items = items;
    }

    public Feed getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Feed item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnFeedItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView feedimage;

        public ViewHolder(View itemView, final OnFeedItemClickListener listener) {
            super(itemView);

            feedimage = itemView.findViewById(R.id.pictureImageView1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        //B Feed 프래그먼트의 아이템에 이미지 적용
        public void setItem(Feed item) {
            Uri picturePath = Uri.parse(item.getPicture());
            try{
                InputStream in = itemView.getContext().getContentResolver().openInputStream(picturePath);
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                feedimage.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e){ e.printStackTrace(); }

        }

    }
}
