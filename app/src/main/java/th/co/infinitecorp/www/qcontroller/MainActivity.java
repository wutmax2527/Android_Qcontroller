package th.co.infinitecorp.www.qcontroller;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.Socket;

import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QueueInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.GPIO.GPIOService;
import th.co.infinitecorp.www.qcontroller.Management.APIMgr;
import th.co.infinitecorp.www.qcontroller.Management.DisplayMgr;
import th.co.infinitecorp.www.qcontroller.Management.InitializeMgr;
import th.co.infinitecorp.www.qcontroller.Management.QueueMgr;
import th.co.infinitecorp.www.qcontroller.Management.SoundMgr;
import th.co.infinitecorp.www.qcontroller.Management.SystemMgr;
import th.co.infinitecorp.www.qcontroller.Management.ViewLogMgr;
import th.co.infinitecorp.www.qcontroller.Screen.QTouch.TouchScreenStyle1FullscreenActivity;
import th.co.infinitecorp.www.qcontroller.Screen.Setting.SettingScreen;
import th.co.infinitecorp.www.qcontroller.Service.AllKeyService;
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
import th.co.infinitecorp.www.qcontroller.Uart.Uart;
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

    /*Event*/
    private EditText edtText_Div;
    private EditText editText_Serial;

    /*TCP Server*/
    private TCPServer tcpServer_Mobile;
    private TCPServer tcpServer_INNET;
    private TCPServer tcpServer_KEYPAD;
    private TCPServer tcpServer_DISPLAY;
    private TCPServer tcpServer_QTOUCH;
    private TCPServer tcpServer_QDISPLAY;
    private TCPServer tcpServer_Periperal;

    /*TCP Client*/
    private TCPClient tcpClient_TEST;
    private TCPClient tcpClient_KEYPAD;
    private TCPClient tcpClient_DISPLAY;
    private TCPClient tcpClient_QTOUCH;
    private TCPClient tcpClient_QDISPLAY;

    //Uart Q-Print&QSound
    private Uart uart = null;//new Uart(19200);
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

        // Check Police Manager service and admin device.
        devicePolicyManager = (DevicePolicyManager) getSystemService( getBaseContext().DEVICE_POLICY_SERVICE );
        ComponentName mDpm = new ComponentName( this,MyAdmin.class );

        if(!devicePolicyManager.isAdminActive( mDpm )){
            Toast.makeText( this,getString( R.string.not_device_admin ),Toast.LENGTH_SHORT ).show();
        }
        if(devicePolicyManager.isDeviceOwnerApp( this.getPackageName() )){
            devicePolicyManager.setLockTaskPackages( mDpm, new String[]{getPackageName()} );
        }else {
            Toast.makeText( this,getString( R.string.not_device_owner ),Toast.LENGTH_SHORT ).show();
        }

        // Set start kioskmode.
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

        GPIOService.Init();

        /*Call Initial Start Thread*/
        callInitialThread = new Thread(CallInitialThread);
        callInitialThread.start();

        /*Show Touch Screen*/
        //startActivity(new Intent(getApplication(), TouchScreenStyle1.class));

        /*TCP Server*/
        TCP_Server_Listener();

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
        if(mIsKioskEnabled){
            mIsKioskEnabled = false;
            dialogCheckPassword();
        }else {
            mIsKioskEnabled = true;
            enableKioskMode(true);
            setImmersiveMode(mIsKioskEnabled);
        }
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

    public void enableKioskMode(boolean enabled){
        try{
            if(enabled){
                if(devicePolicyManager.isLockTaskPermitted( this.getPackageName() )){
                    startLockTask();
                    mIsKioskEnabled = true;
                    //tvVersionName.setTextColor( getColor( R.color.blue_500 ) );
                }else{
                    Toast.makeText( this,getString( R.string.kiosk_not_permitted ),Toast.LENGTH_SHORT ).show();
                }
            }else{
                stopLockTask();
                mIsKioskEnabled = false;
                //tvVersionName.setTextColor( getColor( R.color.white ) );
            }
        } catch (Exception e){

        }
    }
    public void setImmersiveMode(Boolean enable){
        if(enable){
            final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.KEEP_SCREEN_ON
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            // Set the app into full screen mode
            getWindow().getDecorView().setSystemUiVisibility( flags );
        }else{
            final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.KEEP_SCREEN_ON
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            getWindow().getDecorView().setSystemUiVisibility( flags );
        }
    }
    void dialogCheckPassword(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder( MainActivity.this );
        alertDialog.setTitle( "PASSWORD" );
        alertDialog.setMessage( "Enter Password" );

        final EditText edPassword = new EditText( MainActivity.this );
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT );
        edPassword.setLayoutParams( layoutParams );
        alertDialog.setView( edPassword );

        alertDialog.setPositiveButton( "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = edPassword.getText().toString();
                if(password.compareTo( "12345678" ) == 0){
                    Log.d(TAG, "Pass Word Pass");
                    enableKioskMode(false);
                }
            }
        } );
        alertDialog.show();
    }
    void Init_Event() {
        Button btn_ClearDebug = (Button) findViewById(R.id.btn_ClearDebug);
        btn_ClearDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageDebug = "";
                messageCountLine = 0;
                tv_MessageDebug.setText(messageDebug);
            }
        });
        Button btn_Setting = (Button) findViewById(R.id.btn_Setting);
        btn_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), SettingScreen.class));
                /*
                PlaylistInfo p=new PlaylistInfo();
                p.setType(1);
                p.setQueue("1234");
                p.setCounter(1);
                SoundService.AddToPlayList(p);
                */
            }
        });

        Button btn_GetQ = (Button) findViewById(R.id.btn_GetQ);
        btn_GetQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GData.initialFinished) {
                    byte divId = 1;
                    String sDiv = edtText_Div.getText().toString();
                    divId = Byte.valueOf(sDiv);
                    QueueInfo queueInfo = Get_NewQueue(divId);
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
                startActivity(new Intent(getApplication(), TouchScreenStyle1FullscreenActivity.class));
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

                /*Test Request Queue*/
                QTouchService qTouchService = new QTouchService(MainActivity.this);
                qTouchService.Call_REQUEST_QUEUE((byte) 1);

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
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("Mobile ip=" + ip + " data=" + message.trim() + " len=" + bytes.length));
                if (GData.initialFinished) {
                    MobileService.Handle_Mobile(tcpServer_Mobile, socket, bytes);
                }
            }
        });
        tcpServer_INNET = new TCPServer(TCP.Server.ListenPort.INNET);
        tcpServer_INNET.start();
        tcpServer_INNET.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {

                EventBus.getDefault().post(new DebugMessageEvent("INNET ip=" + ip + " data=" + message.trim() + " len=" + bytes.length));
                if (GData.initialFinished) {
                    ServerService.Handle_Server_INNET(tcpServer_INNET, socket, bytes);
                }

            }
        });

        tcpServer_KEYPAD = new TCPServer(TCP.Server.ListenPort.KEYPAD);
        tcpServer_KEYPAD.start();
        tcpServer_KEYPAD.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onDataReceived(Socket socket, String message, String ip, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("KEYPAD ip=" + ip + " data=" + message.trim() + " len=" + bytes.length));
                Response_DataReceived(MainActivity.this, tcpServer_QTOUCH, socket, ip, message, bytes);
            }
        });
        tcpServer_DISPLAY = new TCPServer(TCP.Server.ListenPort.DISPLAY);
        tcpServer_DISPLAY.start();
        tcpServer_DISPLAY.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onDataReceived(Socket socket, String message, String ip, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("DISPLAY ip=" + ip + " data=" + message.trim() + " len=" + bytes.length));
                Response_DataReceived(MainActivity.this, tcpServer_QTOUCH, socket, ip, message, bytes);
            }
        });

        tcpServer_QTOUCH = new TCPServer(TCP.Server.ListenPort.QTOUCH);
        tcpServer_QTOUCH.start();
        tcpServer_QTOUCH.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("QTOUCH ip=" + ip + " data=" + message.trim() + " len=" + bytes.length));
                Response_DataReceived(MainActivity.this, tcpServer_QTOUCH, socket, ip, message, bytes);
            }
        });

        tcpServer_QDISPLAY = new TCPServer(TCP.Server.ListenPort.QDISPLAY);
        tcpServer_QDISPLAY.start();
        tcpServer_QDISPLAY.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onDataReceived(Socket socket, String message, String ip, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("QDISPLAY ip=" + ip + " data=" + message.trim() + " len=" + bytes.length));
                Response_DataReceived(MainActivity.this, tcpServer_QTOUCH, socket, ip, message, bytes);
            }
        });

        tcpServer_Periperal = new TCPServer(TCP.Server.ListenPort.Periperal);
        tcpServer_Periperal.setOnDataReceivedListener(new TCPServer.OnDataReceivedListener() {
            @Override
            public void onDataReceived(Socket socket, String ip, String message, byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("Device IP=" + ip + " data=" + message.trim() + " len=" + bytes.length));
                Response_DataReceived(MainActivity.this, tcpServer_Periperal, socket, ip, message, bytes);
            }
        });
    }

    public void Response_DataReceived(Context context, TCPServer tcpServer, Socket socket, String ip, String message, byte[] bytes) {
        if (GData.initialFinished) {

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
                    break;
                case Protocol.DeviceType.SW_KEYPAD:
                    AllKeyService.Handle_ALLKey(MainActivity.this, tcpServer, socket, bytes);
                    break;
            }
        }
    }

    /*Start&Stop Service*/
    void Start_Service() {
        startService(new Intent(MainActivity.this, SoundService.class));
        startService(new Intent(MainActivity.this, DisplayService.class));
        startService(new Intent(MainActivity.this, RealtimeService.class));
        startService(new Intent(MainActivity.this, QDisplayService.class));
    }

    void Stop_Service() {
        stopService(new Intent(MainActivity.this, SoundService.class));
        stopService(new Intent(MainActivity.this, DisplayService.class));
        stopService(new Intent(MainActivity.this, RealtimeService.class));
        stopService(new Intent(MainActivity.this, QDisplayService.class));
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
                //Check Reset Queue
                if (SystemMgr.CheckResetQueue(MainActivity.this)) {
                    showDataOnView();
                }
                /*
                PlaylistInfo p=new PlaylistInfo();
                p.setType(ProcessIndex);
                p.setQueue("1234");
                p.setCounter(1);
                SoundService.AddToPlayList(p);
                */
            }
        }
    });

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

            while (true) {
                /*Communicate Printer for Printing Queue (First Piority)*/
                Process_PrintQueue();
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
                    }catch (Exception ex){}
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

    public QueueInfo Get_NewQueue(byte div) {
        QueueInfo queueInfo = QueueMgr.GetNewQueue(MainActivity.this, div);
        EventBus.getDefault().post(new DebugMessageEvent("GetQ div=" + div));
        EventBus.getDefault().post(new DebugMessageEvent("Q.Size=" + GData.Queue.size() + " div=" + div + " Q=" + queueInfo.getQueueNo()));
        return queueInfo;
    }

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
    }

    /*TCP Server*/
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
    }

}
