package cn.marco.meizhi.module.other;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.marco.meizhi.module.BaseSwipeBackActivity;
import cn.marco.meizhi.C;
import cn.marco.meizhi.R;
import cn.marco.meizhi.util.Utils;

public class WebViewActivity extends BaseSwipeBackActivity {

    private WebView mWebView;
    private Toolbar mToolbar;
    private String mUrl;

    @Override protected int getContentView() {
        return R.layout.activity_webview;
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getIntent().getStringExtra(C.extra.url);
        String desc = getIntent().getStringExtra(C.extra.desc);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(desc);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new XWebViewClient());
        mWebView.setWebChromeClient(new XWebChromeClient());
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        mWebView.loadUrl(mUrl);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            mWebView.loadUrl(mUrl);
        });
    }

    private class XWebChromeClient extends WebChromeClient{
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mToolbar.setTitle(title);
        }
    }

    private class XWebViewClient extends WebViewClient {

        @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mSwipeRefreshLayout.setRefreshing(true);
            mSwipeRefreshLayout.setEnabled(true);
        }

        @Override public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(false);
        }

        @Override public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // 处理 HTTPS 证书过期或私有证书无法打开的情况~
            handler.proceed();
        }

        @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url != null){
                view.loadUrl(url);
            }
            return true;
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_share:
                Utils.share(this, mWebView.getTitle() + " " + mWebView.getUrl());
                break;
            case R.id.action_refresh:
                mWebView.reload();
                break;
            case R.id.action_copyurl:
                Utils.copyToClipBoard(this, mWebView.getTitle() + " " + mWebView.getUrl());
                break;
            case R.id.action_open:
                Utils.openWithBrowser(this, mWebView.getUrl());
                break;
        }
        return true;
    }

    @Override public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override protected void onDestroy() {
        if (mWebView != null) {
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.destroy();
        }
        super.onDestroy();
    }
}
