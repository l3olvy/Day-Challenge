package org.techtown.daychallenge.ui.Interface;

import android.view.View;

import org.techtown.daychallenge.ui.Feed.FeedAdapter;

public interface OnFeedItemClickListener {
    public void onItemClick(FeedAdapter.ViewHolder holder, View view, int index);
}
