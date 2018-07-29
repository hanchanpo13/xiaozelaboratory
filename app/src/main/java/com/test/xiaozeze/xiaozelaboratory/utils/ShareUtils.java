package com.test.xiaozeze.xiaozelaboratory.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.content.FileProvider;

import com.test.xiaozeze.xiaozelaboratory.BuildConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: fengzeyuan
 * Date: 2018/7/29 下午2:54
 * Version: 1.0
 */
public class ShareUtils {

    /**
     * 发送文件
     *
     * @param context
     * @param path
     */
    public static void sendFileByOtherApp(Context context, String path) {
        File file = new File(path);
        if (file.exists()) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            Uri data;
            // 判断版本大于等于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                data = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", new File(path));
                // 给目标应用一个临时授权
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                data = Uri.fromFile(new File(path));
            }
            shareIntent.putExtra(Intent.EXTRA_STREAM, data);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setType(getMimeType(path));
            List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(shareIntent, 0);
            if (!resInfo.isEmpty()) {
                ArrayList<Intent> targetIntents = new ArrayList<>();
                for (ResolveInfo info : resInfo) {
                    ActivityInfo activityInfo = info.activityInfo;
                    if (activityInfo.packageName.contains("com.tencent.mobileqq")
                            || activityInfo.packageName.contains("com.tencent.mm")) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setPackage(activityInfo.packageName);
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        intent.setClassName(activityInfo.packageName, activityInfo.name);
                        targetIntents.add(intent);
                    }


                }
                Intent chooser = Intent.createChooser(targetIntents.remove(0), "Send mail...");
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.toArray(new Parcelable[]{}));
                context.startActivity(chooser);
            }
        }

    }


    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param filePath
     */
    public static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "text/plain";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }


}
