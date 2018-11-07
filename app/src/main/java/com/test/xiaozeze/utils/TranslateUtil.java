package com.test.xiaozeze.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class TranslateUtil {

    private static final String APP_ID = "20180920000209994";
    private static final String xx = "xhj_uNauf_reiFNw8cj6";

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
            URL url = new URL(getTranslateUrl(content,sourceLan, targetLan));
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
//            urlConn.setRequestProperty("User-Agent", USER_AGENT);
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
            JSONObject jsonObject = new JSONObject(googleResult);
            JSONArray jsonArray = jsonObject.getJSONArray("trans_result");
            for (int i = 0; i < jsonArray.length(); i++) {
                result += jsonArray.getJSONObject(i).getString("dst");
            }
        } catch (Exception e) {
//            result = "翻译失败";
            e.printStackTrace();
            result = "";
        }
        return result;
    }


    /**
     * 获取请求
     * @param content
     * @param from
     * @param to
     * @return
     */
    private static String getTranslateUrl(String content, String from, String to) {
        String salt = System.currentTimeMillis() + "";
        return String.format("http://api.fanyi.baidu.com/api/trans/vip/translate?q=%s&from=%s&to=%s&appid=%s&salt=%s&sign=%s",
                EncodeUtils.urlEncode(content, "UTF-8"), from, to, APP_ID, salt, getSign(content,salt));
    }

    /**
     * 获取验签
     * @param content
     * @return
     */
    private static String getSign(String content,String salt) {
        String str = APP_ID + content + salt + xx;
        return EncryptUtils.encryptMD5ToString(str).toLowerCase();

    }
}
