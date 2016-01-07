package com.demo.flow.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

public class WebViewActivity extends AppCompatActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private WebView mContentWv;
    private BGARefreshLayout mRefreshLayout;
    private App mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mApp = App.getInstance();
        mRefreshLayout = (BGARefreshLayout)findViewById(R.id.rl_webview_refresh);
        mContentWv = (WebView)findViewById(R.id.wv_webview_content);
        setListener();
        processLogic();
    }

    public static void start(Context context,String url) {
        Intent intent = new Intent();
        intent.setClass(context, WebViewActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

    protected void setListener() {
        mRefreshLayout.setDelegate(this);
        mContentWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mRefreshLayout.endRefreshing();
            }
        });
    }

    protected void processLogic() {
        mRefreshLayout.setRefreshViewHolder(new BGAMoocStyleRefreshViewHolder(mApp, true));
        mContentWv.getSettings().setJavaScriptEnabled(true);
        mContentWv.loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mContentWv.reload();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mRefreshLayout.endRefreshing();
        return false;
    }
}
