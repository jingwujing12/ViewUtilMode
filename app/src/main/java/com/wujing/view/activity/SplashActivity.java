package com.wujing.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.view.View;

import com.wujing.view.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

    }
    @OnClick({R.id.clock, R.id.btn_eq})
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.clock) {
            ClockActivity.startActivity(this);
        }else if(id==R.id.btn_eq){
            EQActivity.startActivity(this);
        }
    }
}
