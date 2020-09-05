package org.techtown.daychallenge;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.techtown.daychallenge.ui.Challenge.ChallengeFragment;


public class TerminationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //B 앱 강제종료 시 실행
    @Override
    public void onTaskRemoved(Intent rootIntent) { //핸들링 하는 부분
        ((MainActivity)MainActivity.tContext).save(ChallengeFragment.music, ChallengeFragment.drawing, ChallengeFragment.happiness, ChallengeFragment.photo);
        ((MainActivity)MainActivity.tContext).change(ChallengeFragment.m_change, ChallengeFragment.d_change, ChallengeFragment.h_change, ChallengeFragment.p_change);

        Log.e("Error","onTaskRemoved - 강제 종료 " + rootIntent);
        Toast.makeText(this, "onTaskRemoved ", Toast.LENGTH_SHORT).show();
        stopSelf(); //서비스 종료
    }

}
