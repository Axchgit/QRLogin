package cn.xct.qrLogin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cn.xct.qrLogin.http.ApiUtil;

public class LoginOutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
        Log.i("Api",  preferences.getString("workNum", ""));

        SharedPreferences.Editor editor = getSharedPreferences("token", MODE_PRIVATE).edit();
        Log.i("Api", "onSuccess: 失败4");

//        ApiUtil.WORK_NUM = preferences.getString("workNum", "");
//                editor.putBoolean("isPermit", false);
//                editor.apply();
        editor.clear();
        editor.commit();
//        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
        Log.i("Api",  preferences.getString("workNum", "5"));
        Log.i("Api",  preferences.getString("isPermit", "5"));

        Intent intent = new Intent(LoginOutActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
