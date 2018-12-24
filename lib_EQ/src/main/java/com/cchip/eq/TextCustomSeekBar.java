package com.cchip.eq;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cchip.lib_eq.R;

public class TextCustomSeekBar extends RelativeLayout implements View.OnClickListener {
    private final String TAG = "TextCustomSeekBar";
    private ImageView mSeekBarSub, mSeekBarAdd;
    private TextView mFrequency, mTextProgress;
    private TextCustomSeekBar.OnTextCustomSeekbarChangeListenner OnTextCustomSeekbarChangeListenner;

    public ImageView getSeekBarSub() {
        return mSeekBarSub;
    }

    public void setSeekBarSub(ImageView mSeekBarSub) {
        this.mSeekBarSub = mSeekBarSub;
    }

    public ImageView getSeekBarAdd() {
        return mSeekBarAdd;
    }

    public void setSeekBarAdd(ImageView mSeekBarAdd) {
        this.mSeekBarAdd = mSeekBarAdd;
    }

    public TextView getFrequency() {
        return mFrequency;
    }

    public void setFrequency(TextView mFrequency) {
        this.mFrequency = mFrequency;
    }

    public TextView getTextProgress() {
        return mTextProgress;
    }

    public void setTextProgress(TextView mTextProgress) {
        this.mTextProgress = mTextProgress;
    }

    public CustomSeekBar getCustomSeekBar() {
        return mCustomSeekBar;
    }

    public void setCustomSeekBar(CustomSeekBar mCustomSeekBar) {
        this.mCustomSeekBar = mCustomSeekBar;
    }

    private CustomSeekBar mCustomSeekBar;

    public TextCustomSeekBar(Context context) {
        this(context, null);
    }

    public TextCustomSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextCustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_text_custom_seekbar, null);
        mSeekBarSub = inflate.findViewById(R.id.seekbar_sub);
        mSeekBarAdd = inflate.findViewById(R.id.seekbar_add);
        mSeekBarSub.setOnClickListener(this);
        mSeekBarAdd.setOnClickListener(this);
        mFrequency = inflate.findViewById(R.id.frequency);
        mTextProgress = inflate.findViewById(R.id.seekbar_progress);
        mCustomSeekBar = inflate.findViewById(R.id.custom_seekbar);
        mTextProgress.setText(mCustomSeekBar.getProgress() + "");
        mCustomSeekBar.setResponseOnTouch(mResponseOnTouch);
        addView(inflate);
    }

    CustomSeekBar.ResponseOnTouch mResponseOnTouch = new CustomSeekBar.ResponseOnTouch() {
        @Override
        public void onTouchResponse(int progress) {
            if (OnTextCustomSeekbarChangeListenner != null) {
                OnTextCustomSeekbarChangeListenner.onTouchResponse(progress);
            }
            mTextProgress.setText(progress + "");
        }
    };

    public void setProgress(int progress) {
        mTextProgress.setText(progress + "");
        mCustomSeekBar.setProgress(progress);
        Log.e(TAG, "setProgress: " + mCustomSeekBar);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.seekbar_sub) {//                setProgress(mCustomSeekBar.getProgress() - 1);
            mCustomSeekBar.setProgress(mCustomSeekBar.getProgress() - 1);
            mCustomSeekBar.listenner();

        } else if (i == R.id.seekbar_add) {//                setProgress(mCustomSeekBar.getProgress() + 1);
            mCustomSeekBar.setProgress(mCustomSeekBar.getProgress() + 1);
            mCustomSeekBar.listenner();

        }
    }


    public interface OnTextCustomSeekbarChangeListenner {
        void onTouchResponse(int progress);
    }

    public void setOnTextCustomSeekbarChangeListenner(OnTextCustomSeekbarChangeListenner listenner) {
        OnTextCustomSeekbarChangeListenner = listenner;
    }


}
