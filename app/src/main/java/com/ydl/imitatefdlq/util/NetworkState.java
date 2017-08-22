package com.ydl.imitatefdlq.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by qweenhool on 2017/4/30.
 * 判断当前的网络状态，是否有网络连接，WiFi或者是移动数据。
 */

public class NetworkState {
    // 检查是否连接到网络
    // whether connect to internet
    public static boolean networkConnected(Context context){

        if (context != null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null)
                return info.isAvailable();
        }

        return false;
    }

    // 检查WiFi是否连接
    // if wifi connect
    public static boolean wifiConnected(Context context){
        if (context != null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null){
                if (info.getType() == ConnectivityManager.TYPE_WIFI)
                    return info.isAvailable();
            }
        }
        return false;
    }

    // 检查移动网络是否连接
    // if mobile data connect
    public static boolean mobileDataConnected(Context context){
        if (context != null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null){
                if (info.getType() == ConnectivityManager.TYPE_MOBILE)
                    return true;
            }
        }
        return false;
    }
}
