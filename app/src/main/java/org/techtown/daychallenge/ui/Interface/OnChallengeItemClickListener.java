package org.techtown.daychallenge.ui.Interface;

import android.view.View;

import org.techtown.daychallenge.ui.Challenge.ChallengeAdapter;

public interface OnChallengeItemClickListener {
    public void onItemClick(ChallengeAdapter.ViewHolder holder, View view, int index);
}
