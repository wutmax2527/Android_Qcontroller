package th.co.infinitecorp.www.qcontroller.QClientWeb;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.protocols.Protocol;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentDivisionInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentGroupInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentStationInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QueueInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.Management.CounterMgr;
import th.co.infinitecorp.www.qcontroller.Management.DivisionMgr;
import th.co.infinitecorp.www.qcontroller.Management.GroupMgr;
import th.co.infinitecorp.www.qcontroller.Management.LogMgr;
import th.co.infinitecorp.www.qcontroller.Management.QueueMgr;
import th.co.infinitecorp.www.qcontroller.Management.UserMgr;
import th.co.infinitecorp.www.qcontroller.QClientWeb.Models.QClientWebInfo;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCP;
import th.co.infinitecorp.www.qcontroller.Utils.DateTime;
import th.co.infinitecorp.www.qcontroller.Utils.GData;
import th.co.infinitecorp.www.qcontroller.Utils.constant;

public class QClientOnWebService extends Service {

    Context context;
    private static final String TAG = QClientOnWebService.class.getSimpleName();

    static WebSocketServer wsServer=null;

    public enum CONNECTION_STATE {
        Disconnect(0),
        Connecting(1),
        Connected(2);
        private int value;
        private static Map map = new HashMap<>();

        private CONNECTION_STATE(int value) {
            this.value = value;
        }

        static {
            for (CONNECTION_STATE p : CONNECTION_STATE.values()) {
                map.put(p.value, p);
            }
        }

        public static CONNECTION_STATE valueOf(int state) {
            return (CONNECTION_STATE) map.get(state);
        }

        public int getValue() {
            return value;
        }
    }
    public class STATION {
        int ID;
        CONNECTION_STATE connection;
        String IP;
        boolean IsAutoReserv;

        public STATION() {
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public CONNECTION_STATE getConnection() {
            return connection;
        }

        public void setConnection(CONNECTION_STATE connection) {
            this.connection = connection;
        }

        public String getIP() {
            return IP;
        }

        public void setIP(String IP) {
            this.IP = IP;
        }

        public boolean isAutoReserv() {
            return IsAutoReserv;
        }

        public void setAutoReserv(boolean autoReserv) {
            IsAutoReserv = autoReserv;
        }
        //public WebSocket webSocket;
    }

    public static  List<STATION> stationSetting=new ArrayList<>();
    public static WebSocket[] webSocket=new  WebSocket[constant.nStation];
    public static STATION[] stations=new  STATION[constant.nStation];
    public static WebSocket webSocket_Temp;
    private int[] currentCommand=new  int[constant.nStation];

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context=QClientOnWebService.this;
        stations=new  STATION[constant.nStation];
        for (int i = 0; i < constant.nStation; i++) {
            stations[i]=new STATION();
        }
        stationSetting=load_StationSetting();
        EventBus.getDefault().post(new DebugMessageEvent("***stationSetting.size="+stationSetting.size()));
        if(stationSetting.size()==0) {
            for (int i = 0; i < constant.nStation; i++) {
                int staId = i + 1;
                stations[i].setID(staId);
                stations[i].setIP("");
                stations[i].setAutoReserv(false);
                stations[i].setConnection(CONNECTION_STATE.Disconnect);
                //stationSetting.add(stations[i]);
            }
        }else {
            for (int i = 0; i < constant.nStation; i++) {
                int staId = i + 1;
                stations[i].setID(staId);
                stations[i].setIP("");
                stations[i].setAutoReserv(false);
                stations[i].setConnection(CONNECTION_STATE.Disconnect);
                for (STATION s:stationSetting) {
                    if(staId==s.getID()) {
                        stations[i].setID(s.getID());
                        stations[i].setIP(s.getIP());
                        stations[i].setAutoReserv(s.isAutoReserv());
                        stations[i].setConnection(CONNECTION_STATE.Disconnect);
                        break;
                    }
                }
            }
        }
        callQclientOnWebThread=new Thread(QclientOnWebThread);
        callQclientOnWebThread.start();
        EventOnQclientWeb();
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
    public interface QClientWebListener {
        void onOpen(WebSocket conn,String IP);
        void onClose(WebSocket conn,String IP);
        void onMessage(WebSocket conn,String IP,String Message);
        void onError(WebSocket conn,String IP,String ExMessage);
    }

    QClientWebListener qClientWebListener;
    private Thread  callQclientOnWebThread=null;

     Thread QclientOnWebThread=new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //EventBus.getDefault().post(new DebugMessageEvent("**UpdateWaitQueueInfo**"));
                UpdateStationProfile();
                UpdateStationInfo();

                UpdateGroupInfo();
                UpdateDivInfo();

                UpdateQueueStatusInfo();
                UpdateWaitQueueInfo();
                UpdateHoldQueueInfo();
            }
        }
    });
    public void UpdateStationProfile() {
        for (int i = 0; i < constant.nStation; i++) {
            if(stations[i]==null) continue;
            if (stations[i].connection != CONNECTION_STATE.Connected) continue;
            if (webSocket[i] == null)  continue;
                int staId=i+1;
                CurrentStationInfo curS = CounterMgr.Find_CurrentUserByStation(context, (byte) staId);
                byte grpID = curS.getGroupId();
                CurrentGroupInfo curG = GroupMgr.Find_CurrentByGroup(context, Integer.valueOf(grpID));
                String grpName = curG.getName();
                QClientWebInfo.StationProfile sp = new QClientWebInfo.StationProfile();

                sp.setGroupID(grpID);
                sp.setGroupName(grpName);

                String json = new Gson().toJson(sp, new TypeToken<QClientWebInfo.StationProfile>() {}.getType());
               try {
                   if (webSocket[i] != null)
                       webSocket[i].send(json);
               }catch (Exception ex){}
        }
    }
    public void UpdateStationInfo() {

            List<QClientWebInfo.StationInfo> sList = new ArrayList<>();
            QClientWebInfo.StationInfo s = new QClientWebInfo.StationInfo();

            for(int sId = 1; sId<= constant.nStation; sId++) {

                s = new QClientWebInfo.StationInfo();
                s.setID(sId);
                s.setName("Counter"+sId);
                sList.add(s);
            }

            QClientWebInfo.StationInfos sInfos = new QClientWebInfo.StationInfos();
            sInfos.setStations(sList);
            String json = new Gson().toJson(sInfos, new TypeToken<QClientWebInfo.StationInfos>() {}.getType());
            for (int i = 0; i < constant.nStation; i++) {
                if(stations[i]==null) continue;
               if (stations[i].getConnection().getValue() != (byte)CONNECTION_STATE.Connected.getValue()) continue;
               if (webSocket[i] == null)  continue;

                try {
                    if (webSocket[i] != null)
                        webSocket[i].send(json);
                }catch (Exception ex){}
            }

    }
    public void UpdateGroupInfo() {

        List<QClientWebInfo.GroupInfo> gList=new ArrayList<>();
        QClientWebInfo.GroupInfo g=new QClientWebInfo.GroupInfo();
        for(int grpId = 1; grpId<= constant.nGroup; grpId++) {
            CurrentGroupInfo curS = GroupMgr.Find_CurrentByGroup(context, grpId);
            if(curS!=null) {
                g=new QClientWebInfo.GroupInfo();
                g.setID(grpId);
                g.setName(curS.getName());
                g.setAvailableCount(curS.getAvailableCounter());
                g.setWaitQ(curS.getWaitQ());
                g.setMaxWaitTime(curS.maxWaitTime);

                String lastQ="";
                if(curS.lastQ!=null)
                  lastQ=curS.lastQ.getQueueNo();

                g.setLastQ(lastQ);
                gList.add(g);
            }
        }
        QClientWebInfo.GroupInfos gInfos=new  QClientWebInfo.GroupInfos();
        gInfos.setGroups(gList);

        String json=new Gson().toJson(gInfos,new TypeToken<QClientWebInfo.GroupInfos>(){}.getType());

        for (int i = 0; i < constant.nStation; i++) {
            if(stations[i]==null) continue;
            if (stations[i].getConnection().getValue() != (byte)CONNECTION_STATE.Connected.getValue()) continue;
            if (webSocket[i] == null)  continue;

            try {
                if (webSocket[i] != null)
                    webSocket[i].send(json);
            }catch (Exception ex){}
        }
    }
    public void UpdateDivInfo() {

            List<QClientWebInfo.DivisionInfo> dList = new ArrayList<>();
            QClientWebInfo.DivisionInfo d = new QClientWebInfo.DivisionInfo();
            for (int divId = 1; divId <= constant.nDiv; divId++) {
                CurrentDivisionInfo curDiv = DivisionMgr.GetCurrentDivisionInfo(context, divId);
                if (curDiv != null) {
                    d = new QClientWebInfo.DivisionInfo();
                    d.setID(divId);
                    d.setName(curDiv.getName());
                    d.setWaitQ(curDiv.getWaitQ());
                    d.setServQ(curDiv.getServQ());
                    d.setMaxWaitTime(curDiv.getMaxWaitTime());
                    d.setNaxtQ("");
                    dList.add(d);
                }
            }

            QClientWebInfo.DivisionInfos gInfos = new QClientWebInfo.DivisionInfos();
            gInfos.setDivisions(dList);
            String json = new Gson().toJson(gInfos, new TypeToken<QClientWebInfo.DivisionInfos>(){}.getType());

            for (int i = 0; i < constant.nStation; i++) {
                if(stations[i]==null) continue;
                if (stations[i].getConnection().getValue() != (byte)CONNECTION_STATE.Connected.getValue()) continue;
                if (webSocket[i] == null)  continue;

                try {
                    if (webSocket[i] != null)
                        webSocket[i].send(json);
                }catch (Exception ex){}
             }

    }
    public void UpdateQueueStatusInfo() {

        for (int i = 0; i < constant.nStation; i++) {
            int staId=i+1;
            if(stations[i]==null) continue;
            if (stations[i].getConnection().getValue() != (byte)CONNECTION_STATE.Connected.getValue()) continue;
            if (webSocket[i] == null)  continue;
                QClientWebInfo.QueueStatus qs = new QClientWebInfo.QueueStatus();
                CurrentStationInfo curS = CounterMgr.Find_CurrentUserByStation(context, (byte) staId);
                if (curS != null) {
                    qs.setWaitingQForGroup(curS.getWaitQ());
                    qs.setHoldQForGroup(curS.getHoldQ());
                    qs.setStationGangOpen(curS.getStaGangOpen());
                    qs.setTranferQ(curS.getTransferQ());
                    qs.setAlertQ("");
                    qs.setAlertPaper("");
                    qs.setAlertOther("");

                }

                String json = new Gson().toJson(qs, new TypeToken<QClientWebInfo.QueueStatus>() {}.getType());
            try {
                if (webSocket[i] != null)
                    webSocket[i].send(json);
            }catch (Exception ex){}
        }

    }
    private void UpdateWaitQueueInfo() {
        List<QClientWebInfo.QueueInfo> qList=new ArrayList<>();
        QClientWebInfo.QueueInfo q=new QClientWebInfo.QueueInfo();

        for (QueueInfo que: QueueMgr.CurrentWaitQ) {
            q = new QClientWebInfo.QueueInfo();
            q.setDivID(que.getDivisionId());
            q.setQNo(que.getQueueNo());
            q.setSevID(que.getServid());
            q.setUrlPicCustomer("");
            q.setCustomerName("");
            q.setRef1("");
            q.setRef2("");
            qList.add(q);
        }

        QClientWebInfo.WaitQueueInfos qInfos=new  QClientWebInfo.WaitQueueInfos();
        qInfos.setQueues(qList);
        String json=new Gson().toJson(qInfos,new TypeToken<QClientWebInfo.WaitQueueInfos>(){}.getType());
        for (int i = 0; i < constant.nStation; i++) {
            if(stations[i]==null) continue;
            if (stations[i].getConnection().getValue() != (byte)CONNECTION_STATE.Connected.getValue()) continue;
            if (webSocket[i] == null)  continue;

            try {
                if (webSocket[i] != null)
                    webSocket[i].send(json);
            }catch (Exception ex){}
        }
    }
    private void UpdateHoldQueueInfo() {
        List<QClientWebInfo.QueueInfo> qList=new ArrayList<>();
        QClientWebInfo.QueueInfo q=new QClientWebInfo.QueueInfo();

        for (QueueInfo que: QueueMgr.CurrentHoldQ) {
            q = new QClientWebInfo.QueueInfo();
            q.setDivID(que.getDivisionId());
            q.setQNo(que.getQueueNo());
            q.setSevID(que.getServid());
            q.setUrlPicCustomer("");
            q.setCustomerName("");
            q.setRef1("");
            q.setRef2("");
            qList.add(q);
        }

        QClientWebInfo.HoldQueueInfos qInfos=new  QClientWebInfo.HoldQueueInfos();
        qInfos.setQueues(qList);
        String json=new Gson().toJson(qInfos,new TypeToken<QClientWebInfo.HoldQueueInfos>(){}.getType());
        for (int i = 0; i < constant.nStation; i++) {
            if(stations[i]==null) continue;
            if (stations[i].getConnection().getValue() != (byte)CONNECTION_STATE.Connected.getValue()) continue;
            if (webSocket[i] == null)  continue;

            try {
                if (webSocket[i] != null)
                    webSocket[i].send(json);
            }catch (Exception ex){}
        }

    }

    public  void EventOnQclientWeb() {
        QClientOnWebService.WebSocketServerForQclient(new QClientOnWebService.QClientWebListener() {
            @Override
            public void onOpen(WebSocket conn,String IP) {
                EventBus.getDefault().post(new DebugMessageEvent("**QclientOnWeb:OnOpen IP:"+IP));

                STATION s=Search_StationSetting(IP);
                if(s!=null) {
                    EventBus.getDefault().post(new DebugMessageEvent("IP:"+IP+" StaID:"+s.getID()));
                    webSocket[s.getID()-1] = conn;
                    stations[s.getID()-1].setConnection(CONNECTION_STATE.Connected);
                    EventBus.getDefault().post(new DebugMessageEvent("QClientWeb ID="+s.getID()+" Connected"));
                    //---Answer Setting
                    QClientWebInfo.Setting m = new QClientWebInfo.Setting();
                    m.setStationID(s.getID());
                    m.setAutoReserv(false);

                    String json = new Gson().toJson(m, new TypeToken<QClientWebInfo.Setting>() {}.getType());
                    if (webSocket[s.getID()-1] != null)
                        webSocket[s.getID()-1].send(json);
                    EventBus.getDefault().post(new DebugMessageEvent("**WS:Open:Setting:Json=" + json));
                    if(s.getID()>0)
                    {
                        //---Answer Login Response
                        QClientWebInfo.Login_response m2 = new QClientWebInfo.Login_response();
                        m2.setStationID(s.getID());
                        m2.setStatus(true);
                        m2.setUserID("123456");
                        m2.setUserName("Name123456");
                        m2.setDescription("");

                        json = new Gson().toJson(m2, new TypeToken<QClientWebInfo.Login_response>() {}.getType());

                        if (webSocket[s.getID()-1] != null)
                            webSocket[s.getID()-1].send(json);

                        EventBus.getDefault().post(new DebugMessageEvent("**WS:Open:Login_response:Json=" + json));
                        //---NextQ Response
                        QueueInfo qStart = QueueMgr.Search_Serving_Queue((byte) s.getID());
                        if(qStart.getqNum()>0)
                        {
                            QClientWebInfo.ServQ m3=new QClientWebInfo.ServQ();
                            m3.setStationID(s.getID());
                            m3.setQueueNo(qStart.getQueueNo());

                            json=new Gson().toJson(m3,new TypeToken<QClientWebInfo.ServQ>(){}.getType());
                            if (webSocket[s.getID()-1] != null)
                                webSocket[s.getID()-1].send(json);
                            EventBus.getDefault().post(new DebugMessageEvent("**WS:Open:ServQ:Json="+json));
                        }

                    }

                }else {
                    webSocket_Temp=conn;
                    //---Answer Setting
                    QClientWebInfo.Setting m = new QClientWebInfo.Setting();
                    m.setStationID(0);
                    m.setAutoReserv(false);

                    String json = new Gson().toJson(m, new TypeToken<QClientWebInfo.Setting>() {}.getType());
                    if (webSocket_Temp != null)
                        webSocket_Temp.send(json);
                    Log.d(TAG, "**WebSocket:OnMessage Setting Json=" + json);
                    EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage Setting Json=" + json));
                }
            }

            @Override
            public void onClose(WebSocket conn,String IP) {
                EventBus.getDefault().post(new DebugMessageEvent("**QclientOnWeb:OnClose IP:"+IP));
                STATION s=Search_StationSetting(IP);
                if(s!=null) {
                    EventBus.getDefault().post(new DebugMessageEvent("QClientWeb ID="+s.getID()+" Disconnected"));
                    webSocket[s.getID()-1] = null;
                    stations[s.getID()-1].setConnection(CONNECTION_STATE.Disconnect);
                }else {
                    webSocket_Temp=null;
                }
            }

            @Override
            public void onMessage(WebSocket conn,String IP, String Message) {
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage IP:"+IP+" Message="+Message));

               int protocolID = GetProtocolID(Message);
               STATION s=new STATION();
               s=Search_StationSetting(IP);
               if(s!=null) {
                   EventBus.getDefault().post(new DebugMessageEvent("IP:"+IP+" StaID:"+s.getID()));
                   QFunction(webSocket[s.getID()-1], IP, protocolID, Message, s.getID(), 0);
                   if(protocolID==QClientWebInfo.Protocal.Setting.getValue())
                   {
                       int newStaId=GetStationID(Message);
                       if(s.getID()!=newStaId) {
                           webSocket[s.getID() - 1] = null;
                       }
                   }
               }else {
                   int newStaId=GetStationID(Message);
                   if(newStaId>0) {
                       webSocket[newStaId-1]=null;
                       QFunction(webSocket_Temp, IP, protocolID, Message, newStaId, 0);
                   }
               }
            }

            @Override
            public void onError(WebSocket conn,String IP, String ExMessage) {
                EventBus.getDefault().post(new DebugMessageEvent("**QclientOnWeb:OnError IP:"+IP+" ex="+ExMessage));
                STATION s=Search_StationSetting(IP);
                if(s!=null) {
                    EventBus.getDefault().post(new DebugMessageEvent("QClientWeb ID="+s.getID()+" OnErr"));
                    webSocket[s.getID()-1] = null;
                    stations[s.getID()-1].setConnection(CONNECTION_STATE.Disconnect);
                }else {
                    webSocket_Temp=null;
                }
            }
        });

    }
    public static void WebSocketServerForQclient(final QClientWebListener listener) {
        InetAddress inetAddress = getInetAddress();
        String ipAddress="";
        if(inetAddress!=null)
           ipAddress = inetAddress.getHostAddress();

        InetSocketAddress inetSockAddress = new InetSocketAddress(ipAddress, TCP.Server.ListenPort.WS_QClientWeb);
        wsServer = new WebSocketServer(inetSockAddress) {
            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                //EventBus.getDefault().post(new DebugMessageEvent("***WebSocket:OnOpen IP:"+conn.getLocalSocketAddress().getAddress()));
                if(listener!=null) {
                    if(conn!=null)
                     listener.onOpen(conn, conn.getRemoteSocketAddress().getAddress().toString().replace('/', ' ').trim());
                }
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                //EventBus.getDefault().post(new DebugMessageEvent("***WebSocket:Close IP:"+conn.getLocalSocketAddress().getAddress()));
                if(listener!=null) {
                    if(conn!=null)
                      listener.onClose(conn, conn.getRemoteSocketAddress().getAddress().toString().replace('/', ' ').trim());
                }
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                //EventBus.getDefault().post(new DebugMessageEvent("***WebSocket:OnMessage IP:"+conn.getLocalSocketAddress().getAddress() +" Message="+message));
                if(listener!=null) {
                    if(conn!=null)
                      listener.onMessage(conn, conn.getRemoteSocketAddress().getAddress().toString().replace('/', ' ').trim(), message);
                }
            }

            @Override
            public void onError(WebSocket conn, Exception ex) {

                //EventBus.getDefault().post(new DebugMessageEvent("***WebSocket:OnError IP:"+conn.getLocalSocketAddress().getAddress()+" ex="+ex.getMessage()));
               if(listener!=null) {
                   if(conn!=null)
                     listener.onError(conn, conn.getLocalSocketAddress().getAddress().toString().replace('/', ' ').trim(), ex.getMessage());
               }
            }

            @Override
            public void onStart() {
                EventBus.getDefault().post(new DebugMessageEvent("***WebSocket:OnStart QClient"));
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
        //callQclientOnWebThread.stop();
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

    /*Get Data ID*/
    private int GetProtocolID(String Message) {

        int iProtocolID=0;
        Log.d(TAG,"Message="+Message);
        JSONObject movieObject = null;
        try {
            movieObject = new JSONObject(Message);
            String protocolID = movieObject.getString("protocolID");
            Log.d(TAG,"protocolID="+protocolID);
            EventBus.getDefault().post(new DebugMessageEvent("protocolID="+protocolID));
            iProtocolID=Integer.valueOf(protocolID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return iProtocolID;
    }
    private int GetDivisionID(String Message) {

        int iDivID=0;
        Log.d(TAG,"Message="+Message);
        JSONObject movieObject = null;
        try {
            movieObject = new JSONObject(Message);
            String DivID = movieObject.getString("DivID");
            Log.d(TAG,"DivID="+DivID);
            EventBus.getDefault().post(new DebugMessageEvent("DivID="+DivID));
            iDivID=Integer.valueOf(DivID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return iDivID;
    }
    private int GetServID(String Message) {

        int iServID=0;
        Log.d(TAG,"Message="+Message);
        JSONObject movieObject = null;
        try {
            movieObject = new JSONObject(Message);
            String ServID = movieObject.getString("ServID");
            Log.d(TAG,"ServID="+ServID);
            EventBus.getDefault().post(new DebugMessageEvent("ServID="+ServID));
            iServID=Integer.valueOf(ServID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return iServID;
    }
    private int GetToGroupID(String Message) {

        int iToGroupID=0;
        Log.d(TAG,"Message="+Message);
        JSONObject movieObject = null;
        try {
            movieObject = new JSONObject(Message);
            String ToGroupID = movieObject.getString("ToGroupID");
            Log.d(TAG,"ToGroupID="+ToGroupID);
            EventBus.getDefault().post(new DebugMessageEvent("ToGroupID="+ToGroupID));
            iToGroupID=Integer.valueOf(ToGroupID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return iToGroupID;
    }
    private int GetStationID(String Message) {

        int iStationID=0;
        Log.d(TAG,"Message="+Message);
        JSONObject movieObject = null;
        try {
            movieObject = new JSONObject(Message);
            String StationID= movieObject.getString("StationID");
            Log.d(TAG,"StationID="+StationID);
            EventBus.getDefault().post(new DebugMessageEvent("StationID="+StationID));
            iStationID=Integer.valueOf(StationID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return iStationID;
    }
    public static boolean Send(QueueInfo qStart ,byte sta) {   if (sta <= 0 || sta > constant.nStation)
          return false;
        if(webSocket[sta-1]==null) return  false;


        QClientWebInfo.ServQ m = new QClientWebInfo.ServQ();
        m.setStationID(sta);
        m.setQueueNo(qStart.getQueueNo());
        String json = new Gson().toJson(m, new TypeToken<QClientWebInfo.ServQ>() {}.getType());
        QClientOnWebService.webSocket[sta - 1].send(json);
        webSocket[sta-1].send(json);
        EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:Send:ServQ:Json=" + json));
        return true;
    }

    /*Queue Function*/
    private void QFunction(WebSocket webSocket,String ip,int protocolID,String Message,int sta,int currentCmd) {
        if(webSocket==null) return;
        if(sta<=0) return;

        QClientWebInfo.Protocal pId= QClientWebInfo.Protocal.valueOf(protocolID);
        QueueInfo qStart;
        QueueInfo qEnd;
        CurrentStationInfo curS=new CurrentStationInfo();

        switch (pId) {
            case none:

                break;
            case NextQ:
                EventBus.getDefault().post(new DebugMessageEvent("***WS:NextQ"));
                qStart = QueueMgr.Search_Next_Queue((byte) sta);
                QClientWebInfo.ServQ m1=new QClientWebInfo.ServQ();
                m1.setStationID(sta);
                m1.setQueueNo(qStart.getQueueNo());

                String json1=new Gson().toJson(m1,new TypeToken<QClientWebInfo.ServQ>(){}.getType());
                webSocket.send(json1);
                Log.d(TAG,"**WebSocket:OnMessage:ServQ:Json="+json1);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:ServQ:Json="+json1));
                if(qStart.getqNum()>0) {
                    qEnd = new QueueInfo();
                    QueueMgr.CallingQueue(context, qStart, qEnd, (byte) sta, false, true);
                    CounterMgr.Remove_reserve_station(sta);
                }else {
                    CounterMgr.Add_reserve_station(sta, (byte) CounterMgr.KEYTYPE.softkeyWeb.getValue());
                }

                break;
            case ReCallQ:
                EventBus.getDefault().post(new DebugMessageEvent("***WS:ReCallQ"));
                QClientWebInfo.ReCallQ m2=new QClientWebInfo.ReCallQ();
                m2.setStationID(sta);
                String json2=new Gson().toJson(m2,new TypeToken<QClientWebInfo.ReCallQ>(){}.getType());
                webSocket.send(json2);
                Log.d(TAG,"**WebSocket:OnMessage:ReCallQ:Json="+json2);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:ReCallQ:Json="+json2));

                qStart = QueueMgr.Search_Serving_Queue((byte) sta);
                qEnd=new QueueInfo();
                QueueMgr.CallingQueue(context,qStart ,qEnd,(byte) sta,true,true);

                break;
            case HoldQ:
                EventBus.getDefault().post(new DebugMessageEvent("***WS:HoldQ"));
                qStart = QueueMgr.Search_Serving_Queue((byte) sta);
                qStart.setStationId((byte) sta);
                qStart.setEndDateTime(DateTime.GetDateTimeNow());
                qStart.setStatus(QueueMgr.qstatus.qhold);
                QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.qhold);

                curS=new CurrentStationInfo();
                curS=CounterMgr.Find_CurrentUserByStation(context,(byte) sta);
                curS.setQueueInfo(qStart);
                CounterMgr.UpdateCurrentStationStatus(context,(byte) sta,curS);
                QueueMgr.UpdateQueueStatChange();

                QClientWebInfo.HoldQ m3=new QClientWebInfo.HoldQ();
                m3.setStationID(sta);
                String json3=new Gson().toJson(m3,new TypeToken<QClientWebInfo.HoldQ>(){}.getType());
                webSocket.send(json3);
                Log.d(TAG,"**WebSocket:OnMessage:HoldQ:Json="+json3);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:HoldQ:Json="+json3));
                break;
            case CallHoldQ:
                EventBus.getDefault().post(new DebugMessageEvent("***WS:CallHoldQ"));
                QueueMgr.Finish_Queue(context, (byte) sta);
                qStart = QueueMgr.Search_Hold_Queue((byte)sta); //Find Hold Queue

                QClientWebInfo.ServQ m4=new QClientWebInfo.ServQ();
                m4.setStationID(sta);
                m4.setQueueNo(qStart.getQueueNo());

                String json4=new Gson().toJson(m4,new TypeToken<QClientWebInfo.ServQ>(){}.getType());
                webSocket.send(json4);
                Log.d(TAG,"**WebSocket:OnMessage:ServQ:Json="+json4);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:ServQ:Json="+json4));
                qEnd=new QueueInfo();
                QueueMgr.CallingQueue(context,qStart ,qEnd,(byte) sta,false,true);

                break;
            case EndQ:
                EventBus.getDefault().post(new DebugMessageEvent("***WS:EndQ"));
                QClientWebInfo.EndQ m5=new QClientWebInfo.EndQ();
                m5.setStationID(sta);
                String json5=new Gson().toJson(m5,new TypeToken<QClientWebInfo.EndQ>(){}.getType());
                webSocket.send(json5);
                Log.d(TAG,"**WebSocket:OnMessage:EndQ:Json="+json5);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:EndQ:Json="+json5));

                qStart = QueueMgr.Search_Serving_Queue((byte) sta);
                qStart.setEndDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.finish_by_next);

                break;
            case TranferQStation:
                QClientWebInfo.TranferQStation m6=new QClientWebInfo.TranferQStation();
                m6.setStationID(sta);
                String json6=new Gson().toJson(m6,new TypeToken<QClientWebInfo.TranferQStation>(){}.getType());
                webSocket.send(json6);
                Log.d(TAG,"**WebSocket:OnMessage:TranferQStation:Json="+json6);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:TranferQStation:Json="+json6));
                break;
            case ReservQ:
                break;
            case ServQ:

                break;
            case Login:
                EventBus.getDefault().post(new DebugMessageEvent("***WS:LoginQ"));

                QClientWebInfo.Login_response m9=new QClientWebInfo.Login_response();
                m9.setStationID(sta);
                m9.setUserID("123456");
                m9.setUserName("Name123456");
                m9.setStatus(true);
                m9.setDescription("");
                String json9=new Gson().toJson(m9,new TypeToken<QClientWebInfo.Login_response>(){}.getType());
                webSocket.send(json9);
                Log.d(TAG,"**WebSocket:OnMessage Login_response Json="+json9);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:Login_response:Json="+json9));
                break;
            case Logoff:
                EventBus.getDefault().post(new DebugMessageEvent("***WS:LogoffQ"));
                QClientWebInfo.Logoff m10=new QClientWebInfo.Logoff();
                m10.setStationID(sta);

                String json10=new Gson().toJson(m10,new TypeToken<QClientWebInfo.Logoff>(){}.getType());
                webSocket.send(json10);
                Log.d(TAG,"**WebSocket:OnMessage:Logoff:Json="+json10);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:Logoff:Json="+json10));
                break;
            case Break:
                EventBus.getDefault().post(new DebugMessageEvent("***WS:Break"));
                QClientWebInfo.Break m11=new QClientWebInfo.Break();
                m11.setStationID(sta);
                String json11=new Gson().toJson(m11,new TypeToken<QClientWebInfo.Break>(){}.getType());
                webSocket.send(json11);
                Log.d(TAG,"**WebSocket:OnMessage:Break:Json="+json11);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:Break:Json="+json11));
                break;
            case ChangGroup:
                int toGroupID=GetToGroupID(Message);
                EventBus.getDefault().post(new DebugMessageEvent("***WS:ChangGroup:ToGroupID="+toGroupID));


                if(CounterMgr.ChangeGroup((byte) sta,(byte) toGroupID)) {
                    curS=CounterMgr.Find_CurrentUserByStation(context,(byte) sta);
                    curS.setGroupId((byte) toGroupID);
                    CounterMgr.UpdateCurrentStationStatus(context,(byte) sta,curS);
                    curS=CounterMgr.Find_CurrentUserByStation(context,(byte) sta);
                    CounterMgr.Remove_reserve_station(sta);
                }

                QClientWebInfo.ChangGroup m12=new QClientWebInfo.ChangGroup();
                m12.setStationID(sta);
                m12.setToGroupID(toGroupID);
                String json12=new Gson().toJson(m12,new TypeToken<QClientWebInfo.ChangGroup>(){}.getType());
                webSocket.send(json12);
                Log.d(TAG,"**WebSocket:OnMessage:ChangGroup:Json="+json12);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:ChangGroup:Json="+json12));
                break;
            case DirectQ:
                EventBus.getDefault().post(new DebugMessageEvent("***WS:DirectQ"));
                Integer servID=GetServID(Message);

                qStart = QueueMgr.Search_Direct_Queue(servID);
                EventBus.getDefault().post(new DebugMessageEvent("WS:DirectQ:servID="+servID+" Q="+qStart.getQueueNo()));

                QClientWebInfo.ServQ m13=new QClientWebInfo.ServQ();
                m13.setStationID(sta);
                m13.setQueueNo(qStart.getQueueNo());

                String json13=new Gson().toJson(m13,new TypeToken<QClientWebInfo.ServQ>(){}.getType());
                webSocket.send(json13);
                Log.d(TAG,"**WebSocket:OnMessage:ServQ:Json="+json13);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:ServQ:Json="+json13));
                qEnd=new QueueInfo();
                QueueMgr.CallingQueue(context,qStart ,qEnd,(byte) sta,false,true);

                break;
            case WalkInQ:
                EventBus.getDefault().post(new DebugMessageEvent("***WS:WalkInQ"));

                byte divId=(byte) GetDivisionID(Message);
                qStart = QueueMgr.GetNewQueue(context, divId);
                EventBus.getDefault().post(new DebugMessageEvent("WS:WalkInQ:divId="+divId));

                QClientWebInfo.ServQ m14=new QClientWebInfo.ServQ();
                m14.setStationID(sta);
                m14.setQueueNo(qStart.getQueueNo());

                String json14=new Gson().toJson(m14,new TypeToken<QClientWebInfo.ServQ>(){}.getType());
                webSocket.send(json14);
                Log.d(TAG,"**WebSocket:OnMessage:ServQ:Json="+json14);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:ServQ:Json="+json14));
                qEnd=new QueueInfo();
                QueueMgr.CallingQueue(context,qStart ,qEnd,(byte) sta,false,false);

                break;
            case Help:
                EventBus.getDefault().post(new DebugMessageEvent("***WS:Help"));
                QClientWebInfo.Help m15=new QClientWebInfo.Help();
                m15.setStationID(sta);
                String json15=new Gson().toJson(m15,new TypeToken<QClientWebInfo.Help>(){}.getType());
                webSocket.send(json15);
                Log.d(TAG,"**WebSocket:OnMessage:Help:Json="+json15);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:Help:Json="+json15));
                break;

            case Login_response:
                EventBus.getDefault().post(new DebugMessageEvent("***WS:Login_response"));
                break;
            case CancelQ:
                EventBus.getDefault().post(new DebugMessageEvent("***WS:CancelQ"));
                qStart = QueueMgr.Finish_Queue(context,(byte) sta);
                QClientWebInfo.CancelQ m17=new QClientWebInfo.CancelQ();
                m17.setStationID(sta);
                m17.setServID(qStart.getServid());
                String json17=new Gson().toJson(m17,new TypeToken<QClientWebInfo.CancelQ>(){}.getType());
                webSocket.send(json17);
                Log.d(TAG,"**WebSocket:OnMessage:CancelQ:Json="+json17);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:CancelQ:Json="+json17));

                break;
            case ServQ_Ack:
                EventBus.getDefault().post(new DebugMessageEvent("***WS:ServQ_Ack"));
                Log.d(TAG,"**WebSocket:OnMessage:ServQ_Ack");
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:ServQ_Ack"));

                if(currentCommand[sta-1]== QClientWebInfo.Protocal.ServQ.getValue()) {
                    qStart = QueueMgr.Search_Next_Queue((byte) sta);
                    qEnd=new QueueInfo();
                    QueueMgr.CallingQueue(context,qStart ,qEnd,(byte) sta,false,true);
                    currentCommand[sta-1]=0;
                }
                break;
            case StationProfile:
                break;
            case QueueStatus:
                break;
            case DivisionInfos:
                break;
            case GroupInfo:
                break;
            case TranferQDivision:
                QClientWebInfo.TranferQDivision m23=new QClientWebInfo.TranferQDivision();
                m23.setStationID(sta);
                String json23=new Gson().toJson(m23,new TypeToken<QClientWebInfo.TranferQDivision>(){}.getType());
                webSocket.send(json23);
                Log.d(TAG,"**WebSocket:OnMessage:TranferQDivision:Json="+json23);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:TranferQDivision:Json="+json23));
                break;
            case BreakingInfo:
                break;
            case HoldReasonInfo:
                break;
            case GroupInfos:
                break;
            case BreakingInfos:
                break;
            case HoldReasonInfos:
                break;
            case StationInfo:
                break;
            case StationInfos:
                break;
            case AddTransaction:
                byte addDivId=(byte) GetDivisionID(Message);

                EventBus.getDefault().post(new DebugMessageEvent("WS:AddTransaction:divId="+addDivId));
                qStart = QueueMgr.Search_Serving_Queue((byte) sta);

                QClientWebInfo.AddTransaction m31=new QClientWebInfo.AddTransaction();
                m31.setStationID(sta);
                m31.setDivID(addDivId);

                String json31=new Gson().toJson(m31,new TypeToken<QClientWebInfo.AddTransaction>(){}.getType());
                webSocket.send(json31);
                Log.d(TAG,"**WebSocket:OnMessage:AddTransaction:Json="+json31);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:AddTransaction:Json="+json31));

                qStart.setEndDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.finish_by_transec);
                QueueMgr.AddNewWaitQueue(context,qStart);
                Integer tranno=qStart.getTranno();
                qStart.setTranno(++tranno);
                qStart.setDivisionId(addDivId);
                qStart.setReqDateTime(DateTime.GetDateTimeNow());
                qStart.setStDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.qcalling_serving);

                break;
            case QueueInfo:
                break;
            case QueueInfos:
                break;
            case HoldQueueInfos:
                break;
            case Setting:
                int staId=GetStationID(Message);



                //---Clear Old Station
                stations[sta-1].setID(sta);
                stations[sta-1].setConnection(CONNECTION_STATE.Disconnect);
                stations[sta-1].setAutoReserv(false);

                //--Add New Station
                stations[staId-1].setID(staId);
                stations[staId-1].setConnection(CONNECTION_STATE.Connected);
                stations[staId-1].setIP(ip);
                stations[staId-1].setAutoReserv(false);

                change_StationSetting(stations[sta - 1], stations[staId - 1]);
                /*
                if(stations[sta-1].getIP()==stations[staId-1].getIP()) {


                }else {
                    add_StationSetting(stations[staId - 1]);
                }
                //---Clear Old Station
                stations[sta-1].setID(sta);
                stations[sta-1].setConnection(CONNECTION_STATE.Disconnect);
                stations[sta-1].setAutoReserv(false);
                */

                STATION s=new STATION();
                s.setID(staId);
                s.setConnection(CONNECTION_STATE.Connected);
                s.setIP(ip);
                s.setAutoReserv(false);
                QClientWebInfo.Setting m35=new QClientWebInfo.Setting();
                m35.setStationID(s.getID());
                m35.setAutoReserv(s.isAutoReserv());
                String json35=new Gson().toJson(m35,new TypeToken<QClientWebInfo.Setting>(){}.getType());
                webSocket.send(json35);
                Log.d(TAG,"**WebSocket:OnMessage:Setting:Json="+json35);
                EventBus.getDefault().post(new DebugMessageEvent("**WebSocket:OnMessage:Setting:Json="+json35));

                break;
            case RequestQ:
                break;
            case RequestQ_response:
                break;
        }

        currentCommand[sta-1]=pId.getValue();
    }

    /*Station Setting*/
    private boolean change_StationSetting(STATION oldSta,STATION newSta) {

        List<STATION> stationSettingTemp=new ArrayList<>();
        for (STATION s : stationSetting)
            stationSettingTemp.add(s);

        stationSetting.clear();
        for (STATION s : stationSettingTemp) {
            if(s.getID()==oldSta.getID()) continue;
            if(s.getID()==newSta.getID()) continue;
            if(s.getIP()==newSta.getIP()) continue;
            stationSetting.add(s);
        }

        stationSetting.add(newSta);

        EventBus.getDefault().post(new DebugMessageEvent("***stationSetting.size="+stationSetting.size()));

        if(LogMgr.Save_StationSetting(context,stationSetting)) {
            return true;
        }else {
            return  false;
        }
    }
    private boolean add_StationSetting(STATION newSta) {

        List<STATION> stationSettingTemp=new ArrayList<>();
        for (STATION s : stationSetting)
            stationSettingTemp.add(s);


        stationSetting.add(newSta);

        EventBus.getDefault().post(new DebugMessageEvent("***stationSetting.size="+stationSetting.size()));

        if(LogMgr.Save_StationSetting(context,stationSetting)) {
            return true;
        }else {
            return  false;
        }
    }
    private boolean remove_StationSetting(STATION station) {

        boolean found=false;
        int idx=0;
        //---Remove No Device
        List<Integer> list=new ArrayList<>();
        idx=0;
        for (STATION s: stationSetting) {
            if(s.getID()==station.getID()) {
                list.add(idx);
            }
            idx++;
        }
        for (Integer i:list) {
            if(stationSetting.size()>0)
                stationSetting.remove(i);
        }

        EventBus.getDefault().post(new DebugMessageEvent("***stationSetting.size="+stationSetting.size()));

        if(LogMgr.Save_StationSetting(context,stationSetting)) {
            return true;
        }else {
            return  false;
        }
    }
    private boolean clear_StationSetting() {
        stationSetting.clear();
        //EventBus.getDefault().post(new DebugMessageEvent("***stationSetting.size="+stationSetting.size()));
        if(LogMgr.Save_StationSetting(context,stationSetting)) {
            return true;
        }else {
            return  false;
        }
    }
    private List<STATION> load_StationSetting() {
        List<STATION> stationList=new ArrayList<>();
        stationList=LogMgr.Load_StationSetting(context);
        return  stationList;
    }
    private STATION Search_StationSetting(String ip) {
       if(stationSetting.size()>0) {
           for (STATION s : stationSetting) {
               if (s.getIP().equals("")) continue;
               if (s.getIP().equals(ip)) {
                   return s;
               }
           }
       }
        return null;
    }
}
