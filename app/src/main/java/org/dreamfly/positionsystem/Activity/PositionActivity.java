package org.dreamfly.positionsystem.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import org.dreamfly.positionsystem.R;

/**
 * Created by zhengyl on 15-1-13.
 * 定位界面Activity类
 */
public class PositionActivity extends ActionBarActivity {
    private TextView txtPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.position_layout);
        this.initial();

    }

    private void initial(){
        this.bindID();
        this.positionInfo(this.txtPosition);

    }

    private void positionInfo(TextView txt){
        SharedPreferences sp=this.getSharedPreferences("position",0);
        String location=sp.getString("location","");

        txt.setText("当前定位(经纬度)是: "+location+"");
    }
    private void bindID(){
        txtPosition=(TextView)this.findViewById(R.id.txt_position);
    }
}
