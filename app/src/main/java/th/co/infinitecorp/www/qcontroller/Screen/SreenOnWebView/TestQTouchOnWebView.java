package th.co.infinitecorp.www.qcontroller.Screen.SreenOnWebView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import th.co.infinitecorp.www.qcontroller.R;

public class TestQTouchOnWebView extends AppCompatActivity {
    private static final String TAG = TestQTouchOnWebView.class.getSimpleName();
    WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_qtouch_on_web_view);
        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSupportZoom(true);
        webSettings.setAppCacheEnabled(false);

        //myWebView.loadUrl("https://www.judjod.com/");

        String fileName="TestQTouchOnWebView.html";
        InputStream is = null;
        int size = 0;
        byte[] buffer=null;
        String content="";
        try {
            is = getAssets().open(fileName);
            size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            content = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String encodedHtml = Base64.encodeToString(content.getBytes(), Base64.NO_PADDING);
        myWebView.loadData( encodedHtml, "text/html", "base64");

        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        myWebView.setWebViewClient(new MyWebViewClient());

        Button btnSend=(Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Hello WebView hey";
                if (!message.isEmpty()) {
                    String jsString = ("javascript:setTextField(" +
                            "'" + message + "')");
                    myWebView.loadUrl(jsString);
                }
            }
        });
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if ("www.google.co.th".equals(Uri.parse(url).getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }
    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            String strShow="showToast="+toast;
            Toast.makeText(mContext, strShow, Toast.LENGTH_SHORT).show();
            Log.d(TAG,"showToast1");
        }
        @JavascriptInterface
        public void someMethodOnAndroid(String toast) {
            String strShow="someMethodOnAndroid="+toast;
            Toast.makeText(mContext, strShow, Toast.LENGTH_SHORT).show();
            Log.d(TAG,"showToast2");
        }

        @JavascriptInterface
        public void showToast() {
            Toast.makeText(mContext
                    , "Hello WebView"
                    , Toast.LENGTH_SHORT).show();
        }
    }

}
