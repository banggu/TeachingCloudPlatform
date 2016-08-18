package com.scnu.bangzhu.teachingcloudplatform.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.scnu.bangzhu.qrcodelibrary.activity.CaptureActivity;
import com.scnu.bangzhu.teachingcloudplatform.R;
import com.scnu.bangzhu.teachingcloudplatform.adapter.SignManagementAdapter;
import com.scnu.bangzhu.teachingcloudplatform.model.StudentSign;
import com.scnu.bangzhu.teachingcloudplatform.util.AsyncHttpUtil;
import com.scnu.bangzhu.teachingcloudplatform.util.JSonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class TeacherSignActivity extends Activity implements View.OnClickListener,MaterialSpinner.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener{
    private ImageView iv_locationSign, iv_codeSign, iv_startSign;
    private ListView lv_signRecord;
    //联动下拉框
    private MaterialSpinner s_signTime, s_signCourse, s_signCondition;
    private List<String> signTimeList, signCourseList, signConditionList;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SignManagementAdapter signManagementAdapter;
    private List<StudentSign> signList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_sign);
        initView();
        bindView();
        setContents();
        setListeners();
    }

    private void initView() {
        iv_locationSign  = (ImageView) findViewById(R.id.iv_locationSign);
        iv_codeSign = (ImageView) findViewById(R.id.iv_codeSign);
        iv_startSign = (ImageView) findViewById(R.id.iv_startSign);
        lv_signRecord = (ListView) findViewById(R.id.lv_signRecord);
        s_signTime = (MaterialSpinner) findViewById(R.id.s_signTime);
        s_signCourse = (MaterialSpinner) findViewById(R.id.s_signCourse);
        s_signCondition = (MaterialSpinner) findViewById(R.id.s_signCondition);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
    }

    private void bindView(){
        signList = new ArrayList<>();
        signTimeList = new ArrayList<String>();
        signCourseList = new ArrayList<String>();
        signConditionList = new ArrayList<String>();
        signManagementAdapter = new SignManagementAdapter(this,signList);
        lv_signRecord.setAdapter(signManagementAdapter);

        // 第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeResources(R.color.swipe_scheme_color);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.swipe_bg_color);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);
    }

    private void setContents() {
        String[] strSignTimeList = getResources().getStringArray(R.array.signTime);
        String[] strSignConditionList = getResources().getStringArray(R.array.signCondition);
        for(int i=0;i<strSignConditionList.length;i++){
            signTimeList.add(strSignTimeList[i]);
            signConditionList.add(strSignConditionList[i]);
        }
        s_signTime.setItems(signTimeList);
        s_signCondition.setItems(signConditionList);
        getAllCourse();
        getAllStudentSign();
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);
        iv_locationSign.setOnClickListener(this);
        iv_codeSign.setOnClickListener(this);
        iv_startSign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_locationSign:
                Intent intent = new Intent(TeacherSignActivity.this, StudentSignActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_codeSign:
                Intent it = new Intent(TeacherSignActivity.this, CaptureActivity.class);
                startActivityForResult(it, 0);
                break;
            case R.id.iv_startSign:
                Intent i = new Intent(TeacherSignActivity.this, TeacherStartSignActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            String url = bundle.getString("result");
            Intent intent = new Intent(TeacherSignActivity.this, ShowZxingUrlActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        //请求数据，刷新列表

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

    }

    /**
     *  工具方法
     */
    private void getAllCourse(){
        AsyncHttpUtil.get("getAllCourse", null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject msg = response.optJSONObject("msg");
                            JSONArray jsonArray = msg.optJSONArray("course");
                            Log.i("HZWING", "course list is "+jsonArray.toString()+"===============");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject obj = (JSONObject) jsonArray.get(i);
                                signCourseList.add(obj.getString("courseName"));
                            }
//                            List<Course> courseList = JSonUtil.getJsonList(jsonArray.toString(), Course.class);
//                            for (Course course : courseList) {
//                                signCourseList.add(course.getCourseName());
//                            }
                            s_signCourse.setItems(signCourseList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getAllStudentSign(){
        AsyncHttpUtil.get("getAllStudentSign", null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject msg = response.optJSONObject("msg");
                            JSONArray jsonArray = msg.optJSONArray("studentSign");
                            List<StudentSign> studentSignList = JSonUtil.getJsonList(jsonArray.toString(), StudentSign.class);
                            signList.addAll(studentSignList);
                            signManagementAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
