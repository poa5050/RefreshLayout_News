package com.demo.flow.net;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;

public abstract class VolleyResponseListener implements Response.Listener<String> {

    // 状态码 : 登录已过期
    static final int NOT_LOGIN = 400;

    @Override
    public void onResponse(String response) {
        Log.e("news", "返回值：" + response.toString());

        JSONObject json;

        try {
            json = JSON.parseObject(response);
        } catch (Exception e) {
            Log.e("news", e.getMessage());
            Log.e("news", "转换返回值为JSON时失败");

            json = new JSONObject();
        }

        try {
            /*
            ResponseModel respModel = JSON.toJavaObject(json, ResponseModel.class);

            Log.d(VolleyResponseListener.class.getName(), "message : " + respModel.getMessage());

            if (respModel.getResultCode() == NOT_LOGIN) {
                // Toast提示
                QBLToast.show(R.string.sesstion_id_has_expired);
                // 退出用户登录
                UserInfo.logOut();
                // 重启应用
                App.self.restart();

                return;
            }
            */
        } catch (Exception e) {
            Log.e("news", e.toString());
        }

        try {
            handleJson(json);
        } catch (Exception e) {
            Log.e("news", e.toString());
        }
    }

    public void handleJson(JSONObject json) {

    }
}
