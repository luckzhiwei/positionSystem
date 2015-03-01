package org.dreamfly.positionsystem.Activity;

import android.app.Activity;
import android.content.Context;
import
        android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import org.dreamfly.positionsystem.Adapter.RegulatorAdapter;
import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.Custom.DefineListView;
import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.Database.DefinedShared;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Thread.ManagerListThread;
import org.dreamfly.positionsystem.Utils.CurrentInformationUtils;
import org.dreamfly.positionsystem.Utils.LocationUtils;
import org.dreamfly.positionsystem.Utils.ToastUtils;
import org.dreamfly.positionsystem.bean.User;
import org.dreamfly.positionsystem.CommonParameter.ComParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengyl on 15-1-13.
 * 被管理者界面Activity类
 */
public class RegulatorActivity extends Activity implements OnGetGeoCoderResultListener {

    private DefineListView listViewRegulatorActivityReglutorList;
    private TextView txtRegulatorActivityTitle;
    private LinearLayout layout;
    private DefineDialog mDefineDialog;
    private RegulatorAdapter mRegulatordapter;
    private User oneRegulator = new User();
    private ManagerActivity manager = new ManagerActivity();
    private ComParameter com = new ComParameter();
    private int userTouchDistance;
    private float touchY;
    private boolean isClear = true;
    private ManagerListThread managerListThread;
    protected LocationUtils mLocationUtils;
    protected LocationClient locationClient;
    protected CurrentInformationUtils mInformation = new CurrentInformationUtils(this);
    protected DefinedShared mdata = new DefinedShared(this);
    protected DataBase mDataBase = new DataBase(this);
    protected com.baidu.mapapi.search.geocode.GeoCoder mcoder;
    protected String lat;
    protected String lon;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        String libName = "BaiduMapSDK_v3_2_0_11";
        System.loadLibrary(libName);
        this.isFirstConnect();
        this.initial();
    }

    protected void onResume() {
        super.onResume();
        this.bindID();
        Log.i("lzw", "manage_intial");
        //从数据库读取上一次的地理位置
        if (!mdata.getString(ComParameter.LOADING_STATE, ComParameter.CLICKING_STATE)
                .equals(ComParameter.STATE_FIRST)) {
            String temp = mdata.getString("pos", "pos");
            int pos = Integer.parseInt(temp);
            mDataBase.items_changeValue(ComParameter.MANTABLENAME, "position", mdata.getString("locationback", "locationback")
                    , pos);
            this.loadList();
        }
        //控制程序在第一次启动时不在这里加载列表数据(第一次请求的数据从网络获得)
        if (!mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE)
                .equals(ComParameter.STATE_SECOND)) {
            this.loadList();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            RegulatorActivity.this.finish();
            //释放httpEntity请求空间
            // this.managerListThread.closeHttp();
            //确保第一次启动时在请求成功前处于数据加载界面
            if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                    equals(ComParameter.STATE_SECOND)) {
                mdata.putString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE, ComParameter.STATE_FIRST);
            }
        }
        return false;
    }

    private void initial() {
        Log.i("lzw", "unmanager_init");
        this.bindID();
        mLocationUtils = new LocationUtils(this);
        mLocationUtils.LocationInfo();
        mcoder = GeoCoder.newInstance();
        mcoder.setOnGetGeoCodeResultListener(this);
        if (!mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                equals(ComParameter.STATE_SECOND)) {
            //如果是第一次启动,不在这里加载列表数据(第一次请求的数据从网络获得)
            this.loadList();
        }
        this.telNumSave(mInformation);
        this.locationSave();
        this.sendIdtoSever();

    }

    private void bindID() {
        this.listViewRegulatorActivityReglutorList = (DefineListView)
                this.findViewById(R.id.listivew_regulatoractivity_regulatorlist);
        this.txtRegulatorActivityTitle = (TextView)
                this.findViewById(R.id.txt_regulatoractivity_title);
        this.layout = (LinearLayout)
                this.findViewById(R.id.myregulator_activity_layout);

    }

    private void setCLickListener() {
        this.listViewRegulatorActivityReglutorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                setDialogShow(position);
            }
        });
        //设置下拉事件监听,请求服务器刷新列表
        this.listViewRegulatorActivityReglutorList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                userTouchDistance = listViewRegulatorActivityReglutorList.getUserTouchDistance();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchY = event.getRawY();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    float detalY = event.getRawY() - touchY;
                    touchY = event.getRawY();
                    listViewRegulatorActivityReglutorList.dynSetHeadViewHeight(listViewRegulatorActivityReglutorList.calDistance(detalY));
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (listViewRegulatorActivityReglutorList.getFirstVisiblePosition() == 0
                            && userTouchDistance > 250) {
                        listViewRegulatorActivityReglutorList.dynSetHeadViewHeight(250);
                        sendIdtoSever();
                        Log.v("zyl", "请求服务器中");
                        userTouchDistance = 0;
                    }

                    //用户划过的距离必须超过最小才行，对顶部设置一个合适的大小来显示
                    else if (userTouchDistance < 250) {
                        listViewRegulatorActivityReglutorList.dynSetHeadViewHeight(0);
                    }  //如果用户实际划过的距离小于最小距离,那么listview的头部是不会显示的
                    userTouchDistance = 0;
                    //每次划过之后都将用户的实际划过的距离清零
                }
                return false;
            }
        });
    }

    public List<User> getData() {
        List<User> list = new ArrayList<User>();
        String length = mdata.getString("itemslength", "length");
        int k = Integer.parseInt(length);
        if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                equals(ComParameter.STATE_SECOND)) {
            //记录登陆状态
            mdata.putString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE,
                    ComParameter.STATE_THIRD);
        }
        for (int i = 0; i < k; i++) {
            User r = new User();
            r.setDeviceNma(mInformation.setFirstDeviceName(i));
            r.setLastDateTouch(mInformation.getCurrentTime());
            r.setMangerMarks("null");
            r.setLastLocation(mInformation.setFirstLocation(i));
            r.setIsOnLine("false");
            list.add(r);

        }
        this.changeRegBackground(list);
        Log.v("textlist", "" + list.size());
        return list;
    }

    public void changeRegBackground(List<User> list) {
        if (list.size() == 0) {
            layout.setBackgroundResource(R.drawable.regulator_background);
        } else {
            layout.setBackgroundResource(R.color.white_layout);
        }
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

    public class RegPositiveButtonListener implements View.OnClickListener {
        protected EditText mEditText;
        protected User regulator;
        protected DataBase mDataBase;
        protected Context mcontext;
        protected int pos;
        protected DefineDialog mDialog;

        public RegPositiveButtonListener(int pos, final User regulator,
                                         DataBase mDataBase, EditText mEdittext, DefineDialog mDialog) {
            this.pos = pos;
            this.regulator = regulator;
            this.mDataBase = mDataBase;
            this.mEditText = mEdittext;
            this.mDialog = mDialog;
        }

        @Override
        public void onClick(View view) {
            regulator.setMangerMarks(mEditText.getText().toString());
            Log.v("textstring", regulator.getMangerMarks());
            mDataBase.items_changeValue(com.MANTABLENAME, "subname", regulator.getMangerMarks(), (pos - 1));
            mDialog.dismiss();
        }
    }

    /**
     * 百度定位接口
     */
    public class BDListener implements com.baidu.location.BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }

            lat = location.getLatitude() + "";

            lon = location.getLongitude() + "";
            mDataBase.items_changeValue(ComParameter.DEVICE, "latitude", lat, 0);
            mDataBase.items_changeValue(ComParameter.DEVICE, "longitude", lon, 0);
            reverseCode(lat, lon);
            Log.i("lzw", lat + "");
            Log.i("lzw", lon + "");
            locationClient.stop();
            Log.i("lzw", "unlink");

        }

        @Override
        public void onReceivePoi(BDLocation bdLocation) {

        }
    }

    public void locationSave() {

        locationClient = mLocationUtils.getLocationClient();
        locationClient.start();
        locationClient.requestLocation();
        BDListener bdListener = new BDListener();
        locationClient.registerLocationListener(bdListener);
        Log.i("lzw", "locationSDKwork");
    }

    private void isFirstConnect() {
        mdata.putString(ComParameter.LOADING_STATE, ComParameter.IDENTITY_STATE, "regulator");
        if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE)
                .equals(ComParameter.STATE_FIRST)) {
            this.setContentView(R.layout.manager_layout_first);
            mdata.putString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE,
                    ComParameter.STATE_SECOND);
        } else {
            this.setContentView(R.layout.regulator_layout);
            return;
        }

    }

    private void loadList() {
        this.bindID();
        this.mRegulatordapter = new RegulatorAdapter(this.getData(), this, mDataBase);
        this.listViewRegulatorActivityReglutorList.setAdapter(mRegulatordapter);
        this.setCLickListener();
    }

    protected void telNumSave(CurrentInformationUtils mInformation) {
        Log.i("zyl", mInformation.getDeviceTelNum());
        mDataBase.items_changeValue(ComParameter.DEVICE, "telnumber", mInformation.getDeviceTelNum(), 0);
    }

    /**
     * 向服务器发送请求
     */
    protected void sendIdtoSever() {
        this.managerListThread = new ManagerListThread(mHandler, "managerlistid");
        String requestURL = ComParameter.HOST + "contect.action";
        this.managerListThread.setRequestPrepare(requestURL, this.prepareListParams());
        this.managerListThread.start();
    }

    protected Map prepareListParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", mdata.getString("tableid", mInformation.getDeviceId()));
        return params;
    }

    private android.os.Handler mHandler = new android.os.Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.getData().getInt("managerlistid") == ComParameter.STATE_RIGHT) {
                Map<String, String> resultMap = managerListThread.getResultMap();
                dealListFromSever(resultMap);
            } else if (msg.getData().getInt("managerlistid") == ComParameter.STATE_ERROR) {
                Map<String, String> resultMap = managerListThread.getResultMap();
                Log.i("zyl", "网络连接停止");
                if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                        equals(ComParameter.STATE_SECOND)) {
                    //如果第一次连接失败,下一次重新请求服务器
                    mdata.putString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE,
                            ComParameter.STATE_FIRST);
                }
                //获取错误报告
                mdata.putString("errorreport", "a", resultMap.get("test"));
                ToastUtils.showToast(RegulatorActivity.this, "请求失败");
                if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                        equals(ComParameter.STATE_THIRD)) {
                    listViewRegulatorActivityReglutorList = (DefineListView)
                            findViewById(R.id.listivew_regulatoractivity_regulatorlist);
                    listViewRegulatorActivityReglutorList.dynSetHeadViewHeight(0);
                }
            }
        }
    };

    /**
     * 处理从服务器获取的数据,加载列表
     *
     * @param resultMap
     */
    protected void dealListFromSever(Map<String, String> resultMap) {

        mdata.putString("itemslength", "length", resultMap.get("length"));

        //如果是首次启动
        if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                equals(ComParameter.STATE_SECOND)) {
            setDataBase(resultMap);
        } else {
            dealDataBase(resultMap);
        }
        setContentView(R.layout.regulator_layout);
        loadList();
        listViewRegulatorActivityReglutorList.dynSetHeadViewHeight(0);
        userTouchDistance = 0;
    }

    /**
     * 第一次从服务器获取的列表,并首次插入数据库
     *
     * @param resultMap
     */
    private void setDataBase(Map<String, String> resultMap) {
        //记录从服务器获取的列表长度
        String length = mdata.getString("itemslength", "length");
        int k = Integer.parseInt(length);
        mdata.putString("itemslength", "lastlength", length);
        if (k == 0) {
            return;
        } else {
            //插入数据库
            for (int i = 0; i < k; i++) {
                mDataBase.itemsInsert(ComParameter.MANTABLENAME, i, resultMap.get("idname" + i + ""),
                        resultMap.get("subname" + i + ""), resultMap.get("subname" + i + ""), "暂未获取地理位置",
                        mInformation.getCurrentTime(), resultMap.get("isconnect" + i + ""));
            }
        }
    }

    /**
     * 不是首次获取,再原有数据基础上插入新数据
     *
     * @param resultMap
     */
    private void dealDataBase(Map<String, String> resultMap) {
        //上次和这次的列表长度可能不一样,用xml存储,并记录
        String lastlenth = mdata.getString("itemslength", "lastlength");
        int last = Integer.parseInt(lastlenth);
        String lenth = mdata.getString("itemslength", "length");
        int length = Integer.parseInt(lenth);
        //新条目插入
        for (int i = last; i < length; i++) {
            mDataBase.itemsInsert(ComParameter.MANTABLENAME, i, resultMap.get("idname" + i + ""),
                    resultMap.get("subname" + i + ""), resultMap.get("subname" + i + ""), "暂未获取地理位置",
                    mInformation.getCurrentTime(), resultMap.get("isconnect" + i + ""));
        }
        //将这次的长度作为下一次更新的"上次长度"
        mdata.putString("itemslength", "lastlength", lenth);
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
            Toast.makeText(RegulatorActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        String s = result.getAddress();
        mDataBase.items_changeValue(ComParameter.DEVICE, "location", s, 0);
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
        Log.i("thislzw", "您的当前位置" + s + "已被保存");

    }


}


