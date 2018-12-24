package com.wujing.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;


import com.cchip.eq.CustomRecyclerView;
import com.cchip.eq.NumberPickerView;
import com.wujing.view.Cos.Constants;
import com.wujing.view.R;
import com.wujing.view.adapter.EqListviewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wujing.view.Cos.Constants.RESET_EQ;


public class EQActivity extends FragmentActivity implements  NumberPickerView.OnValueChangeListener {
    @BindView(R.id.recyclerview_horizontal)
    CustomRecyclerView mRecyclerview;
    @BindView(R.id.numberpicker_eq)
    NumberPickerView numberPicker;
    private SharedPreferences sp;
    private int position;
    private String[] eqStrs;
    private EqListviewAdapter mEqListviewAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context,EQActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eq);
        ButterKnife.bind(this);
        sp = getSharedPreferences(Constants.SP_NAME,
                Context.MODE_PRIVATE);
        position = sp.getInt(Constants.SP_EQ, RESET_EQ);
        initUI();
    }


    public void initUI() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mEqListviewAdapter = new EqListviewAdapter(this);
        mRecyclerview.setAdapter(mEqListviewAdapter);
//        mEqListviewAdapter.onShow(true);
        mEqListviewAdapter.setOnTextCustomSeekbarChangeListenner(mOnTextCustomSeekbarChangeListenner);
        eqStrs = getResources().getStringArray(R.array.eq_text);
        mEqListviewAdapter.onShow(true);
//        mSppManager.getEQData();
        numberPicker.setMaxValue(eqStrs.length - 1);
        numberPicker.setValue(position);
        mEqListviewAdapter.setMode(position);

//        mBleManager.getEQMode();
//        mSppManager.getEQData();
//        mController.onGetEqMode(this);
        numberPicker.setOnValueChangedListener(this);

    }


    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        changeMode(newVal);
    }

    private void changeMode(int newVal) {
        position = newVal;
        mEqListviewAdapter.setMode(position);
    }

    @OnClick({R.id.lay_base_left, R.id.tv_comfirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_base_left:
                numberPicker.setValue(RESET_EQ);
                sp.edit().putInt(Constants.SP_EQ, RESET_EQ).commit();
//                mBleManager.restoreEQ();
                mEqListviewAdapter.reset();
                break;
            case R.id.tv_comfirm:
                position = numberPicker.getValue();
                sp.edit().putInt(Constants.SP_EQ, position).commit();
                ColoseActivity();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            sp.edit().putInt(Constants.SP_EQ, position).commit();
            ColoseActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void ColoseActivity() {
        finish();
//        overridePendingTransition(R.anim.translate_null, R.anim.translate_out);
    }

    @Override
    public void onDestroy() {
        if (mEqListviewAdapter != null) {
            mEqListviewAdapter.onShow(false);
        }
        super.onDestroy();

    }
    EqListviewAdapter.OnTextCustomSeekbarChangeListenner mOnTextCustomSeekbarChangeListenner = new EqListviewAdapter.OnTextCustomSeekbarChangeListenner() {
        @Override
        public void onTouchResponse(View view, int gain) {
            int position = (int) view.getTag();
            mEqListviewAdapter.setEditItem(position, gain);
            if (numberPicker.getValue() != RESET_EQ) {
                numberPicker.setValue(RESET_EQ);
                EQActivity.this.position = RESET_EQ;
//                mSppManager.PeakingEQ(mEqListviewAdapter.getTenEq(), RESET_EQ);
            } else {
//                mSppManager.setSingleEQ(gain, position);
            }

        }
    };


}
