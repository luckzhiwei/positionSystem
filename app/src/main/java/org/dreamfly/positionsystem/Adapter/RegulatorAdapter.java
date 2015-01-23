package org.dreamfly.positionsystem.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Utils.CurrentInformationUtils;
import org.dreamfly.positionsystem.bean.User;
import java.util.List;

/**
 * Created by zhengyl on 15-1-22.
 */
public class RegulatorAdapter extends ManagerAdapter {

    private CurrentInformationUtils mInformation = new CurrentInformationUtils(mContext);
    private Cursor cur;
    private DefineDialog mDefineDialog = null;
    private static  final String MANTABLENAME="manageritems";

    /**
     * o构造方法继承自父类
     * @param mRegulatorList
     * @param context
     * @param mDataBase
     */
    public RegulatorAdapter(List<User>mRegulatorList,Context context,DataBase mDataBase) {
        super(mRegulatorList,context,mDataBase);
    }

    @Override
    public int getCount(){return (this.mRegulatorList.size());}
    @Override
    public Object getItem(int position) {return (this.mRegulatorList.get(position));}
    @Override
    public long getItemId(int position) {
        return (position);
    }
    @Override
    public View getView(int position,View contentview,ViewGroup arg2){
        ViewHolder holder;
        this.regulator=mRegulatorList.get(position);
        if(contentview==null){
            contentview = LayoutInflater.from(this.mContext).inflate(R.layout.regulator_items, null);
            holder=new ViewHolder();
            this.bindID(contentview, holder);
            this.setManItemInfo(holder, position, mDataBase);
            this.setClickListener(holder, position, mDataBase);
            contentview.setTag(holder);
        }
        else {
            holder = (ViewHolder) contentview.getTag();
            this.setManItemInfo(holder, position, mDataBase);
            this.setClickListener(holder, position, mDataBase);
        }
        return contentview;
    }

    private class ViewHolder extends ManagerAdapter.ViewHolder{

        TextView txtRegulatorItemMarkName;
        TextView txtRegulatorLastTouchTime;
        TextView txtRegulatorItemLastLocation;
        TextView txtRegulatorgetDeviceName;
        Button btnRegulatorItemPhone;
        Button btnRegulatorItemposition;
        ImageView imvRegulatorItemUserHead;
    }

    private void bindID(View contentview, ViewHolder holder){

        holder.txtRegulatorgetDeviceName=
                (TextView)contentview.findViewById(R.id.regulatoractivity_txt2_name);
        holder.txtRegulatorItemMarkName=
                (TextView)contentview.findViewById(R.id.regulatoractivity_txt2_name);
        holder.txtRegulatorItemLastLocation=
                (TextView)contentview.findViewById(R.id.regulatoractivity_txt2_location);
        holder.txtRegulatorLastTouchTime=
                (TextView)contentview.findViewById(R.id.regulatoractivity_txt3_time);
        holder.btnRegulatorItemPhone=
                (Button)contentview.findViewById(R.id.regulatoractivity_btn_phone);
        holder.btnRegulatorItemposition=
                (Button)contentview.findViewById(R.id.regulatoractivity_btn_position);
        holder.imvRegulatorItemUserHead=
                (ImageView)contentview.findViewById(R.id.regulatoractivity_imv1);

    }

    /**
     * 自定义对话框确定监听类继承自manageradapter的监听类
     */
    public class RegPositiveButtonListener extends PositiveButtonListener{
        public RegPositiveButtonListener(int pos, final User oneRegulator, DataBase mDataBase,
                                         DefineDialog mDefineDialog){
            super(pos,oneRegulator,mDataBase,mDefineDialog);
        }
        @Override
        public void onClick(View view){
            oneRegulator.setLastLocation("上次的位置:" + s[pos]);
            mDataBase.items_changeValue(MANTABLENAME,"position", oneRegulator.getLastLocation(), pos);
            oneRegulator.setLastDateTouch(mInformation.getCurrentTime());
            mDataBase.items_changeValue(MANTABLENAME,"time", oneRegulator.getLastDateTouch(), pos);
            mDefineDialog.dismiss();
        }
    }

    private void setManItemInfo(ViewHolder holder, int position, DataBase mDataBase){
        this.initManItems(holder,mDataBase,position);
        this.changeManPortrait(holder,position,mDataBase);
    }

    /**
     * 从本地数据表manageritems中读取相应的数据,初始化条目
     * @param holder
     * @param mDataBase
     * @param position
     */
    public void initManItems( ViewHolder holder,DataBase mDataBase,int position){
        cur = mDataBase.Selector(position,MANTABLENAME);
        while (cur.moveToNext()) {
            holder.txtRegulatorLastTouchTime.setText(cur.getString(cur.getColumnIndex("time")));
            holder.txtRegulatorItemLastLocation.setText(cur.getString(cur.getColumnIndex("position")));
            //如果用户已经修改了备注名,就显示备注名,否则显示设备名
            if(cur.getString(cur.getColumnIndex("subname")).equals("null")){
                holder.txtRegulatorgetDeviceName.setText(cur.getString(cur.getColumnIndex("name")));
            }
            else {
                holder.txtRegulatorgetDeviceName.setText(cur.getString(cur.getColumnIndex("subname")));
            }
        }
        cur.close();
    }

    /**
     * 该方法用于实现头像交替变换,如果网络未连接,显示灰色头像
     * @param holder
     * @param position
     */
    public void changeManPortrait(ViewHolder holder,int position,DataBase mDataBase) {
        cur=mDataBase.Selector(position,MANTABLENAME);
        while (cur.moveToNext()) {
            String connection = cur.getString(cur.getColumnIndex("isconnect"));
            if (connection.equals("false")) {
                if ((position % 2) == 0) {
                    holder.imvRegulatorItemUserHead.setImageResource(R.drawable.manager_protrait2_unconected);
                    holder.imvRegulatorItemUserHead.getDrawable();
                } else {
                    holder.imvRegulatorItemUserHead.setImageResource(R.drawable.manager_protrait1_unconected);
                    holder.imvRegulatorItemUserHead.getDrawable();
                }
            } else {
                if ((position % 2) == 0) {
                    holder.imvRegulatorItemUserHead.setImageResource(R.drawable.manregactivity_imv_portrait2);
                    holder.imvRegulatorItemUserHead.getDrawable();
                } else {
                    holder.imvRegulatorItemUserHead.setImageResource(R.drawable.manregactivity_imv_portrait1);
                    holder.imvRegulatorItemUserHead.getDrawable();
                }
            }
        }
        cur.close();
    }

    /**
     * 为地理位置按钮绑定监听事件
     * @param holder
     * @param pos
     * @param mDataBase
     */
    private void setClickListener(final ViewHolder holder, final int pos, final DataBase mDataBase) {
        final User oneRegulator = this.mRegulatorList.get(pos);

        holder.btnRegulatorItemposition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setRegListDialog(pos, oneRegulator, mDataBase);
            }
        });
    }
    /**
     * 为确定按钮绑定监听事件
     * @param pos
     * @param oneRegulator
     * @param mDataBase
     */
    private void setRegListDialog(int pos, User oneRegulator, DataBase mDataBase) {
        mDefineDialog = new DefineDialog(mContext).buiider(false).setDefineDialogCanceable(true)
                .setTitle("是否获取地理位置").show();
        RegPositiveButtonListener mPositiveButtonListener =

                new RegPositiveButtonListener(pos, oneRegulator, mDataBase,mDefineDialog);

        mDefineDialog.setPosBtnClickListener(mPositiveButtonListener);

    }



}
