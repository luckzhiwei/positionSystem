package org.dreamfly.positionsystem.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Utils.CurrentInformationUtils;
import org.dreamfly.positionsystem.bean.Manager;
import org.dreamfly.positionsystem.bean.Regulator;


import java.util.List;

/**
 * Created by zhengyl on 15-1-22.
 */
public class RegulatorAdapter extends BaseAdapter {

    private Context mcontext;
    private List<Regulator> mRegulatorList;
    private DataBase mDataBase;
    private Regulator mRegulator;
    private Cursor cur;
    private DefineDialog mDefineDialog = null;
    private CurrentInformationUtils mInformation = new CurrentInformationUtils(mcontext);

    public RegulatorAdapter(List<Regulator> mRegulator, Context mcontext, DataBase mDataBase) {
        this.mRegulatorList = mRegulator;
        this.mcontext = mcontext;
        this.mDataBase = mDataBase;
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

    public View getView(int position, View contentview, ViewGroup parent) {
        ViewHolder holder;
        this.mRegulator = this.mRegulatorList.get(position);
        if (contentview == null) {
            contentview = LayoutInflater.from(this.mcontext).inflate(R.layout.regulator_items, null);
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

    private class ViewHolder {

        TextView txtRegulatorItemMarkName;
        TextView txtRegulatortmeLastTouchTime;
        TextView txtRegulatorItemLastLocation;
        TextView txtRegulatorgetDeviceName;
        ImageView imgRegulatorItemUserHead;
        Button btnRegulatorItemPhone;
        Button btnRegulatorItemPosition;
    }

    /**
     * 绑定控件ID
     * @param conteneview
     * @param holder
     */
    private void bindID(View conteneview,ViewHolder holder){
        holder.btnRegulatorItemPhone=
                (Button)conteneview.findViewById(R.id.regulatoractivity_btn_phone);
        holder.btnRegulatorItemPosition=
                (Button)conteneview.findViewById(R.id.regulatoractivity_btn_position);
        holder.txtRegulatorgetDeviceName=
                (TextView)conteneview.findViewById(R.id.regulatoractivity_txt2_name);
        holder.txtRegulatorItemLastLocation=
                (TextView)conteneview.findViewById(R.id.regulatoractivity_txt2_location);
        holder.txtRegulatortmeLastTouchTime=
                (TextView)conteneview.findViewById(R.id.regulatoractivity_txt3_time);
        holder.txtRegulatorItemMarkName=
                (TextView)conteneview.findViewById(R.id.manageractivity_txt2_name);
        holder.imgRegulatorItemUserHead=
                (ImageView)conteneview.findViewById(R.id.regulatoractivity_imv1);

    }
    private void setItemInfo(ViewHolder holder, int position, DataBase mDataBase) {

    }
    private void setClickListener(final ViewHolder holder, final int pos, final DataBase mDataBase) {

    }
}