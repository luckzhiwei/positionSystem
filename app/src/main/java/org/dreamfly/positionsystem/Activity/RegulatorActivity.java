package org.dreamfly.positionsystem.Activity;

import android.content.Context;
import
        android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import org.dreamfly.positionsystem.Adapter.RegulatorAdapter;
import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.Custom.DefineListView;
import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Utils.CurrentInformationUtils;
import org.dreamfly.positionsystem.Utils.LocationUtils;
import org.dreamfly.positionsystem.bean.User;
import org.dreamfly.positionsystem.CommonParameter.ComParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengyl on 15-1-13.
 * 被管理者界面Activity类
 */
public class RegulatorActivity extends ManagerActivity {

    private DefineListView listViewRegulatorActivityReglutorList;
    private TextView txtRegulatorActivityTitle;
    private LinearLayout layout;
    private DefineDialog mDefineDialog;
    private RegulatorAdapter mRegulatordapter;
    private User oneRegulator = new User();
    private ManagerActivity manager = new ManagerActivity();
    private ComParameter com = new ComParameter();
    private DataBase mDataBase=new DataBase(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        String libName = "BaiduMapSDK_v3_2_0_11";
        System.loadLibrary(libName);
        this.setContentView(R.layout.regulator_layout);
        this.initial();
    }

    private void initial() {
        Log.i("lzw","unmanager_init");
        this.bindID();
        mLocationUtils = new LocationUtils(this);
        mLocationUtils.LocationInfo();
        mcoder = GeoCoder.newInstance();
        mcoder.setOnGetGeoCodeResultListener(this);
        this.locationSave();

        this.telNumSave(mInformation);

        this.mRegulatordapter = new RegulatorAdapter(this.getData(), this, mDataBase);
        this.listViewRegulatorActivityReglutorList.setAdapter(mRegulatordapter);
        this.setCLickListener();


    }

    private void bindID() {
        this.listViewRegulatorActivityReglutorList = (DefineListView)
                this.findViewById(R.id.listivew_regulatoractivity_regulatorlist);
        this.txtRegulatorActivityTitle = (TextView)
                this.findViewById(R.id.txt_regulatoractivity_title);
        this.layout=(LinearLayout)
                this.findViewById(R.id.myregulator_activity_layout);

    }

    private void setCLickListener() {
        this.listViewRegulatorActivityReglutorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                setDialogShow(position);
            }
        });
    }

    public List<User> getData() {
        List<User> list = new ArrayList<User>();
        for (int i = 0; i < 7; i++) {
            User r = new User();
            r.setDeviceNma(mInformation.setFirstDeviceName(i));
            r.setLastDateTouch(mInformation.getCurrentTime());
            r.setMangerMarks("null");
            r.setLastLocation(mInformation.setFirstLocation(i));
            r.setIsOnLine("false");
            list.add(r);

        }
        this.setData(mDataBase, list);
        this.changeRegBackground(list);
        Log.v("textlist", "" + list.size());
        return list;
    }
    public void changeRegBackground(List<User> list){
        if(list.size()==0){
            layout.setBackgroundResource(R.drawable.regulator_background);
        }
        else{
            layout.setBackgroundResource(R.color.white_layout);
        }
    }

    /**
     * 向数据库中存储数据
     *
     * @param mDataBase
     * @param list
     */

    public void setData(DataBase mDataBase, List<User> list) {
        Cursor cur = mDataBase.Selector(0, com.MANTABLENAME);
        if (!cur.moveToNext()) {
            for (int i = 0; i < 7; i++) {
                User regulator = list.get(i);
                mDataBase.itemsInsert(com.MANTABLENAME, i, regulator.getDeviceName(), regulator.getMangerMarks()
                        , regulator.getLastLocation(), regulator.getLastDateTouch(), regulator.getOnLine());

            }
        }
        cur.close();
    }

    /**
     * 实现点击由用户修改备注名的效果
     *
     * @param position
     */

    private void setDialogShow(int position) {
        this.mDefineDialog = new DefineDialog(RegulatorActivity.this).buiider(true).
                setTitle("修改备注名:").setDefineDialogCanceable(true).setPosBtnTxt("确定").
                setNegBtnTxt("取消").show();

        RegPositiveButtonListener mPositiveButtonListener =

                new RegPositiveButtonListener(position, oneRegulator, mDataBase,
                        this.mDefineDialog.getEditText(), this.mDefineDialog);
        mDefineDialog.setPosBtnClickListener(mPositiveButtonListener);

    }

    public class RegPositiveButtonListener extends ManagerActivity.PositiveButtonListener {
        RegPositiveButtonListener(int pos, final User regulator,
                                  DataBase mDataBase, EditText mEdittext, DefineDialog mDialog) {
            super(pos, regulator, mDataBase, mEdittext, mDialog);
        }

        @Override
        public void onClick(View view) {
            regulator.setMangerMarks(mEditText.getText().toString());
            Log.v("textstring", regulator.getMangerMarks());
            mDataBase.items_changeValue(com.MANTABLENAME, "subname", regulator.getMangerMarks(), (pos - 1));
            mDialog.dismiss();
        }
    }
}


