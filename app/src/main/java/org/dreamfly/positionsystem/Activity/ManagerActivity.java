package org.dreamfly.positionsystem.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import org.dreamfly.positionsystem.CommonParameter.ComParameter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by zhengyl on 15-1-13.
 * 管理者界面Activity类
 */
public class ManagerActivity extends Activity implements OnGetGeoCoderResultListener {


    private DefineListView managerActivityListView;
    private ManagerAdapter mManagerAdapter;
    private TextView txtManagerActivityTitle, txtManagertgetDeviceName;
    private LinearLayout layout;
    private ProgressBar proManactivity;
    private Cursor cur;
    protected DataBase mDataBase = new DataBase(this);
    private User oneManager = new User();
    private User oneRegulator = new User();
    private DefineDialog mDefineDialog;
    private ManagerListThread managerListThread;
    protected LocationUtils mLocationUtils;
    protected LocationClient locationClient;
    protected String lat;
    protected String lon;
    protected CurrentInformationUtils mInformation = new CurrentInformationUtils(this);
    protected DefinedShared mdata=new DefinedShared(this);
    protected final static String DEVICE="deviceinformation";
    private boolean isClear = true;
    private boolean isFirstGetLocation=true;
    private boolean isFirstGetFromServer=true;
    com.baidu.mapapi.search.geocode.GeoCoder mcoder;
    private final static String TABLENAME = "regulatoritems";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        String libName = "BaiduMapSDK_v3_2_0_11";
        System.loadLibrary(libName);
        this.ifFirstConnect();
        this.initial();


    }
    @Override
    protected void onResume(){
        super.onResume();
        this.bindID();
        Log.i("lzw","manage_intial");
        mLocationUtils = new LocationUtils(this);
        mLocationUtils.LocationInfo();
        mcoder = GeoCoder.newInstance();
        mcoder.setOnGetGeoCodeResultListener(this);
        if(!mdata.getString("isfirstconnect","isfirstconnect").equals("1")) {
            this.loadList();
        }
        this.telNumSave(mInformation);

    }

    private void initial() {
        this.bindID();
        Log.i("lzw","manage_intial");
        mLocationUtils = new LocationUtils(this);
        mLocationUtils.LocationInfo();
        mcoder = GeoCoder.newInstance();
        mcoder.setOnGetGeoCodeResultListener(this);
        if(!mdata.getString("isfirstconnect","isfirstconnect").equals("1")) {
           this.loadList();
        }
        this.telNumSave(mInformation);
        this.locationSave();
        this.sendIdtoSever();


    }

    private void bindID() {
        this.proManactivity=(ProgressBar)
                this.findViewById(R.id.progressBar_manactivity);
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
        String length=mdata.getString("itemslength","length");
        int k=Integer.parseInt(length);
        if(mdata.getString("isfirstconnect","isfirstconnect").equals("1")) {
            mdata.putString("isfirstconnect", "isfirstconnect", "2");
        }
        for (int i = 0; i < k; i++) {

                    User r = new User();
                    r.setDeviceNma(mInformation.setFirstDeviceName(i));
                    r.setLastDateTouch(mInformation.getCurrentTime());
                    r.setMangerMarks("null");
                    r.setLastLocation(mInformation.setFirstLocation(i));
                    r.setIsOnLine("n");
                    list.add(r);


            }


        //this.setData(mDataBase, list);
        this.changeBackground(list);
        Log.i("zyl",mdata.getString("tableid",mInformation.getDeviceId()));
        return (list);
    }

    /**
     * 如果没有条目,显示一个提示背景
     * @param list
     */
    public void changeBackground(List<User> list){
        if (mdata.getString("isfirstconnect","isfirstconnect").equals("1")){
            return;
        }
        if(list.size()==0){
          layout.setBackgroundResource(R.drawable.manager_background_none);
        }
        else{
            layout.setBackgroundResource(R.color.white_layout);
        }
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
            Log.i("lzw",lat+"");
            Log.i("lzw",lon+"");
            locationClient.stop();
            Log.i("lzw","unlink");

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
          Log.i("lzw","locationSDKwork");
    }

    /**
     * 判断是否首次启动
     */
    private void ifFirstConnect(){

        if(mdata.getString("isfirstconnect","isfirstconnect").equals("0")){
            this.setContentView(R.layout.manager_layout_first);

         mdata.putString("isfirstconnect","isfirstconnect","1");
        }
        else{
            this.setContentView(R.layout.manager_layout);
            return;
        }
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
        Log.i("thislzw", "您的当前位置" + s + "已被保存");

    }

    /**
     * 向服务器发送请求
     */
    private void sendIdtoSever(){
        this.managerListThread=new ManagerListThread(mHandler,"managerlistid");
        String requestURL = ComParameter.HOST + "contect.action";
        this.managerListThread.setRequestPrepare(requestURL,this.prepareListParams());
        this.managerListThread.start();
    }

    /**
     * 向服务器提供参数
     * @return
     */
    private Map prepareListParams(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("id",mdata.getString("tableid",mInformation.getDeviceId()));
        return params;
    }

    /**
     * 处理服务器得到的联系人列表信息
     */
    private android.os.Handler mHandler = new android.os.Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {

            if (msg.getData().getInt("managerlistid") == ComParameter.STATE_RIGHT) {
                Map<String, String> resultMap = managerListThread.getResultMap();
                dealListFromSever(resultMap);
                mdata.putString("errorreport","a",resultMap.get("test"));

            }
            else if(msg.getData().getInt("managerlistid") ==ComParameter.STATE_ERROR) {
                Map<String, String> resultMap = managerListThread.getResultMap();
                ToastUtils.showToast(ManagerActivity.this,"网络连接失败");
                //如果连接失败,下一次重新请求服务器
                mdata.putString("isfirstconnect","isfirstconnect","0");
                mdata.putString("errorreport","a",resultMap.get("test"));
            }
        }
    };

    /**加载list数据
     *
     */
    private void loadList(){
        this.bindID();
        this.setListViewListener();
        this.mManagerAdapter = new ManagerAdapter(this.getData(), this, mDataBase);
        this.managerActivityListView.setAdapter(this.mManagerAdapter);
    }

    /**
     * 处理从服务器获取的数据,加载列表
     * @param resultMap
     */
    private void dealListFromSever(Map<String, String> resultMap){

        mdata.putString("itemslength","length",resultMap.get("length"));

            //如果是首次启动
            if(mdata.getString("isfirstconnect","isfirstconnect").equals("1"))
            {
                setDataBase(resultMap);
            }
            else {
                dealDataBase(resultMap);
            }
            setContentView(R.layout.manager_layout);
            loadList();

    }

    /**
     * 第一次从服务器获取的列表,并首次插入数据库
     * @param resultMap
     */
    private void setDataBase(Map<String, String> resultMap){
        //记录从服务器获取的列表长度
        String length=mdata.getString("itemslength","length");
        int k=Integer.parseInt(length);
        mdata.putString("itemslength","lastlength",length);
        if(k==0){return;}
        else {
            //插入数据库
            for (int i=0;i<k;i++){
                mDataBase.itemsInsert(TABLENAME,i,resultMap.get("idname"+i+""),
                        resultMap.get("subname"+i+""),resultMap.get("subname"+i+""),"暂未获取地理位置",
                        mInformation.getCurrentTime(),resultMap.get("isconnect"+i+""));
            }
        }
    }

    /**
     * 不是首次获取,删除原有数据,插入新数据
     * @param resultMap
     */
    private void dealDataBase(Map<String, String> resultMap){
        //上次和这次的列表长度可能不一样,用xml存储,并记录
        String lastlenth=mdata.getString("itemslength","lastlength");
        int last=Integer.parseInt(lastlenth);
        String lenth=mdata.getString("itemslength","length");
        int length=Integer.parseInt(lenth);
        //删除原有条目
        for (int i=0;i<last;i++){
            mDataBase.delitems(i,TABLENAME);
        }
        //新条目获取
        for (int i=0;i<length;i++){
            mDataBase.itemsInsert(TABLENAME,i,resultMap.get("idname"+i+""),
                    resultMap.get("subname"+i+""),resultMap.get("subname"+i+""),"暂未获取地理位置",
                    mInformation.getCurrentTime(),resultMap.get("isconnect"+i+""));
        }
    }
}
