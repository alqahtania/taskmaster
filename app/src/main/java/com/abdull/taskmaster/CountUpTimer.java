package com.abdull.taskmaster;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Abdullah Alqahtani on 9/24/2020.
 */
public class CountUpTimer extends CountDownTimer {
    private static final String TAG = "CountUpTimer";

    private TextView mTvTimer;

    private int mSecondsCountUp;
    private int mMinutesCountUp;
    private int mHoursCountUp;
    private int mDaysCountUp;
    private boolean minsUp = false;
    private boolean hoursUp = false;
    private boolean daysUp = false;

    public CountUpTimer(long millisInFuture, long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);

        /*if(context != null){
            this.mContext = context;
        }else {
            throw new IllegalArgumentException("Must provide a context for the CountUpTimer");
        }*/
        mTvTimer = textView;
        if(mTvTimer == null){
            throw new IllegalArgumentException("TextView not found");
        }
        mSecondsCountUp = 0;
        mMinutesCountUp = 0;
        mHoursCountUp = 0;
        mDaysCountUp = 0;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mSecondsCountUp++;
        if (mSecondsCountUp == 60) {
            mMinutesCountUp++;
            mSecondsCountUp = 0;

        }
        if (mMinutesCountUp == 60) {
            mHoursCountUp++;
            mMinutesCountUp = 0;

        }
        if (mHoursCountUp == 24) {
            mDaysCountUp++;
            mHoursCountUp = 0;

        }

        String seconds = String.format(Locale.getDefault(),"%02d", mSecondsCountUp);
        String minutes = String.format(Locale.getDefault(), "%02d", mMinutesCountUp) + ":";
        String hours = String.format(Locale.getDefault(),"%02d", mHoursCountUp) + ":";
        String days = String.format(Locale.getDefault(),"%02d", mDaysCountUp) + ":";


        if(mMinutesCountUp > 0){
            minsUp = true;
        }
        if(mHoursCountUp > 0){
            hoursUp = true;
            minsUp = false;
        }
        if(mDaysCountUp > 0){
            daysUp = true;
            hoursUp = false;
        }
        if(!minsUp && !hoursUp && !daysUp){
            mTvTimer.setText(seconds);
        }
        if(minsUp){
            mTvTimer.setText(minutes  + seconds);
        }
        if(hoursUp){
            mTvTimer.setText(hours + minutes + seconds);
        }
        if(daysUp){
            mTvTimer.setText(days + hours + minutes + seconds);
        }
    }

    public void resetTimer(){
        mSecondsCountUp = 0;
        mMinutesCountUp = 0;
        mHoursCountUp = 0;
        mDaysCountUp = 0;
        minsUp = false;
        hoursUp = false;
        daysUp = false;
        if(mTvTimer != null){
            mTvTimer.setText("");
        }
    }

    @Override
    public void onFinish() {
        this.start();
    }

    public int getSecondsCountUp() {
        return mSecondsCountUp;
    }

    public void setSecondsCountUp(int secondsCountUp) {
        mSecondsCountUp = secondsCountUp;
    }

    public int getMinutesCountUp() {
        return mMinutesCountUp;
    }

    public void setMinutesCountUp(int minutesCountUp) {
        mMinutesCountUp = minutesCountUp;
    }

    public int getHoursCountUp() {
        return mHoursCountUp;
    }

    public void setHoursCountUp(int hoursCountUp) {
        mHoursCountUp = hoursCountUp;
    }

    public int getDaysCountUp() {
        return mDaysCountUp;
    }

    public void setDaysCountUp(int daysCountUp) {
        mDaysCountUp = daysCountUp;
    }
}
