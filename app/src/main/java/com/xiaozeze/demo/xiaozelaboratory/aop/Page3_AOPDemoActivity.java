package com.xiaozeze.demo.xiaozelaboratory.aop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.test.xiaozeze.xiaozelaboratory.R;

/**
 * @Description:
 * @Author: fengzeyuan
 * @Date: 18/1/30 上午1:00
 * @Version: 1.0
 */
public class Page3_AOPDemoActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        findViewById(R.id.btn_change_icon).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        
    }
}
