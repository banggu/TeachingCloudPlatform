package com.scnu.bangzhu.teachingcloudplatform.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scnu.bangzhu.teachingcloudplatform.MyApplication;
import com.scnu.bangzhu.teachingcloudplatform.R;
import com.scnu.bangzhu.teachingcloudplatform.model.Course;
import com.scnu.bangzhu.teachingcloudplatform.util.AsyncHttpUtil;
import com.scnu.bangzhu.teachingcloudplatform.util.JSonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bangzhu on 2016/7/28.
 */
public class TeacherSignActivity extends Activity implements View.OnClickListener{
    //百度地图相关
    private MapView mMapView;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private MyLocationConfiguration.LocationMode mLocationMode;

    private double mLongitude;
    private double mLatitude;
    private boolean isFirstLoc = true;
    //弹出对话框相关
    private AlertDialog mAlert;
    private AlertDialog.Builder mBuilder;
    private View mCustomView;
    private EditText mSignCourse, mSignClass;
    private WheelPicker mCourseChioce, mClassChioce, mWeekChioce;
    private List<String> mCourseList, mClassList, mWeekList;
    private Button mStartSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_sign);
        initView();
        bindView();
        initLocation();
        setListener();
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmap_teacher_sign);
        mBaiduMap = mMapView.getMap();

        mSignCourse = (EditText) findViewById(R.id.et_sign_course);
        mSignClass = (EditText) findViewById(R.id.et_sign_class);

        mStartSign = (Button) findViewById(R.id.btn_startSign);
    }

    private void bindView(){

        mCourseList = new ArrayList<>();
        mClassList = new ArrayList<>();
        mWeekList = new ArrayList<>();

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
        mStartSign.setOnClickListener(this);
        mSignCourse.setOnClickListener(this);
        mSignClass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_startSign:
                Toast.makeText(TeacherSignActivity.this, mLongitude+":"+mLatitude, Toast.LENGTH_LONG).show();
//                teacherStartSign();
                break;
            case R.id.et_sign_course:
            case R.id.et_sign_class:
                showChooseDialog();
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

    private void loadCourseAndClass(){
        AsyncHttpUtil.get("getAllCourse", null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject msg = response.optJSONObject("msg");
                JSONArray course = msg.optJSONArray("course");
                //List<Course> courseList = JSonUtil.getJsonList(course.toString(), Course.class);
                for(int i=0;i<course.length();i++){
                    try {
                        JSONObject obj  = (JSONObject) course.get(i);
                        mCourseList.add(obj.getString("courseName"));
                        mClassList.add(obj.getString("className"));
                        mWeekList.add(obj.getInt("courseWeekday")+"");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mCourseChioce.setData(mCourseList);
                mClassChioce.setData(mClassList);
                mWeekChioce.setData(mWeekList);
            }
        });
    }

    //打开滚轮选择器对话框
    private void showChooseDialog(){
        mBuilder = new AlertDialog.Builder(this);
        mCustomView = getLayoutInflater().inflate(R.layout.dialog_layout, null, false);
        mCourseChioce = (WheelPicker) mCustomView.findViewById(R.id.wp_course);
        mClassChioce = (WheelPicker) mCustomView.findViewById(R.id.wp_class);
        mWeekChioce = (WheelPicker) mCustomView.findViewById(R.id.wp_week);
        //设置滚轮选择器样式
        setWheelStyle();
        loadCourseAndClass();
        mBuilder.setView(mCustomView);
        mBuilder.setCancelable(true);
        mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int coursePosition = mCourseChioce.getCurrentItemPosition();
                int classPosition = mClassChioce.getCurrentItemPosition();
                mSignCourse.setText(mCourseList.get(coursePosition));
                mSignClass.setText(mClassList.get(classPosition));
            }
        });
        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAlert.dismiss();
            }
        });
        mAlert = mBuilder.create();
        mAlert.show();
    }

    //设置滚轮选择器样式
    private void setWheelStyle(){
        mCourseChioce.setCyclic(true);
        mCourseChioce.setIndicator(true);
        mCourseChioce.setIndicatorColor(R.color.indecator_color);
        mCourseChioce.setItemTextSize((int) getResources().getDimension(R.dimen.wheel_size));
        mCourseChioce.setCurtain(true);
        mCourseChioce.setCurtainColor(R.color.curtain_color);
        mCourseChioce.setAtmospheric(true);

        mClassChioce.setCyclic(true);
        mClassChioce.setIndicator(true);
        mClassChioce.setIndicatorColor(R.color.indecator_color);
        mClassChioce.setItemTextSize((int) getResources().getDimension(R.dimen.wheel_size));
        mClassChioce.setCurtain(true);
        mClassChioce.setCurtainColor(R.color.curtain_color);
        mClassChioce.setAtmospheric(true);

        mWeekChioce.setCyclic(true);
        mWeekChioce.setIndicator(true);
        mWeekChioce.setIndicatorColor(R.color.indecator_color);
        mWeekChioce.setItemTextSize((int) getResources().getDimension(R.dimen.wheel_size));
        mWeekChioce.setCurtain(true);
        mWeekChioce.setCurtainColor(R.color.curtain_color);
        mWeekChioce.setAtmospheric(true);
    }

    //教师发起考勤
    private void teacherStartSign(){
        String url = "";
        RequestParams params = new RequestParams();
        AsyncHttpUtil.post(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            }
        });
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
