package com.cstor.tanjiance.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * BaseActivity
 *
 * @author CJZ
 * @Time 2019/7/8
 */
public class BaseActivity extends Activity {

    /**
     * 每个页面唯一标识tag，不需要子页面做优化处理 处理
     */
    public String Tag;


    public String TAG;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tag = getPackageName() + "." + getClass().getSimpleName();
        TAG = getClass().getSimpleName();

    }




}
