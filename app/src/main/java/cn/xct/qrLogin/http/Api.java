package cn.xct.qrLogin.http;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import cn.xct.qrLogin.activity.LoginActivity;
import cn.xct.qrLogin.activity.LoginOutActivity;
import cn.xct.qrLogin.activity.SetupActivity;
import okhttp3.Call;

/**
 * 处理服务器端回调
 */
public abstract class Api {

    private ApiListener apiListener = null; // 将成功与否通过该接口回调

    private JSONObject jsonObject; // 响应结果
//    在非Activity下，例如自定义adapter.我们需要定义一个运行上下文来启动页面跳转：

    private Context context; //运行上下文

    private OkHttpCallback okHttpCallback = new OkHttpCallback() {

        @Override
        protected boolean isRunOnUiThread() {
            return isBackToUiThread();
        }

        @Override
        public void onSuccess(Call call, JSONObject jsonObject) { // 成功收到响应结果
            Log.i("getTest", "成功: ");
            Api.this.jsonObject = jsonObject;
//            if(isTokenTimeout()){
////                SharedPreferences.Editor editor = BaseApplication.getInstance().getSharedPreferences("token", MODE_PRIVATE).edit();
//////                editor.putBoolean("isPermit", false);
//////                editor.apply();
////                editor.clear();
////                editor.commit();
////                Intent intent = new Intent(null, LoginActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                startActivity(intent);
//                Log.i("Api", "onSuccess: 失败1");
//                Log.i("Api", "失败"+String.valueOf(jsonObject));
////                Log.i("Api", "错误信息"+e.toString());
//                Intent intent = new Intent();
//                intent.setClass(context, LoginOutActivity.class);
//                context.startActivity(intent);
//            }
            if (isSuccess()) { // 根据状态码判断调用成功与否
                try {
                    Log.i("Api", "onSuccess: 失败3");

                    parseData(jsonObject);//调用parseData解析响应结果
                    apiListener.success(Api.this); // 回调成功
                } catch (Exception e) {
                    Log.i("Api", "onSuccess: 失败2");
                    Log.i("Api", "失败"+String.valueOf(jsonObject));
                    Log.i("Api", "错误信息"+e.toString());


                    e.printStackTrace();
                    apiListener.failure(Api.this); // 回调失败，解析响应结果中的data错误
                }
            } else {
                Log.i("Api", "onSuccess: 失败4");

                try {
                    parseCode(jsonObject);
                    apiListener.failure(Api.this); // 回调失败，状态码非0
                } catch (Exception e) {
                    e.printStackTrace();
                    apiListener.failure(Api.this); // 回调失败，解析响应结果中的data错误
                }
            }
        }

        @Override
//        public void onFailure(Call call) {
//            apiListener.failure(Api.this);
//        }
        public void onFailure(Call call) {
            apiListener.failure(Api.this);
        }
    };

    private boolean isSuccess() {
        return "0".equals(jsonObject.optString("code"))
                || "200".equals(jsonObject.optString("code"));
    }

    private boolean isTokenTimeout() {
        return "304".equals(jsonObject.optString("code"));
//                || "200".equals(jsonObject.optString("code"));
    }

//    public void skip(Context context) {
//
//        Intent intent = new Intent(context, LoginOutActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//
//    }

    protected boolean isBackToUiThread() {
        return false;
    }

    private HashMap<String,String> paramsMap = new HashMap<>();
    void setParamsMap(HashMap<String,String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public void get(ApiListener apiListener) {
        this.apiListener = apiListener;
        OkHttpUtil.get(getUrl(), okHttpCallback, paramsMap);
    }

    public void post(ApiListener apiListener) {
        this.apiListener = apiListener;
        OkHttpUtil.post(getUrl(), okHttpCallback, paramsMap);
    }


    protected abstract void parseCode(JSONObject jsonObject) throws Exception; //解析响应状态
    protected abstract void parseData(JSONObject jsonObject) throws Exception; //解析响应结果

    protected abstract String getUrl();
}
