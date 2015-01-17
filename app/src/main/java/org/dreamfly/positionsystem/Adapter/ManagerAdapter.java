package org.dreamfly.positionsystem.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import java.util.List;
import org.dreamfly.positionsystem.Activity.ManagerActivity;
import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.bean.Manager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lzw on 2015/1/15.
 */

public class ManagerAdapter extends BaseAdapter {


    private Context mContext;
    private List<Manager> mMangerList;//适配器中应该含有的容器,
    private DataBase db;
    private Cursor cur;
    private Manager mManager;

    public ManagerAdapter(List<Manager> mMangerList, Context context, DataBase db) {
        this.mMangerList = mMangerList;
        this.mContext=context;
        this.db=db;
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
        this.mManager=this.mMangerList.get(position);
        if(contentview==null)
        {
             contentview=LayoutInflater.from(this.mContext).inflate(R.layout.manager_items,null);
             holder=new ViewHolder();
             this.bindID(contentview,holder);
             this.setItemInfo(this.mManager,holder);
             this.setClickListener(holder,position);
             contentview.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)contentview.getTag();
            this.setItemInfo(this.mManager,holder);
            this.setClickListener(holder,position);
        }
        return contentview;
    }


    private class ViewHolder{

       TextView  txtManagerItemMarkName;
       TextView  txtManagertmeLastTouchTime;
       TextView  txtManagerItemLastLocation;
       ImageView imgManagerItemUserHead;
       Button    btnManagerItemPhone;
       Button    btnManagerItemPosition;

    }

    /**
     * 绑定视图ID,holder是组件容器
     * @param contentview
     * @param holder
     */
    private void bindID(View contentview,ViewHolder holder)
    {
            holder.imgManagerItemUserHead=(ImageView)contentview.findViewById(R.id.manageractivity_imv1);
            holder.txtManagerItemLastLocation=(TextView)contentview.findViewById(R.id.manageractivity_txt2_location);
            holder.txtManagerItemMarkName=(TextView)contentview.findViewById(R.id.manageractivity_txt2_name);
            holder.txtManagertmeLastTouchTime=(TextView)contentview.findViewById(R.id.manageractivity_txt3_time);
            holder.btnManagerItemPhone=(Button)contentview.findViewById(R.id.manageractivity_btn_phone);
            holder.btnManagerItemPosition=(Button)contentview.findViewById(R.id.manageractivity_btn_position);
    }

    /**
     * 一个容器的实例填入函数中去
     * @param oneManager
     * @param holder
     */
    private void  setItemInfo(Manager oneManager,ViewHolder holder)
    {
          holder.txtManagerItemMarkName.setText(oneManager.getMangerMarks());
          holder.txtManagertmeLastTouchTime.setText(oneManager.getLastDateTouch());
          holder.txtManagerItemLastLocation.setText(oneManager.getLastLocation());
    }


    private void setClickListener(ViewHolder holder,int pos)
    {
           final Manager oneManger=this.mMangerList.get(pos);
           holder.btnManagerItemPosition.setOnClickListener(new View.OnClickListener() {
               public void onClick(View view) {

               }
           });
    }

}
