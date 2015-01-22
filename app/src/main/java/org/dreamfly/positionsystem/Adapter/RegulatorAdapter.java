package org.dreamfly.positionsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.bean.User;

import java.util.List;

/**
 * Created by zhengyl on 15-1-22.
 */
public class RegulatorAdapter extends ManagerAdapter {
    private User manager;
    private List<User> mManagerList;
    private Context mContext;
    private DataBase mdataBase=new DataBase(mContext);
    public RegulatorAdapter(List<User> mManagerList,Context mContext,DataBase mdataBase,
    List<User>mRegulatorList,Context context,DataBase mDataBase) {
        super(mRegulatorList,context,mDataBase);
    }

    @Override
    public View getView(int position, View contentview, ViewGroup arg2) {
        ViewHolder holder;
        this.manager=mManagerList.get(position);
        if (contentview == null) {
            contentview = LayoutInflater.from(this.mContext).inflate(R.layout.regulator_items, null);
            holder = new ViewHolder();
            this.bindID(contentview, holder);
            //this.setItemInfo(holder, position, mdataBase);
            //this.setClickListener(holder, position, mDataBase);
            contentview.setTag(holder);
        } else {
            holder = (ViewHolder) contentview.getTag();
            //this.setItemInfo( holder, position, mDataBase);
            //this.setClickListener(holder, position, mDataBase);
        }
       return contentview;
    }

    private class ViewHolder {

        TextView txtRegulatorItemMarkName;
        TextView txtRegulatortmeLastTouchTime;
        TextView txtRegulatorItemLastLocation;
        TextView txtRegulatortgetDeviceName;
        ImageView imgRegulatorItemUserHead;
        Button btnRegulatorItemPhone;
        Button btnRegulatorItemPosition;

    }

    public void bindID(View contentview,ViewHolder holder){
        holder.imgRegulatorItemUserHead=(ImageView)contentview.findViewById(R.id.regulatoractivity_imv1);
        holder.btnRegulatorItemPosition=(Button)contentview.findViewById(R.id.regulatoractivity_btn_position);
        holder.txtRegulatortgetDeviceName=(TextView)contentview.findViewById(R.id.manageractivity_txt2_name);
        holder.txtRegulatorItemMarkName=(TextView)contentview.findViewById(R.id.manageractivity_txt2_name);
        holder.txtRegulatortmeLastTouchTime=(TextView)contentview.findViewById(R.id.regulatoractivity_txt3_time);
     }
}
