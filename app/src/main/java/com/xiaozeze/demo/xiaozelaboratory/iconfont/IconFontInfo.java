package com.xiaozeze.demo.xiaozelaboratory.iconfont;

import android.content.Context;

import com.test.xiaozeze.xiaozelaboratory.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description:
 * Author: fengzeyuan
 * Date: 2018/7/29 下午4:01
 * Version: 1.0
 */ // 数据处理
public class IconFontInfo {


    public static Map<String, List<IconFontInfo>> types = new HashMap<>();

    public static void add(String type, String name, String code4Show, String code4XML) {
        IconFontInfo info = new IconFontInfo(type,  name, code4Show,code4XML);
        if (!types.containsKey(type)) {
            types.put(type, new ArrayList<IconFontInfo>());
        }
        List<IconFontInfo> iconFontInfos = types.get(type);
        iconFontInfos.add(info);
    }

    /**
     * 获取全部数据
     * @return
     */
    public static List<IconFontInfo> getAllList() {
        List<IconFontInfo> list = new ArrayList<>();
        Set<Map.Entry<String, List<IconFontInfo>>> entries = types.entrySet();
        for (Map.Entry<String, List<IconFontInfo>> entry : entries) {
            List<IconFontInfo> infoList = entry.getValue();
            list.addAll(infoList);
        }
        return list;
    }

    /**
     * 获取需要翻译的数据
     * @param cx
     * @return
     */
    public static Map<String, List<IconFontInfo>> getTranslateList(Context cx) {
        List<String> oldIconCode = Arrays.asList(cx.getResources().getStringArray(R.array.old_icon));
        Map<String, List<IconFontInfo>> allInfo = new HashMap<>();
        allInfo.putAll(types);
        Set<Map.Entry<String, List<IconFontInfo>>> entries = allInfo.entrySet();
        for (Map.Entry<String, List<IconFontInfo>> entry : entries) {
            List<IconFontInfo> infoList = entry.getValue();
            Iterator<IconFontInfo> iterator = infoList.iterator();
            while (iterator.hasNext()){
                IconFontInfo fontInfo = iterator.next();
                if (oldIconCode.contains(fontInfo.code4Show)) {
                    iterator.remove();
                }
            }
            if (infoList.isEmpty()) {
                entries.remove(infoList);
            }
        }
        return allInfo;
    }

    public static void clear() {
        types.clear();
    }

//        _________________________________________________________________________

    public String type;
    public String name;
    public String code4Show;
    public String code4XML;

    public IconFontInfo(String type, String name, String code4Show, String code4XML) {
        this.type = type;
        this.name = name;
        this.code4Show = code4Show;
        this.code4XML = code4XML;
    }


}
