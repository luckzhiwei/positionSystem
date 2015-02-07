package org.dreamfly.positionsystem.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import org.dreamfly.positionsystem.Adapter.ManagerAdapter;
import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.Custom.DefineListView;
import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Utils.CurrentInformationUtils;

import org.dreamfly.positionsystem.Utils.LocationUtils;
import org.dreamfly.positionsystem.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengyl on 15-1-13.
 * 管理者界面Activity类
 */
public class ManagerActivity extends Activity implements OnGetGeoCoderResultListener {


    private DefineListView managerActivityListView;
    private ManagerAdapter mManagerAdapter;
    private TextView txtManagerActivityTitle, txtManagertgetDeviceName;
    private LinearLayout layout;
    protected DataBase mDataBase = new DataBase(this);
    private User oneManager = new User();
    private User oneRegulator = new User();
    private DefineDialog mDefineDialog;
    protected LocationUtils mLocationUtils;
    protected LocationClient locationClient;
    protected String lat;
    protected String lon;
    protected CurrentInformationUtils mInformation = new CurrentInformationUtils(this);
    protected final static String DEVICE="deviceinformation";
    private boolean isClear = true;
    private boolean isFirstGetLocation=true;
    private boolean isGetFromServer=true;
    com.baidu.mapapi.search.geocode.GeoCoder mcoder;
    private final static String TABLENAME = "regulatoritems";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        String libName = "BaiduMapSDK_v3_2_0_11";
        System.loadLibrary(libName);
        this.setContentView(R.layout.manager_layout);
        this.initial();


    }

    private void initial() {
        this.bindID();
        mLocationUtils = new LocationUtils(this);
        mLocationUtils.LocationInfo();
        mcoder = GeoCoder.newInstance();
        mcoder.setOnGetGeoCodeResultListener(this);
        this.mManagerAdapter = new ManagerAdapter(this.getData(), this, mDataBase);
        this.managerActivityListView.setAdapter(this.mManagerAdapter);
        this.telNumSave(mInformation);
        this.setListViewListener();
        this.locationSave();

    }

    private void bindID() {
        this.managerActivityListView = (DefineListView)
                this.findViewById(R.id.delistiview_manageractivity_showmanger);
        this.txtManagerActivityTitle = (TextView)
                this.findViewById(R.id.txt_manageractivity_title);
        this.txtManagertgetDeviceName = (TextView)
                this.findViewById(R.id.manageractivity_txt2_name);
        this.layout=(LinearLayout)
                this.findViewById(R.id.manageractivity_layout);

    }

    /**
     * 为每个条目增加监听事件
     */
    private void setListViewListener() {
        this.managerActivityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                setDialogShow(position);
            }
        });

    }

    /**
     * 向adapter中加载初始数据
     *
     * @return
     */
    private List<User> getData() {
        List<User> list = new ArrayList<User>();
        for (int i = 0; i < 0; i++) {
            User r = new User();
            r.setDeviceNma(mInformation.setFirstDeviceName(i));
            r.setLastDateTouch(mInformation.getCurrentTime());
            r.setMangerMarks("null");
            r.setLastLocation(mInformation.setFirstLocation(i));
            r.setIsOnLine("false");
            list.add(r);
        }
        this.setData(mDataBase, list);
        this.changeBackground(list);
        return list;
    }

    /**
     * 如果没有条目,显示一个提示背景
     * @param list
     */
    public void changeBackground(List<User> list){
        if(list.size()==0){
          layout.setBackgroundResource(R.drawable.manager_background_none);
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
    private void setData(DataBase mDataBase, List<User> list) {
        Cursor cur = mDataBase.Selector(0, TABLENAME);
        if (!cur.moveToNext()) {
            for (int i = 0; i < 0; i++) {
                User regulator = list.get(i);
                mDataBase.itemsInsert(TABLENAME, i, regulator.getDeviceName(), regulator.getMangerMarks()
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
        this.mDefineDialog = new DefineDialog(ManagerActivity.this).buiider(true).
                setTitle("修改备注名:").setDefineDialogCanceable(true).setPosBtnTxt("确定").
                setNegBtnTxt("取消").show();
        PositiveButtonListener mPositiveButtonListener =

                new PositiveButtonListener(position, oneRegulator, mDataBase,
                        this.mDefineDialog.getEditText(), this.mDefineDialog);
        mDefineDialog.setPosBtnClickListener(mPositiveButtonListener);

    }

    /**
     * 自定义对话框按钮监听类
     */
    public class PositiveButtonListener implements View.OnClickListener {
        protected EditText mEditText;
        protected User regulator;
        protected DataBase mDataBase;
        protected Context mcontext;
        protected int pos;
        protected DefineDialog mDialog;

        public PositiveButtonListener(int pos, final User regulator,
                                      DataBase mDataBase, EditText mEdittext, DefineDialog mDialog) {
            this.pos = pos;
            this.regulator = regulator;
            this.mDataBase = mDataBase;
            this.mEditText = mEdittext;
            this.mDialog = mDialog;
        }

        /**
         * 将用户修改的备注名保存到数据库中
         *
         * @param view
         */
        public void onClick(View view) {
            regulator.setMangerMarks(mEditText.getText().toString());
            Log.v("textstring", regulator.getMangerMarks());
            mDataBase.items_changeValue(TABLENAME, "subname", regulator.getMangerMarks(), (pos - 1));
            mDialog.dismiss();
        }
    }

    public class BDListener implements com.baidu.location.BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }

            lat = location.getLatitude() + "";

            lon = location.getLongitude() + "";
            mDataBase.items_changeValue(DEVICE,"latitude",lat,0);
            mDataBase.items_changeValue(DEVICE,"longitude",lon,0);
            reverseCode(lat, lon);
            Log.v("connection","监听已连接");
            locationClient.stop();


        }

        @Override
        public void onReceivePoi(BDLocation bdLocation) {

        }
    }


    public DataBase getDataBase() {
        return mDataBase;
    }

    /**
     * 调用百度定位Sdk,并存储定位数据
     */
    public void locationSave() {

          locationClient = mLocationUtils.getLocationClient();
          locationClient.start();
          locationClient.requestLocation();
          BDListener bdListener = new BDListener();
          locationClient.registerLocationListener(bdListener);

    }

    /**
     * 存储本机号码
     */
    public void telNumSave(CurrentInformationUtils mInformation){
        Log.i("zyl",mInformation.getDeviceTelNum());
        mDataBase.items_changeValue(DEVICE,"telnumber",mInformation.getDeviceTelNum(),0);
    }

    /**
     * 调用经纬度编码转换函数并用Sharepreference保存
     *
     * @param lat
     * @param lon
     */
    public void reverseCode(String lat, String lon) {
        LatLng ptCenter = new LatLng(
                (Float.valueOf(lat)),
                Float.valueOf(lon));
        mcoder.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(ManagerActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        String s = result.getAddress();
        mDataBase.items_changeValue(DEVICE,"location",s,0);
        //将获得的地址保存
        SharedPreferences mpreference = getSharedPreferences("address", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mpreference.edit();
        editor.putString("address", s);
        editor.commit();
        if (isClear) {
            editor.clear();
            editor.putString("address", s);
            editor.commit();
        }
        Log.i("lzw", "您的当前位置" + s + "已被保存");

    }
    /*
    public class locationRunnable implements Runnable  {
        @Override
        public void run() {
            try {
                    locationSave();
                    Log.i("zyl","线程启动");

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }*/


}
