package org.techtown.daychallenge.ui.Challenge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.daychallenge.ui.Interface.OnChallengeItemClickListener;
import org.techtown.daychallenge.R;

import java.util.ArrayList;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ViewHolder>
        implements OnChallengeItemClickListener {
    ArrayList<Challenge> items = new ArrayList<Challenge>();

    OnChallengeItemClickListener listener;

    @NonNull
    @Override //B 뷰홀더 객체 생성
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.challenge_item, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override //B 데이터를 뷰홀더에 바인딩
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) {
        Challenge item = items.get(position);
        viewholder.setItem(item);
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void addItem(Challenge item) {
        items.add(item);
    }

    public void setItems(ArrayList<Challenge> items) {
        this.items = items;
    }

    public Challenge getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Challenge item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnChallengeItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView ch_content;
        TextView date;

        public ViewHolder(View itemView, final OnChallengeItemClickListener listener) {
            super(itemView);

            ch_content = itemView.findViewById(R.id.completed_challenge);
            date = itemView.findViewById(R.id.date);

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

        //B Challenge 프래그먼트의 아이템에 (현재는)작성내용과 날짜 적용
        public void setItem(Challenge item) {
            ch_content.setText(item.getCh_content()); //B 이거 바꾸면 Challenge Fragment 아이템에 ch_content 내용 뜸. 챌린지 테이블 안 넣어서 일단 구분 위해 작성 내용으로 해놓음
            date.setText(item.getRdate());
        }

    }
}