package org.dreamfly.positionsystem.Adapter;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;


import org.dreamfly.positionsystem.Activity.ManagerActivity;
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
    private CurrentInformationUtils mInformation = new CurrentInformationUtils();

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

    public View getView(int position, View contentview, ViewGroup arg2) {//加载XML视图文件

        ViewHolder holder;
        this.mManager = this.mMangerList.get(position);
        if (contentview == null) {
            contentview = LayoutInflater.from(this.mContext).inflate(R.layout.manager_items, null);
            holder = new ViewHolder();
            this.bindID(contentview, holder);
            this.setItemInfo(this.mManager, holder, position, mDataBase);
            this.setClickListener(holder, position, mDataBase);
            contentview.setTag(holder);
        } else {
            holder = (ViewHolder) contentview.getTag();
            this.setItemInfo(this.mManager, holder, position, mDataBase);
            this.setClickListener(holder, position, mDataBase);
        }
        return contentview;
    }


    private class ViewHolder {

        TextView txtManagerItemMarkName;
        TextView txtManagertmeLastTouchTime;
        TextView txtManagerItemLastLocation;
        TextView txtManagertgetDeviceName;
        ImageView imgManagerItemUserHead;
        Button btnManagerItemPhone;
        Button btnManagerItemPosition;

    }

    public class PositiveButtonListener implements View.OnClickListener {

        int pos;
        Manager oneManager;
        DataBase mDataBase;

        public PositiveButtonListener(int pos, final Manager oneManager, DataBase mDataBase) {
            this.pos = pos;
            this.oneManager = oneManager;
            this.mDataBase = mDataBase;
        }

        public void onClick(View view) {
            final String s[] = {"南京路234号", "上海路278号", "北京路123号", "河北路456号",
                    "南山路88号", "合肥路87号", "河南路768号"};
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
     *
     * @param oneManager
     * @param holder
     */
    private void setItemInfo(Manager oneManager, ViewHolder holder, int position, DataBase mDataBase) {

        cur = mDataBase.Selector(position);
        while (cur.moveToNext()) {
            holder.txtManagertmeLastTouchTime.setText(cur.getString(cur.getColumnIndex("time")));
            holder.txtManagertgetDeviceName.setText(cur.getString(cur.getColumnIndex("name")));
            holder.txtManagerItemLastLocation.setText(cur.getString(cur.getColumnIndex("position")));
        }
        if ((position % 2) == 0) {
            holder.imgManagerItemUserHead.setImageResource(R.drawable.manregactivity_imv_portrait2);
            holder.imgManagerItemUserHead.getDrawable();
        } else {
            holder.imgManagerItemUserHead.setImageResource(R.drawable.manregactivity_imv_portrait1);
            holder.imgManagerItemUserHead.getDrawable();
        }
        cur.close();
    }


    private void setClickListener(final ViewHolder holder, final int pos, final DataBase mDataBase) {
        final Manager oneManger = this.mMangerList.get(pos);

        holder.btnManagerItemPosition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setListDialog(pos, oneManger, mDataBase);
            }
        });
    }

    private void setListDialog(int pos, Manager oneManager, DataBase mDataBase) {
        mDefineDialog = new DefineDialog(mContext).buiider().setDefineDialogCanceable(true)
                .setTitle("是否获取地理位置").show();
        PositiveButtonListener mPositiveButtonListener =

                new PositiveButtonListener(pos, oneManager, mDataBase);

        mDefineDialog.setPosBtnClickListener(mPositiveButtonListener);

    }


}
