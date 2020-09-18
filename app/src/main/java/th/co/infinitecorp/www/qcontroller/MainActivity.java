package th.co.infinitecorp.www.qcontroller;

import android.Manifest;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QueueInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.Database.DBManager;
import th.co.infinitecorp.www.qcontroller.Database.Model.QMSDB;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.GPIO.GPIOService;
import th.co.infinitecorp.www.qcontroller.Management.APIMgr;
import th.co.infinitecorp.www.qcontroller.Management.DisplayMgr;
import th.co.infinitecorp.www.qcontroller.Management.InitializeMgr;
import th.co.infinitecorp.www.qcontroller.Management.PeriperalMgr;
import th.co.infinitecorp.www.qcontroller.Management.QueueMgr;
import th.co.infinitecorp.www.qcontroller.Management.SoundMgr;
import th.co.infinitecorp.www.qcontroller.Management.SystemMgr;
import th.co.infinitecorp.www.qcontroller.Management.ViewLogMgr;
import th.co.infinitecorp.www.qcontroller.QClientWeb.QClientOnWebService;
import th.co.infinitecorp.www.qcontroller.QTouchWeb.QTouchOnWebService;
import th.co.infinitecorp.www.qcontroller.Screen.QTouch.TouchScreenStyle1;
import th.co.infinitecorp.www.qcontroller.Screen.QTouch.TouchScreenStyle1FullscreenActivity;
import th.co.infinitecorp.www.qcontroller.Screen.Setting.SettingScreen;
import th.co.infinitecorp.www.qcontroller.Screen.SreenOnWebView.QTouchOnWebView;
import th.co.infinitecorp.www.qcontroller.Screen.SreenOnWebView.TestQTouchOnWebView;
import th.co.infinitecorp.www.qcontroller.Service.AllKeyService;
import th.co.infinitecorp.www.qcontroller.Service.AllSoftkeyService;
import th.co.infinitecorp.www.qcontroller.Service.DisplayService;
import th.co.infinitecorp.www.qcontroller.Service.MobileService;
import th.co.infinitecorp.www.qcontroller.Service.PrinterService;
import th.co.infinitecorp.www.qcontroller.Service.QTouchService;
import th.co.infinitecorp.www.qcontroller.Service.RealtimeService;
import th.co.infinitecorp.www.qcontroller.Service.QDisplayService;
import th.co.infinitecorp.www.qcontroller.Service.ServerService;
import th.co.infinitecorp.www.qcontroller.Service.SoundService;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCP;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCPClient;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCPServer;
import th.co.infinitecorp.www.qcontroller.UDPNetwork.UDP;
import th.co.infinitecorp.www.qcontroller.UDPNetwork.UDPServer;
import th.co.infinitecorp.www.qcontroller.Uart.Uart;
import th.co.infinitecorp.www.qcontroller.Utils.Convert;
import th.co.infinitecorp.www.qcontroller.Utils.DateTime;
import th.co.infinitecorp.www.qcontroller.Utils.ExternalStorage;
import th.co.infinitecorp.www.qcontroller.Utils.GData;
import th.co.infinitecorp.www.qcontroller.Utils.Protocol;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "debug";

    DevicePolicyManager devicePolicyManager;
    private boolean mIsKioskEnabled = true;
    /*Debug*/
    private TextView tv_MessageDebug;
    private String messageDebug = "";
    private Integer messageCountLine = 0;

    /*Thread*/
    private Thread callInitialThread = null;
    private Thread callMainThread = null;
    private Thread callCommunicateUartThread = null;

    /*Widget*/
    private EditText edtText_Div;
    private EditText editText_Serial;
    private LinearLayout layout_DebugView;
    private LinearLayout layout_QTouchView;
    private LinearLayout layout_SettingView;
    WebView myWebView;

    /*TCP Server*/
    private TCPServer tcpServer_Mobile;
    private TCPServer tcpServer_INNET;
    private TCPServer tcpServer_KEYPAD;
    private TCPServer tcpServer_DISPLAY;
    private TCPServer tcpServer_QTOUCH;
    private TCPServer tcpServer_QDISPLAY;
    private TCPServer tcpServer_Periperal;
    private TCPServer tcpServer_SoftKeyCmd;
    public static TCPServer tcpServer_SoftKeyUpdate;
    private TCPServer tcpServer_Web;
    private TCPServer tcpServer_QClient;
    private TCPServer tcpServer_QTouchWeb;

    /*TCP Client*/
    private TCPClient tcpClient_TEST;
    private TCPClient tcpClient_KEYPAD;
    private TCPClient tcpClient_DISPLAY;
    private TCPClient tcpClient_QTOUCH;
    private TCPClient tcpClient_QDISPLAY;

    /*UDP Server*/
    private UDPServer udpServer_SetTicket;
    private UDPServer udpServer_PrintTicket;
    DBManager dbMgr=new DBManager(MainActivity.this);

    final static int MY_PERMISSIONS_REQUEST=1;

    //Uart Q-Print&QSound
    private Uart uart =null;//new Uart(19200);
    private PrinterService printerService = new PrinterService();
    private SoundService soundService = new SoundService();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Start App...");
        tv_MessageDebug = (TextView) findViewById(R.id.tv_MessageDebug);
        tv_MessageDebug.setText(messageDebug);
        tv_MessageDebug.setMovementMethod(new ScrollingMovementMethod());

        /* Check Police Manager service and admin device.*/
        devicePolicyManager = (DevicePolicyManager) getSystemService(getBaseContext().DEVICE_POLICY_SERVICE);
        ComponentName mDpm = new ComponentName(this, MyAdmin.class);

        if (!devicePolicyManager.isAdminActive(mDpm)) {
            Toast.makeText(this, getString(R.string.not_device_admin), Toast.LENGTH_SHORT).show();
        }
        if (devicePolicyManager.isDeviceOwnerApp(this.getPackageName())) {
            devicePolicyManager.setLockTaskPackages(mDpm, new String[]{getPackageName()});
        } else {
            Toast.makeText(this, getString(R.string.not_device_owner), Toast.LENGTH_SHORT).show();
        }

        /*Set start kiosk mode*/
        mIsKioskEnabled = true;
        enableKioskMode(mIsKioskEnabled);
        setImmersiveMode(mIsKioskEnabled);

        /*KEEP_SCREEN_ON*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /*Register EventBus*/
        if (!EventBus.getDefault().isRegistered(MainActivity.this)) {
            EventBus.getDefault().register(MainActivity.this);
        }
        /*Start Application*/
        EventBus.getDefault().post(new DebugMessageEvent("Start APP..."));
        InitializeMgr.Init_BranchStatus(MainActivity.this);

        /*Event*/
        Init_Event();

        /*GPIO*/
        GPIOService.Init();

        /*Call Initial Start Thread*/
        callInitialThread = new Thread(CallInitialThread);
        callInitialThread.start();

        /*Show Touch Screen*/
        //startActivity(new Intent(getApplication(), TouchScreenStyle1.class));

        /*TCP Server*/
        TCP_Server_Listener();

        /*UDP Server*/
        UDP_Server_Listener();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

            }else {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST);
            }
        }

        if (!ExternalStorage.isExternalStorageAvailable() || ExternalStorage.isExternalStorageReadOnly()) {
            EventBus.getDefault().post(new DebugMessageEvent("***ExternalStorage is not OK!"));
        } else {
            EventBus.getDefault().post(new DebugMessageEvent("***ExternalStorage is OK"));
        }
        /*
        if( (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }
        */
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},23
                );
            }
        }

        /*Test SQlite*/
        //Test_SQLite();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Stop_Service();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Stop_Service();
        TCPServerStop();
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (mIsKioskEnabled) {
            mIsKioskEnabled = false;
            dialogCheckPassword();
        } else {
            mIsKioskEnabled = true;
            enableKioskMode(true);
            setImmersiveMode(mIsKioskEnabled);
        }


        layout_DebugView.setVisibility(View.VISIBLE);
        layout_QTouchView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setImmersiveMode(true);
    }

    @Subscribe
    public void onEvent(DebugMessageEvent event) {
        final String message = event.getMessage();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //show message
                String msg = "<" + DateTime.GetCurrentDateTime("HH:mm:ss.SSS") + ">" + message + "\n" + messageDebug;
                messageDebug = msg;
                tv_MessageDebug.setText(msg);
                if (++messageCountLine > 100) {
                    messageCountLine = 0;
                    messageDebug = "";
                }
            }
        });
    }

    /*Kiosk Mode*/
    public void enableKioskMode(boolean enabled) {
        try {
            if (enabled) {
                if (devicePolicyManager.isLockTaskPermitted(this.getPackageName())) {
                    startLockTask();
                    mIsKioskEnabled = true;
                    //tvVersionName.setTextColor( getColor( R.color.blue_500 ) );
                } else {
                    Toast.makeText(this, getString(R.string.kiosk_not_permitted), Toast.LENGTH_SHORT).show();
                }
            } else {
                stopLockTask();
                mIsKioskEnabled = false;
                //tvVersionName.setTextColor( getColor( R.color.white ) );
            }
        } catch (Exception e) {

        }
    }

    public void setImmersiveMode(Boolean enable) {
        if (enable) {
            final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.KEEP_SCREEN_ON
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            // Set the app into full screen mode
            getWindow().getDecorView().setSystemUiVisibility(flags);
        } else {
            final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.KEEP_SCREEN_ON
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    void dialogCheckPassword() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("PASSWORD");
        alertDialog.setMessage("Enter Password");

        final EditText edPassword = new EditText(MainActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        edPassword.setLayoutParams(layoutParams);
        alertDialog.setView(edPassword);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = edPassword.getText().toString();
                //if(password.compareTo( "12345678" ) == 0){
                Log.d(TAG, "Pass Word Pass");
                enableKioskMode(false);
                //}
            }
        });
        alertDialog.show();
    }

    /*Init_Event*/
    void Init_Event() {

        try {
            String versionName = BuildConfig.VERSION_NAME;

            TextView txt = (TextView) findViewById(R.id.tvVerion);
            txt.setText("Version:" + versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //---Webview Setting
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
        webSettings.setSupportZoom(false);
        webSettings.setAppCacheEnabled(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setDomStorageEnabled(true);

        String filePath1= Environment.getExternalStorageDirectory().getPath();
        String htmlFile4="file:///"+filePath1+"/storage"+"/Qcontroller/QTouchWeb/QTouch.html";
        String htmlFile=htmlFile4;
        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.loadUrl(htmlFile);

        //---Layout Setting
        layout_DebugView=(LinearLayout)findViewById(R.id.layout_DebugView);
        layout_DebugView.setVisibility(View.VISIBLE);
        layout_QTouchView=(LinearLayout)findViewById(R.id.layout_QTouchView);
        layout_QTouchView.setVisibility(View.GONE);

        Button btn_ResetSystem = (Button) findViewById(R.id.btn_ResetSystem);
        btn_ResetSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageDebug = "";
                messageCountLine = 0;
                tv_MessageDebug.setText(messageDebug);
                SystemMgr.Reset_System(MainActivity.this);
            }
        });
        Button btn_Setting = (Button) findViewById(R.id.btn_Setting);
        btn_Setting.setVisibility(View.GONE);
        btn_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplication(), SettingScreen.class));
                //startActivity(new Intent(getApplication(), QTouchOnWebView.class));
                //startActivity(new Intent(getApplication(), TestQTouchOnWebView.class));
                //startActivity(new Intent(getApplication(), TouchScreenStyle1FullscreenActivity.class));
                //startActivity(new Intent(getApplication(), TouchScreenStyle1.class));
            }
        });

        Button btn_Exit=(Button)findViewById(R.id.btn_Exit);
        btn_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(getResources().getText(R.string.exit).toString());
                builder.setPositiveButton(getResources().getText(R.string.ok).toString(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });
                builder.show();
            }
        });
        Button btn_GetQ = (Button) findViewById(R.id.btn_GetQ);
        btn_GetQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GData.initialFinished) {
                    try {
                        byte divId = 1;
                        String sDiv = edtText_Div.getText().toString();
                        divId = Byte.valueOf(sDiv);
                        QueueInfo queueInfo = Get_NewQueue(divId);
                    }catch (Exception ex){}
                }
            }
        });
        Button btn_ResetQ = (Button) findViewById(R.id.btn_ResetQ);
        btn_ResetQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemMgr.ResetQ_All(MainActivity.this);

            }
        });
        Button btn_ShowTouch = (Button) findViewById(R.id.btn_ShowTouch);
        btn_ShowTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplication(), TouchScreenStyle1FullscreenActivity.class));
                //startActivity(new Intent(getApplication(), QTouchOnWebView.class));
                layout_DebugView.setVisibility(View.GONE);
                layout_QTouchView.setVisibility(View.VISIBLE);
            }
        });
        edtText_Div = (EditText) findViewById(R.id.edtText_Div);
        edtText_Div.setText("1");

        Button btn_Test1 = (Button) findViewById(R.id.btn_Test1);
        btn_Test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Test Display*/
                QInfo qStart = new QInfo();
                qStart.setqType(QInfo.QType.Type_3DG);
                qStart.setqAlp((byte) 0x41);
                qStart.setqNum((short) 1);
                QInfo qEnd = new QInfo();
                qEnd.setqType(QInfo.QType.Type_3DG);
                qEnd.setqAlp((byte) 0x41);
                qEnd.setqNum((short) 9999);
                DisplayMgr.UpdateQueueOnDisplay(qStart, qEnd, (byte) 1);

            }
        });
        Button btn_Test2 = (Button) findViewById(R.id.btn_Test2);
        btn_Test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QInfo qStart = new QInfo();
                qStart.setqType(QInfo.QType.Type_3DG);
                qStart.setqAlp((byte) 0x41);
                qStart.setqNum((short) 1);
                QInfo qEnd = new QInfo();
                qEnd.setqType(QInfo.QType.Type_3DG);
                qEnd.setqAlp((byte) 0x41);
                qEnd.setqNum((short) 999);
                SoundMgr.UpdateCallingQueue(qStart, qEnd, (byte) 1);
            }
        });
        editText_Serial = findViewById(R.id.editText_Serial);
        Button btn_SendData = (Button) findViewById(R.id.btn_SendData);
        btn_SendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte divId = 1;
                String sDiv = edtText_Div.getText().toString();
                divId = Byte.valueOf(sDiv);
                /*Test Request Queue*/
                QTouchService qTouchService = new QTouchService(MainActivity.this);
                qTouchService.Call_REQUEST_QUEUE((byte) divId);

                //TCPClient tcpClient=new TCPClient("10.172.102.136", TCP.Client.TargetPort.QTOUCH);
                //PrinterService.PrintTicket_MaintenanceReportOnQPrint(uart);
                /*TCP Clent Test*/
                /*
                EventBus.getDefault().post(new DebugMessageEvent("tcpClient Send data=1234"));
                tcpClient_TEST=new TCPClient("10.172.102.136",TCP.Client.TargetPort.TEST);
                tcpClient_TEST.SendReceive_Message(tcpClient_TEST.getSocket(),2000,"1234");
                tcpClient_TEST.setOnDataReceivedListener(new TCPClient.OnDataReceivedListener() {
                    @Override
                    public void onDataReceived(Socket socket, String message, byte[] bytes) {
                        EventBus.getDefault().post(new DebugMessageEvent("tcpClient Rev data="+message));
                    }
                });
                */

            }
        });
    }

    /*TCP Server*/
    void TCP_Server_Listener() {

        /*
        updateConversationHandler = new Handler();
        this.serverThread = new Thread(new ServerThread());
        this.serverThread.start();
        */

        tcpServer_Mobile = new TCPServer(TCP.Server.ListenPort.Mobile);
        tcpServer_Mobile.start();
        tcpServer_Mobile.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onConnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("Mobile Connect ip=" + ip));
            }

            @Override
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("Mobile ip=" + ip + " len=" + bytes.length));
                if (GData.initialFinished) {
                    MobileService.Handle_Mobile(tcpServer_Mobile, socket, bytes);
                }
            }

            @Override
            public void onDisconnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("Mobile Disconnect ip=" + ip));
            }
        });

        tcpServer_INNET = new TCPServer(TCP.Server.ListenPort.INNET);
        tcpServer_INNET.start();
        tcpServer_INNET.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onConnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("INNET Connect ip=" + ip));
            }
            @Override
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {

                EventBus.getDefault().post(new DebugMessageEvent("INNET ip="  + ip + " len=" + bytes.length));
                if (GData.initialFinished) {
                    ServerService.Handle_Server_INNET(tcpServer_INNET, socket, bytes);
                }

            }
            @Override
            public void onDisconnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("INNET Disconnect ip=" + ip));
            }
        });

        tcpServer_KEYPAD = new TCPServer(TCP.Server.ListenPort.KEYPAD);
        tcpServer_KEYPAD.start();
        tcpServer_KEYPAD.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onConnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("KEYPAD Connect ip=" + ip));
            }
            @Override
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("KEYPAD ip=" + ip + " len=" + bytes.length));
                Response_DataReceived(MainActivity.this, tcpServer_KEYPAD, socket, ip, message, bytes);
            }
            @Override
            public void onDisconnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("KEYPAD Disconnect ip=" + ip));
            }
        });
        tcpServer_DISPLAY = new TCPServer(TCP.Server.ListenPort.DISPLAY);
        tcpServer_DISPLAY.start();
        tcpServer_DISPLAY.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onConnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("DISPLAY Connect ip=" + ip));
            }
            @Override
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("DISPLAY ip="  + ip + " len=" + bytes.length));
                Response_DataReceived(MainActivity.this, tcpServer_DISPLAY, socket, ip, message, bytes);
            }
            @Override
            public void onDisconnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("DISPLAY Disconnect ip=" + ip));
            }
        });

        tcpServer_QTOUCH = new TCPServer(TCP.Server.ListenPort.QTOUCH);
        tcpServer_QTOUCH.start();
        tcpServer_QTOUCH.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onConnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("QTOUCH Connect ip=" + ip));
            }
            @Override
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("QTOUCH ip=" + ip + " len=" + bytes.length));
                Response_DataReceived(MainActivity.this, tcpServer_QTOUCH, socket, ip, message, bytes);
            }
            @Override
            public void onDisconnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("QTOUCH Disconnect ip=" + ip));
            }
        });

        tcpServer_QDISPLAY = new TCPServer(TCP.Server.ListenPort.QDISPLAY);
        tcpServer_QDISPLAY.start();
        tcpServer_QDISPLAY.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onConnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("QDISPLAY Connect ip=" + ip));
            }
            @Override
            public void onDataReceived(Socket socket,String ip, String message, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("QDISPLAY ip=" + ip + " len=" + bytes.length));
                Response_DataReceived(MainActivity.this, tcpServer_QDISPLAY, socket, ip, message, bytes);
            }
            @Override
            public void onDisconnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("QDISPLAY Disconnect ip=" + ip));
            }
        });

        tcpServer_Periperal = new TCPServer(TCP.Server.ListenPort.Periperal);
        tcpServer_Periperal.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onConnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("Device Connect ip=" + ip));
            }
            @Override
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("Device IP="  + ip + " len=" + bytes.length));
                Response_DataReceived(MainActivity.this, tcpServer_Periperal, socket, ip, message, bytes);
            }
            @Override
            public void onDisconnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("Device Disconnect ip=" + ip));
            }
        });

        tcpServer_SoftKeyCmd=new TCPServer(TCP.Server.ListenPort.SoftkeyCMD);
        tcpServer_SoftKeyCmd.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onConnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("SoftkeyCMD Connect ip=" + ip));
                PeriperalMgr.HandleTcpSocket_Connect(MainActivity.this,ip,socket);
            }
            @Override
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("SoftkeyCMD IP="  + ip + " len=" + bytes.length));
                Response_SoftkeyCmd_DataReceived(MainActivity.this, tcpServer_SoftKeyCmd, socket, ip, message, bytes);
            }
            @Override
            public void onDisconnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("SoftkeyCMD Disconnect ip=" + ip));
                PeriperalMgr.HandleTcpSocket_Disconnect(MainActivity.this,ip,socket);
            }
        });

        tcpServer_SoftKeyUpdate=new TCPServer(TCP.Server.ListenPort.SoftkeyUpdate);
        tcpServer_SoftKeyUpdate.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onConnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("SoftkeyUpdate Connect ip=" + ip));
            }
            @Override
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("SoftkeyUpdate IP="  + ip + " len=" + bytes.length));
                Response_SoftkeyUpdate_DataReceived(MainActivity.this, tcpServer_SoftKeyUpdate, socket, ip,message, bytes);
            }
            @Override
            public void onDisconnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("SoftkeyUpdate Disconnect ip=" + ip));
            }
        });

        tcpServer_Web=new TCPServer(TCP.Server.ListenPort.Web);
        tcpServer_Web.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onConnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("Web Connect ip=" + ip));
            }

            @Override
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("Web IP="  + ip + " len=" + bytes.length+" msg="+message));
                Response_WEB_DataReceived(MainActivity.this,tcpServer_Web,socket,ip,message,bytes);
            }

            @Override
            public void onDisconnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("Web Disconnect ip=" + ip));
            }
        });


        tcpServer_QClient=new TCPServer(TCP.Server.ListenPort.QClientWeb);
        tcpServer_QClient.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onConnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("QClientWeb Connect ip=" + ip));
            }

            @Override
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("QClientWeb IP="  + ip + " len=" + bytes.length+" msg="+message));
                Response_QClientWeb_DataReceived(MainActivity.this,tcpServer_QClient,socket,ip,message,bytes);
            }

            @Override
            public void onDisconnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("QClientWeb Disconnect ip=" + ip));
            }
        });

        /*
        tcpServer_QTouchWeb=new TCPServer(TCP.Server.ListenPort.QTouchWeb);
        tcpServer_QTouchWeb.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onConnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("QTouchWeb Connect ip=" + ip));
            }

            @Override
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("QTouchWeb IP="  + ip + " len=" + bytes.length+" msg="+message));
                Response_QTouchWeb_DataReceived(MainActivity.this,tcpServer_QTouchWeb,socket,ip,message,bytes);
            }

            @Override
            public void onDisconnect(Socket socket, String ip) {
                EventBus.getDefault().post(new DebugMessageEvent("QTouchWeb Disconnect ip=" + ip));
            }
        });
        */

    }

    private void TCPServerStop() {
        if (tcpServer_Mobile != null)
            tcpServer_Mobile.stop();
        if (tcpServer_INNET != null)
            tcpServer_INNET.stop();
        if (tcpServer_KEYPAD != null)
            tcpServer_KEYPAD.stop();
        if (tcpServer_DISPLAY != null)
            tcpServer_DISPLAY.stop();
        if (tcpServer_QTOUCH != null)
            tcpServer_QTOUCH.stop();
        if (tcpServer_QDISPLAY != null)
            tcpServer_QDISPLAY.stop();
        if (tcpServer_Periperal != null)
            tcpServer_Periperal.stop();
        if(tcpServer_SoftKeyCmd!=null)
            tcpServer_SoftKeyCmd.stop();
        if(tcpServer_SoftKeyUpdate!=null)
            tcpServer_SoftKeyUpdate.stop();
        if(tcpServer_Web!=null)
            tcpServer_Web.stop();
        if(tcpServer_QClient!=null)
            tcpServer_QClient.stop();
    }

    /*TCP Data Received*/
    public void Response_DataReceived(Context context, TCPServer tcpServer, Socket socket, String ip,String  message, byte[] bytes) {
        if (!GData.initialFinished)
            return;

        byte cmd = bytes[4];
        byte deviceType = bytes[5];
        byte deviceId = bytes[6];

        switch (deviceType) {
            case Protocol.DeviceType.NONE_DEVICE:
                break;
            case Protocol.DeviceType.HW_KEYPAD:
                AllKeyService.Handle_ALLKey(context, tcpServer, socket, bytes);
                break;
            case Protocol.DeviceType.DISPLAY:

                break;
            case Protocol.DeviceType.QTOUCH:
                QTouchService qTouchService = new QTouchService(MainActivity.this);
                qTouchService.Handle_Server_QTOUCH(MainActivity.this, tcpServer, socket, bytes);
                break;
            case Protocol.DeviceType.QDISPLAY:
                answer(tcpServer,socket,cmd,deviceType,deviceId,Protocol.NACK);
                break;
            case Protocol.DeviceType.SW_KEYPAD:
                AllKeyService.Handle_ALLKey(MainActivity.this, tcpServer, socket, bytes);
                break;
        }
        PeriperalMgr.HandlePeriperalIp(context, deviceType, deviceId, ip,socket);
        PeriperalMgr.HandleTcpSocket(context, deviceType, deviceId, ip,socket);
    }

    private static void answer(TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte ans) {
        byte[] b=Protocol.Answer_Protocol_V2(cmd, Protocol.FrameID.ID2,deviceType,deviceId,ans);
        tcpServer.Send(socket,b);
    }

    public void Response_SoftkeyCmd_DataReceived(Context context, TCPServer tcpServer, Socket socket, String ip, String message, byte[] bytes) {
        if (!GData.initialFinished)
            return;
        byte deviceType = Protocol.DeviceType.SW_KEYPAD;
        byte deviceId = bytes[3];
        byte cmd=bytes[4];

        AllSoftkeyService.Handle_ALLSoftkeyCmd(context,tcpServer,socket,bytes);
        PeriperalMgr.HandlePeriperalIp(context, deviceType, deviceId, ip,socket);
        PeriperalMgr.HandleTcpSocket(context, deviceType, deviceId, ip,socket);
    }

    public void Response_SoftkeyUpdate_DataReceived(Context context, TCPServer tcpServer, Socket socket, String ip,String  message ,byte[] bytes) {
        if (!GData.initialFinished)
            return;

        byte deviceType = Protocol.DeviceType.SW_KEYPAD;
        byte deviceId = bytes[3];
        byte cmd=bytes[4];

        AllSoftkeyService.Handle_ALLSoftkeyUpdate(context,tcpServer,socket,bytes);
        PeriperalMgr.HandlePeriperalIp(context, deviceType, deviceId, ip,socket);
        PeriperalMgr.HandleTcpSocket(context, deviceType, deviceId, ip,socket);
    }

    public void Response_WEB_DataReceived(Context context, TCPServer tcpServer, Socket socket, String ip,String  message, byte[] bytes) {

        if (!GData.initialFinished)
            return;

        //String content="Hello World 555";
        String path="file:///android_asset/sample.html";

        String content="<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">\n" +
                "<html>\n" +
                "<head>\n" +
                "  <title>My first styled page</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<!-- Site navigation menu -->\n" +
                "<ul class=\"navbar\">\n" +
                "  <li><a href=\"index.html\">Home page</a>\n" +
                "  <li><a href=\"musings.html\">Musings</a>\n" +
                "  <li><a href=\"town.html\">My town</a>\n" +
                "  <li><a href=\"links.html\">Links</a>\n" +
                "</ul>\n" +
                "\n" +
                "<!-- Main content -->\n" +
                "<h1>My first styled page</h1>\n" +
                "\n" +
                "<p>Welcome to my styled page!\n" +
                "\n" +
                "<p>It lacks images, but at least it has style.\n" +
                "And it has links, even if they don't go\n" +
                "anywhere&hellip;\n" +
                "\n" +
                "<p>There should be more here, but I don't know\n" +
                "what yet.\n" +
                "\n" +
                "<!-- Sign and date the page, it's only polite! -->\n" +
                "<address>Made 5 April 2004<br>\n" +
                "  by myself.</address>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

        //String sHTML = "HTTP/1.0 200 OK "+ Environment.NewLine+ "Content-Type: text/plain; charset=UTF-8"+ Environment.NewLine+ "Content-Length: " + content.length()+ Environment.NewLine+ Environment.NewLine+ content;
        String sHTML = "HTTP/1.0 200 OK "+ "\r\n"+ "Content-Type: text/html; charset=UTF-8"+ "\r\n"+ "Content-Length: " + content.length()+ "\r\n"+ "\r\n"+ content;

        InputStream is = null;
        int size = 0;
        try {
            is = getAssets().open("Qclient.html");
            size = is.available();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buffer = new byte[size];
        try {
            is.read(buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String content2 = new String(buffer);

        String sHTML2 = "HTTP/1.0 200 OK "+ "\r\n"+ "Content-Type: text/html; charset=UTF-8"+ "\r\n"+ "Content-Length: " + content2.length()+ "\r\n"+ "\r\n"+ content2;

        byte[] b=null;
        try {
            b = sHTML2.getBytes("UTF-8");
        }catch (Exception ex){

        }

        EventBus.getDefault().post(new DebugMessageEvent("Web len=" + b.length));
        if(b!=null)
           tcpServer.Send(socket, b);
    }

    public void Response_QClientWeb_DataReceived(Context context, TCPServer tcpServer, Socket socket, String ip,String  message, byte[] bytes) {

        if (!GData.initialFinished)
            return;

        String fileName="Qclient.html";
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

        String sHTML = "HTTP/1.0 200 OK "+ "\r\n"+ "Content-Type: text/html; charset=UTF-8"+ "\r\n"+ "Content-Length: " + content.length()+ "\r\n"+ "\r\n"+ content;

        byte[] b=null;
        try {
            b = sHTML.getBytes("UTF-8");
        }catch (Exception ex){

        }

        EventBus.getDefault().post(new DebugMessageEvent("Web len=" + b.length));
        if(b!=null)
            tcpServer.Send(socket, b);
    }

    public void Response_QTouchWeb_DataReceived(Context context, TCPServer tcpServer, Socket socket, String ip,String  message, byte[] bytes) {

        if (!GData.initialFinished)
            return;
        //file:///android_asset/QTouchWeb/Style2/QTouch.html
        String fileName="QTouchWeb/Style1/QTouch.html";
        //String fileName="QTouch.html";
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

        String sHTML = "HTTP/1.0 200 OK "+ "\r\n"+ "Content-Type: text/html; charset=UTF-8"+ "\r\n"+ "Content-Length: " + content.length()+ "\r\n"+ "\r\n"+ content;

        byte[] b=null;
        try {
            b = sHTML.getBytes("UTF-8");
        }catch (Exception ex){

        }

        EventBus.getDefault().post(new DebugMessageEvent("QTouchWeb len=" + b.length));
        if(b!=null)
            tcpServer.Send(socket, b);
    }

    /*UDP Server*/
    void UDP_Server_Listener() {
        udpServer_SetTicket=new UDPServer(UDP.Server.ListenPort.SetTicket);
        udpServer_SetTicket.setOnDataReceivedListener(new UDPServer.OnDataReceivedListener() {
            @Override
            public void onDataReceived(DatagramSocket socket, DatagramPacket packet, String message, byte[] bytes, String ip, int port) {
                EventBus.getDefault().post(new DebugMessageEvent("udpServer_SetTicket="+message +" len="+bytes.length +" ip="+ip+":"+port));
                UDP_Response_SetTicket(MainActivity.this,udpServer_SetTicket,socket,packet,bytes);
            }
        });
        udpServer_PrintTicket=new UDPServer(UDP.Server.ListenPort.PrintTicket);
        udpServer_PrintTicket.setOnDataReceivedListener(new UDPServer.OnDataReceivedListener() {
            @Override
            public void onDataReceived(DatagramSocket socket, DatagramPacket packet, String message, byte[] bytes, String ip, int port) {
                EventBus.getDefault().post(new DebugMessageEvent("udpServer_PrintTicket="+message +" len="+bytes.length +" ip="+ip+":"+port));
                UDP_Response_PrintTicket(MainActivity.this,udpServer_PrintTicket,socket,packet,bytes);
            }
        });
    }
    /*UDP Data Received*/
    public void UDP_Response_SetTicket(Context context,final UDPServer udpServer,final DatagramSocket socket,final DatagramPacket packet,byte[] bytes){

        if(bytes==null) return;
        if(bytes.length==0) return;

        int idx=0;
        byte[] b=null;

        Integer nBytes=0;

        final byte fCode = (byte) bytes[3];
        final byte addr = (byte) bytes[4];
        final byte cmdcode = (byte) bytes[5];
        switch (cmdcode) {
            case Protocol.QPRINT_CMD.SETTICKET:
                final byte dataCode = (byte) bytes[6];
                /*
                int bh=(int)((byte)bytes[1] * 256);
                int bl=(int)bytes[2];

                nBytes =  (int)(bh + bl);
                nBytes =(int)(nBytes- 3);
                //nBytes = (int)((int)(((byte)bytes[1] * 256) + (byte)bytes[2]) - 3);
                Integer len=nBytes+6;
                */

                int len=0;
                nBytes=(int) Convert.GetShort(bytes[1], bytes[2]);
                len=nBytes+3;
                byte[] sbytes = new byte[len];

                sbytes[0] = Protocol.STX;
                sbytes[1] = addr;
                sbytes[2] = (byte) ((nBytes >> 8)&0xFF);
                sbytes[3] = (byte) (nBytes&0xFF);
                int sIdx=4;
                idx=6;
                for (int i = 0; i < (len-6); i++) {
                    if(idx>=bytes.length) break;
                    sbytes[sIdx++] = (byte)bytes[idx++];
                }

                sbytes[2] = (byte) (((sIdx-4) >> 8)&0xFF);
                sbytes[3] = (byte) ((sIdx-4)&0xFF);

                byte sum = 0x00;
                for (int i = 1; i < sIdx; i++)
                    sum ^= (byte) sbytes[i];

                sbytes[sIdx++]  = sum;
                sbytes[sIdx++]  = Protocol.EOT;


                GPIOService.Enable_Uart_QPrint();
                new PrinterService().SendFrame_ExtPrinter(uart, sbytes, new PrinterService.ResponseDataListener() {
                    @Override
                    public void onResponseDataResult(byte frameStatus, byte[] bytes) {
                        int idx=0;
                        byte[] b=null;
                        byte[] sbytes = new byte[1024 * 1];
                        boolean isCorrect=true;

                        if(frameStatus==Protocol.FRAME_STATUS.CORRECT)
                        {
                            if(bytes[4] != dataCode) {
                                isCorrect=false;
                            }else {
                                if (dataCode == Protocol.QPRINT_SUBCMD.PRINT_FlashButton) {
                                    if (bytes[6] != Protocol.QPRINT_RES_CODE.SUCCESS) {
                                        isCorrect=false;
                                    }
                                }
                                else {
                                    if (bytes[7] != Protocol.QPRINT_RES_CODE.SUCCESS) {
                                        isCorrect=false;
                                    }
                                }
                            }

                        }else {
                            isCorrect=false;
                        }
                        //isCorrect=true;
                        //---send ack to server
                        if(isCorrect) {
                            idx=0;
                            sbytes[idx++] = addr;
                            sbytes[idx++] = cmdcode;
                            sbytes[idx++] = Protocol.ACK;
                            b = Arrays.copyOf(sbytes, idx);
                            udpServer.Send(socket,packet,Protocol.PrepareData_Protocol_V5(fCode, b));
                        }else {
                            idx=0;
                            sbytes[idx++] = addr;
                            sbytes[idx++] = cmdcode;
                            sbytes[idx++] = Protocol.NACK;
                            b = Arrays.copyOf(sbytes, idx);
                            udpServer.Send(socket,packet,Protocol.PrepareData_Protocol_V5(fCode, b));
                        }
                    }
                });

                break;

            default:
                //nBytes = ((bytes[1] << 8) | bytes[2]) - 2;
                nBytes = (int)((int)((bytes[1] * 256) + bytes[2]) - 2);
                bytes[0] = Protocol.STX;
                bytes[1] = addr;
                bytes[2] = (byte) ((nBytes >> 8)&0xFF);
                bytes[3] = (byte) (nBytes&0xFF);
                for (int i = 0; i < nBytes; i++)
                    bytes[4+i] = bytes[5+i];
                sum = 0x00;
                for (int i = 1; i < (nBytes+4); i++)
                    sum ^= bytes[i];
                bytes[nBytes+4] = sum;
                bytes[nBytes+5] =Protocol.EOT;
                GPIOService.Enable_Uart_QPrint();
                new PrinterService().SendFrame_ExtPrinter(uart, bytes, new PrinterService.ResponseDataListener() {
                            @Override
                            public void onResponseDataResult(byte frameStatus, byte[] bytes) {
                                int idx=0;
                                byte[] b=null;
                                byte[] sbytes = new byte[1024 * 1];
                                boolean isCorrect=true;
                                if(frameStatus==Protocol.FRAME_STATUS.CORRECT)
                                {
                                    if ((bytes[1] != addr) || (bytes[4] != cmdcode) || (bytes[5] != Protocol.ACK))
                                    {
                                        isCorrect=false;
                                    }
                                    else
                                    {
                                        isCorrect=true;
                                    }
                                }else {
                                    isCorrect=false;
                                }

                                //---send ack to server
                                if(isCorrect) {
                                    idx=0;
                                    sbytes[idx++] = addr;
                                    sbytes[idx++] = cmdcode;
                                    sbytes[idx++] = Protocol.ACK;
                                    b = Arrays.copyOf(sbytes, idx);
                                    udpServer.Send(socket,packet,Protocol.PrepareData_Protocol_V5(fCode, b));
                                }else {
                                    idx=0;
                                    sbytes[idx++] = addr;
                                    sbytes[idx++] = cmdcode;
                                    sbytes[idx++] = Protocol.NACK;
                                    b = Arrays.copyOf(sbytes, idx);
                                    udpServer.Send(socket,packet,Protocol.PrepareData_Protocol_V5(fCode, b));
                                }
                            }
                        });

                break;
        }

    }
    public void UDP_Response_PrintTicket(Context context,final UDPServer udpServer,final DatagramSocket socket,final DatagramPacket packet,byte[] bytes){

        if(bytes==null) return;
        if(bytes.length==0) return;

        int idx=0;
        int maxSizeBuffer=(1024*2);
        int nBytes=0;
        byte[] sbytes=new  byte[maxSizeBuffer];

        final byte addr = bytes[3];
        final byte cmd = bytes[4];
        if (cmd == Protocol.QPRINT_CMD.PING)
        {
            sbytes = new byte[1024 * 1];
            byte[] b=null;
            idx=0;
            sbytes[idx++] = Protocol.ACK;
            b = Arrays.copyOf(sbytes, idx);
            udpServer.Send(socket,packet,Protocol.PrepareData_Protocol_V4(cmd,addr, b));
            return;
        }

        nBytes = (int)((int)((bytes[1] * 256) + bytes[2]) - 1);
        if (nBytes > maxSizeBuffer) {
            return;
        }

        bytes[0] = Protocol.STX;
        bytes[1] = addr;
        bytes[2] = (byte) ((nBytes >> 8)&0xFF);
        bytes[3] = (byte) (nBytes&0xFF);

        nBytes = nBytes + 6;
        byte sum = 0x00;
        for (int i = 1; i < (nBytes-2); i++)
            sum ^= bytes[i];

        bytes[nBytes-2] = sum;
        bytes[nBytes-1] = Protocol.EOT;

        new PrinterService().SendFrame_ExtPrinter(uart, bytes, new PrinterService.ResponseDataListener() {
            @Override
            public void onResponseDataResult(byte frameStatus, byte[] bytes) {
                int idx=0;
                byte[] b=null;
                byte[] sbytes = new byte[1024 * 1];
                boolean isCorrect=true;

                if(frameStatus==Protocol.FRAME_STATUS.CORRECT)
                {
                    isCorrect=true;

                }else
                {
                    isCorrect=false;
                }
                //---send ack to server
                if(isCorrect) {
                    idx=0;
                    sbytes[idx++] = Protocol.ACK;
                    b = Arrays.copyOf(sbytes, idx);
                    udpServer.Send(socket,packet,Protocol.PrepareData_Protocol_V4(cmd,addr, b));
                }else {
                    idx=0;
                    sbytes[idx++] = Protocol.NACK;
                    b = Arrays.copyOf(sbytes, idx);
                    udpServer.Send(socket,packet,Protocol.PrepareData_Protocol_V4(cmd,addr, b));
                }
            }
        });

    }
    /*Start&Stop Service*/
    void Start_Service() {
        startService(new Intent(MainActivity.this, SoundService.class));
        startService(new Intent(MainActivity.this, DisplayService.class));
        startService(new Intent(MainActivity.this, RealtimeService.class));
        startService(new Intent(MainActivity.this, QDisplayService.class));
        startService(new Intent(MainActivity.this, AllKeyService.class));
        startService(new Intent(MainActivity.this, AllSoftkeyService.class));
        startService(new Intent(MainActivity.this, QClientOnWebService.class));
        //startService(new Intent(MainActivity.this, QTouchOnWebService.class));

    }

    void Stop_Service() {
        stopService(new Intent(MainActivity.this, SoundService.class));
        stopService(new Intent(MainActivity.this, DisplayService.class));
        stopService(new Intent(MainActivity.this, RealtimeService.class));
        stopService(new Intent(MainActivity.this, QDisplayService.class));
        stopService(new Intent(MainActivity.this, AllKeyService.class));
        stopService(new Intent(MainActivity.this,AllSoftkeyService.class));
        stopService(new Intent(MainActivity.this, QClientOnWebService.class));
        //stopService(new Intent(MainActivity.this, QTouchOnWebService.class));
    }

    /*Print Ticket*/
    void Process_PrintQueue() {
        if (checking_WillPrintQueue()) {
            GPIOService.Enable_Uart_QPrint();
            Integer retry = 3;
            while (retry-- > 0) {
                EventBus.getDefault().post(new DebugMessageEvent("Printing Queue...retry=" + retry));
                if (uart != null) {
                    printerService.PrintQTicketOnQPrint(uart, GData.qTicketInfo.getDivId(), GData.qTicketInfo, printerService);
                    printerService.setOnDataReceivedListener(new PrinterService.OnDataReceivedListener() {
                        @Override
                        public void onDataReceived(boolean status) {
                            EventBus.getDefault().post(new DebugMessageEvent("Get Ticket status=" + status));
                        }

                        @Override
                        public void onReceiveFail() {
                        }
                    });
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            GData.qTicketInfo.setActivePrint(false);
        }
    }

    boolean checking_WillPrintQueue() {
        if (GData.qTicketInfo != null) {
            if ((GData.qTicketInfo.getCopy() > 0) && GData.qTicketInfo.isActivePrint()) {
                return true;
            }
        }
        return false;
    }

    boolean delayMs_CheckPrintQ(Integer ms) {
        try {
            Thread.sleep(1);
            if (checking_WillPrintQueue()) {
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public QueueInfo Get_NewQueue(byte div) {
        QueueInfo queueInfo = QueueMgr.GetNewQueue(MainActivity.this, div);
        EventBus.getDefault().post(new DebugMessageEvent("GetQ div=" + div));
        EventBus.getDefault().post(new DebugMessageEvent("Q.Size=" + GData.Queue.size() + " div=" + div + " Q=" + queueInfo.getQueueNo()));
        return queueInfo;
    }

    /*Show Data on View*/
    public void showDataOnView() {
        //Profile Info
        if (GData.branchInfos != null)
            EventBus.getDefault().post(new DebugMessageEvent("BranchInfo.Size=" + GData.branchInfos.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("BranchInfo is null"));

        if (GData.divInfos != null)
            EventBus.getDefault().post(new DebugMessageEvent("DivInfo.Size=" + GData.divInfos.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("DivInfo is null"));

        if (GData.langDivInfos != null)
            EventBus.getDefault().post(new DebugMessageEvent("LangDivInfo.Size=" + GData.langDivInfos.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("LangDivInfo is null"));

        if (GData.staInfos != null)
            EventBus.getDefault().post(new DebugMessageEvent("StaInfo.Size=" + GData.staInfos.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("StaInfo is null"));

        if (GData.breakInfos != null)
            EventBus.getDefault().post(new DebugMessageEvent("BreakInfo.Size=" + GData.breakInfos.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("BreakInfo is null"));

        if (GData.grpInfos != null)
            EventBus.getDefault().post(new DebugMessageEvent("GrpInfo.Size=" + GData.grpInfos.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("GrpInfo is null"));

        if (GData.userInfos != null)
            EventBus.getDefault().post(new DebugMessageEvent("UserInfo.Size=" + GData.userInfos.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("UserInfo is null"));

        if (GData.employeeInfos != null)
            EventBus.getDefault().post(new DebugMessageEvent("EmployeeInfo.Size=" + GData.employeeInfos.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("EmployeeInfo is null"));

        if (GData.divisionAdvInfos != null)
            EventBus.getDefault().post(new DebugMessageEvent("DivisionAdvInfo.Size=" + GData.divisionAdvInfos.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("DivisionAdvInfo is null"));

        if (GData.pf_others != null)
            EventBus.getDefault().post(new DebugMessageEvent("PF_OTHER.Size=" + GData.pf_others.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("PF_OTHER is null"));

        if (GData.pf_divisions != null)
            EventBus.getDefault().post(new DebugMessageEvent("PF_DIVISION.Size=" + GData.pf_divisions.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("PF_DIVISION is null"));

        if (GData.pf_autotransfers != null)
            EventBus.getDefault().post(new DebugMessageEvent("PF_AUTOTRANSFER.Size=" + GData.pf_autotransfers.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("PF_AUTOTRANSFER is null"));

        if (GData.pf_divmaps != null)
            EventBus.getDefault().post(new DebugMessageEvent("PF_DIVMAP.Size=" + GData.pf_divmaps.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("PF_DIVMAP is null"));

        if (GData.pf_stamaps != null)
            EventBus.getDefault().post(new DebugMessageEvent("PF_STAMAP.Size=" + GData.pf_stamaps.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("PF_STAMAP is null"));

        if (GData.pf_alarmgroups != null)
            EventBus.getDefault().post(new DebugMessageEvent("PF_ALARMGROUP.Size=" + GData.pf_alarmgroups.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("PF_ALARMGROUP is null"));

        //Current Info
        if (GData.branchStatusInfos != null)
            EventBus.getDefault().post(new DebugMessageEvent("BranchStatus.Size=" + GData.branchStatusInfos.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("BranchStatus is null"));


        if (GData.Queuelog != null)
            EventBus.getDefault().post(new DebugMessageEvent("Queuelog.Size=" + GData.Queuelog.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("Queuelog is null"));

        if (GData.QLaunching != null)
            EventBus.getDefault().post(new DebugMessageEvent("QLaunching.Size=" + GData.QLaunching.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("QLaunching is null"));

        if (GData.Division != null)
            EventBus.getDefault().post(new DebugMessageEvent("Division.Size=" + GData.Division.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("Division is null"));

        ViewLogMgr.ShowQueue();
        ViewLogMgr.ShowUserlog();
        ViewLogMgr.ShowCounterlog();
        ViewLogMgr.ShowPeriperal();
        ViewLogMgr.ShowStaMapGroup();
        ViewLogMgr.ShowDivMapGroup();
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
            //Toast.makeText(mContext, "PrintTicket div="+sDivID, Toast.LENGTH_SHORT).show();
            Toast.makeText(mContext, "กรุณารับบัตรคิวค่ะ", Toast.LENGTH_SHORT).show();
            QTouchService qTouchService=new QTouchService(getApplicationContext());
            qTouchService.Call_REQUEST_QUEUE((byte) divId);
        }
    }
    /*SQLite*/
    void Test_SQLite() {
        dbMgr.open();
        dbMgr.Delete.TB_Test(null);
        dbMgr.insert_("Name1","DescName1");
        dbMgr.insert_("Name2","DescName2");
        EventBus.getDefault().post(new DebugMessageEvent("#dbMgr:Name1"));
        EventBus.getDefault().post(new DebugMessageEvent("#dbMgr:Name2"));
        QMSDB.TB_Test m=new QMSDB.TB_Test();
        m.setSUBJECT("Name3");
        m.setDESC("DescName3");
        dbMgr.Insert.TB_Test(m);
        QMSDB.TB_Test m2=new QMSDB.TB_Test();
        m2.setID(42);
        m2.setSUBJECT("Name2_1");
        m2.setDESC("DescName2_1");
        dbMgr.Update.TB_Test(m2);
        List<QMSDB.TB_Test> list=dbMgr.get_TB_Test_List();
        EventBus.getDefault().post(new DebugMessageEvent("#dbMgr:list="+list.size()));
        for (QMSDB.TB_Test l : list) {
            EventBus.getDefault().post(new DebugMessageEvent("#dbMgr:ID="+l.getID()+" SUBJECT="+l.getSUBJECT()+" Desc="+l.getDESC()));
        }
    }
    /*Initial Thread*/
    Thread CallInitialThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "InitialThread Run...");
            EventBus.getDefault().post(new DebugMessageEvent("====InitialThread Run==="));
            /*Call API from Server*/
            APIMgr.Call_API_Profile(MainActivity.this);
            /*Waiting Load Profile from Server*/
            Integer timeOutSec = 15; //Timeout wait API Server 15Sec
            long start_Sec = System.currentTimeMillis() / 1000;
            while (true) {
                Log.d(TAG, "InitialThread Run API Index=" + GData.callAPI_Index);
                //EventBus.getDefault().post(new DebugMessageEvent("InitialThread Run API Index="+callAPI_Index));
                long cur_Sec = System.currentTimeMillis() / 1000;
                if ((cur_Sec - start_Sec) > timeOutSec) {
                    break;
                }
                if (GData.callAPI_Index >= 15) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!ExternalStorage.isExternalStorageAvailable() || ExternalStorage.isExternalStorageReadOnly()) {
                EventBus.getDefault().post(new DebugMessageEvent("ExternalStorage is not OK!"));
            } else {
                EventBus.getDefault().post(new DebugMessageEvent("ExternalStorage is OK"));
            }
            InitializeMgr.Start(MainActivity.this);
            showDataOnView();
            SystemMgr.CheckResetQueue(MainActivity.this);

            EventBus.getDefault().post(new DebugMessageEvent("===InitialThread Finished==="));
            EventBus.getDefault().post(new DebugMessageEvent("MainThread Start..."));
            callMainThread = new Thread(CallMainThread);
            callMainThread.start();
            callCommunicateUartThread = new Thread(CallCommunicateUartThread);
            callCommunicateUartThread.start();
            GData.initialFinished = true;
            /*Start Service*/
            Start_Service();
        }
    });
    /*Main Thread*/
    Thread CallMainThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Integer ProcessIndex = 0;
            long curMs = System.currentTimeMillis();
            long startMs = System.currentTimeMillis();
            Integer intervalMs_ShowRunning = 10000;
            boolean toggle_LedStatus = false;
            while (true) {
                if (toggle_LedStatus) {
                    toggle_LedStatus = false;
                    GPIOService.ON_LED_1();
                    GPIOService.ON_LED_2();
                    GPIOService.ON_LED_3();
                } else {
                    toggle_LedStatus = true;
                    GPIOService.OFF_LED_1();
                    GPIOService.OFF_LED_2();
                    GPIOService.OFF_LED_3();
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                curMs = System.currentTimeMillis();
                if (curMs - startMs >= intervalMs_ShowRunning) {
                    startMs = System.currentTimeMillis();
                    //Log.d(TAG, "MainThread Run...");
                    EventBus.getDefault().post(new DebugMessageEvent("MainThread Run..." + ProcessIndex));
                    if (ProcessIndex++ > 100)
                        ProcessIndex = 0;

                }
                //---Check Reset Queue
                if (SystemMgr.CheckResetQueue(MainActivity.this)) {
                    showDataOnView();
                }

                //---UpdateWaitingQueue
                if(QueueMgr.IsUpdateWaitingQ) {
                    QueueMgr.UpdateWaitingQueue();
                    QueueMgr.IsUpdateWaitingQ=false;
                }
                if(QueueMgr.IsUpdateQStatus)
                {
                    QueueMgr.cal_Qstat();
                    QueueMgr.IsUpdateQStatus=false;
                }
            }
        }
    });

    /*CallCommunicateUartThread Thread*/
    Thread CallCommunicateUartThread = new Thread(new Runnable() {
        @Override
        public void run() {

            Integer ProcessIndex = 0;
            long curMs = System.currentTimeMillis();
            long startMs = System.currentTimeMillis();
            Integer intervalMs_ShowRunning = 10000;
            long startMs_QPrint = System.currentTimeMillis();
            Integer intervalMs_scan_QPrint = 8000;
            long startMs_QSound = System.currentTimeMillis();
            Integer intervalMs_scan_QSound = 5000;
            ReentrantLock lock = new ReentrantLock();
            while (true) {
                /*Communicate Printer for Printing Queue (First Piority)*/
                lock.lock();
                try {
                    Process_PrintQueue();
                } finally {
                    lock.unlock();
                }
                if (delayMs_CheckPrintQ(200)) {
                    continue;
                }
                curMs = System.currentTimeMillis();
                if (curMs - startMs >= intervalMs_ShowRunning) {
                    startMs = System.currentTimeMillis();
                    //Log.d(TAG, "MainThread Run...");
                    EventBus.getDefault().post(new DebugMessageEvent("CommunicateUartThread Run..." + ProcessIndex));
                    if (ProcessIndex++ > 100)
                        ProcessIndex = 0;
                }
                /*Communicate Printer for Event Paper Alarm*/
                curMs = System.currentTimeMillis();
                if (curMs - startMs_QPrint >= intervalMs_scan_QPrint) {
                    try {
                        GPIOService.Enable_Uart_QPrint();
                        if (uart != null)
                            printerService.CommunicatePrinter(uart, printerService);
                    } catch (Exception ex) {
                    }
                    startMs_QPrint = System.currentTimeMillis();
                }
                if (delayMs_CheckPrintQ(300)) {
                    continue;
                }
                /*Communicate QSound for PlaySound*/
                curMs = System.currentTimeMillis();
                if (curMs - startMs_QSound >= intervalMs_scan_QSound) {
                    GPIOService.Enable_Uart_QSound();
                    //if(uart!=null)
                    //soundService.CommunicateQSound(uart);
                    startMs_QSound = System.currentTimeMillis();
                }
            }
        }
    });

}
