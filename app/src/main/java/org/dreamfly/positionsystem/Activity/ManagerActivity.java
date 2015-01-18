package org.dreamfly.positionsystem.Activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.bean.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengyl on 15-1-13.
 * 管理者界面Activity类
 */
public class ManagerActivity extends ActionBarActivity {

    private DefineListView managerActivityListView;
    private ManagerAdapter mManagerAdapter;
    private TextView  txtManagerActivityTitle;
    DataBase mDataBase=new DataBase(this);
    SQLiteDatabase db;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.manager_layout);

        this.initila(mDataBase);


    }
    public void initila(DataBase mDataBase)
    {
        this.managerActivityListView=(DefineListView)
        this.findViewById(R.id.delistiview_manageractivity_showmanger);
        this.txtManagerActivityTitle=(TextView)
        this.findViewById(R.id.txt_manageractivity_title);
        this.mManagerAdapter=new ManagerAdapter(this.getData(),this,mDataBase);
        this.managerActivityListView.setAdapter(this.mManagerAdapter);
    }



    public List<Manager> getData(){
        List<Manager> list=new ArrayList<Manager>();
        for(int i=0;i<7;i++)
        {
            Manager m=new Manager();
            m.setDeviceNma("LG"+i);
            m.setLastDateTouch("1-16");
            m.setMangerMarks("mother"+i);
            m.setLastLocation("usetc"+i);
            list.add(m);
        }
        this.setData(mDataBase,list);
        return list;
    }

    public void setData(DataBase mDataBase,List<Manager> list){
        Cursor cur=mDataBase.Selector(0);
        if(!cur.moveToNext()) {
            for (int i = 0; i < 7; i++) {
                Manager manager = list.get(i);
                mDataBase.items_newItem(i,manager.getDeviceName(),manager.getMangerMarks()
                ,manager.getLastLocation(),manager.getLastDateTouch());

            }
        }
        cur.close();
    }
}
