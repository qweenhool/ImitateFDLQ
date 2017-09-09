package com.ydl.imitatefdlq.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by qweenhool on 2017/9/5.
 */

public class DiskLruCacheUtil {

    public static DiskLruCache getDiskLruCache(Context context) {
        DiskLruCache mDiskLruCache = null;
        try {
            File cacheDir = getDiskCacheDir(context, "xBitmapCache");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            //设置50M的缓存
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 50 * 1024 * 1024);
            return mDiskLruCache;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public static void cacheFile(String srcPath, OutputStream out) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            fis = new FileInputStream(srcPath);
            bis = new BufferedInputStream(fis, 8 * 1024);
            bos = new BufferedOutputStream(out, 8 * 1024);
            int len;
            while ((len = bis.read()) != -1) {
                bos.write(len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {//先关闭
                    bos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
