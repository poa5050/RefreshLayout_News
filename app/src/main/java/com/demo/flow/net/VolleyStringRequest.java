package com.demo.flow.net;


import android.util.Log;

import com.alibaba.fastjson.serializer.URLCodec;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;

import java.net.URLEncoder;
import java.util.Map;

public class VolleyStringRequest extends StringRequest {

    public VolleyStringRequest(int method, String keyword,int page, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, "http://apis.baidu.com/songshuxiansheng/real_time/search_news?keyword="+ URLEncoder.encode(keyword)+"&count=10&page="+page, listener, errorListener);
    }

    public VolleyStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public VolleyStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        //return VolleyRetryPolicy.getDefault();
        return new DefaultRetryPolicy(20 * 1000, 1, 1.0f);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new VolleyParams();

  /*      params.put("format", "json");
        params.put("client", "Android");
        // 其实应为encrypt_type
        params.put("enpty_type", "yes");

        boolean login = UserInfo.isLogin();
        if (login) {
            params.put("sid", UserInfo.getSessionId());
        }*/

        return params;
    }
}
