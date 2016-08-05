package com.scnu.bangzhu.teachingcloudplatform.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.scnu.bangzhu.teachingcloudplatform.MyApplication;
import com.scnu.bangzhu.teachingcloudplatform.R;
import com.scnu.bangzhu.teachingcloudplatform.listener.MyOrientationListener;
import com.scnu.bangzhu.teachingcloudplatform.util.DistanceUtil;

import java.util.List;

/**
 * Created by bangzhu on 2016/6/12.
 */
public class BaiduMapActivity extends Activity implements View.OnClickListener{
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private Button mstartSign;
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private NotifyLister mNotifyer;
    private boolean isFirstLoc = true;
    private double mLongitude;
    private double mLatitude;
    //自定义定位图标
    private BitmapDescriptor mLocationIcon;
    private MyOrientationListener myOrientationListener;
    //当前方向参数
    private float mCurrentDirection;
    //模式参数
    private MyLocationConfiguration.LocationMode mLocationMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_baidu_map);
        initView();
        initLocation();
        addMarker();
        setListener();
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mstartSign = (Button) findViewById(R.id.btn_startSign);
    }

    private void initLocation() {
        //初始化自定义图标
        mLocationIcon  = BitmapDescriptorFactory.fromResource(R.drawable.location_arrow);
        //定位模式
        mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
        //初始化地图核心类
        mLocationClient = ((MyApplication)getApplication()).getBdLocationClient();
        //初始化监听接口
        myLocationListener = new MyLocationListener();
        mNotifyer = new NotifyLister();
        mLocationClient.registerLocationListener(myLocationListener);
        mNotifyer.SetNotifyLocation(23.1467,113.3537,30,"bd09ll");
        mLocationClient.registerNotify(mNotifyer);
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
        //初始化方向监听器，方向监听器回调参数返回方向参数x
        myOrientationListener = new MyOrientationListener(this);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void OnOrientationChanged(float x) {
                mCurrentDirection = x;
            }
        });
    }

    private void addMarker() {
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                InfoWindow infoWindow;
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(R.color.marker_bg);
                textView.setPadding(30, 20, 30, 50);
                textView.setText("经度为" + mLatitude + "，纬度为：" + mLongitude);

                final LatLng ll = new LatLng(mLatitude, mLongitude);
                Point p = mBaiduMap.getProjection().toScreenLocation(ll);
                p.y -= 47;
                LatLng l = mBaiduMap.getProjection().fromScreenLocation(p);
                infoWindow = new InfoWindow(textView, l, 1000);
                mBaiduMap.showInfoWindow(infoWindow);
                return true;
            }
        });
    }

    private void setListener() {
        mstartSign.setOnClickListener(this);
    }

    //地图监听接口
    class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if(bdLocation == null){
                return;
            }
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .direction(mCurrentDirection).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            //设置自定义图标
            MyLocationConfiguration config = new MyLocationConfiguration(mLocationMode,
                    true, mLocationIcon);
            mBaiduMap.setMyLocationConfigeration(config);
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

    //地图提醒类，用于
    class NotifyLister extends BDNotifyListener {
        public void onNotify(BDLocation mlocation, float distance){
            Toast.makeText(BaiduMapActivity.this, "可以进行签到"+"距离为："+distance, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //开始定位
        mLocationClient.start();
        //开启方向传感器
        myOrientationListener.start();
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
        //关闭方向传感器
        myOrientationListener.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_startSign:
                double distance = DistanceUtil.getDistance(mLongitude, mLatitude, 114.13224, 22.60957);
                if(distance > 20){
                    Log.i("HZWING", mLongitude+":"+mLatitude);
                    Toast.makeText(this, "超出范围，无法签到,"+"距离为："+distance+"米", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "可以进行签到,"+"距离为："+distance+"米", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(1, 1, 1, "普通地图");
        menu.add(1, 2, 2, "卫星地图");
        menu.add(1, 3, 3, "实时交通");
        menu.add(1, 4, 4, "我的位置");
        menu.add(1, 5, 5, "普遍模式");
        menu.add(1, 6, 6, "跟随模式");
        menu.add(1, 7, 7, "罗盘模式");
        menu.add(1, 8, 8, "显示我的位置");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case 1:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case 2:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case 3:
                if(mBaiduMap.isTrafficEnabled()){
                    mBaiduMap.setTrafficEnabled(false);
                    item.setTitle("实时交通（OFF）");
                }else{
                    mBaiduMap.setTrafficEnabled(true);
                    item.setTitle("实时交通（ON）");
                }
                break;
            case 4:
                returnToCurrentLocation();
                break;
            case 5:
                mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
                break;
            case 6:
                mLocationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                break;
            case 7:
                mLocationMode = MyLocationConfiguration.LocationMode.COMPASS;
                break;
            case 8:
               addOverLays();
                Toast.makeText(this, mLatitude+"=="+mLongitude, Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addOverLays() {
        mBaiduMap.clear();
        LatLng latLng = null;
        Marker marker = null;
        OverlayOptions overlayOptions;
        latLng = new LatLng(mLatitude, mLongitude);
        //图标
        overlayOptions = new MarkerOptions().position(latLng).icon(mLocationIcon).zIndex(5);
        mBaiduMap.addOverlay(overlayOptions);

        //更新位置
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(latLng, 16);
        mBaiduMap.setMapStatus(msu);

    }

    //返回当前位置
    public void returnToCurrentLocation(){
        LatLng ll = new LatLng(mLatitude, mLongitude);
        MapStatusUpdate mapUpdate = MapStatusUpdateFactory.newLatLngZoom(ll, 16);
        mBaiduMap.animateMapStatus(mapUpdate);
    }


}
