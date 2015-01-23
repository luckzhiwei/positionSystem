package org.dreamfly.positionsystem.Activity;

import android.location.Location;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Utils.CurrentInformationUtils;
import org.dreamfly.positionsystem.Utils.FileUitls;
import org.dreamfly.positionsystem.Utils.UserInfoUtils;


import android.app.Activity;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengyl on 15-1-13.
 * 登陆界面Activity类
 */
public class LoginActivity extends Activity {

    private TextView txtLoginactivityRegister;
    private Button btnLoginactivityLogin;
    private EditText edittextLoginactivityUsername;
    private EditText editextLoginactivityPassword;
    private DefineDialog mIsManagerDialog;
    private CurrentInformationUtils mInformation = new CurrentInformationUtils(this);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.login_layout);
        this.initial();
        Location mLocation=mInformation.getUserLocation(this);
        if(mLocation!=null){Log.v("textlocation",""+mLocation.getLatitude());}
    }

    /**
     * @deprecated 初始化函数
     * 对组件进行视图的ID绑定
     */
    private void initial() {
        this.txtLoginactivityRegister = (TextView)
                findViewById(R.id.txt_loginactivity_register);
        this.btnLoginactivityLogin = (Button)
                findViewById(R.id.btn_loginactivity_login);
        this.edittextLoginactivityUsername = (EditText)
                findViewById(R.id.edtext_loginactivity_username);
        this.editextLoginactivityPassword = (EditText)
                findViewById(R.id.edtext_loginactivity_password);
        this.bindListener();


    }

    /**
     * 注册组件的监听器
     */
    private void bindListener() {

        //跳入注册界面的事件监听
        this.txtLoginactivityRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent registerIntent = new Intent();
                registerIntent.setClass(getApplicationContext(), RegistActivity.class
                );
                startActivity(registerIntent);


            }
        });
        this.btnLoginactivityLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showIsManagerDialog();
            }
        });
    }

    /**
     * 提示未登录用户的在登录成功的情况下选择管理者和被管理者对话框
     */
    private void showIsManagerDialog() {
        this.mIsManagerDialog = new DefineDialog(LoginActivity.this).buiider(false).
                setTitle("是否成为管理者").setPosBtnTxt("是").setNegBtnTxt("否")
                .setNegBtnClickListenr(regulatorClickListener).setPosBtnClickListener(managerClickListener)
                .setDefineDialogCanceable(false).show();
    }

    /**
     * 成为管理者的话,写入本地文件是管理这身份
     */
    private View.OnClickListener managerClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            writeUserInfo("manager",mInformation);
            mIsManagerDialog.dismiss();
            Intent in = new Intent(LoginActivity.this, ManagerActivity.class);
            startActivity(in);
            finish();
        }
    };
    /**
     * 成为被管理者的话,写入本地文件是被管理者身份
     */
    private View.OnClickListener regulatorClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            writeUserInfo("unmanager",mInformation);
            mIsManagerDialog.dismiss();
            Intent in = new Intent(LoginActivity.this, RegulatorActivity.class);
            startActivity(in);
            finish();
        }
    };

    /**
     * 这是在登录界面中，按钮所确定的本地信息的缓存,
     * 如果选择是的话,则写入被身份是管理者
     * 如果选择否的话，则写入身份是管理者
     *
     * @param isManager
     * @param mInformation 得到的本机信息
     */
    private void writeUserInfo(String isManager,CurrentInformationUtils mInformation) {
        UserInfoUtils mUserInfoUitls = new UserInfoUtils(LoginActivity.this);
        HashMap<String, String> hashmap = new HashMap<String, String>();
        hashmap.put("loginstate", "login");
        //记录登录状态
        hashmap.put("managerstate", isManager);
        //记录是否是管理者
        hashmap.put("userrID", "2");
        //记录服务器中数据库的主键的数值
        hashmap.put("famliyName", "Tree");
        //记录用户登录的帐号名字
        hashmap.put("devName", mInformation.getCurrentDeviceName());
        //记录本机的设备名字
        mUserInfoUitls.updateUserInfo(hashmap);
    }
}

