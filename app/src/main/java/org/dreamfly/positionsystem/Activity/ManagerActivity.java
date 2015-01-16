package org.dreamfly.positionsystem.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
//import org.dreamfly.positionsystem.Adapter.ManagerAdapter;
import org.dreamfly.positionsystem.Adapter.ManagerAdapter;
import org.dreamfly.positionsystem.Custom.DefineListView;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.bean.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengyl on 15-1-13.
 * 管理者界面Activity类
 */
public class ManagerActivity extends ActionBarActivity {

    private DefineListView managerActivityListView;
    private ManagerAdapter mManagerAdapter;
    private TextView  txtManagerActivityTitle;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.manager_layout);
        this.initila();

    }
    public void initila()
    {
        this.managerActivityListView=(DefineListView)
        this.findViewById(R.id.delistiview_manageractivity_showmanger);
        this.txtManagerActivityTitle=(TextView)
        this.findViewById(R.id.txt_manageractivity_title);
        this.mManagerAdapter=new ManagerAdapter(this.getData(),getApplicationContext());

        this.managerActivityListView.setAdapter(this.mManagerAdapter);
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



}
