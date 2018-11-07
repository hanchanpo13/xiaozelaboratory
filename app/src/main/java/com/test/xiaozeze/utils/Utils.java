package com.test.xiaozeze.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.List;

/**
 * @Description:
 * @Author: fengzeyuan
 * @Date: 18/1/30 下午1:52
 * @Version: 1.0
 */
public class Utils {

    private static Context mContext;

    public static void init(Context cx) {
        mContext = cx.getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public static void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    /**
     * desc 获取app在sd卡中的文件夹,如不存在sd卡，则放入Data目录
     */
    public static String getFilePath(String dirName) {
        return String.format("%s/%s", getBasePath(), dirName);
    }

    @NonNull
    public static String getBasePath() {
        String packageName = getContext().getPackageName();
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = String.format("%s/%s", Environment.getExternalStorageDirectory(), packageName);
        } else {
            path = String.format("%s/%s", getContext().getFilesDir().getAbsolutePath(), packageName);
        }
        File dir = new File(path);
        if (!dir.exists() && !dir.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdir();
        }
        return path;
    }


    /**
     * 获取本apk所在目录
     *
     * @return
     */
    public static String getApkPath() {
        ApplicationInfo applicationInfo = getContext().getApplicationInfo();
        return applicationInfo.sourceDir;
    }

    /**
     * 根据文件路径拷贝文件
     *
     * @param fromPath
     * @param destPath
     * @return boolean 成功true、失败false
     */
    public static boolean copyFile(String fromPath, String destPath) {
        boolean result = false;
        if ((fromPath == null) || (destPath == null)) {
            return result;
        }
        File src = new File(fromPath);
        if (!src.exists()) {
            return result;
        }
        File dest = new File(destPath);
        if (dest != null && dest.exists()) {
            dest.delete(); // delete file
        }
        try {
            dest.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileChannel srcChannel = null;
        FileChannel dstChannel = null;

        try {
            srcChannel = new FileInputStream(src).getChannel();
            dstChannel = new FileOutputStream(dest).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), dstChannel);
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        } finally {
            close(srcChannel);
            close(dstChannel);
        }
        return result;
    }


    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getFileMd5(String path) {
        File file = new File(path);
        if (file.exists()) {
            MessageDigest digest;
            FileInputStream in;
            byte buffer[] = new byte[1024];
            int len;
            try {
                digest = MessageDigest.getInstance("MD5");
                in = new FileInputStream(file);
                while ((len = in.read(buffer, 0, 1024)) != -1) {
                    digest.update(buffer, 0, len);
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        }
        return null;
    }

    public static void installAPK(String appPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file:///" + appPath),
                "application/vnd.android.package-archive");
        getContext().startActivity(intent);
    }

    /**
     * 获取已安装应用信息（不包含系统自带）
     */
    public static String getAppPath(String pkgName) {
        @SuppressLint("WrongConstant")
        List<ApplicationInfo> apps = getContext().getPackageManager().getInstalledApplications(PackageManager.GET_SIGNATURES);
        for (ApplicationInfo info : apps) {
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                // 非系统应用
                if (pkgName.equalsIgnoreCase(info.packageName))
                    return info.sourceDir;
            }
        }
        return "";
    }

    public static File getServicePatchFile(final String endFix) {
        File baseDirectory = new File(Utils.getBasePath());
        if (baseDirectory.exists() && baseDirectory.isDirectory()) {
            File[] files = baseDirectory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name != null && name.endsWith(endFix);
                }
            });
            if (files != null && files.length > 0) {
                return files[0];
            }
        }
        return null;
    }


    public static String getStringFromInputStream(InputStream a_is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(a_is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }
}
