package cn.xct.qrLogin.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;

import java.util.HashMap;

import cn.xct.qrLogin.R;
import cn.xct.qrLogin.entity.UserEntity;
import cn.xct.qrLogin.http.Api;
import cn.xct.qrLogin.http.ApiListener;
import cn.xct.qrLogin.http.ApiUtil;
import cn.xct.qrLogin.http.UniteApi;
import cn.xct.qrLogin.util.StatusBarUtil;
import cn.xct.qrLogin.util.StrUtils;


public class LoginActivity extends AppCompatActivity {

    private EditText loginAccount;
    private EditText loginPassword;
    private Boolean accountFlag = false;
    private Boolean passwordFlag = false;
    private Button loginGoBtn;

//    private LinearLayout loginQQBtn;
//    private LinearLayout loginWeChatBtn;
//    private LinearLayout loginLinkedInBtn;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorBack);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        init();
        initListener();
    }


    private void init() {
        loginAccount = findViewById(R.id.loginAccount);
        loginPassword = findViewById(R.id.loginPassword);
        loginGoBtn = findViewById(R.id.loginGoBtn);
        loginGoBtn.setEnabled(false);
//        loginQQBtn = findViewById(R.id.loginQQBtn);
//        loginWeChatBtn = findViewById(R.id.loginWeChatBtn);
//        loginLinkedInBtn = findViewById(R.id.loginLinkedInBtn);
    }

    private void initListener() {

        loginGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = DialogUIUtils.showLoading(LoginActivity.this,
                        "验证中...", false, false,
                        false, true)
                        .show();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("work_num", loginAccount.getText().toString());
                hashMap.put("password", loginPassword.getText().toString());
                hashMap.put("is_phone", "true");

//                DialogUIUtils.showToastCenter(loginAccount.getText().toString());
//                Log.i("getTest", loginAccount.getText().toString());

//                DialogUIUtils.showToastCenter(ApiUtil.LOGIN);
//                DialogUIUtils.showToastCenter(String.valueOf(hashMap));
//                return false;
                //ApiUtil存放url及用户数据
                new UniteApi(ApiUtil.LOGIN, hashMap).post(new ApiListener() {
                    @Override
                    public void success(Api api) {
                        dialog.dismiss();
                        UniteApi uniteApi = (UniteApi) api;
                        Gson gson = new Gson();
                        UserEntity user = gson.fromJson(uniteApi.getJsonData().toString(), UserEntity.class);

                        if (!StrUtils.isBlank(user.getUserToken())) {
                            // 保存在设置中
                            ApiUtil.WORK_NUM = String.valueOf(user.getWorkNum());
                            ApiUtil.USER_TOKEN = user.getUserToken();
                            ApiUtil.USER_AVATAR = user.getUserAvatar();
                            ApiUtil.USER_NAME = user.getUserName();

                            // 保存在系统文件中
                            SharedPreferences.Editor editor = getSharedPreferences("token", MODE_PRIVATE).edit();
                            editor.putString("workNum", String.valueOf(user.getWorkNum()));
                            editor.putString("userToken", user.getUserToken());
                            editor.putString("userAvatar", user.getUserAvatar());
                            editor.putString("userName", user.getUserName());
                            editor.putBoolean("isPermit", true);
                            editor.apply();
                            // 登录成功进行跳转
                            Intent intent = new Intent(LoginActivity.this, ScanActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            DialogUIUtils.showToastCenter("账号或密码错误");
                        }
                    }

                    @Override
                    public void failure(Api api) {
                        dialog.dismiss();
                        UniteApi uniteApi = (UniteApi) api;

//                        Gson gson = new Gson();
//                        UserEntity user = gson.fromJson(uniteApi.getJsonData().toString(), UserEntity.class);
                        DialogUIUtils.showToastCenter("网络访问出错");
//                        DialogUIUtils.showToastCenter(String.valueOf(user));

                    }
                });
            }
        });

        loginAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                accountFlag = s.length() > 0;
                if (accountFlag && passwordFlag) {
                    loginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));
                    loginGoBtn.setEnabled(true);
                } else {
                    loginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_pale_circle));
                    loginGoBtn.setEnabled(false);
                }
            }
        });

        loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordFlag = s.length() > 0;
                if (accountFlag && passwordFlag) {
                    loginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));
                    loginGoBtn.setTextColor(getResources().getColor(R.color.colorLight));
                    loginGoBtn.setEnabled(true);
                } else {
                    loginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_pale_circle));
                    loginGoBtn.setTextColor(getResources().getColor(R.color.colorLight));
                    loginGoBtn.setEnabled(false);
                }
            }
        });


//        loginQQBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//        loginWeChatBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//        loginLinkedInBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
    }
}
