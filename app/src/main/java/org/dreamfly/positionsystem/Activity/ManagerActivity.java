package org.dreamfly.positionsystem.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import org.dreamfly.positionsystem.Adapter.ManagerAdapter;
import org.dreamfly.positionsystem.Adapter.ManagerAdapter;
import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.Custom.DefineListView;
import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Utils.CurrentInformationUtils;
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
    private TextView txtManagerActivityTitle;
    private DataBase mDataBase = new DataBase(this);
    private SQLiteDatabase db;
    private CurrentInformationUtils mInformation = new CurrentInformationUtils();
    private DefineDialog mDefineDialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.manager_layout);
        this.initial(mDataBase);


    }

    public void initial(DataBase mDataBase) {
        this.bindID();
        this.mManagerAdapter = new ManagerAdapter(this.getData(), this, mDataBase);
        this.managerActivityListView.setAdapter(this.mManagerAdapter);
        this.setListViewListener();
    }

    private void bindID() {
        this.managerActivityListView = (DefineListView)
                this.findViewById(R.id.delistiview_manageractivity_showmanger);
        this.txtManagerActivityTitle = (TextView)
                this.findViewById(R.id.txt_manageractivity_title);
    }

    private void setListViewListener() {
        this.managerActivityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                setDialogShow();
            }
        });

    }

    private List<Manager> getData() {
        List<Manager> list = new ArrayList<Manager>();
        for (int i = 0; i < 7; i++) {
            Manager m = new Manager();
            m.setDeviceNma(mInformation.setFirstDeviceName(i));
            m.setLastDateTouch(mInformation.getCurrentTime());
            m.setMangerMarks("mother"+i);
            m.setLastLocation(mInformation.setFirstLocation(i));
            m.setMangerMarks("mother" + i);
            m.setLastLocation(mInformation.setFirstLocation(i));
            list.add(m);
        }
        this.setData(mDataBase, list);
        return list;
    }

    public void setData(DataBase mDataBase, List<Manager> list) {
        Cursor cur = mDataBase.Selector(0);
        if (!cur.moveToNext()) {
            for (int i = 0; i < 7; i++) {
                Manager manager = list.get(i);
                mDataBase.items_newItem(i, manager.getDeviceName(), manager.getMangerMarks()
                        , manager.getLastLocation(), manager.getLastDateTouch());

            }
        }
        cur.close();
    }

    private void setDialogShow() {
        this.mDefineDialog = new DefineDialog(ManagerActivity.this).buiider().
                setTitle("是否修改备注").setDefineDialogCanceable(true).show();
    }

}
