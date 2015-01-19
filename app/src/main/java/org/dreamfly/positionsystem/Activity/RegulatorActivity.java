package org.dreamfly.positionsystem.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;

import org.dreamfly.positionsystem.Adapter.ManagerAdapter;
import org.dreamfly.positionsystem.Custom.DefineListView;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.bean.Manager;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengyl on 15-1-13.
 * 被管理者界面Activity类
 */
public class RegulatorActivity extends ActionBarActivity {

    private DefineListView listViewRegulatorActiivityReglutorList;
    private ManagerAdapter mManagerAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.regulator_layout);
        this.initial();
    }

    private void initial() {
        this.bindID();
        this.setCLickListener();
    }

    private void bindID() {
        this.listViewRegulatorActiivityReglutorList = (DefineListView) this.findViewById(R.id.listivew_regulatoractivity_regulatorlist);

    }

    private void setCLickListener() {
        this.listViewRegulatorActiivityReglutorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private List<Manager> getData() {
        List<Manager> list = new ArrayList<Manager>();
        for (int i = 0; i < 7; i++) {
            Manager m = new Manager();
            m.setDeviceNma("HTC " + i);
            m.setLastDateTouch("1-19");
            m.setMangerMarks("mother" + i);
            m.setLastLocation("usetc" + i);
            list.add(m);
        }
        return (list);
    }

}
