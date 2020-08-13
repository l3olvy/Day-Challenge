package org.techtown.daychallenge.ui.Interface;

import org.techtown.daychallenge.ChContent;
import org.techtown.daychallenge.ui.Challenge.Challenge;
import org.techtown.daychallenge.ui.Feed.Feed;

public interface OnTabItemSelectedListener {
    public void onFragmentChanged(int position);
    public void showPostFragment(Feed item);
    public void showPostFragment2(Challenge item);
    public void showPostFragment3(String picture, String ch_content, String content);
    public void challenge(ChContent item);
}

