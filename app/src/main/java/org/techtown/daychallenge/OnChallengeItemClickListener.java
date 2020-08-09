package org.techtown.daychallenge;

import android.view.View;

public interface OnChallengeItemClickListener {
    public void onItemClick(ChallengeAdapter.ViewHolder holder, View view, int index);
}
