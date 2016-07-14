package com.scnu.bangzhu.teachingcloudplatform;

import android.app.Application;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by bangzhu on 2016/6/21.
 */
public class MyApplication extends Application {
    private LocationClient bdLocationClient;
    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        bdLocationClient = new LocationClient(getApplicationContext());
    }

    public LocationClient getBdLocationClient(){
        return bdLocationClient;
    }
}
