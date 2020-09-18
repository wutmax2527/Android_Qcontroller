package th.co.infinitecorp.www.qcontroller.Screen.SreenOnWebView;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import th.co.infinitecorp.www.qcontroller.QTouchWeb.QTouchOnWebService;
import th.co.infinitecorp.www.qcontroller.R;
import th.co.infinitecorp.www.qcontroller.Service.QTouchService;
import th.co.infinitecorp.www.qcontroller.Utils.FileName;
import th.co.infinitecorp.www.qcontroller.Utils.FolderPath;

public class QTouchOnWebView extends AppCompatActivity {
    private static final String TAG = QTouchOnWebView.class.getSimpleName();
    Context context;
    WebView myWebView;
    final static int PICKFILE_RESULT_CODE =1;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qtouch_on_web_view);
        context=QTouchOnWebView.this;
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        /*
        //---Old Config
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSupportZoom(false);
        webSettings.setAppCacheEnabled(false);
        //----
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setDomStorageEnabled(true);
        */
        //---New Config
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(false);
        webSettings.setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webSettings.setSafeBrowsingEnabled(false);
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(myWebView, true);
        }


        //---Extras tried for Android 9.0, can be removed if want.
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBlockNetworkImage(false);



        File external_m1 =  Environment.getExternalStorageDirectory();
        String filePath1= Environment.getExternalStorageDirectory().getPath();
        File filePath2= new File(context.getExternalFilesDir(FolderPath.QTouchWeb), "QTouch.html");
        File filePath3= new File(context.getExternalFilesDir(FolderPath.QTouchWeb_Style1), "QTouch.html");
        File filePath4= new File(context.getExternalFilesDir(FolderPath.QTouchWeb_Style2), "QTouch.html");

        String htmlFile1="file:///android_asset/QTouchWeb/QTouch.html";
        String htmlFile2="file:///android_asset/QTouchWeb/Style1/QTouch.html";
        String htmlFile3="file:///android_asset/QTouchWeb/Style2/QTouch.html";

        String htmlFile4="file:///"+filePath1+"/storage"+"/Qcontroller/QTouchWeb/QTouch.html";
        String htmlFile5="file:///"+filePath1+"/storage"+"/Qcontroller/QTouchWeb/Style1/QTouch.html";
        String htmlFile6="file:///"+filePath1+"/storage"+"/Qcontroller/QTouchWeb/Style2/QTouch.html";

        String htmlFile7="file:///"+filePath2.getPath();
        String htmlFile8="file:///"+filePath3.getPath();
        String htmlFile9="file:///"+filePath4.getPath();

        String htmlFile10="http://127.0.0.1:20601";

        String htmlFile=htmlFile4;

        /*
        File f=new File(htmlFile);
        if(f.exists())
        {
            Toast.makeText(this, "path="+htmlFile, Toast.LENGTH_SHORT).show();
            int j=0;
        }else
            {
           int k=0;
        }
        */


        //myWebView.loadUrl("https://www.judjod.com/");
        /*
        myWebView.setWebChromeClient(new WebChromeClient(){

            public void onProgressChanged(WebView view, int progress) {

            }
        });
        */
        //myWebView.loadUrl("javascript:doConnect()");
        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.loadUrl(htmlFile);
        //myWebView.setWebChromeClient(new WebChromeClient());
        //myWebView.setWebViewClient(new WebViewClient());

        /*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            myWebView.evaluateJavascript("enable();", null);
        } else {
            myWebView.loadUrl("javascript:enable();");
        }
        */



        /**
        myWebView.setWebContentsDebuggingEnabled(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        myWebView.clearCache(true);
        */

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

        //String url="file:///android_asset/QTouchWeb/"+fileName;

        /*
        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(htmlFile9));
        browserIntent.setPackage("com.android.chrome"); // Whatever browser you are using
        startActivity(browserIntent);
        */
        /*
        File file=new File(htmlFile);
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(file));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(htmlFile9));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setPackage("com.android.chrome");
        startActivity(intent);
        */

        //openInBrowser(file);
        //final Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        //browserIntent.setDataAndType(Uri.fromFile(file), "text/html");
        //startActivity(browserIntent);

        /*
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(htmlFile);
        intent.setDataAndType(uri, "text/html");
        intent.setPackage("com.android.chrome");
        startActivityForResult(intent,PICKFILE_RESULT_CODE);
       */
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
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
            //intent.setDataAndType(Uri.fromFile(url), "text/html");
            //intent.setPackage("com.android.chrome");
            //view.getContext().startActivity(intent);
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //for(int i=0; i<1; i++) {
                myWebView.loadUrl("javascript:doConnect()");
                myWebView.loadUrl("javascript:doSend()");
            //}
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

        }
        @JavascriptInterface
        public void someMethodOnAndroid(String toast) {
            String strShow="someMethodOnAndroid="+toast;
            Toast.makeText(mContext, strShow, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void showToast() {
            Toast.makeText(mContext, "กรุณารอสักครู่...", Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void getQueueDiv() {
            Toast.makeText(mContext, "Div=", Toast.LENGTH_SHORT).show();
        }
        @JavascriptInterface
        public void getQueueDiv1(int div) {
            Toast.makeText(mContext, "Div1...", Toast.LENGTH_SHORT).show();
        }
        @JavascriptInterface
        public void getQueueDiv2(int div) {
            Toast.makeText(mContext, "Div12...", Toast.LENGTH_SHORT).show();
        }
        @JavascriptInterface
        public void SendPrinter(String sDivID) {

            Toast.makeText(mContext, "SendPrinter...div="+sDivID, Toast.LENGTH_SHORT).show();
            QTouchService qTouchService=new QTouchService(getApplicationContext());
            qTouchService.Call_REQUEST_QUEUE((byte) 1);
        }
        @JavascriptInterface
        public void  PrintTicket(String sDivID) {
            Integer iDivID=Integer.valueOf(sDivID);
            byte divId= iDivID.byteValue();
            Toast.makeText(mContext, "PrintTicket div="+sDivID, Toast.LENGTH_SHORT).show();
            QTouchService qTouchService=new QTouchService(getApplicationContext());
            qTouchService.Call_REQUEST_QUEUE((byte) divId);
        }
    }

    private void openInBrowser(File file) {
        final Uri uri = Uri.fromFile(file);
        try {
            final Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setClassName("com.android.chrome", "com.android.chrome.BrowserActivity");
            browserIntent.setData(uri);
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            final Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setDataAndType(Uri.fromFile(file), "text/html");
            startActivity(browserIntent);
        }
    }
    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PICKFILE_RESULT_CODE :
                if(resultCode==RESULT_OK){
                    Uri uri1 = data.getData();
                    File file1 = new File(uri1.getPath());
                    final String[] split = file1.getPath().split(":");

                    String filePath = split[1];

                    //---after splitting it will be /storage/emulated/0/Download/index.html
                    /*
                    if (filePath.endsWith(".html")){
                        myWebView.setWebContentsDebuggingEnabled(true);
                        myWebView.getSettings().setJavaScriptEnabled(true);
                        myWebView.getSettings().setBuiltInZoomControls(true);
                        myWebView.getSettings().setUseWideViewPort(true);
                        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                        myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
                        myWebView.clearCache(true);
                        myWebView.setWebViewClient(new WebViewClientClass());
                        myWebView.loadUrl(filePath);
                        //myWebView.evaluateJavascript(filePath);
                    }
                   */
                }
                break;
        }
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}
