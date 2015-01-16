package org.dreamfly.positionsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

import org.dreamfly.positionsystem.Activity.ManagerActivity;
import org.dreamfly.positionsystem.R;

import org.dreamfly.positionsystem.bean.Manager;

import android.view.View;
import android.view.ViewGroup;

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

    public View getView(int arg0, View arg1, ViewGroup arg2) {//加载XML视图文件
        View view = LayoutInflater.from(context).inflate(R.layout.manager_items, null);
        return view;
    }

    private class ViewHolder{

    }
}
