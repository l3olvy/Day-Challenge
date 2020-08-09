package org.techtown.daychallenge;

import android.view.View;

public interface OnFeedItemClickListener {
    public void onItemClick(FeedAdapter.ViewHolder holder, View view, int index);
}
