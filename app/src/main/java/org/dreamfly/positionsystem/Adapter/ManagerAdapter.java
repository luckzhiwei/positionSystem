package org.dreamfly.positionsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.Map;

import org.dreamfly.positionsystem.Activity.ManagerActivity;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.bean.Manager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by asus on 2015/1/15.
 */

public class ManagerAdapter extends BaseAdapter {
    private ManagerActivity mManagerActivity;
    private Context context;
    private List<Manager> mMangerList;//适配器中应该含有的容器,

    public ManagerAdapter(List<Manager> mMangerList, Context context) {
        this.mMangerList = mMangerList;
        this.context = context;
    }

    public int getCount() {
        return (this.mMangerList.size());
    }

    public Object getItem(int position) {
        return (this.mMangerList.get(position));
    }

    public long getItemId(int position) {
        return (position);
    }

    public View getView(int position, View arg1, ViewGroup arg2) {//加载XML视图文件
        View view = LayoutInflater.from(context).inflate(R.layout.manager_items, null);
        TextView managerActivityTxt2Name,managerActivityTxt2Position,managerActivityTxt3Time;
        Button managerActivityBtnPhone,managerActivityBtnPosition;
        ImageView managerImv1;
        managerActivityTxt2Name=(TextView)
                view.findViewById(R.id.manageractivity_txt2_name);
        managerActivityTxt2Position=(TextView)
                view.findViewById(R.id.manageractivity_txt2_location);
        managerActivityTxt3Time=(TextView)
                view.findViewById(R.id.manageractivity_txt3_time);
        managerActivityBtnPhone=(Button)
                view.findViewById(R.id.manageractivity_btn_phone);
        managerActivityBtnPosition=(Button)
                view.findViewById(R.id.manageractivity_btn_position);
        managerImv1=(ImageView)
                view.findViewById(R.id.manageractivity_imv1);

        if((position%2)==0){
            managerImv1.setImageResource(R.drawable.manregactivity_imv_portrait2);
            managerImv1.getResources().getDrawable(R.drawable.manregactivity_imv_portrait2);
        }
        String [] s=new String[]{"nokia","iphone","htc","mi2","lg","oppo","sumsung"};
        managerActivityTxt2Name.setText(s[position]);
        return view;
    }

    private class ViewHolder{

    }

}
