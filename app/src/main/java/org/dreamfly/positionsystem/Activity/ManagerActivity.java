package org.dreamfly.positionsystem.Activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
    private TextView txtManagerActivityTitle,txtManagertgetDeviceName;
    private DataBase mDataBase = new DataBase(this);
    private CurrentInformationUtils mInformation = new CurrentInformationUtils(this);
    private Manager oneManager=new Manager();
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
        this.txtManagertgetDeviceName=(TextView)
                this.findViewById(R.id.manageractivity_txt2_name);

    }

    private void setListViewListener() {
        this.managerActivityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                setDialogShow(position);
            }
        });

    }

    /**
     * 向adapter中加载初始数据
     * @return
     */
    private List<Manager> getData() {
        List<Manager> list = new ArrayList<Manager>();
        for (int i = 0; i < 7; i++) {
            Manager m = new Manager();
            m.setDeviceNma(mInformation.setFirstDeviceName(i));
            m.setLastDateTouch(mInformation.getCurrentTime());
            m.setMangerMarks("null");
            m.setLastLocation(mInformation.setFirstLocation(i));
            m.setIsOnLine("false");
            list.add(m);
        }
        this.setData(mDataBase, list);
        return list;
    }

    /**
     * 向数据库中存储数据
     * @param mDataBase
     * @param list
     */
    public void setData(DataBase mDataBase, List<Manager> list) {
        Cursor cur = mDataBase.Selector(0);
        if (!cur.moveToNext()) {
            for (int i = 0; i < 7; i++) {
                Manager manager = list.get(i);
                mDataBase.items_newItem(i, manager.getDeviceName(), manager.getMangerMarks()
                        , manager.getLastLocation(), manager.getLastDateTouch(),manager.getOnLine());

            }
        }
        cur.close();
    }

    /**
     * 实现点击由用户修改备注名的效果
     * @param position
     */
    private void setDialogShow(int position) {
        this.mDefineDialog = new DefineDialog(ManagerActivity.this).buiider(true).
                setTitle("修改备注名:").setDefineDialogCanceable(true).setPosBtnTxt("确定").
                setNegBtnTxt("取消").show();
        PositiveButtonListener mPositiveButtonListener =

                new PositiveButtonListener(position, oneManager, mDataBase,
                        this.mDefineDialog.getEditText(),this.mDefineDialog);
        mDefineDialog.setPosBtnClickListener(mPositiveButtonListener);

    }

    /**
     * 自定义对话框按钮监听类
     */
    public class PositiveButtonListener implements View.OnClickListener{
        private EditText mEditText;
        private Manager oneManager;
        private DataBase mDataBase;
        private Context mcontext;
        private int pos;
        private DefineDialog mDialog;

        public PositiveButtonListener (int pos, final Manager oneManager,
                                       DataBase mDataBase,EditText mEdittext,DefineDialog mDialog){
            this.pos=pos;
            this.oneManager=oneManager;
            this.mDataBase=mDataBase;
            this.mEditText=mEdittext;
            this.mDialog=mDialog;
        }

        /**
         * 将用户修改的备注名保存到数据库中
         * @param view
         */
        public void onClick(View view){
            oneManager.setMangerMarks(mEditText.getText().toString());
            Log.v("textstring",oneManager.getMangerMarks());
            mDataBase.items_changeValue("subname",oneManager.getMangerMarks(),(pos-1));
            mDialog.dismiss();
        }
    }

}
