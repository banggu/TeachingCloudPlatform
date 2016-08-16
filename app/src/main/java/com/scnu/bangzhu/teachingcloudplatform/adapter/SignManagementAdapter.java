package com.scnu.bangzhu.teachingcloudplatform.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scnu.bangzhu.teachingcloudplatform.R;
import com.scnu.bangzhu.teachingcloudplatform.model.Sign;
import com.scnu.bangzhu.teachingcloudplatform.model.StudentSign;

import java.util.List;

/**
 * Created by bangzhu on 2016/6/7.
 */
public class SignManagementAdapter extends BaseAdapter{
    private Context mContext;
    private List<StudentSign> mSignList;
    
    public SignManagementAdapter(Context context, List<StudentSign> signList){
        mContext = context;
        mSignList = signList;
    }

    @Override
    public int getCount() {
        return mSignList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSignList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        StudentSign sign = mSignList.get(position);
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sign_item, null);
            viewHolder.signCondition = (TextView) convertView.findViewById(R.id.tv_signCondition);
            viewHolder.signStuName = (TextView) convertView.findViewById(R.id.tv_signStuName);
            viewHolder.signTime = (TextView) convertView.findViewById(R.id.tv_signTime);
            viewHolder.signDate = (TextView) convertView.findViewById(R.id.tv_signDate);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(sign.getIsSigned() == 0){
            viewHolder.signCondition.setText("缺勤");
            viewHolder.signCondition.setBackground(mContext.getResources().getDrawable(R.drawable.sign_absence));
        }else {
            if (sign.getIsSigned() == 1) {
                viewHolder.signCondition.setText("已签");
//            viewHolder.signCondition.setBackgroundColor(R.color.success_green);
                viewHolder.signCondition.setBackground(mContext.getResources().getDrawable(R.drawable.sign_attendance));
            }
        }
        viewHolder.signStuName.setText(sign.getName());
        viewHolder.signTime.setText(sign.getSignTime());
        return convertView;
    }

    public final class ViewHolder{
        public TextView signCondition;
        public TextView signStuName;
        public TextView signTime;
        public TextView signDate;
    }
}
