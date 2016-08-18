package com.scnu.bangzhu.teachingcloudplatform.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by bangzhu on 2016/8/18.
 */
public class NetWorkUtil {
    private Context mContext;
    private ConnectivityManager mConnManager;

    private static NetWorkUtil instance;

    private NetWorkUtil(Context context){
        mContext = context;
        mConnManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static NetWorkUtil getInstance(Context context){
        if(instance == null){
            instance =  new NetWorkUtil(context);
        }
        return instance;
    }

    /**
     *
     * @return 网络连接是否可用
     */
    public boolean isNetWorkConnected(){
        NetworkInfo netWorkInfo = mConnManager.getActiveNetworkInfo();
        if(netWorkInfo != null){
            return netWorkInfo.isConnected();
        }
        return false;
    }

    /**
     *
     * @return WIFI是否连接可用
     */
    public boolean isWIFIConnected(){
        NetworkInfo wifiConn = mConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiConn != null){
            return wifiConn.isConnected();
        }
        return false;
    }

    /**
     *
     * @return 移动网络是否连接可用
     */
    public boolean isMobileConnected(){
        NetworkInfo mobileConn = mConnManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(mobileConn != null){
            return mobileConn.isConnected();
        }
        return false;
    }
}
