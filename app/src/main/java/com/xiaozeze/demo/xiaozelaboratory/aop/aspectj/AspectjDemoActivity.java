package com.xiaozeze.demo.xiaozelaboratory.aop.aspectj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.test.xiaozeze.xiaozelaboratory.R;

/**
 * @Description:
 * @Author: fengzeyuan
 * @Date: 18/1/30 上午1:00
 * @Version: 1.0
 */
public class AspectjDemoActivity extends AppCompatActivity implements View.OnClickListener {

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
