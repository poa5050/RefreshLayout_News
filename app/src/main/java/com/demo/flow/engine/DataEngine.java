package com.demo.flow.engine;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.flow.model.BannerModel;
import com.demo.flow.model.News;
import com.demo.flow.model.RefreshModel;
import com.demo.flow.model.StaggeredModel;
import com.demo.flow.net.VolleyParams;
import com.demo.flow.net.VolleyResponseListener;
import com.demo.flow.net.VolleyStringRequest;
import com.demo.flow.news.R;
import com.demo.flow.util.Constants;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 *
 */
public class DataEngine {
    private static AsyncHttpClient sAsyncHttpClient = new AsyncHttpClient();

    private static List list;
    private static List formatJsonArray(JSONArray array){
        list = new ArrayList();
        try {
            for (int i = 0; i < array.size(); i++) {
                list.add(JSON.toJavaObject((JSONObject)array.get(i), News.class));
            }
        }catch (Exception e){}
        return list;
    }

    private static void loadData(final NewsModelResponseHandler responseHandler,int pageNum){
        StringRequest request = new VolleyStringRequest(Request.Method.GET, Constants.type,pageNum, new VolleyResponseListener() {
            @Override
            public void handleJson(JSONObject json) {
                super.handleJson(json);
                int errNum = json.getInteger("errNum");
                JSONObject retData = json.getJSONObject("retData");
                List<News> newsModel = new ArrayList<News>();
                if(errNum == 0) {
                    newsModel = (List<News>)formatJsonArray(retData.getJSONArray("data"));
                    responseHandler.onSuccess(newsModel,retData.getInteger("has_more") == 1);
                }
            }

            @Override
            public void onResponse(String response) {
                //Log.e("news", "response:" + response);
                super.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("news", "访问失败:" + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new VolleyParams();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new VolleyParams();
                header.put("apikey","bb55a806b684b59d1f028bf185b3f4fa");
                return header;
            }
        };
        getVolleyRequestQueue().add(request);
    }

    private static RequestQueue mVolleyRequestQueue;

    private static RequestQueue getVolleyRequestQueue() {
        if (mVolleyRequestQueue == null) {
            mVolleyRequestQueue = Volley.newRequestQueue(contentActivity);
        }
        return mVolleyRequestQueue;
    }

    public static void loadInitDatas(final NewsModelResponseHandler responseHandler) {
        loadData(responseHandler,1);
        /*
        sAsyncHttpClient.get("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/api/defaultdata.json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                responseHandler.onFailure();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                List<RefreshModel> refreshModels = new GsonBuilder().create().fromJson(responseString, new TypeToken<ArrayList<RefreshModel>>() {
                }.getType());
                responseHandler.onSuccess(refreshModels);
            }
        });*/

    }

    public static void loadNewData(final int pageNumber, final NewsModelResponseHandler responseHandler) {
        loadData(responseHandler,pageNumber);
        /*
        // 测试数据放到七牛云存储的，再加上WiFi环境，加载数据非常快，看不出效果，这里延时2秒再请求的网络数据
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sAsyncHttpClient.get("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/api/newdata" + pageNumber + ".json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        responseHandler.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        List<RefreshModel> refreshModels = new GsonBuilder().create().fromJson(responseString, new TypeToken<ArrayList<RefreshModel>>() {
                        }.getType());
                        responseHandler.onSuccess(refreshModels);
                    }
                });
            }
        }, 2000);
        */
    }

    public static void loadMoreData(final int pageNumber, final NewsModelResponseHandler responseHandler) {
        loadData(responseHandler,pageNumber);
        /*
        // 测试数据放到七牛云存储的，再加上WiFi环境，加载数据非常快，看不出效果，这里延时1秒再请求的网络数据
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sAsyncHttpClient.get("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/api/moredata" + pageNumber + ".json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        responseHandler.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        List<RefreshModel> refreshModels = new GsonBuilder().create().fromJson(responseString, new TypeToken<ArrayList<RefreshModel>>() {
                        }.getType());
                        responseHandler.onSuccess(refreshModels);
                    }
                });
            }
        }, 1000);
        */
    }

    public static void loadDefaultStaggeredData(final StaggeredModelResponseHandler responseHandler) {
        /*
        sAsyncHttpClient.get("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/api/staggered_default.json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                responseHandler.onFailure();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                List<StaggeredModel> refreshModels = new GsonBuilder().create().fromJson(responseString, new TypeToken<ArrayList<StaggeredModel>>() {
                }.getType());
                responseHandler.onSuccess(refreshModels);
            }
        });
        */
    }

    public static void loadNewStaggeredData(final int pageNumber, final StaggeredModelResponseHandler responseHandler) {
        /*
        // 测试数据放到七牛云存储的，再加上WiFi环境，加载数据非常快，看不出效果，这里延时2秒再请求的网络数据
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sAsyncHttpClient.get("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/api/staggered_new" + pageNumber + ".json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        responseHandler.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        List<StaggeredModel> refreshModels = new GsonBuilder().create().fromJson(responseString, new TypeToken<ArrayList<StaggeredModel>>() {
                        }.getType());
                        responseHandler.onSuccess(refreshModels);
                    }
                });
            }
        }, 2000);
        */
    }

    public static void loadMoreStaggeredData(final int pageNumber, final StaggeredModelResponseHandler responseHandler) {
        /*
        // 测试数据放到七牛云存储的，再加上WiFi环境，加载数据非常快，看不出效果，这里延时1秒再请求的网络数据
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sAsyncHttpClient.get("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/api/staggered_more" + pageNumber + ".json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        responseHandler.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        List<StaggeredModel> refreshModels = new GsonBuilder().create().fromJson(responseString, new TypeToken<ArrayList<StaggeredModel>>() {
                        }.getType());
                        responseHandler.onSuccess(refreshModels);
                    }
                });
            }
        }, 1000);
        */
    }

    public static Context contentActivity;

    public static View getCustomHeaderView(Context context) {
        View headerView = View.inflate(context, R.layout.view_custom_header, null);
        final BGABanner banner = (BGABanner) headerView.findViewById(R.id.banner);
        final List<View> views = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            views.add(View.inflate(context, R.layout.view_image, null));
        }
        banner.setViews(views);
        sAsyncHttpClient.get("http://7xk9dj.com1.z0.glb.clouddn.com/banner/api/5item.json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                BannerModel bannerModel = new Gson().fromJson(responseString, BannerModel.class);
                ImageLoader imageLoader = ImageLoader.getInstance();
                for (int i = 0; i < views.size(); i++) {
                    imageLoader.displayImage(bannerModel.imgs.get(i), (ImageView) views.get(i));
                }
                banner.setTips(bannerModel.tips);
            }
        });
        return headerView;
    }

    public interface RefreshModelResponseHandler {
        void onFailure();

        void onSuccess(List<RefreshModel> refreshModels);
    }

    public interface StaggeredModelResponseHandler {
        void onFailure();

        void onSuccess(List<StaggeredModel> staggeredModels);
    }

    public interface NewsModelResponseHandler {
        void onFailure();

        void onSuccess(List<News> newsModels,boolean has_more);
    }
}