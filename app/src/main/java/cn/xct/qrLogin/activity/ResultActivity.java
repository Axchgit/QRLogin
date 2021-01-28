package cn.xct.qrLogin.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dou361.dialogui.DialogUIUtils;

import org.json.JSONException;

import java.util.HashMap;
//import java.util.StrUtils;


import cn.xct.qrLogin.R;
import cn.xct.qrLogin.entity.AuthEntity;
import cn.xct.qrLogin.http.Api;
import cn.xct.qrLogin.http.ApiListener;
import cn.xct.qrLogin.http.ApiUtil;
import cn.xct.qrLogin.http.UniteApi;

import static cn.xct.qrLogin.util.StrUtils.stampToTime;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ResultActivity extends AppCompatActivity {

    private AuthEntity auth;

    private ImageView resultBackBtn;
    private ImageView resultUAvatar;
    private TextView resultUName;
    private TextView resultTime;
    private TextView resultAddress;
    private TextView resultInfo;
    private Button resultConfirmBtn;
    private Button resultCancelBtn;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        auth = (AuthEntity) getIntent().getSerializableExtra("auth");

        init();
        initListener();
    }

    private void init() {
        resultBackBtn = findViewById(R.id.resultBackBtn);
        resultUAvatar = findViewById(R.id.resultUAvatar);
        resultUName = findViewById(R.id.resultUName);
        resultTime = findViewById(R.id.resultTime);
        resultAddress = findViewById(R.id.resultAddress);
        resultInfo = findViewById(R.id.resultInfo);
        resultConfirmBtn = findViewById(R.id.resultConfirmBtn);
        resultCancelBtn = findViewById(R.id.resultCancelBtn);


        Glide.with(this)
                .load(ApiUtil.USER_AVATAR)
                .apply(bitmapTransform(new CircleCrop()))
                .into(resultUAvatar);
        resultUName.setText(ApiUtil.USER_NAME);
        resultTime.setText(stampToTime(auth.getAuthTime().toString()));
        resultAddress.setText(auth.getAuthAddress());
        resultInfo.setText("请求登录的IP地址为"+auth.getAuthIp()+"，请确认是否本人操作");
    }

    private void initListener() {
        resultConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = DialogUIUtils.showLoading(ResultActivity.this,
                        "登录中...", false, false,
                        false, true)
                        .show();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("work_num", ApiUtil.WORK_NUM);
                new UniteApi(ApiUtil.TOKEN_USE + auth.getAuthToken(), hashMap).post(new ApiListener() {
                    @Override
                    public void success(Api api) {
                        dialog.dismiss();
                        UniteApi uniteApi = (UniteApi) api;

                        try {

                            if (uniteApi.getJsonData().getInt("state") == 1) {
                                DialogUIUtils.showToastCenter("登录成功");
                                finish();
                            } else if(uniteApi.getJsonData().getInt("state") == 2) {
                                DialogUIUtils.showToastCenter("获取口令信息失败");
                            } else if(uniteApi.getJsonData().getInt("state") == 3) {
                                DialogUIUtils.showToastCenter("登录码过期");
                            } else if(uniteApi.getJsonData().getInt("state") == 4) {
                                DialogUIUtils.showToastCenter("口令信息错误");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            DialogUIUtils.showToastCenter("登录失败，请重试");
                        }
                    }

                    @Override
                    public void failure(Api api) {
//                        UniteApi uniteApi = (UniteApi) api;
                        Log.i("Api", String.valueOf(api));

                        dialog.dismiss();
                        DialogUIUtils.showToastCenter("登录失败，请重试");
                    }
                });
            }
        });

        resultBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resultCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
