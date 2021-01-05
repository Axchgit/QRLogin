package cn.xct.qrLogin.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import cn.xct.qrLogin.R;
import cn.xct.qrLogin.util.StatusBarUtil;

public class PhoneLoginActivity extends AppCompatActivity {
    private EditText phone_number;
    private EditText check_code;
    private Button send_code;
    private Button login;

//


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
//        StatusBarUtil.setStatusBarMode(this, true, R.color.colorBack);

//        if (getSupportActionBar() != null)
//            getSupportActionBar().hide();

        init();
//        initListener();
    }


    private void init() {
        phone_number = findViewById(R.id.phone_number);
        check_code = findViewById(R.id.check_code);
        send_code = findViewById(R.id.send_code);
        login = findViewById(R.id.login);
        login.setEnabled(false);
        send_code.setEnabled(false);

        //        loginQQBtn = findViewById(R.id.loginQQBtn);
//        loginWeChatBtn = findViewById(R.id.loginWeChatBtn);
//        loginLinkedInBtn = findViewById(R.id.loginLinkedInBtn);
    }



}
