package com.demo.flow.news;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demo.flow.util.ToastUtil;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

public class WebViewActivity extends AppCompatActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private BGARefreshLayout mRefreshLayout;
    private App mApp;

    @Bind(R.id.webTitle)
    TextView mTitle;

    @Bind(R.id.previewWeb)
    WebView webPreview;

    @Bind(R.id.previewProgressBar)
    ProgressBar mProgressBar;

    @Bind(R.id.back_up)
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_container);
        ButterKnife.bind(this);
        //initActivityWH();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mApp = App.getInstance();
        mRefreshLayout = (BGARefreshLayout)findViewById(R.id.rl_webview_refresh);
        mRefreshLayout.setDelegate(this);
        processLogic();
        initContentView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                //onClickShare(v);
            }
        });
    }

    public void onClickShare(View view) {

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "终于可以了!!!");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getTitle()));

    }

    private void initActivityWH() {
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参值
        p.height = (int) (d.getHeight() * 0.7); // 高度
        p.width = (int) (d.getWidth() * 0.7); // 宽度
        p.alpha = 1.0f; // 设置本身透明度
        p.dimAmount = 0.8f; // 设置黑暗度
        getWindow().setAttributes(p); // 设置生效
        getWindow().setGravity(Gravity.CENTER); // 设置靠右对齐
    }

    public static void start(Context context,String url) {
        Intent intent = new Intent();
        intent.setClass(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    protected void processLogic() {
        BGAMoocStyleRefreshViewHolder refreshViewHolder = new BGAMoocStyleRefreshViewHolder(mApp, true);
        refreshViewHolder.setUltimateColor(getResources().getColor(R.color.colorPrimaryDark));
        refreshViewHolder.setOriginalBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.mooc_ico_60));
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        webPreview.reload();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mRefreshLayout.endLoadingMore();
        return false;
    }

    private void initContentView() {
        // 网页中的视频，上屏幕的时候，可能出现闪烁的情况，需要如下设置：Activity在onCreate时需要设置:
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        WebSettings webSettings = webPreview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);//设定支持viewport
        webSettings.enableSmoothTransition();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);//设定支持缩放

        // 自定义WebChromeClient
        webPreview.setWebChromeClient(new MyWebChromeClient());
        // 自定义WebViewClient
        webPreview.setWebViewClient(new MyWebViewClient());

        mRefreshLayout.beginRefreshing();
        loadUrl(getIntent().getStringExtra("url"));

    }

    private void loadUrl(String url){
        webPreview.loadUrl(url);
    }

    private class MyWebViewClient extends com.tencent.smtt.sdk.WebViewClient {

        //重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
        @Override
        public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView view, String url) {
            loadUrl(url);

            return true;
        }

        @Override
        public void onPageStarted(com.tencent.smtt.sdk.WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
            //evaluateWeilianJavascript(view);
        }

        @Override
        public void onPageFinished(com.tencent.smtt.sdk.WebView view, String url) {
            super.onPageFinished(view, url);
            mRefreshLayout.endRefreshing();
            mProgressBar.setVisibility(View.GONE);
        }

        //加载有ssl层的https页面
        @Override
        public void onReceivedSslError(final com.tencent.smtt.sdk.WebView webView, SslErrorHandler handler, SslError error) {
            //super.onReceivedSslError(webView, sslErrorHandler, sslError);
            //sslErrorHandler.cancel(); 默认的处理方式，WebView变成空白页
            handler.proceed(); // 接受证书
            // handleMessage(Message msg); 其他处理
        }

    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(com.tencent.smtt.sdk.WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            mProgressBar.setProgress(newProgress);
        }

        @Override
        public void onReceivedTitle(com.tencent.smtt.sdk.WebView view, String title) {
            super.onReceivedTitle(view, title);

            mTitle.setText(title.length() > 15 ? title.substring(0, 15) + "..." : title);

            //evaluateJavascript(view, JavaScript.JS_READ_PAGE_SOURCE);
        }

    }
}
