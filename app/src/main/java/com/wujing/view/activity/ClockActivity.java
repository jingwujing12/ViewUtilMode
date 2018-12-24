package com.wujing.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.wujing.view.R;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.feeeei.circleseekbar.CircleOutSizeClockSeekBar;
import io.feeeei.circleseekbar.CircleSeekBar;

public class ClockActivity extends FragmentActivity {
    @BindView(R.id.bedtime_icon)
    ImageView mBedtimeIcon;
    @BindView(R.id.sleep_sound_icon)
    ImageView mSleepSoundIcon;
    @BindView(R.id.wake_up_icon)
    ImageView mWakeUpIcon;
    @BindView(R.id.tv_bedtime_end_time)
    TextView mBedtimeTime;
    @BindView(R.id.tv_bedtime_start_time)
    TextView mBedtimeProcessTime;
    @BindView(R.id.tv_sleep_sound_end_time)
    TextView mSleepSoundEndTime;
    @BindView(R.id.tv_sleep_sound_start_time)
    TextView mSleepSoundStartTime;
    @BindView(R.id.tv_wake_up_end_time)
    TextView mWakeUpTime;
    @BindView(R.id.tv_wake_up_start_time)
    TextView mWakeUpProcessTime;
    @BindView(R.id.seek_sleep)
    CircleSeekBar mSeekSleep;
    @BindView(R.id.circle_clock_seek)
    CircleOutSizeClockSeekBar mCircleOutSizeClockSeekBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        ButterKnife.bind(this);
        initUI();
    }
    public static void startActivity(Context context) {
        Intent intent = new Intent(context,ClockActivity.class);
        context.startActivity(intent);
    }

    private void initUI() {
        mSeekSleep.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onChanged(CircleSeekBar seekbar, int curStartProcess, int curEndProcess) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));//**TimeZone时区，加上这句话就解决啦**
                String format = simpleDateFormat.format(curStartProcess * 1000 * 60);
                String format2 = simpleDateFormat.format(curEndProcess * 1000 * 60);
                mSleepSoundStartTime.setText(format);
                mSleepSoundEndTime.setText(format2);
            }
        });
        mCircleOutSizeClockSeekBar.setOnSeekBarChangeListener(new CircleOutSizeClockSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onChanged(CircleOutSizeClockSeekBar seekbar, int curStartProcess, int curEndProcess, int curStartInProcess, int curEndInProcess) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));//**TimeZone时区，加上这句话就解决啦**
                String format = simpleDateFormat.format(curStartProcess * 1000 * 60);
                String formatProcess = simpleDateFormat.format(mCircleOutSizeClockSeekBar.getDegreeCycleSubAngle(curStartProcess - curStartInProcess, 720 * 2) * 1000 * 60);
                String format2 = simpleDateFormat.format(curEndProcess * 1000 * 60);
                String format2Process = simpleDateFormat.format(mCircleOutSizeClockSeekBar.getDegreeCycleSubAngle(curEndProcess - curEndInProcess, 720 * 2) * 1000 * 60);
                mBedtimeTime.setText(format);
                mBedtimeProcessTime.setText(formatProcess);
                mWakeUpTime.setText(format2);
                mWakeUpProcessTime.setText(format2Process);
            }
        });
    }
}
