package org.dreamfly.positionsystem.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.dreamfly.positionsystem.R;

/**
 * Created by zhengyl on 15-1-13.
 * 登陆界面Activity类
 */
public class LoginActivity extends ActionBarActivity{
    TextView txt_loginactivity_register;
    Button btn_loginactivity_login;
    EditText edtext_loginactivity_username ,edtext_loginactivity_password;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.login_layout);
        txt_loginactivity_register=(TextView)
                findViewById(R.id.txt_loginactivity_register);

        btn_loginactivity_login=(Button)
                findViewById(R.id.btn_loginactivity_login);
        edtext_loginactivity_username=(EditText)
                findViewById(R.id.edtext_loginactivity_username);
        edtext_loginactivity_password=(EditText)
                findViewById(R.id.edtext_loginactivity_password);
    }
}

