package com.ydl.imitatefdlq.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * 将srcPath的文件写入到outputStream中
     *
     * @param srcPath
     * @param outputStream
     */
    public static void cacheFile(String srcPath, OutputStream outputStream) {
        try {
            int byteSum = 0;
            int byteRead = 0;
            File srcFile = new File(srcPath);
            if (srcFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(srcPath); //读入原文件,读入到代码中
                byte[] buffer = new byte[1444];
                while ((byteRead = inStream.read(buffer)) != -1) {
                    byteSum += byteRead; //字节数 文件大小
                    outputStream.write(buffer, 0, byteRead);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
