package th.co.infinitecorp.www.qcontroller.QTouchWeb;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.QTouchWeb.Models.QTouchWebInfo;
import th.co.infinitecorp.www.qcontroller.Screen.SreenOnWebView.QTouchOnWebView;
import th.co.infinitecorp.www.qcontroller.Service.QTouchService;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCP;

public class QTouchOnWebService extends Service {


    private static final String TAG = QTouchOnWebService.class.getSimpleName();
    static WebSocket connTest=null;

    static WebSocketServer wsServer=null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        EventOnQTouchWeb();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Destroy();
    }
    public interface QTouchWebListener {
        void onOpen(WebSocket conn,String IP);
        void onClose(WebSocket conn,String IP);
        void onMessage(WebSocket conn,String IP,String Message);
        void onError(WebSocket conn,String IP,String ExMessage);
    }
    public interface BasicQFunctionListener {
        void onLogin(String message);
        void onLogout(String message);
        void onNext(String message);
    }
    QTouchWebListener qTouchWebListener;



    public  void EventOnQTouchWeb() {
        QTouchOnWebService.WebSocketServerForQTouch(new QTouchOnWebService.QTouchWebListener() {
            @Override
            public void onOpen(WebSocket conn,String IP) {
                Log.d(TAG,"**QTouchOnWeb:OnOpen IP:"+IP);
                EventBus.getDefault().post(new DebugMessageEvent("**QTouchOnWeb:OnOpen IP:"+IP));
                connTest=conn;
            }

            @Override
            public void onClose(WebSocket conn,String IP) {
                Log.d(TAG,"**QclientOnWeb:OnClose IP:"+IP);
                EventBus.getDefault().post(new DebugMessageEvent("**QclientOnWeb:OnClose IP:"+IP));
                connTest=null;
            }

            @Override
            public void onMessage(WebSocket conn,String IP, String Message) {
                Log.d(TAG,"**QTouchOnWeb:OnMessage IP:"+IP+" Message="+Message);
                EventBus.getDefault().post(new DebugMessageEvent("**QTouchOnWeb:OnMessage IP:"+IP+" Message="+Message));

                int protocolID = GetProtocolID(Message);
                QFunction(protocolID,Message);
            }

            @Override
            public void onError(WebSocket conn,String IP, String ExMessage) {
                Log.d(TAG,"**QTouchOnWeb:OnError IP:"+IP+" ex="+ExMessage);
                EventBus.getDefault().post(new DebugMessageEvent("**QTouchOnWeb:OnError IP:"+IP+" ex="+ExMessage));
            }
        });

    }
    public static  void WebSocketServerForQTouch(final  QTouchWebListener listener) {
        InetAddress inetAddress = getInetAddress();
        String ipAddress="127.0.0.1";
        if(inetAddress!=null)
            ipAddress = inetAddress.getHostAddress();

        Log.d(TAG,"**QTouchOnWeb:ipAddress="+ipAddress);
        InetSocketAddress inetSockAddress = new InetSocketAddress(ipAddress, TCP.Server.ListenPort.WS_QTouchWeb);
        wsServer = new WebSocketServer(inetSockAddress) {
            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                //EventBus.getDefault().post(new DebugMessageEvent("***WebSocket:OnOpen IP:"+conn.getLocalSocketAddress().getAddress()));
                Log.d(TAG,"**QTouchOnWeb:OnOpen");
                if(listener!=null) {
                    if(conn!=null)
                        listener.onOpen(conn, conn.getLocalSocketAddress().getAddress().toString().replace('/', ' ').trim());
                }
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                //EventBus.getDefault().post(new DebugMessageEvent("***WebSocket:Close IP:"+conn.getLocalSocketAddress().getAddress()));
                Log.d(TAG,"**QTouchOnWeb:OnClose");
                if(listener!=null) {
                    if(conn!=null)
                        listener.onClose(conn, conn.getLocalSocketAddress().getAddress().toString().replace('/', ' ').trim());
                }
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                //EventBus.getDefault().post(new DebugMessageEvent("***WebSocket:OnMessage IP:"+conn.getLocalSocketAddress().getAddress() +" Message="+message));
                Log.d(TAG,"**QTouchOnWeb:message="+message);
                if(listener!=null) {
                    if(conn!=null)
                        listener.onMessage(conn, conn.getLocalSocketAddress().getAddress().toString().replace('/', ' ').trim(), message);
                }
            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                Log.d(TAG,"**QTouchOnWeb:Error="+ex.getMessage());
                //EventBus.getDefault().post(new DebugMessageEvent("***WebSocket:OnError IP:"+conn.getLocalSocketAddress().getAddress()+" ex="+ex.getMessage()));
                if(listener!=null) {
                    if(conn!=null)
                        listener.onError(conn, conn.getLocalSocketAddress().getAddress().toString().replace('/', ' ').trim(), ex.getMessage());
                }
            }

            @Override
            public void onStart() {
                EventBus.getDefault().post(new DebugMessageEvent("***WebSocket:OnStart QTouch"));
            }
        };
        wsServer.start();
    }
    public void Destroy() {
        try {
            if(wsServer!=null)
                wsServer.stop();
        } catch (IOException ex) { }
        catch (InterruptedException ex){}

    }
    private static InetAddress getInetAddress() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface networkInterface = (NetworkInterface) en.nextElement();

                for (Enumeration enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            Log.e(TAG, "Error getting the network interface information");
        }

        return null;
    }

    private int GetProtocolID(String Message) {

        int iProtocolID=0;
        Log.d(TAG,"Message="+Message);
        JSONObject movieObject = null;
        try {
            movieObject = new JSONObject(Message);
            String protocolID = movieObject.getString("ProtocolID");
            Log.d(TAG,"protocolID="+protocolID);
            EventBus.getDefault().post(new DebugMessageEvent("ProtocolID="+protocolID));
            iProtocolID=Integer.valueOf(protocolID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return iProtocolID;
    }
    private QTouchWebInfo.RequestQ GetData_RequestQ(String Message) {
        QTouchWebInfo.RequestQ m=new QTouchWebInfo.RequestQ();
        if(Message!="") {
            m = new Gson().fromJson(Message, new TypeToken<QTouchWebInfo.RequestQ>() {}.getType());
        }
        return m;
    }
    private void QFunction(int protocolID,String Message) {
        QTouchWebInfo.Protocal pId= QTouchWebInfo.Protocal.valueOf(protocolID);
        EventBus.getDefault().post(new DebugMessageEvent("QTouch Function:ProtocolID="+protocolID));

        switch (pId)
        {
            case none:

                break;
            case RequestQ:
                QTouchWebInfo.RequestQ m= GetData_RequestQ(Message);
                if(m.DivID>0) {
                    QTouchService qTouchService = new QTouchService(getApplicationContext());
                    qTouchService.Call_REQUEST_QUEUE((byte) m.DivID);
                }
                break;
            default: break;

        }
    }
}
