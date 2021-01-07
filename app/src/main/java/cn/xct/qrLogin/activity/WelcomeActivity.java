package cn.xct.qrLogin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dou361.dialogui.DialogUIUtils;

import cn.xct.qrLogin.R;
import cn.xct.qrLogin.http.ApiUtil;
import cn.xct.qrLogin.http.OkHttpUtil;

public class WelcomeActivity extends AppCompatActivity {
//    private TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //初始化通讯
        OkHttpUtil.init();
        //初始化弹框
        DialogUIUtils.init(this);

        init();
        initListener();
    }

    private void init() {
//        appName = findViewById(R.id.appName);
//        appName.setTypeface(appName.getTypeface(), Typeface.ITALIC);
    }

    private void initListener() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
                        ApiUtil.WORK_NUM = preferences.getString("workNum", "");
                        ApiUtil.USER_TOKEN = preferences.getString("userToken", "");
                        ApiUtil.USER_AVATAR = preferences.getString("userAvatar", "");
                        ApiUtil.USER_NAME = preferences.getString("userName", "未知用户");
                        boolean isPermit = preferences.getBoolean("isPermit", false);
                        if (isPermit) {
                            startActivity(new Intent(
                                    WelcomeActivity.this, ScanActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(
//                                    WelcomeActivity.this, LoginActivity.class));
                            WelcomeActivity.this, LoginActivity.class));

                            finish();
                        }
                    }
                });
            }
        }).start();
    }

}
