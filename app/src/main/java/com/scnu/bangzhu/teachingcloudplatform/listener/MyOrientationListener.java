package com.scnu.bangzhu.teachingcloudplatform.listener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by bangzhu on 2016/6/22.
 */
public class MyOrientationListener implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Context mContext;

    private float lastX;
    private OnOrientationListener mOnOrientationListener;

    public MyOrientationListener(Context context){
        this.mContext = context;
    }

    public void start(){
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager != null){
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if(mSensor != null){
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop(){
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            float x = event.values[SensorManager.DATA_X];
            if(Math.abs(x - lastX)>1.0){
                if(mOnOrientationListener != null){
                    mOnOrientationListener.OnOrientationChanged(x);
                }
            }
            lastX = x;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setOnOrientationListener(OnOrientationListener mOnOrientationListener) {
        this.mOnOrientationListener = mOnOrientationListener;
    }

    public interface OnOrientationListener{
        void OnOrientationChanged(float x);
    }
}
