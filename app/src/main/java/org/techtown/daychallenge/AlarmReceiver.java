package org.techtown.daychallenge;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.techtown.daychallenge.ui.Challenge.ChallengeFragment;


import java.util.Calendar;


import static android.content.Context.MODE_PRIVATE;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingI = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

        //OREO API 26 이상에서는 채널 필요
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남

            String channelName ="매일 알람 채널";
            String description = "매일 정해진 시간에 알람합니다.";
            int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌

            NotificationChannel channel = new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);

            if (notificationManager != null) {
                // 노티피케이션 채널을 시스템에 등록
                notificationManager.createNotificationChannel(channel);
            }
        }else builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        //B 정해진 시간에 puch 알람 띄움
        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())

                .setTicker("{Time to watch some cool stuff!}")
                .setContentTitle("Day Challenge")
                .setContentText("새로운 챌린지가 도착하였습니다.")
                .setContentInfo("INFO")
                .setContentIntent(pendingI);

        //B 정해진 시간에 Challenge 내용 넣음
        dbAction db = new dbAction(context);
        ChallengeFragment.m_items = db.getChallenge("MUSIC");
        ChallengeFragment.d_items = db.getChallenge("DRAWING");
        ChallengeFragment.h_items = db.getChallenge("HAPPINESS");
        ChallengeFragment.p_items = db.getChallenge("PHOTO");
        ChallengeFragment.m_change = 1;
        ChallengeFragment.d_change = 1;
        ChallengeFragment.h_change = 1;
        ChallengeFragment.p_change = 1;

        // m_chg = setSharedPreferences("m_change", MODE_PRIVATE);
        // set을 하면 (강종 했을 때만) 알람이 안와...

        if (notificationManager != null) {

            // 노티피케이션 동작시킴
            notificationManager.notify(1234, builder.build());

            Calendar nextNotifyTime = Calendar.getInstance();

            //내일 같은 시간으로 알람시간 결정
            nextNotifyTime.add(Calendar.DATE, 1);

            //  Preference에 설정한 값 저장
            SharedPreferences.Editor editor = context.getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
            editor.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis());
            editor.apply();

        }

        // set을 if 밑에서 쓰면 알람은 오는데 설정이 안돼...
    }
}
