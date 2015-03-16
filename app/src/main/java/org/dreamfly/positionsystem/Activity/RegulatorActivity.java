package org.dreamfly.positionsystem.Activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;

import org.dreamfly.positionsystem.Adapter.RegulatorAdapter;
import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.Custom.DefineListView;
import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.Database.DefinedShared;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Services.QuerySerivcesBinder;
import org.dreamfly.positionsystem.Services.QueryService;
import org.dreamfly.positionsystem.Thread.BaseThread;
import org.dreamfly.positionsystem.Thread.LoginRequestThread;
import org.dreamfly.positionsystem.Thread.ManagerListThread;
import org.dreamfly.positionsystem.Thread.RenameThread;
import org.dreamfly.positionsystem.Utils.CurrentInformationUtils;
import org.dreamfly.positionsystem.Utils.LocationUtils;
import org.dreamfly.positionsystem.Utils.ToastUtils;
import org.dreamfly.positionsystem.Utils.UserInfoUtils;
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
public class RegulatorActivity extends Activity  {

    private static RegulatorActivity regulatorActivity;
    private DefineListView listViewRegulatorActivityReglutorList;
    private TextView txtRegulatorActivityTitle;
    private LinearLayout layout;
    private DefineDialog mDefineDialog;
    private Button btnRefresh;
    private RegulatorAdapter mRegulatordapter;
    private ProgressBar proRegulator;
    private User oneRegulator = new User();
    private ManagerActivity manager = new ManagerActivity();
    private ComParameter com = new ComParameter();
    private int userTouchDistance;
    private View contentview;
    private float touchY;
    private UserInfoUtils logoutUserInfoUtils;
    private boolean isClear = true;
    private ManagerListThread managerListThread;
    private RenameThread renameThread;
    private BaseThread secLoginThread;
    private WindowManager wm;
    protected LocationUtils mLocationUtils;
    protected LocationClient locationClient;
    protected CurrentInformationUtils mInformation = new CurrentInformationUtils(this);
    protected DefinedShared mdata = new DefinedShared(this);
    protected DataBase mDataBase = new DataBase(this);
    protected QueryService mService;
    private QueryService.MsgSender mMessageSender;
    protected com.baidu.mapapi.search.geocode.GeoCoder mcoder;
    protected String lat;
    protected String lon;

    public RegulatorActivity(){
        regulatorActivity=this;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        String libName = "BaiduMapSDK_v3_2_0_11";
        System.loadLibrary(libName);
        this.isFirstConnect();
        this.initial();
    }
    @Override
    /**
     * 重写onStop()方法
     */
    protected void onStop() {
        super.onStop();
        //this.unbindLocationService();
    }

    protected void onDestroy(){
        super.onDestroy();
        unbindLocationService();
    }

    protected void onResume() {
        super.onResume();
        this.bindID();
        this.serviceIntital();
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
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if(contentview==null){
                contentview=this.findViewById(R.id.manfirst);
            }
            this.showPopwindow(this, contentview);
        }
        return false;
    }

    private void initial() {
        Log.i("lzw", "unmanager_init");
        this.bindID();


        if (!mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                equals(ComParameter.STATE_SECOND)) {
            //如果是第一次启动,不在这里加载列表数据(第一次请求的数据从网络获得)
            this.loadList();
        }

        this.sendSecLoginToSever();
        this.sendIdtoSever();




    }

    private void bindID() {
        contentview=this.findViewById(R.id.myregulator_activity_layout);
        this.proRegulator=(ProgressBar)
                this.findViewById(R.id.progressBar_manactivity);
        this.listViewRegulatorActivityReglutorList = (DefineListView)
                this.findViewById(R.id.listivew_regulatoractivity_regulatorlist);
        this.txtRegulatorActivityTitle = (TextView)
                this.findViewById(R.id.txt_regulatoractivity_title);
        this.layout = (LinearLayout)
                this.findViewById(R.id.myregulator_activity_layout);
        this.btnRefresh=(Button)
                this.findViewById(R.id.btn_regulator_refresh);
        wm=(WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if(proRegulator==null){
            Log.i("zyl175","progressbar is null");
        }


    }

    private void sendSecLoginToSever() {
        this.secLoginThread = new LoginRequestThread(secLoginHandler, "secloginstate");
        UserInfoUtils mUtils = new UserInfoUtils(this);
        Map<String, String> info = mUtils.getUserInfoMap();
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", info.get("userrID"));
        params.put("username", info.get("famliyName"));
        params.put("password", info.get("password"));
        String requestURL = ComParameter.HOST + "user_login.action";
        this.secLoginThread.setRequestPrepare(requestURL, params);
        this.secLoginThread.start();
    }

    private void serviceIntital() {
        if (!mdata.getString(ComParameter.LOADING_STATE, ComParameter.SERVICE_STATE)
                .equals(ComParameter.STATE_SECOND)) {
            this.mMessageSender=new QueryService.MsgSender() {
                public void sendMsgLocationToShow(String userLcation) {
                    Message msg=new Message();
                    Bundle bd=new Bundle();
                    bd.putString("userlocation",userLcation);
                    msg.setData(bd);
                    queryServiceHandler.sendMessage(msg);
                }
                public void sendMsgError(int state){
                    Message msg=new Message();
                    Bundle bd=new Bundle();
                    bd.putInt("errorstate",state);
                    msg.setData(bd);
                    queryServiceHandler.sendMessage(msg);
                }
            };
            this.startLocationService();
            this.bindLocationService();
        } else {
            this.bindLocationService();
        }
    }


    private void setCLickListener() {
        this.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIdtoSever();
                btnRefresh.setVisibility(View.INVISIBLE);
            }
        });
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
                      if(!listViewRegulatorActivityReglutorList.getIsFreshing()) {
                          listViewRegulatorActivityReglutorList.setIsFreshing(true);
                          sendIdtoSever();
                      }
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
        if(length.equals("")){
            length=""+0;
        }
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
            layout.setBackgroundResource(R.drawable.regulator_none);
            btnRefresh.setVisibility(View.VISIBLE);
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

                new RegPositiveButtonListener(position, mDataBase,
                        this.mDefineDialog.getEditText(), this.mDefineDialog);
        mDefineDialog.setPosBtnClickListener(mPositiveButtonListener);

    }

    public class RegPositiveButtonListener implements View.OnClickListener {
        protected EditText mEditText;

        protected DataBase mDataBase;
        protected Context mcontext;
        protected int pos;
        protected DefineDialog mDialog;

        public RegPositiveButtonListener(int pos,
                                         DataBase mDataBase, EditText mEdittext, DefineDialog mDialog) {
            this.pos = pos;
            this.mDataBase = mDataBase;
            this.mEditText = mEdittext;
            this.mDialog = mDialog;
        }

        @Override
        public void onClick(View view) {
            oneRegulator.setMangerMarks(mEditText.getText().toString());
            Log.v("textstring", oneRegulator.getMangerMarks());
            tellSeverRename(pos, mEditText);
            oneRegulator.setDataBaseID(pos-1);
            mDialog.dismiss();
        }
    }
    private void isFirstConnect() {

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
        this.mRegulatordapter = new RegulatorAdapter(this.getData(), this, mDataBase,mHandler);
        this.listViewRegulatorActivityReglutorList.setAdapter(mRegulatordapter);
        this.setCLickListener();
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
    /**
     * 发送修改备注名请求
     *
     * @param pos 表示第几个条目
     */
    protected void tellSeverRename(int pos, EditText mEditText) {
        this.renameThread = new RenameThread(renameHandler, "renamestate");
        String requestURL = ComParameter.HOST + "rename.action";
        this.renameThread.setRequestPrepare(requestURL, this.prepareNameListParams(pos, mEditText));
        this.renameThread.start();
    }

    protected Map prepareListParams() {

        Map<String, String> params = new HashMap<String, String>();
        UserInfoUtils userInfoUtils=new UserInfoUtils(this);
        String userID=userInfoUtils.getServerId()+"";
        params.put("id", userID);
        return params;
    }

    protected Map prepareNameListParams(int pos, EditText mEditText) {
        Map<String, String> params = new HashMap<String, String>();
        UserInfoUtils userInfoUtils=new UserInfoUtils(this);
        params.put("fromid",userInfoUtils.getServerId()+"");
        Cursor cur = mDataBase.Selector(pos - 1, ComParameter.MANTABLENAME);
        if (cur.moveToNext()) {
            params.put("toid", cur.getString(cur.getColumnIndex("subid")));
        }
        params.put("name", mEditText.getText().toString());
        cur.close();
        return params;
    }
    private Handler renameHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.getData().getInt("renamestate") == ComParameter.STATE_RIGHT) {
                Map<String,String> resultMap=renameThread.getResultMap();
                if(renameThread!=null) {
                    dealRenameMessage(resultMap);
                }
            } else if (msg.getData().getInt("renamestate") == ComParameter.STATE_ERROR) {
                ToastUtils.showToast(getApplicationContext(), ComParameter.ERRORINFO);
            }
        }
    };
    private Handler queryServiceHandler=new Handler(Looper.getMainLooper()){
        public void handleMessage(Message msg) {
            String userlcation=msg.getData().getString("userlocation");
            if(userlcation!=null){
                dealUserLocation(userlcation);
            }

        }

        /**
         * 处理UI跳转
         * @param userlocation
         */
        private void dealUserLocation(String userlocation){
            if(userlocation.equals("null")){
                //提示用户处理失败
            }else{
                //跳转到PositionActivity中去
            }
        }
    };
    private Handler secLoginHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.getData().getInt("secloginstate") == ComParameter.STATE_RIGHT) {
                dealSecLoginMessage(secLoginThread.getResultMap());
            } else {
                Log.i("lzw", "二次登录失败");
            }
        }

        private void dealSecLoginMessage(Map<String, String> resultMap) {
            if (resultMap.get("loginstate") == null) {
                Log.i("lzw", "reg-L468 哈希表处理异常");
            } else {
                if (resultMap.get("loginstate").equals("unlogin")) {
                    ToastUtils.showToast(getApplicationContext(), "与服务器连接失败,尝试重新登陆");
                }
            }
        }

    };

    private void dealRenameMessage(Map<String, String> resultMap) {

        String state = resultMap.get("state");
        if (state != null) {
            if (state.equals("success")) {
                ToastUtils.showToast(getApplication(), "修改成功");
                mDataBase.items_changeValue(ComParameter.MANTABLENAME, "subname", oneRegulator.getMangerMarks(), oneRegulator.getDataBaseID());
                loadList();
            } else if (state.equals("fail")) {
                Log.i("lzw", "rename_failed");
                ToastUtils.showToast(getApplication(), resultMap.get("failReason"));
            }
        } else {
            Log.i("zyl", "请求失败");
        }
    }

    private android.os.Handler mHandler = new android.os.Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.getData().getInt("managerlistid") == ComParameter.STATE_RIGHT) {
                Map<String, String> resultMap = managerListThread.getResultMap();
                dealListFromSever(resultMap);
            } else {
                this.dealErrorMsg(msg);
                //处理错误信息
            }

        }
        private void dealErrorMsg(Message msg) {

            if (msg.getData().getInt("NetWorkException") == ComParameter.STATE_ERROR_NETWORK) {
                ToastUtils.showToast(getApplicationContext(), "网络连接超时，请稍候尝试");
            }else{
                 ToastUtils.showToast(getApplication(),"加载错误，请稍候重试");
            }
            if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                    equals(ComParameter.STATE_SECOND)) {
                //弹出按钮再请求一次
                proRegulator.setVisibility(View.GONE);
                Button btnRetry=(Button)findViewById(R.id.btn_manageractivity_retry);
                btnRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendIdtoSever();
                        proRegulator.setVisibility(View.VISIBLE);
                    }
                });
                btnRetry.setVisibility(View.VISIBLE);
                //如果第一次连接失败,下一次重新请求服务器
                mdata.putString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE,
                        ComParameter.STATE_FIRST);
            }
            if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                    equals(ComParameter.STATE_THIRD)) {

                    listViewRegulatorActivityReglutorList = (DefineListView)
                        findViewById(R.id.listivew_regulatoractivity_regulatorlist);
                    listViewRegulatorActivityReglutorList.dynSetHeadViewHeight(0);
                //第二次请求失败，列表上部的视图消失
                listViewRegulatorActivityReglutorList.setIsFreshing(false);
            }

        }

    };

    /**
     * 处理从服务器获取的数据,加载列表
     *
     * @param resultMap
     */
    protected void dealListFromSever(Map<String, String> resultMap) {
        if(resultMap.get("connectedstate")==null){
            ToastUtils.showToast(getApplicationContext(),"处理异常,请稍后再刷新");
            return;
        }
        else {
            if (resultMap.get("connectedstate").equals("n")) {
                mdata.putString("itemslength", "length", "" + 0);
            } else {
                mdata.putString("itemslength", "length", resultMap.get("length"));
            }
            //如果第一次请求失败,按下那个按钮请求又成功了,在这里修改一下登陆状态
            if(mdata.getString(ComParameter.LOADING_STATE,ComParameter.LOADING_STATE).
                    equals(ComParameter.STATE_FIRST)){
                mdata.putString(ComParameter.LOADING_STATE,ComParameter.LOADING_STATE,
                        ComParameter.STATE_SECOND);
                setDataBase(resultMap);
            }
            if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                    equals(ComParameter.STATE_SECOND)) {
                setDataBase(resultMap);

            } else {
                dealDataBase(resultMap);
            }
            ToastUtils.showToast(getApplicationContext(),"联系人列表更新成功");
            //每一次启动对本地数据库的操作是不一样的
            setContentView(R.layout.regulator_layout);
            loadList();
            listViewRegulatorActivityReglutorList.dynSetHeadViewHeight(0);
            userTouchDistance = 0;
            listViewRegulatorActivityReglutorList.setIsFreshing(false);
        }

    }

    /**
     * 第一次从服务器获取的列表,并首次插入数据库
     *
     * @param resultMap
     */
    private void setDataBase(Map<String, String> resultMap) {
        //记录从服务器获取的列表长度
        if(resultMap.get("connectedstate").equals("n")){
            mdata.putString("itemslength","length",""+0);
        }
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

        //更新数据
        for (int i=0;i<length;i++){
            mDataBase.items_changeValue(ComParameter.MANTABLENAME,"isconnect",resultMap.get("isconnect"+i+""),i);
        }
        //将这次的长度作为下一次更新的"上次长度"
        mdata.putString("itemslength", "lastlength", lenth);
    }
    /**
     * 显示popwindow替代菜单栏效果
     *
     * @param context
     * @param parent
     */
    private void showPopwindow(Context context, View parent) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View mPopwindow = inflater.inflate(R.layout.man_popwindow, null, false);
        int height=wm.getDefaultDisplay().getHeight()/3;
        final PopupWindow popWindow = new PopupWindow(mPopwindow,wm.getDefaultDisplay().getWidth() , height, true);
        bindButtonID(mPopwindow, popWindow);
        popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }
    /**
     * 为按钮绑定监听并设置监听事件
     *
     * @param mPopwindow
     * @param popWindow
     */
    private void bindButtonID(View mPopwindow, final PopupWindow popWindow) {
        Button logoutButton = (Button) mPopwindow.findViewById(R.id.btn_menu_logout);
        Button exitButon = (Button) mPopwindow.findViewById(R.id.btn_menu_exit);
        Button cancelButton = (Button) mPopwindow.findViewById(R.id.btn_menu_cancel);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popWindow.dismiss();
                callServerLogout();
                finish();
            }
        });
        //注销登录按钮的事件监听
        exitButon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popWindow.dismiss();
                finish();
                stopLocationService();
            }
        });
        //退出按钮的时间监听
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });
        //popwindow消除
    }

    private void callServerLogout() {
        dealAfterlogout();
    }

    private void dealAfterlogout() {
        mdata.putString(ComParameter.LOADING_STATE, ComParameter.LOGIN_STATE, ComParameter.STATE_THIRD);
        this.logoutUserInfoUtils = new UserInfoUtils(this);
        this.logoutUserInfoUtils.clearUserInfo();
        for (int i=0;i<getData().size();i++){
            mDataBase.delitems(i,ComParameter.MANTABLENAME);
        }
        startActivity(new Intent(RegulatorActivity.this, LoginActivity.class));
    }
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder myBinder) {
            QuerySerivcesBinder binder= (QuerySerivcesBinder)myBinder;
            binder.setMsgSender(mMessageSender);
            binder.startQuery();
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    /**
     * 开启service
     */
    private void startLocationService() {
        Log.i("service", "[SERVICE]start");
        startService(new Intent(RegulatorActivity.this, QueryService.class));
    }

    /**
     * 停止service
     */
    private void stopLocationService() {
        Log.i("service", "[SERVICE]stop");
        stopService(new Intent(RegulatorActivity.this, QueryService.class));
    }

    /**
     * 绑定service
     */
    private void bindLocationService() {
        Log.i("service", "[SERVICE] beBinded");
        bindService(new Intent(RegulatorActivity.this,
                QueryService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    /**
     * 解除绑定service
     */
    private void unbindLocationService() {
        Log.i("service", "[SERVICE] Unbind");
        unbindService(mConnection);
    }

    public static RegulatorActivity getRegulatorActivity(){
        return regulatorActivity;
    }
}


