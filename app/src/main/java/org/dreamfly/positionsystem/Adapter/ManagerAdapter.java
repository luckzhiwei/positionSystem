package org.dreamfly.positionsystem.Adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.List;

import org.dreamfly.positionsystem.Activity.PositionActivity;
import org.dreamfly.positionsystem.CommonParameter.ComParameter;
import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.Database.DefinedShared;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Thread.BaseThread;
import org.dreamfly.positionsystem.Thread.LocationGetThread;
import org.dreamfly.positionsystem.Utils.CurrentInformationUtils;

import org.dreamfly.positionsystem.Utils.ToastUtils;
import org.dreamfly.positionsystem.Utils.UserInfoUtils;
import org.dreamfly.positionsystem.bean.User;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by lzw on 2015/1/15.
 */

public class ManagerAdapter extends BaseAdapter {

    protected   Context mContext;
    protected List<User> mRegulatorList;//适配器中应该含有的容器,
    protected   DataBase mDataBase;
    protected   User regulator;
    private Cursor cur;
    private DefineDialog mDefineDialog = null;
    private final static String TABLENAME="regulatoritems";
    private CurrentInformationUtils mInformation = new CurrentInformationUtils(mContext);
    private DefinedShared mdata;
    protected Handler mHandler;
    protected LocationGetThread mLocationGetThread;

    public ManagerAdapter(List<User> mRegulatorList, Context context, DataBase mDataBase,Handler mHandler) {
        this.mRegulatorList = mRegulatorList;
        this.mContext = context;
        this.mDataBase = mDataBase;
        this.mHandler=mHandler;
    }

    public int getCount() {

        return (this.mRegulatorList.size());
    }

    public Object getItem(int position) {
        return (this.mRegulatorList.get(position));
    }

    public long getItemId(int position) {
        return (position);
    }

    /**
     * 加载xml的条目,实现数据的初始化,为自己的控件设置监听事件
     * @param position
     * @param contentview
     * @param arg2
     * @return
     */
    public View getView(int position, View contentview, ViewGroup arg2) {//加载XML视图文件

        ViewHolder holder;
        this.regulator = this.mRegulatorList.get(position);
        if (contentview == null) {
            contentview = LayoutInflater.from(this.mContext).inflate(R.layout.manager_items, null);
            holder = new ViewHolder();
            this.bindID(contentview, holder);
            this.setItemInfo(holder, position, mDataBase);
            this.setClickListener(holder, position, mDataBase);
            contentview.setTag(holder);

        } else {
            holder = (ViewHolder) contentview.getTag();
            this.bindID(contentview, holder);
            this.setItemInfo( holder, position, mDataBase);
            this.setClickListener(holder, position, mDataBase);
        }
        return (contentview);
    }

    /**
     * 设置一个容器用于存放控件
     */
    public class ViewHolder {

        TextView txtManagerItemMarkName;
        TextView txtManagertmeLastTouchTime;
        TextView txtManagerItemLastLocation;
        TextView txtManagertgetDeviceName;
        ImageView imgManagerItemUserHead;
        Button btnManagerItemPhone;
        Button btnManagerItemPosition;

    }

    /**
     * 自定义确定按钮监听类
     */
    public class PositiveButtonListener implements View.OnClickListener {

        protected int pos;
        protected User oneRegulator;
        protected DataBase mDataBase;
        protected DefineDialog mDefineDialog;

        final String s[] = {"南京路234号", "上海路278号", "北京路123号", "河北路456号",
                "南山路88号", "合肥路87号", "河南路768号"};

        public PositiveButtonListener(int pos, final User oneRegulator, DataBase mDataBase,
                                      DefineDialog mDefineDialog) {
            this.pos = pos;
            this.oneRegulator = oneRegulator;
            this.mDataBase = mDataBase;
            this.mDefineDialog=mDefineDialog;
        }

        /**
         * 按下按钮后将地理位置信息和定位实现信息保存在数据库中
         * @param view
         */
        public void onClick(View view) {
            ToastUtils.showToast(mContext,"正在获取对方的地理位置");
            this.getUserLocationFromSever(pos);
        }

        /**
         * 向服务器发送获取对方的地理位置的请求
         */
        private void getUserLocationFromSever(int pos){
            HashMap<String,String> params=new HashMap<String,String>();
            params.put("type","location");
            UserInfoUtils tmpUtils=new UserInfoUtils(mContext);
            params.put("fromid",tmpUtils.getServerId()+"");
            Cursor cur=mDataBase.Selector(pos, TABLENAME);
            if(cur.moveToNext()) {
                params.put("toid", cur.getString(cur.getColumnIndex("subid")));
            }
            cur.close();

            mLocationGetThread=new LocationGetThread(mHandler,"getlocationstate");
            String requestURL = ComParameter.HOST + "control.action";
            mLocationGetThread.setRequestPrepare(requestURL, params);
            mLocationGetThread.start();
            oneRegulator.setLastDateTouch(mInformation.getCurrentTime());
            mDataBase.items_changeValue(TABLENAME,"time", oneRegulator.getLastDateTouch(), pos);
            mDefineDialog.dismiss();


        }

    }

    /**
     * 绑定视图ID,holder是组件容器
     *
     * @param contentview
     * @param holder
     */
    private void bindID(View contentview, ViewHolder holder) {
        holder.imgManagerItemUserHead = (ImageView) contentview.findViewById(R.id.manageractivity_imv1);
        holder.txtManagerItemLastLocation = (TextView) contentview.findViewById(R.id.manageractivity_txt2_location);
        holder.txtManagerItemMarkName = (TextView) contentview.findViewById(R.id.manageractivity_txt2_name);
        holder.txtManagertgetDeviceName = (TextView) contentview.findViewById(R.id.manageractivity_txt2_name);
        holder.txtManagertmeLastTouchTime = (TextView) contentview.findViewById(R.id.manageractivity_txt3_time);
        holder.btnManagerItemPhone = (Button) contentview.findViewById(R.id.manageractivity_btn_phone);
        holder.btnManagerItemPosition = (Button) contentview.findViewById(R.id.manageractivity_btn_position);
    }

    /**
     * 一个容器的实例填入函数中去
     * 传递必要的参数,调用items初始化方法和变换头像方法
     * @param
     * @param holder
     */
    public void setItemInfo(ViewHolder holder, int position, DataBase mDataBase) {

        this.initItems(holder,mDataBase,position);
        this.changePortrait(holder,position,mDataBase);

    }

    /**
     * 设置监听事件
     * @param holder
     * @param pos
     * @param mDataBase
     */
    private void setClickListener(final ViewHolder holder, final int pos, final DataBase mDataBase) {
        final User oneRegulator = this.mRegulatorList.get(pos);

        holder.btnManagerItemPosition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setListDialog(pos, oneRegulator, mDataBase);
            }
        });
    }

    /**
     * 调用自定义dialog,实现弹出对话框
     * @param pos
     * @param oneRegulator
     * @param mDataBase
     */
    private void setListDialog(int pos, User oneRegulator, DataBase mDataBase) {
        mDefineDialog = new DefineDialog(mContext).buiider(false).setDefineDialogCanceable(true)
                .setTitle("是否获取地理位置").show();
        PositiveButtonListener mPositiveButtonListener =
                new PositiveButtonListener(pos, oneRegulator, mDataBase,mDefineDialog);

        mDefineDialog.setPosBtnClickListener(mPositiveButtonListener);

    }

    /**
     * 该方法用于实现头像交替变换,如果网络未连接,显示灰色头像
     * @param holder
     * @param position
     */
    public void changePortrait(ViewHolder holder,int position,DataBase mDataBase) {
        cur=mDataBase.Selector(position,TABLENAME);
        while (cur.moveToNext()) {
            String connection = cur.getString(cur.getColumnIndex("isconnect"));
            if (connection.equals("n")) {
                if ((position % 2) == 0) {
                    holder.imgManagerItemUserHead.setImageResource(R.drawable.manager_protrait2_unconected);
                    holder.imgManagerItemUserHead.getDrawable();
                } else {
                    holder.imgManagerItemUserHead.setImageResource(R.drawable.manager_protrait1_unconected);
                    holder.imgManagerItemUserHead.getDrawable();
                }
            } else {
                if ((position % 2) == 0) {
                    holder.imgManagerItemUserHead.setImageResource(R.drawable.manregactivity_imv_portrait2);
                    holder.imgManagerItemUserHead.getDrawable();
                } else {
                    holder.imgManagerItemUserHead.setImageResource(R.drawable.manregactivity_imv_portrait1);
                    holder.imgManagerItemUserHead.getDrawable();
                }
            }
        }
        cur.close();
    }



    /**
     * 从本地数据库中读取相应的数据,初始化条目
     * @param holder
     * @param mDataBase
     * @param position
     */
    public void initItems( ViewHolder holder,DataBase mDataBase,int position){
        cur = mDataBase.Selector(position,TABLENAME);
        while (cur.moveToNext()) {
            holder.txtManagertmeLastTouchTime.setText(cur.getString(cur.getColumnIndex("time")));
            holder.txtManagerItemLastLocation.setText(cur.getString(cur.getColumnIndex("position")));
            //如果用户已经修改了备注名,就显示备注名,否则显示设备名
            if(cur.getString(cur.getColumnIndex("subname")).equals("null")){
                holder.txtManagertgetDeviceName.setText(cur.getString(cur.getColumnIndex("name")));
            }
            else {
                holder.txtManagertgetDeviceName.setText(cur.getString(cur.getColumnIndex("subname")));
            }
        }
        cur.close();
    }

    /**
     * 得到位置信息的数据,并存储,启动positionactivity
     */
    protected void sendposition(int pos) {
        Intent in = new Intent(mContext, PositionActivity.class);
        mContext.startActivity(in);
        this.mdata=new DefinedShared(mContext);
        mdata.putString("pos","pos",pos+"");
        mdata.putString("isfirstconnect","isfirstclick","1");

    }

    public BaseThread getLocationThread(){
         return(this.mLocationGetThread);

    }



}
