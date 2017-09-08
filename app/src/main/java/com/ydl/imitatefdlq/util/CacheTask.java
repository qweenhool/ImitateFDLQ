package com.ydl.imitatefdlq.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.hss01248.dialog.StyledDialog;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 内存和硬盘缓存异步任务
 * Created by qweenhool on 2017/9/8.
 */

public class CacheTask extends AsyncTask<String, Void, Bitmap> {

    private Context context;
    private ImageView imageView;
    private DiskLruCache diskLruCache;
    private int resId = 0;

    public CacheTask(Context context, ImageView imageView, @DrawableRes int resId) {
        this.context = context;
        this.imageView = imageView;
        this.resId = resId;
        diskLruCache = DiskLruCacheUtil.getDiskLruCache(this.context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        StyledDialog.buildLoading("请稍后");
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String imageUrl = params[0];
        FileInputStream fileInputStream = null;
        FileDescriptor fileDescriptor = null;
        DiskLruCache.Snapshot snapshot;
        try {
            // 生成图片URL对应的key
            String key = MD5Encoder.encode(imageUrl);
            snapshot = diskLruCache.get(key);
            if (snapshot == null) {
                // 如果没有找到对应的缓存，则准备将imageUrl的图片写入缓存，默认缓存目录为xBitmapCache
                DiskLruCache.Editor editor = diskLruCache.edit(key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    DiskLruCacheUtil.cacheFile(imageUrl, outputStream);
                    editor.commit();
                }
                snapshot = diskLruCache.get(key);
            }
            if (snapshot != null) {
                fileInputStream = (FileInputStream) snapshot.getInputStream(0);
                fileDescriptor = fileInputStream.getFD();
            }
            // 将缓存数据解析成Bitmap对象
            Bitmap bitmap = null;
            if (fileInputStream != null) {
                //TODO 会造成失真！
                bitmap = BitmapUtil.decodeSampledBitmapFromFD(fileDescriptor, 150, 150);
            }
            if (bitmap != null) {
                // 将Bitmap对象添加到内存缓存当中
                LruCacheUtil.getInstance().addBitmapToMemoryCache(imageUrl, bitmap);
            }
            diskLruCache.flush();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileDescriptor == null && fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(resId);
        }
        StyledDialog.dismissLoading();
    }
}
