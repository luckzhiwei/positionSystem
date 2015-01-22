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
    //private DataBase mdataBase=new DataBase(mContext);
    public RegulatorAdapter(List<User>mRegulatorList,Context context,DataBase mDataBase) {
        super(mRegulatorList,context,mDataBase);
    }



}
