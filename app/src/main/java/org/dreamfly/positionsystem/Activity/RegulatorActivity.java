package org.dreamfly.positionsystem.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;

import org.dreamfly.positionsystem.Adapter.ManagerAdapter;
import org.dreamfly.positionsystem.Adapter.RegulatorAdapter;
import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.Custom.DefineListView;
import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Utils.CurrentInformationUtils;
import org.dreamfly.positionsystem.bean.Manager;
import org.dreamfly.positionsystem.bean.Regulator;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengyl on 15-1-13.
 * 被管理者界面Activity类
 */
public class RegulatorActivity extends ActionBarActivity {

    private DefineListView listViewRegulatorActivityReglutorList;
    private TextView txtRegulatorActivityTitle;
    private RegulatorAdapter mRegulatordapter;
    private DefineDialog mDefineDialog;
    private CurrentInformationUtils mInformation = new CurrentInformationUtils(this);
    private Regulator oneRegulator=new Regulator();
    private DataBase mDataBase=new DataBase(this);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.regulator_layout);
        this.initial(mDataBase);
    }

    private void initial(DataBase mDataBase) {
        this.bindID();
        this.mRegulatordapter=new RegulatorAdapter(this.getData(), this, mDataBase);
        this.listViewRegulatorActivityReglutorList.setAdapter(mRegulatordapter);
        this.setCLickListener();
    }

    private void bindID() {
        this.listViewRegulatorActivityReglutorList = (DefineListView)
                this.findViewById(R.id.listivew_regulatoractivity_regulatorlist);
        this.txtRegulatorActivityTitle=(TextView)
                this.findViewById(R.id.txt_regulatoractivity_title);

    }

    private void setCLickListener() {
        this.listViewRegulatorActivityReglutorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });
    }

    private List<Regulator> getData() {
        List<Regulator> list = new ArrayList<Regulator>();
        for (int i = 0; i < 7; i++) {
            Regulator r = new Regulator();
            r.setDeviceNma("HTC " + i);
            r.setLastDateTouch("1-19");
            r.setMangerMarks("mother" + i);
            r.setLastLocation("usetc" + i);
            list.add(r);
        }
        return (list);
    }

}
