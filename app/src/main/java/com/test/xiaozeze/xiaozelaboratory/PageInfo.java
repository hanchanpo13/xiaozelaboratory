package com.test.xiaozeze.xiaozelaboratory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description:
 * Author: fengzeyuan
 * Date: 2018/7/20 下午2:30
 * Version: 1.0
 */
public class PageInfo {

// --------------------- 页面信息注册 ------------------
    private static List<PageInfo> mPageList = new ArrayList<PageInfo>() {
        {
            add(new PageInfo("IconFont项目",Page0_IconFontActivity.class));
        }
    };

    public static List<PageInfo> getPageDispatchList() {
        return mPageList;
    }
    
    
    
//   -------------------------------- 对象功能----------------------------
    
    private Class<? extends Activity> mActivityClass;
    private String mPageName;
    private Map<String, Serializable> mParams;

    public PageInfo(String mPageName,Class<? extends Activity> activityClass) {
        mActivityClass = activityClass;
    }

    public PageInfo addParams(String key, Serializable value) {
        mParams.put(key, value);
        return this;
    }
    
    public void startActivity(Context cx) {
        Intent intent = new Intent(cx, mActivityClass);
        Set<String> keySet = mParams.keySet();
        for (String key : keySet) {
            Serializable value = mParams.get(key);
            intent.putExtra(key, value);
        }
        if (!(cx instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        cx.startActivity(intent);
    }

    public String getPageName() {
        return mPageName;
    }
   
}
