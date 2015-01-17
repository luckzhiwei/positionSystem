package org.dreamfly.positionsystem.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
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

/**
 * Created by asus on 2015/1/15.
 */

public class ManagerAdapter extends BaseAdapter {
    private ManagerActivity mManagerActivity;
    private Context context;
    private List<Manager> mMangerList;//适配器中应该含有的容器,
    private DataBase db;
    private Cursor cur;
    private Manager mManager;

    public ManagerAdapter(List<Manager> mMangerList, Context context, DataBase db) {
        this.mMangerList = mMangerList;
        this.context = context;
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

    public View getView(int position, View arg1, ViewGroup arg2) {//加载XML视图文件
        View view = LayoutInflater.from(context).inflate(R.layout.manager_items, null);
        TextView managerActivityTxt2Name,managerActivityTxt2Position,managerActivityTxt3Time;
        Button managerActivityBtnPhone,managerActivityBtnPosition;
        ImageView managerImv1;
        managerActivityTxt2Name=(TextView)
                view.findViewById(R.id.manageractivity_txt2_name);
        managerActivityTxt2Position=(TextView)
                view.findViewById(R.id.manageractivity_txt2_location);
        managerActivityTxt3Time=(TextView)
                view.findViewById(R.id.manageractivity_txt3_time);
        managerActivityBtnPhone=(Button)
                view.findViewById(R.id.manageractivity_btn_phone);
        managerActivityBtnPosition=(Button)
                view.findViewById(R.id.manageractivity_btn_position);
        managerImv1=(ImageView)
                view.findViewById(R.id.manageractivity_imv1);
        DataBaseInfo(managerActivityTxt2Name,managerActivityTxt2Position,
                managerActivityTxt3Time,db);
        ChangeView(managerImv1,managerActivityTxt2Name,managerActivityTxt2Position
        ,managerActivityTxt3Time,managerActivityBtnPhone,managerActivityBtnPosition,
                db,position,cur);
        getPosition(position);

        return view;
    }

    private class ViewHolder{

    }

    /**
     * 用户操作引起的视图和数据库的变化
     * @param imv 头像
     * @param txt1 设备名字
     * @param txt2 上一次定位位置
     * @param txt3 上一次定位时间
     * @param btn1 电话按钮
     * @param btn2 定位按钮
     * @param db 数据库对象
     * @param position  items的编号
     * @param cur  游标
     */
    private void ChangeView(ImageView imv,TextView txt1,  TextView txt2,TextView txt3
                            ,Button btn1,Button btn2,DataBase db, int position,Cursor cur)
    {

        String [] s1=new String[]{"nokia","iphone","htc","mi2","lg","oppo","sumsung"};
        txt1.setText(s1[position]);
        String [] sLocation=new String[]{"南京路256号","南京路257号","南京路258号","南京路259号",
                "上海路1070号","中山路57号","河北路234号"};

        btn2.setOnClickListener(new View.OnClickListener() {
            Manager m;
            String [] sLocation=new String[]{"南京路256号","南京路257号","南京路258号","南京路259号",
                    "上海路1070号","中山路57号","河北路234号"};
            String [] s2=new String[]{"河北路256号","中山路257号","福建路258号","北京路259号",
                    "郑州路1070号","泰山路57号","河北路634号"};
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("是否获取地理位置?");
                builder.setNegativeButton("取消",new CancelDialogListener());
                builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //m.setLastLocation(s2[position]);
                    }
                });
                /*String temp=m.getLastLocation();
                if (temp!=null){
                    txt2.setText(temp);
                }*/
                builder.show();
            }

         });

        if((position%2)==0){
            imv.setImageResource(R.drawable.manregactivity_imv_portrait2);
            imv.getResources().getDrawable(R.drawable.manregactivity_imv_portrait2);
        }

        cur=db.getItems();

    }


    public int getPosition(int position){
        return position;
    }

    /**
     * 本地数据存储操作
     * @param txt1 设备名字
     * @param txt2 上一次定位位置
     * @param txt3 上一次定位时间
     * @param db
     */
    public void DataBaseInfo(TextView txt1,TextView txt2,TextView txt3,DataBase db){
        db.items_save(txt1.getText().toString().trim(),null,
                txt2.getText().toString().trim(),
                txt3.getText().toString().trim());
    }
    class CancelDialogListener implements DialogInterface.OnClickListener{

        public void onClick(DialogInterface dialog, int which) {

        }
    }


}
