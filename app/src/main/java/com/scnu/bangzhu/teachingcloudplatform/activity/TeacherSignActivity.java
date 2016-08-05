package com.scnu.bangzhu.teachingcloudplatform.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.scnu.bangzhu.teachingcloudplatform.MyApplication;
import com.scnu.bangzhu.teachingcloudplatform.R;

/**
 * Created by bangzhu on 2016/7/28.
 */
public class TeacherSignActivity extends Activity implements View.OnClickListener{
    private MapView mMapView;
    private Button mUseCurLoc;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private MyLocationConfiguration.LocationMode mLocationMode;

    private double mLongitude;
    private double mLatitude;
    private boolean isFirstLoc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_sign);
        initView();
        initLocation();
        setListener();
    }

    private void initView() {
        mUseCurLoc = (Button) findViewById(R.id.btn_useCurLoc);
        mMapView = (MapView) findViewById(R.id.bmap_teacher_sign);
        mBaiduMap = mMapView.getMap();
    }

    private void initLocation() {
        mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
        //获取定位核心类
        mLocationClient = ((MyApplication)getApplication()).getBdLocationClient();
        //初始化定位监听接口
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //设置地图参数
        LocationClientOption option = new LocationClientOption();
        //设置坐标系
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        //设置定位模式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置是否打开gps
        option.setOpenGps(true);
        //设置刷新间隔
        option.setScanSpan(3000);
        mLocationClient.setLocOption(option);
    }

    private void setListener(){
        mUseCurLoc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_useCurLoc:
                Toast.makeText(TeacherSignActivity.this, mLongitude+":"+mLatitude, Toast.LENGTH_LONG).show();
                break;
        }
    }

    //地图监听接口
    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if(bdLocation == null){
                return;
            }
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .direction(100).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);

            //更新经纬度
            mLongitude = bdLocation.getLongitude();
            mLatitude = bdLocation.getLatitude();

            if(isFirstLoc){
                //获取坐标点
                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate mapUpdate = MapStatusUpdateFactory.newLatLngZoom(ll, 16);
                mBaiduMap.animateMapStatus(mapUpdate);
                isFirstLoc = false;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //开始定位
        mLocationClient.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
