package org.dreamfly.positionsystem.Activity;

import android.os.Bundle;

import org.dreamfly.positionsystem.Custom.DefineDialog;
import org.dreamfly.positionsystem.R;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.widget.Toast;

/**
 * Created by zhengyl on 15-1-13.
 * 注册界面Activity类
 */
public class RegistActivity extends Activity {

    private EditText editRegisterActivityUsername;
    private EditText editRegisterActivityPassword;
    private Button btnRegisterActivityRegister;

    private DefineDialog mDefineDialog;

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

    }
}
