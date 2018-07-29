package com.test.xiaozeze.xiaozelaboratory.utils;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TranslateUtil {
    public static final String TRANSLATE_BASE_URL = "https://translate.google.cn/"; // 不需要翻墙即可使用
    //    public static final String TRANSLATE_SINGLE_URL = "https://translate.google.cn/translate_a/single?client=gtx&sl=en&tl=zh&dt=t&q=Do%20not%20work%20overtime%20tonight";
    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

    /**
     * 翻译，包含http请求，需要异步，返回""则为翻译失败
     *
     * @param sourceLan 源语言，如en，自动检测为auto
     * @param targetLan 目标语言如zh
     * @param content   翻译文本
     * @return ""为翻译失败，其余成功
     */
    public static String translate(String sourceLan, String targetLan, String content) {
        String result = "";
        if (content == null || content.equals("")) {
            return result;
        }
        try {
            String googleResult = "";
            // 新建一个URL对象
            URL url = new URL(getTranslateUrl(sourceLan, targetLan, content));
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            urlConn.setRequestProperty("User-Agent", USER_AGENT);
            //设置请求中的媒体类型信息。
//            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
//            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            int statusCode = urlConn.getResponseCode();
            if (statusCode == 200) {
                // 获取返回的数据
                googleResult = Utils.getStringFromInputStream(urlConn.getInputStream());
            }
            // 关闭连接
            urlConn.disconnect();

            // 处理返回结果，拼接
            JSONArray jsonArray = new JSONArray(googleResult).getJSONArray(0);
            for (int i = 0; i < jsonArray.length(); i++) {
                result += jsonArray.getJSONArray(i).getString(0);
            }
        } catch (Exception e) {
//            result = "翻译失败";
            e.printStackTrace();
            result = "";
        }
        return result;
    }



    private static String getTranslateUrl(String sourceLan, String targetLan, String content) {
        try {
            return TRANSLATE_BASE_URL + "translate_a/single?client=gtx&sl=" + sourceLan + "&tl=" + targetLan + "&dt=t&q=" + URLEncoder.encode(content, "UTF-8");
        } catch (Exception e) {
            return TRANSLATE_BASE_URL + "translate_a/single?client=gtx&sl=" + sourceLan + "&tl=" + targetLan + "&dt=t&q=" + content;
        }
    }
}
