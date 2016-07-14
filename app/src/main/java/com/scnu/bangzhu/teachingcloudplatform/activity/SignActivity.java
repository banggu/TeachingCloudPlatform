package com.scnu.bangzhu.teachingcloudplatform.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.scnu.bangzhu.qrcodelibrary.activity.CaptureActivity;
import com.scnu.bangzhu.teachingcloudplatform.R;
import com.scnu.bangzhu.teachingcloudplatform.adapter.SignManagementAdapter;
import com.scnu.bangzhu.teachingcloudplatform.model.Sign;

import java.util.ArrayList;
import java.util.List;


public class SignActivity extends Activity implements View.OnClickListener,AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener{
    private ImageView iv_locationSign, iv_codeSign;
    private ListView lv_signRecord;
    private Spinner s_signTime, s_signCourse, s_signCondition;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayAdapter signTimeAdapter, signNameAdapter, signConditionAdapter;
    private SignManagementAdapter signManagementAdapter;
    private List<String> signTimesList, signCourseList, signConditionList;
    private List<Sign> signList;
    private String[] names = {"赵永远", "陆安然", "井柏然"};
    private String[] times = {"9:30", "10:30", "11:15"};
    private int[] signImg = {0,0,1};
    private String[] signTimes = {"本月", "本周", "今天"};
    private String[] signCourse = {"信息安全原理", "编译原理", "JavaEE"};
    private String[] signCondition = {"全部", "已签", "未签"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initView();
        setContents();
        setListeners();
    }

    private void initView() {

        iv_locationSign  = (ImageView) findViewById(R.id.iv_locationSign);
        iv_codeSign = (ImageView) findViewById(R.id.iv_codeSign);
        lv_signRecord = (ListView) findViewById(R.id.lv_signRecord);
        s_signTime = (Spinner) findViewById(R.id.s_signTime);
        s_signCourse = (Spinner) findViewById(R.id.s_signCourse);
        s_signCondition = (Spinner) findViewById(R.id.s_signCondition);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);

        signList = new ArrayList<>();
        signTimesList = new ArrayList<>();
        signCourseList = new ArrayList<>();
        signConditionList = new ArrayList<>();

        signManagementAdapter = new SignManagementAdapter(this,signList);
        signTimeAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, signTimesList);
        signNameAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, signCourseList);
        signConditionAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, signConditionList);

    }

    private void setContents() {
        lv_signRecord.setAdapter(signManagementAdapter);
        s_signTime.setAdapter(signTimeAdapter);
        s_signCourse.setAdapter(signNameAdapter);
        s_signCondition.setAdapter(signConditionAdapter);

        // 第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeResources(R.color.swipe_scheme_color);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.swipe_bg_color);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        //设置临时数据
        for(int i=0;i<names.length;i++){
            Sign s = new Sign();
            s.signCondition = signImg[i];
            s.courseName = names[i];
            s.signTime = times[i];
            signList.add(s);

            signTimesList.add(signTimes[i]);
            signCourseList.add(signCourse[i]);
            signConditionList.add(signCondition[i]);
        }
        signManagementAdapter.notifyDataSetChanged();
        signTimeAdapter.notifyDataSetChanged();
        signNameAdapter.notifyDataSetChanged();
        signConditionAdapter.notifyDataSetChanged();
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);
        iv_locationSign.setOnClickListener(this);
        iv_codeSign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_locationSign:
                Intent intent = new Intent(SignActivity.this, BaiduMapActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_codeSign:
                Intent it = new Intent(SignActivity.this, CaptureActivity.class);
                startActivityForResult(it, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            String url = bundle.getString("result");
            Intent intent = new Intent(SignActivity.this, ShowZxingUrlActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(view.getId()){
            case R.id.s_signTime:
                break;
            case R.id.s_signCourse:
                break;
            case R.id.s_signCondition:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRefresh() {
        //请求数据，刷新列表

        swipeRefreshLayout.setRefreshing(false);
    }
}
