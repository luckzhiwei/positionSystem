package org.dreamfly.positionsystem.Activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;

import org.dreamfly.positionsystem.Adapter.ManagerAdapter;
import org.dreamfly.positionsystem.CommonParameter.ComParameter;
import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.Custom.DefineListView;
import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.Database.DefinedShared;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Services.BaiduLocationService;
import org.dreamfly.positionsystem.Services.QuerySerivcesBinder;
import org.dreamfly.positionsystem.Services.QueryService;
import org.dreamfly.positionsystem.Services.Services;
import org.dreamfly.positionsystem.Thread.ManagerListThread;
import org.dreamfly.positionsystem.Thread.RenameThread;
import org.dreamfly.positionsystem.Utils.CurrentInformationUtils;
import org.dreamfly.positionsystem.Utils.LocationUtils;
import org.dreamfly.positionsystem.Utils.ToastUtils;
import org.dreamfly.positionsystem.Utils.UserInfoUtils;
import org.dreamfly.positionsystem.bean.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zhengyl on 15-1-13.
 * 管理者界面Activity类
 */
public class ManagerActivity extends Activity {


    private DefineListView managerActivityListView;
    private TextView txtManagerActivityTitle, txtManagertgetDeviceName;
    private LinearLayout layout;
    private ProgressBar proManactivity;
    private DefineDialog mDefineDialog;
    private View contentview;

    private User oneManager = new User();
    private User oneRegulator = new User();


    private ManagerListThread managerListThread;
    private RenameThread renameThread;

    private boolean isClear = true;
    private ManagerAdapter mManagerAdapter;
    private final static String TABLENAME = "regulatoritems";
    private int userTouchDistance;
    private float touchY;
    protected LocationUtils mLocationUtils;
    protected LocationClient locationClient;
    protected String lat;
    protected String lon;
    protected CurrentInformationUtils mInformation = new CurrentInformationUtils(this);
    protected DefinedShared mdata = new DefinedShared(this);
    protected DataBase mDataBase = new DataBase(this);
    protected final static String DEVICE = "deviceinformation";
    protected com.baidu.mapapi.search.geocode.GeoCoder mcoder;
    protected QueryService mService;

    private UserInfoUtils logoutUserInfoUtils;
    private QueryService.MsgSeneder mMessageSender;


    /**
     * 重写onCreate方法,完成数据初始化,加载操作
     *
     * @param savedInstanceState
     */
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
    /**
     * 重写onResume方法,重新获得焦点时更新列表
     */
    protected void onResume() {
        super.onResume();
        this.bindID();
        this.serviceIntital();
        //从数据库读取上一次的地理位置
        if (!mdata.getString(ComParameter.LOADING_STATE, ComParameter.CLICKING_STATE)
                .equals(ComParameter.STATE_FIRST)) {
            String temp = mdata.getString("pos", "pos");
            int pos = Integer.parseInt(temp);
            mDataBase.items_changeValue(TABLENAME, "position", mdata.getString("locationback", "locationback")
                    , pos);
            this.loadList();
        }
        //控制程序在第一次启动时不在这里加载列表数据(第一次请求的数据从网络获得)
        if (!mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE)
                .equals(ComParameter.STATE_SECOND)) {
            this.loadList();
        }
    }

    @Override
    /**
     * 重写onStop()方法
     */
    protected void onStop() {
        super.onStop();
        //this.unbindLocationService();
    }

    @Override
    /**
     * 重写onDestory方法
     */
    protected void onDestroy() {
        super.onDestroy();
        Log.i("zylactivity", "ondestroy");
        //this.stopLocationService();

    }

    @Override
    /**
     * 重写父类onKeyDown()方法
     */

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            //释放httpEntity请求空间
            // this.managerListThread.closeHttp();
            //确保第一次启动时在请求成功前处于数据加载界面
            if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                    equals(ComParameter.STATE_SECOND)) {
                mdata.putString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE, ComParameter.STATE_FIRST);
            }

        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            this.showPopwindow(this, contentview);
        }
        return false;
    }

    private void initial() {
        this.bindID();
        Log.i("lzw", "manage_intial");
        if (!mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                equals(ComParameter.STATE_SECOND)) {
            //如果是第一次启动,不在这里加载列表数据(第一次请求的数据从网络获得)
            this.loadList();
        }
        this.telNumSave(mInformation);
        this.sendIdtoSever();
        //发送请求至服务器，获取服务器上的联系人列表

        try {
            // this.testDealresponse();
        } catch (Exception e) {
        }
        //本地测试
    }

    private void bindID() {
        contentview = this.findViewById(R.id.manageractivity_layout);
        this.proManactivity = (ProgressBar)
                this.findViewById(R.id.progressBar_manactivity);
        this.managerActivityListView = (DefineListView)
                this.findViewById(R.id.delistiview_manageractivity_showmanger);
        this.txtManagerActivityTitle = (TextView)
                this.findViewById(R.id.txt_manageractivity_title);
        this.txtManagertgetDeviceName = (TextView)
                this.findViewById(R.id.manageractivity_txt2_name);
        this.layout = (LinearLayout)
                this.findViewById(R.id.manageractivity_layout);

    }

    private void serviceIntital() {
        if (!mdata.getString(ComParameter.LOADING_STATE, ComParameter.SERVICE_STATE)
                .equals(ComParameter.STATE_SECOND)) {
           this.mMessageSender=new QueryService.MsgSeneder() {
               public void sendMsgLocationToShow(String userLcation) {
                    Message msg=new Message();
                    Bundle bd=new Bundle();
                    bd.putString("userlocation",userLcation);
                    msg.setData(bd);
                    queryServiceHandler.sendMessage(msg);
               }
           };
           //实例化抽象类的线程
           this.startLocationService();
           this.bindLocationService();
     } else {
            this.bindLocationService();
        }
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
        //设置下拉事件监听,请求服务器刷新列表
        this.managerActivityListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                userTouchDistance = managerActivityListView.getUserTouchDistance();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchY = event.getRawY();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    float detalY = event.getRawY() - touchY;
                    touchY = event.getRawY();
                    managerActivityListView.dynSetHeadViewHeight(managerActivityListView.calDistance(detalY));
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (managerActivityListView.getFirstVisiblePosition() == 0
                            && userTouchDistance > 250) {
                        managerActivityListView.dynSetHeadViewHeight(250);
                        if (!managerActivityListView.getIsFreshing()) {
                            sendIdtoSever();
                            managerActivityListView.setIsFreshing(true);
                        }
                        userTouchDistance = 0;
                    }

                    //用户划过的距离必须超过最小才行，对顶部设置一个合适的大小来显示
                    else if (userTouchDistance < 250) {
                        managerActivityListView.dynSetHeadViewHeight(0);
                    }  //如果用户实际划过的距离小于最小距离,那么listview的头部是不会显示的
                    userTouchDistance = 0;
                    //每次划过之后都将用户的实际划过的距离清零
                }
                return false;
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
            r.setIsOnLine("n");
            list.add(r);
        }
        this.changeBackground(list);
        Log.i("zyl", mdata.getString("tableid", mInformation.getDeviceId()));
        return (list);
    }

    /**
     * 如果没有条目,显示一个提示背景
     *
     * @param list
     */
    public void changeBackground(List<User> list) {
        if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                equals(ComParameter.STATE_SECOND)) {
            return;
        }
        if (list.size() == 0) {
            layout.setBackgroundResource(R.drawable.manager_background_none);
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
        this.mDefineDialog = new DefineDialog(ManagerActivity.this).buiider(true).
                setTitle("修改备注名:").setDefineDialogCanceable(true).setPosBtnTxt("确定").
                setNegBtnTxt("取消").show();
        PositiveButtonListener mPositiveButtonListener =

                new PositiveButtonListener(position, mDataBase,
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

        public PositiveButtonListener(int pos,
                                      DataBase mDataBase, EditText mEdittext, DefineDialog mDialog) {
            this.pos = pos;
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
            oneRegulator.setMangerMarks(mEditText.getText().toString());
            Log.v("textstring", oneRegulator.getMangerMarks());
            tellSeverRename(pos, mEditText);
            oneRegulator.setDataBaseID(pos - 1);
            mDialog.dismiss();
        }
    }


    /**
     * 判断是否首次启动，用于设置activity的布局
     */
    private void ifFirstConnect() {
        //用sharedpreference存储登陆状态
        mdata.putString(ComParameter.LOADING_STATE, ComParameter.IDENTITY_STATE, "manager");
        if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE)
                .equals(ComParameter.STATE_FIRST)) {
            //第一次启动加载界面
            this.setContentView(R.layout.manager_layout_first);
            mdata.putString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE,
                    ComParameter.STATE_SECOND);
        } else {
            this.setContentView(R.layout.manager_layout);
            return;
        }
    }

    /**
     * 存储本机号码
     */
    protected void telNumSave(CurrentInformationUtils mInformation) {
        Log.i("zyl", mInformation.getDeviceTelNum());
        mDataBase.items_changeValue(DEVICE, "telnumber", mInformation.getDeviceTelNum(), 0);
    }


    /**
     * 向服务器发送请求
     * 获取联系人列表
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

    /**
     * 向服务器提供参数
     *
     * @return
     */
    protected Map prepareListParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", mdata.getString("tableid", mInformation.getDeviceId()));
        return params;
    }

    protected Map prepareNameListParams(int pos, EditText mEditText) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("fromid", mdata.getString("tableid", mInformation.getDeviceId()));
        Cursor cur = mDataBase.Selector(pos - 1, TABLENAME);
        if (cur.moveToNext()) {
            params.put("toid", cur.getString(cur.getColumnIndex("subid")));
        }
        params.put("name", mEditText.getText().toString());
        cur.close();
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
            } else {
                this.dealErrorMsg(msg);
                //处理错误信息
            }

        }

        private void dealErrorMsg(Message msg) {
            if (msg.getData().getInt("managerlistid") == ComParameter.STATE_ERROR) {
                ToastUtils.showToast(getApplicationContext(), "请求失败，请尝试重新获取");
            } else if (msg.getData().getInt("NetWorkException") == ComParameter.STATE_ERROR_NETWORK) {
                ToastUtils.showToast(getApplicationContext(), "网络连接超时，请稍候尝试");
            }
            if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                    equals(ComParameter.STATE_SECOND)) {
                //如果第一次连接失败,下一次重新请求服务器
                mdata.putString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE,
                        ComParameter.STATE_FIRST);
            }
            if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                    equals(ComParameter.STATE_THIRD)) {
                managerActivityListView = (DefineListView)
                        findViewById(R.id.delistiview_manageractivity_showmanger);
                managerActivityListView.dynSetHeadViewHeight(0);
                //第二次请求失败，列表上部的视图消失
            }
        }
    };
    /**
     * 处理更改备注的信息
     */
    private Handler renameHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.getData().getInt("renamestate") == ComParameter.STATE_RIGHT) {
                dealRenameMessage();

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

    private void dealRenameMessage() {
        Map<String, String> resultMap = renameThread.getResultMap();
        String state = resultMap.get("state");
        if (state != null) {
            if (state.equals("success")) {
                ToastUtils.showToast(getApplication(), "修改成功");
                mDataBase.items_changeValue(TABLENAME, "subname", oneRegulator.getMangerMarks(), oneRegulator.getDataBaseID());
                loadList();
            } else if (state.equals("fail")) {
                Log.i("lzw", "rename_failed");
                ToastUtils.showToast(getApplication(), resultMap.get("failReason"));
            }
        } else {
            Log.i("zyl", "请求失败");
        }
    }


    /**
     * 加载list数据
     */
    private void loadList() {
        this.bindID();
        this.setListViewListener();
        this.mManagerAdapter = new ManagerAdapter(this.getData(), this, mDataBase);
        this.managerActivityListView.setAdapter(this.mManagerAdapter);
    }

    /**
     * 处理从服务器获取的数据,加载列表
     * loadList（）函数请求服务器
     *
     * @param resultMap
     */
    protected void dealListFromSever(Map<String, String> resultMap) {

        if(resultMap.get("connectedstate").equals("n")){
            mdata.putString("itemslength","length",""+0);
        }
        else {
            mdata.putString("itemslength", "length", resultMap.get("length"));
        }

        if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.LOADING_STATE).
                equals(ComParameter.STATE_SECOND)) {
            setDataBase(resultMap);
        } else {
            dealDataBase(resultMap);
        }
        //每一次启动对本地数据库的操作是不一样的

        this.setContentView(R.layout.manager_layout);
        loadList();
        managerActivityListView.setIsFreshing(false);
        //设置listview的加载状态为不刷新
        managerActivityListView.dynSetHeadViewHeight(0);
        userTouchDistance = 0;

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
        }
        //列表为空就返回，不插入数据库
        else {
            for (int i = 0; i < k; i++) {
                mDataBase.itemsInsert(TABLENAME, i, resultMap.get("idname" + i + ""),
                        resultMap.get("subname" + i + ""), resultMap.get("subname" + i + ""), "暂未获取地理位置",
                        mInformation.getCurrentTime(), resultMap.get("isconnect" + i + ""));
            }
            //插入数据库
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
            mDataBase.itemsInsert(TABLENAME, i, resultMap.get("idname" + i + ""),
                    resultMap.get("subname" + i + ""), resultMap.get("subname" + i + ""), "暂未获取地理位置",
                    mInformation.getCurrentTime(), resultMap.get("isconnect" + i + ""));
        }
        //将这次的长度作为下一次更新的"上次长度"
        mdata.putString("itemslength", "lastlength", lenth);
    }

    /**
     * Json字符串本地测试方法
     *
     * @throws Exception
     */
    private void testDealresponse() throws Exception {
        Map<String, String> resultMap = new HashMap<String, String>();
        String responseString1 = "[{\"id\":\"24\",\"subname\":\"xiaomi\",\"isconnect\":\"y\"},{\"id\":\"25\",\"subname\":\"iPhone 6plus\",\"isconnect\":\"y\"},{\"id\":\"26\",\"subname\":\"vertu\",\"isconnect\":\"y\"}]";
        JSONArray jsonArray = new JSONArray(responseString1);
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = (JSONObject) jsonArray.get(i);
            String cast = i + "";
            resultMap.put("idname" + cast, (String) obj.get("id"));
            Log.i("zyl", resultMap.get("idname" + cast));
            resultMap.put("subname" + cast, (String) obj.get("subname"));
            Log.i("zyl", resultMap.get("subname" + cast));
            resultMap.put("isconnect" + cast + "", (String) obj.get("isconnect"));
            Log.i("zyl", resultMap.get("isconnect" + cast));

        }
        resultMap.put("length", "" + jsonArray.length());
        Log.i("zyl", resultMap.get("length"));
        dealListFromSever(resultMap);
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
        final PopupWindow popWindow = new PopupWindow(mPopwindow, 700, 350, true);
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
                ManagerActivity.this.finish();
            }
        });
        //注销登录按钮的事件监听
        exitButon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popWindow.dismiss();
                ManagerActivity.this.finish();
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
        Map<String, String> tmpMap = this.logoutUserInfoUtils.getUserInfoMap();
        if(tmpMap!=null) {
            tmpMap.remove("username");
            tmpMap.remove("password");
            tmpMap.remove("type");
            //注销清楚本地缓存的文件信息
            tmpMap.put("loginstate", "seclogin");
            this.logoutUserInfoUtils.updateUserInfo(tmpMap);
        }
        //写入二次登录的时候的情况
        this.startActivity(new Intent(ManagerActivity.this, LoginActivity.class));
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder myBinder) {
                QuerySerivcesBinder binder= (QuerySerivcesBinder)myBinder;
                binder.setMsgSender(mMessageSender);
                binder.startQuery();
            //绑定到轮询线程要做的事情
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    /**
     * 开启轮询线程的service
     */
    private void startLocationService() {
        Log.i("service", "[SERVICE]start");
        startService(new Intent(ManagerActivity.this, QueryService.class));
    }

    /**
     * 停止轮询线程的service
     */
    private void stopLocationService() {
        Log.i("service", "[SERVICE]stop");
        stopService(new Intent(ManagerActivity.this, QueryService.class));
    }

    /**
     * 绑定轮询线程的service
     */
    private void bindLocationService() {
        Log.i("service", "[SERVICE] beBinded");
        bindService(new Intent(ManagerActivity.this,
                QueryService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    /**
     * 解除绑定轮询线程的service
     */
    private void unbindLocationService() {
        Log.i("service", "[SERVICE] Unbind");
        unbindService(mConnection);
    }

}
