package com.mingbaipintu;

import android.content.Intent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by CCX on 2018/05/28.
 */

public class UpdateTitleTimer {
    public static final String TIMER_BROADCAST="com.mingbaipintu.TIMER_BROADCAST";
    public static final String TIME="time";
    private Timer mTimer;//计时器
    private MyTimerTask myTimerTask;
    private int mUserdTime=0;
    private boolean mIsPause=false;

    private static UpdateTitleTimer mUpdateTitleTimer=null;
    private UpdateTitleTimer()
    {
    }
    public static UpdateTitleTimer getInstance()
    {
        if(mUpdateTitleTimer==null)
        {
            mUpdateTitleTimer=new UpdateTitleTimer();
        }
        return mUpdateTitleTimer;
    }
    public int concelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = null;
        myTimerTask = null;
        return mUserdTime;
    }

    public void startTimer(int time) {
        mUserdTime=time;
        mTimer = new Timer();
        myTimerTask=new MyTimerTask();
        mTimer.schedule(myTimerTask, 1000, 1000);//每1000ms执行一次进程

    }
    public void pause()
    {
        if(GameManager.getInstance().ismIsGaming()) {
            concelTimer();
        }
    }
    public void continueTimer()
    {
        if(GameManager.getInstance().ismIsGaming()) {
            startTimer(mUserdTime);
        }
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            mUserdTime++;
            Intent intent =new Intent(TIMER_BROADCAST);
            intent.putExtra(TIME,mUserdTime);
            GameManager.getInstance().getmLocalBroadcastManager().sendBroadcast(intent);
        }
    }
}
