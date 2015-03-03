package org.dreamfly.positionsystem.Activity;

import android.content.Intent;
import android.os.Bundle;

import org.dreamfly.positionsystem.CommonParameter.ComParameter;
import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Thread.BaseThread;
import org.dreamfly.positionsystem.Thread.RegisterRequestThread;
import org.dreamfly.positionsystem.Utils.ToastUtils;
import org.dreamfly.positionsystem.Utils.UserInfoUtils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengyl on 15-1-13.
 * 注册界面Activity类
 */
public class RegistActivity extends Activity {

    private EditText editRegisterActivityUsername;
    private EditText editRegisterActivityPassword;
    private Button btnRegisterActivityRegister;
    private ProgressBar proRegistActivity;

    private DefineDialog mDefineDialog;

    private BaseThread requestRegisterThread;
    private UserInfoUtils userInfoUtils;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.regsiter_layout);
        this.initial();

    }

    protected void onResume() {
        super.onResume();
        proRegistActivity.setVisibility(View.GONE);
    }

    /**
     * 初始化绑定组件ID
     */
    private void initial() {

        this.bindID();
        this.bindListener();
        proRegistActivity.setVisibility(View.GONE);
        this.userInfoUtils=new UserInfoUtils(this);
    }

    private void bindID() {
        this.editRegisterActivityPassword =
                (EditText) this.findViewById(R.id.editext_registeractivity_password);
        this.editRegisterActivityUsername =
                (EditText) this.findViewById(R.id.eidtext_registeractivity_username);
        this.btnRegisterActivityRegister =
                (Button) this.findViewById(R.id.btn_registeractivity_register);
        this.proRegistActivity =
                (ProgressBar) this.findViewById(R.id.progressBar_registactivity);
    }

    private void bindListener() {
        this.btnRegisterActivityRegister.setOnClickListener(registerBtnListener);
    }

    private View.OnClickListener registerBtnListener = new View.OnClickListener() {
        public void onClick(View view) {
            requestRegisterThread = new RegisterRequestThread
                    (mHandler, "registerstate");
            if (checkoutDataFormat()) {
                String requestURL = ComParameter.HOST + "user_register.action";
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", editRegisterActivityUsername.getText().toString());
                params.put("password", editRegisterActivityPassword.getText().toString());
                requestRegisterThread.setRequestPrepare(requestURL, params);
                requestRegisterThread.start();
                ToastUtils.showToast(getApplication(), "请求服务器中...");
                proRegistActivity.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 关于子线程的交互的handler
     */

    private boolean checkoutDataFormat() {
        if (this.editRegisterActivityUsername.getText().toString().equals("")) {
            ToastUtils.showToast(getApplicationContext(), "帐号不能为空");
            return (false);
        } else {
            if (this.editRegisterActivityPassword.getText().toString().equals("")) {
                ToastUtils.showToast(getApplicationContext(), "密码不能空");
                return (false);
            }
        }
        return (true);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.getData().getInt("registerstate") == ComParameter.STATE_RIGHT) {
                Map<String, String> resultMap = requestRegisterThread.getResultMap();
                String registerState = resultMap.get("registerstate");
                if (registerState != null) {
                    if (registerState.equals("success")) {
                        ToastUtils.showToast(getApplicationContext(), "注册成功!");
                        editRegisterActivityPassword.setText("");
                        editRegisterActivityUsername.setText("");

                        Intent in = new Intent().setClass(RegistActivity.this, LoginActivity.class);
                        startActivity(in);
                    } else if (registerState.equals("fail")) {
                        ToastUtils.showToast(getApplicationContext(), "注册失败:" +
                                resultMap.get("failReason") + "");
                        editRegisterActivityPassword.setText("");
                        editRegisterActivityUsername.setText("");

                    }
                }


            } else if (msg.getData().getInt("registerstate") == ComParameter.STATE_ERROR) {
                ToastUtils.showToast(getApplicationContext(), ComParameter.ERRORINFO);
            }
            proRegistActivity.setVisibility(View.GONE);
        }
    };
}
