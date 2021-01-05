package cn.xct.qrLogin.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;

import java.util.HashMap;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import cn.xct.qrLogin.R;
import cn.xct.qrLogin.entity.AuthEntity;
import cn.xct.qrLogin.http.Api;
import cn.xct.qrLogin.http.ApiListener;
import cn.xct.qrLogin.http.ApiUtil;
import cn.xct.qrLogin.http.UniteApi;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ScanActivity extends AppCompatActivity {
    private static final int CODE_FOR_CAMERA_PERMISSION = 1;//如果勾选了不再询问
    private RelativeLayout scanUAvatarBtn;
    private ImageView scanUAvatar;
    private ZXingView scanView;

    private static final int NOT_NOTICE = 2;//如果勾选了不再询问
    private AlertDialog alertDialog;
    private AlertDialog mDialog;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        init();
        applyCameraPermission();
    }

    public void refresh() {
        finish();
        Intent intent = new Intent(ScanActivity.this, ScanActivity.class);
        startActivity(intent);
//        onCreate(null);
    }
    //权限申请
    private void applyCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }else {
            initListener();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {//选择了“始终允许”
//                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                    DialogUIUtils.showToastCenter("" + "权限申请成功");
                    refresh();
                    initListener();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {//用户选择了禁止不再询问

                        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (mDialog != null && mDialog.isShowing()) {
                                            mDialog.dismiss();
                                        }
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);//注意就是"package",不用改成自己的包名
                                        intent.setData(uri);
                                        startActivityForResult(intent, NOT_NOTICE);
                                    }
                                });
                        mDialog = builder.create();
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();


                    } else {//选择禁止
                        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (alertDialog != null && alertDialog.isShowing()) {
                                            alertDialog.dismiss();
                                        }
                                        ActivityCompat.requestPermissions(ScanActivity.this,
                                                new String[]{Manifest.permission.CAMERA}, 1);
                                    }
                                });
                        alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }

                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==NOT_NOTICE){
            applyCameraPermission();//由于不知道是否选择了允许所以需要再次判断
        }
    }

    private void init() {
        scanUAvatarBtn = findViewById(R.id.scanUAvatarBtn);
        scanUAvatar = findViewById(R.id.scanUAvatar);
        scanView = findViewById(R.id.scanView);
        Glide.with(this)
                .load(ApiUtil.USER_AVATAR)
                .apply(bitmapTransform(new CircleCrop()))
                .into(scanUAvatar);
    }

    private void initListener() {
        scanUAvatarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, SetupActivity.class);
                startActivity(intent);
            }
        });
        scanView.setDelegate(new QRCodeView.Delegate() {
            @Override
            public void onScanQRCodeSuccess(String result) {
                vibrate(); // 震动
//                DialogUIUtils.showToastTop(result);
                dialog = DialogUIUtils.showLoading(ScanActivity.this,
                        "处理中...", false, false,
                        false, true)
                        .show();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user_uuid", ApiUtil.USER_ID);
                hashMap.put("isScan", "true");
//                DialogUIUtils.showToastCenter(ApiUtil.TOKEN_INFO + result);

                new UniteApi(ApiUtil.TOKEN_INFO + result, hashMap).post(new ApiListener() {
                    @Override
                    public void success(Api api) {
                        dialog.dismiss();
                        UniteApi uniteApi = (UniteApi) api;
                        Gson gson = new Gson();
                        AuthEntity auth = gson.fromJson(uniteApi.getJsonData().toString(), AuthEntity.class);
                        if (auth.getAuthState() == 0 || auth.getAuthState() == 2) {
                            Intent intent = new Intent(ScanActivity.this, ResultActivity.class);
                            intent.putExtra("auth", auth);
                            startActivity(intent);
                        } else if (auth.getAuthState() == 1) {
                            DialogUIUtils.showToastCenter("登录码已使用");
                        } else {
                            DialogUIUtils.showToastCenter("登录码已过期");
                        }
                        delayStartSpot();
                    }

                    @Override
                    public void failure(Api api) {
                        dialog.dismiss();
//                        DialogUIUtils.showToastCenter("登录码已过期1");
                        DialogUIUtils.showToastCenter(ApiUtil.TOKEN_INFO + 123);

                        delayStartSpot();
                    }
                });

            }

            @Override
            public void onCameraAmbientBrightnessChanged(boolean isDark) {

            }

            @Override
            public void onScanQRCodeOpenCameraError() {

            }
        });
    }

    private void delayStartSpot() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                scanView.startSpot();//开始识别二维码
            }
        }).start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        scanView.startCamera();//打开相机
        scanView.showScanRect();//显示扫描框
        scanView.startSpot();//开始识别二维码
        //scanView.openFlashlight();//开灯
        //scanView.closeFlashlight();//关灯
    }

    @Override
    protected void onStop() {
        scanView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        scanView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
