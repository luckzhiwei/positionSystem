package org.dreamfly.positionsystem.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
//import org.dreamfly.positionsystem.Adapter.ManagerAdapter;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.bean.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengyl on 15-1-13.
 * 管理者界面Activity类
 */
public class ManagerActivity extends ActionBarActivity {
    private ListView managerActivityListView;
    private ManagerAdapter mManagerAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.manager_layout);
        this.initila();
        managerActivityListView.setAdapter(mManagerAdapter);

    }
    public void initila(){
        managerActivityListView=(ListView)this.findViewById(R.id.manageractivity_listview);
        mManagerAdapter=new ManagerAdapter(getData());
    }
    public List<Manager> getData(){
        List<Manager>list=new ArrayList<Manager>();
        for(int i=0;i<7;i++)
        {
            Manager m=new Manager();
            list.add(m);
        }
        return list;
    }
    public class ManagerAdapter  extends BaseAdapter {
        private ManagerActivity mManagerActivity;
        private List<Manager> mMangerList;//适配器中应该含有的容器,

        public ManagerAdapter(List <Manager> mMangerList)
        {
            this.mMangerList=mMangerList;
        }
        public int getCount()
        {
            return(this.mMangerList.size());
        }

        public Object getItem(int position)
        {
            return(this.mMangerList.get(position));
        }

        public long getItemId(int position)
        {
            return(position);
        }

        public View getView(int arg0, View arg1, ViewGroup arg2) {//加载XML视图文件
            View view= LayoutInflater.from(ManagerActivity.this).inflate(R.layout.manager_items,null);
            return view;
        }

    }


}
