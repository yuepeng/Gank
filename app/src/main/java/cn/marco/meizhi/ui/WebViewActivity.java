package cn.marco.meizhi.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.marco.meizhi.R;
import cn.marco.meizhi.util.Utils;

public class WebViewActivity extends BaseSwipeBackActivity {

    private WebView mWebView;
    private String mUrl;

    public static Intent getStartIntent(Context context, String url, String desc) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(KEY_DESC, desc);
        intent.putExtra(KEY_URL, url);
        return intent;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_webview;
    }

    @Override
    public String getToolbarTitle() {
        return getIntent().getStringExtra(KEY_DESC);
    }

    @Override
    public void initViews() {
        super.initViews();
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new XWebViewClient());
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        mUrl = getIntent().getStringExtra(KEY_URL);
        mWebView.loadUrl(mUrl);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            mWebView.loadUrl(mUrl);
        });

    }

    private class XWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mSwipeRefreshLayout.setRefreshing(true);
            mSwipeRefreshLayout.setEnabled(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(false);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // 处理 HTTPS 证书过期或私有证书无法打开的情况~
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url != null){
                view.loadUrl(url);
            }
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.destroy();
        }
        super.onDestroy();
    }
}
