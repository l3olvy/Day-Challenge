package org.techtown.daychallenge;

public interface OnTabItemSelectedListener {
    public void onFragmentChanged(int position);
    public void showPostFragment(Feed item);
    public void showPostFragment2(Challenge item);
    public void showPostFragment3(String picture, String content);
}

