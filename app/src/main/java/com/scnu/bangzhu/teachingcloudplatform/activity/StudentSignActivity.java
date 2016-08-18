package com.scnu.bangzhu.teachingcloudplatform.activity;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scnu.bangzhu.teachingcloudplatform.MyApplication;
import com.scnu.bangzhu.teachingcloudplatform.R;
import com.scnu.bangzhu.teachingcloudplatform.listener.MyOrientationListener;
import com.scnu.bangzhu.teachingcloudplatform.util.AsyncHttpUtil;
import com.scnu.bangzhu.teachingcloudplatform.util.DistanceUtil;
import com.scnu.bangzhu.teachingcloudplatform.util.NetWorkUtil;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bangzhu on 2016/6/12.
 */
public class StudentSignActivity extends Activity implements View.OnClickListener{
    //地图相关
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private NotifyLister mNotifyer;
    private boolean isFirstLoc = true;
    private double mLongitude;
    private double mLatitude;
    //其他视图
    private Button mstartSign;
    private LinearLayout mNetworkError;
    private TextView mSignCourse, mErrorTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_sign);
        initView();
        initLocation();
        setListener();
//        setContent();
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mstartSign = (Button) findViewById(R.id.btn_startSign);
        mNetworkError = (LinearLayout) findViewById(R.id.ll_network_err);
        mErrorTip = (TextView) findViewById(R.id.tv_error_tip);
    }

    private void initLocation() {
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

    }

    private void setListener() {
        mstartSign.setOnClickListener(this);
        //设置地图加载完成监听
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (NetWorkUtil.getInstance(StudentSignActivity.this).isNetWorkConnected()) {
                    mNetworkError.setVisibility(View.GONE);
                } else {
                    mNetworkError.setVisibility(View.VISIBLE);
                    mErrorTip.setText(getResources().getText(R.string.network_error));
                }
            }
        });
    }

    //请求并获取可签到的课程信息
    private void setContent(){
        String url = "";
        RequestParams params = new RequestParams();
        AsyncHttpUtil.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //请求成功
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //请求数据失败
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_startSign:
                if(NetWorkUtil.getInstance(StudentSignActivity.this).isNetWorkConnected()){
                    mNetworkError.setVisibility(View.GONE);
                    double distance = DistanceUtil.getDistance(mLongitude, mLatitude, 114.13224, 22.60957);
                    if(distance > 15){
                        Log.i("HZWING", mLongitude+":"+mLatitude);
                        mNetworkError.setVisibility(View.VISIBLE);
                        mErrorTip.setText(getResources().getText(R.string.over_range));
                        Toast.makeText(this, "超出范围，无法签到,"+"距离为："+distance+"米", Toast.LENGTH_LONG).show();
                    }else{
                        mNetworkError.setVisibility(View.GONE);
                        Toast.makeText(this, "可以进行签到,"+"距离为："+distance+"米", Toast.LENGTH_LONG).show();
//                        studentSign();
                    }
                }else{
                    mNetworkError.setVisibility(View.VISIBLE);
                    mErrorTip.setText(getResources().getText(R.string.network_error));
                }
                break;
        }
    }

    //学生签到
    private void studentSign(){
        String url = "";
        RequestParams params = new RequestParams();
        AsyncHttpUtil.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //请求成功
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //请求数据失败
            }
        });
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
                    .direction(100).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            //设置自定义图标
//            MyLocationConfiguration config = new MyLocationConfiguration(mLocationMode,
//                    true, mLocationIcon);
//            mBaiduMap.setMyLocationConfigeration(config);
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
            Toast.makeText(StudentSignActivity.this, "可以进行签到"+"距离为："+distance, Toast.LENGTH_LONG).show();
        }
    }

    private void addOverLays() {
//        mBaiduMap.clear();
//        LatLng latLng = null;
//        Marker marker = null;
//        OverlayOptions overlayOptions;
//        latLng = new LatLng(mLatitude, mLongitude);
//        //图标
//        overlayOptions = new MarkerOptions().position(latLng).icon(mLocationIcon).zIndex(5);
//        mBaiduMap.addOverlay(overlayOptions);
//
//        //更新位置
//        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(latLng, 16);
//        mBaiduMap.setMapStatus(msu);

    }

    //返回当前位置
    public void returnToCurrentLocation(){
        LatLng ll = new LatLng(mLatitude, mLongitude);
        MapStatusUpdate mapUpdate = MapStatusUpdateFactory.newLatLngZoom(ll, 16);
        mBaiduMap.animateMapStatus(mapUpdate);
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
