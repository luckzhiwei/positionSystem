package org.dreamfly.positionsystem.Activity;

import android.os.Bundle;

import org.dreamfly.positionsystem.CommonParameter.ComParameter;
import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Thread.BaseThread;
import org.dreamfly.positionsystem.Thread.RegisterRequestThread;
import org.dreamfly.positionsystem.Utils.ToastUtils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.widget.Toast;

import java.util.Map;

/**
 * Created by zhengyl on 15-1-13.
 * 注册界面Activity类
 */
public class RegistActivity extends Activity {

    private EditText editRegisterActivityUsername;
    private EditText editRegisterActivityPassword;
    private Button btnRegisterActivityRegister;

    private DefineDialog mDefineDialog;

    private BaseThread requestRegisterThread;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.regsiter_layout);
        this.initial();

    }

    /**
     * 初始化绑定组件ID
     */
    private void initial() {

        this.bindID();
        this.bindListener();
    }

    private void bindID() {
        this.editRegisterActivityPassword =
                (EditText) this.findViewById(R.id.editext_registeractivity_password);
        this.editRegisterActivityUsername =
                (EditText) this.findViewById(R.id.eidtext_registeractivity_username);
        this.btnRegisterActivityRegister =
                (Button) this.findViewById(R.id.btn_registeractivity_register);
    }

    private void bindListener() {
              this.btnRegisterActivityRegister.setOnClickListener(registerBtnListener);
    }

    private  View.OnClickListener registerBtnListener=new View.OnClickListener() {
         public void onClick(View view) {
                    requestRegisterThread=new RegisterRequestThread
                     (mHandler,"registerstate");
                    requestRegisterThread.start();
        }
    };
    private Handler mHandler=new Handler(Looper.getMainLooper()){
        public void handleMessage(Message msg) {
               if(msg.getData().getInt("registerstate")== ComParameter.STATE_RIGHT)
               {
                     Map<String,String> resultMap=requestRegisterThread.getResultMap();
                     String registerState=resultMap.get("egisterstate");
                     if(registerState!=null)
                     {
                        if(registerState.equals("success"))
                        {
                            ToastUtils.showToast(getApplicationContext(),"注册成功");
                        }else if(registerState.equals("fail"))
                        {
                           ToastUtils.showToast(getApplicationContext(),"注册失败:"+
                           resultMap.get("failReason")+"");
                        }
                     }

               }else if(msg.getData().getInt("registerstate")==ComParameter.STATE_ERROR){

               }
        }
    };
}
