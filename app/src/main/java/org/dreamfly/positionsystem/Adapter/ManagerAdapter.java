package org.dreamfly.positionsystem.Adapter;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Utils.CurrentInformationUtils;
import org.dreamfly.positionsystem.bean.Manager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by lzw on 2015/1/15.
 */

public class ManagerAdapter extends BaseAdapter {

    private Context mContext;
    private List<Manager> mMangerList;//适配器中应该含有的容器,
    private DataBase mDataBase;
    private Cursor cur;
    private Manager mManager;
    private DefineDialog mDefineDialog = null;
    private CurrentInformationUtils mInformation = new CurrentInformationUtils(mContext);

    public ManagerAdapter(List<Manager> mMangerList, Context context, DataBase mDataBase) {
        this.mMangerList = mMangerList;
        this.mContext = context;
        this.mDataBase = mDataBase;
    }

    public int getCount() {
        return (this.mMangerList.size());
    }

    public Object getItem(int position) {
        return (this.mMangerList.get(position));
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
        this.mManager = this.mMangerList.get(position);
        if (contentview == null) {
            contentview = LayoutInflater.from(this.mContext).inflate(R.layout.manager_items, null);
            holder = new ViewHolder();
            this.bindID(contentview, holder);
            this.setItemInfo(holder, position, mDataBase);
            this.setClickListener(holder, position, mDataBase);
            contentview.setTag(holder);
        } else {
            holder = (ViewHolder) contentview.getTag();
            this.setItemInfo( holder, position, mDataBase);
            this.setClickListener(holder, position, mDataBase);
        }
        return contentview;
    }

    /**
     * 设置一个容器用于存放控件
     */
    private class ViewHolder {

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

        private int pos;
        private Manager oneManager;
        private DataBase mDataBase;
        final String s[] = {"南京路234号", "上海路278号", "北京路123号", "河北路456号",
                "南山路88号", "合肥路87号", "河南路768号"};

        public PositiveButtonListener(int pos, final Manager oneManager, DataBase mDataBase) {
            this.pos = pos;
            this.oneManager = oneManager;
            this.mDataBase = mDataBase;
        }

        public void onClick(View view) {

            oneManager.setLastLocation("上次的位置:" + s[pos]);
            mDataBase.items_changeValue("position", oneManager.getLastLocation(), pos);
            oneManager.setLastDateTouch(mInformation.getCurrentTime());
            mDataBase.items_changeValue("time", oneManager.getLastDateTouch(), pos);

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
    private void setItemInfo(ViewHolder holder, int position, DataBase mDataBase) {

        this.initItems(holder,mDataBase,position);
        this.changePortrait(holder,position);

    }

    /**
     * 设置监听事件
     * @param holder
     * @param pos
     * @param mDataBase
     */
    private void setClickListener(final ViewHolder holder, final int pos, final DataBase mDataBase) {
        final Manager oneManger = this.mMangerList.get(pos);

        holder.btnManagerItemPosition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setListDialog(pos, oneManger, mDataBase);
            }
        });
    }

    /**
     * 调用自定义dialog,实现弹出对话框
     * @param pos
     * @param oneManager
     * @param mDataBase
     */
    private void setListDialog(int pos, Manager oneManager, DataBase mDataBase) {
        mDefineDialog = new DefineDialog(mContext).buiider().setDefineDialogCanceable(true)
                .setTitle("是否获取地理位置").show();
        PositiveButtonListener mPositiveButtonListener =

                new PositiveButtonListener(pos, oneManager, mDataBase);

        mDefineDialog.setPosBtnClickListener(mPositiveButtonListener);

    }

    /**
     * 该方法用于实现头像交替变换
     * @param holder
     * @param position
     */
    private void changePortrait(ViewHolder holder,int position){

        if ((position % 2) == 0) {
            holder.imgManagerItemUserHead.setImageResource(R.drawable.manregactivity_imv_portrait2);
            holder.imgManagerItemUserHead.getDrawable();
        } else {
            holder.imgManagerItemUserHead.setImageResource(R.drawable.manregactivity_imv_portrait1);
            holder.imgManagerItemUserHead.getDrawable();
        }
    }

    /**
     * 从本地数据库中读取相应的数据,初始化条目
     * @param holder
     * @param mDataBase
     * @param position
     */
    private void initItems( ViewHolder holder,DataBase mDataBase,int position){
        cur = mDataBase.Selector(position);
        while (cur.moveToNext()) {
            holder.txtManagertmeLastTouchTime.setText(cur.getString(cur.getColumnIndex("time")));
            holder.txtManagertgetDeviceName.setText(cur.getString(cur.getColumnIndex("name")));
            holder.txtManagerItemLastLocation.setText(cur.getString(cur.getColumnIndex("position")));
        }
        cur.close();
    }


}
