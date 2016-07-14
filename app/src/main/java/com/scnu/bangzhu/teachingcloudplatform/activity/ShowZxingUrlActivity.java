package com.scnu.bangzhu.teachingcloudplatform.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.scnu.bangzhu.teachingcloudplatform.R;

/**
 * Created by bangzhu on 2016/6/17.
 */
public class ShowZxingUrlActivity extends Activity {
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_show);
        initView();
        setContents();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
    }

    private void setContents() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        Toast.makeText(this, url, Toast.LENGTH_LONG).show();
        webView.loadUrl(url);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
}
