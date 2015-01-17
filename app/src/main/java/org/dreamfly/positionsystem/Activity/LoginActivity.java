package org.dreamfly.positionsystem.Activity;

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

import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Utils.FileUitls;
import org.dreamfly.positionsystem.Utils.UserInfoUtils;
import org.dreamfly.positionsystem.bean.Manager;

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

    private UserInfoUtils mUserInfoUtils;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.login_layout);
        this.initila();
        this.mUserInfoUtils=new UserInfoUtils();

    }

    /**
     * @deprecated 初始化函数
     * 对组件进行视图的ID绑定
     */
    private void initila() {
        this.txtLoginactivityRegister = (TextView)
                findViewById(R.id.txt_loginactivity_register);
        this.btnLoginactivityLogin = (Button)
                findViewById(R.id.btn_loginactivity_login);
        this.edittextLoginactivityUsername = (EditText)
                findViewById(R.id.edtext_loginactivity_username);
        this.editextLoginactivityPassword = (EditText)
                findViewById(R.id.edtext_loginactivity_password);
        this.bindListener();
        Log.i("lzw","test");

    }

    /**
     * @deprecated 注册组件的监听器
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
        this.btnLoginactivityLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent registerIntent = new Intent();

                registerIntent.setClass(getApplicationContext(), ManagerActivity.class

                );
                startActivity(registerIntent);


            }
        });
    }

}

